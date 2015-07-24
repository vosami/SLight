package com.syncworks.slightblelib.scanner;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;

import com.syncworks.define.Logger;
import com.syncworks.slightblelib.BleManager;
import com.syncworks.slightblelib.resolver.BluetoothCrashResolver;
import com.syncworks.slightblelib.startup.StartupBroadcastReceiver;

import java.util.Date;


/**
 * Created by vosami on 2015-07-24.
 * 블루투스 LE 반복 스캐너
 */
@TargetApi(18)
public abstract class CycledLeScanner {
    private static final Object TAG = CycledLeScanner.class;

    private BluetoothAdapter bluetoothAdapter;

    private long mLastScanCycleStartTime = 0l;
    private long mLastScanCycleEndTime = 0l;
    protected long mNextScanCycleStartTime = 0l;
    private long mScanCycleStopTime = 0l;

    private boolean isScanning;
    protected boolean isScanningPaused;
    private boolean isScanCycleStarted = false;
    private boolean isScanningEnabled = false;
    protected final Context mContext;
    private long mScanPeriod;

    protected long mBetweenScanPeriod;
    protected final Handler handler = new Handler();
    protected final BluetoothCrashResolver bluetoothCrashResolver;
    protected final CycledLeScanCallback cycledLeScanCallback;

    protected boolean backgroundFlag = false;
    protected boolean isRestartNeeded = false;

    protected CycledLeScanner(Context context, long scanPeriod, long betweenScanPeriod, boolean backgroundFlag,
                              CycledLeScanCallback cycledLeScanCallback, BluetoothCrashResolver crashResolver) {
        mScanPeriod = scanPeriod;
        mBetweenScanPeriod = betweenScanPeriod;
        mContext = context;
        this.cycledLeScanCallback = cycledLeScanCallback;
        this.bluetoothCrashResolver = crashResolver;
        this.backgroundFlag = backgroundFlag;
    }

    public static CycledLeScanner createScanner(Context context, long scanPeriod, long betweenScanPeriod, boolean backgroundFlag,
                                                CycledLeScanCallback cycledLeScanCallback, BluetoothCrashResolver crashResolver) {
        boolean useAndroidLScanner = false;
        if (Build.VERSION.SDK_INT < 18) {
            Logger.w(TAG, "Not supported prior to API 18.");
            return null;
        }
        if (Build.VERSION.SDK_INT < 21) {
            Logger.i(TAG,"This is not Android 5.0.  We are using old scanning APIs");
            useAndroidLScanner = false;
        } else if (BleManager.isAndroidLScanningDisabled()) {
            Logger.i(TAG,"This Android 5.0, but L scanning is disabled. We are using old scanning APIs");
        } else {
            Logger.i(TAG,"This Android 5.0.  We are using new scanning APIs");
            useAndroidLScanner = true;
        }
        if (useAndroidLScanner) {
            return new CycledLeScannerForLollipop(context, scanPeriod, betweenScanPeriod, backgroundFlag, cycledLeScanCallback, crashResolver);
        } else {
            return new CycledLeScannerForJellyBeanMr2(context, scanPeriod, betweenScanPeriod, backgroundFlag, cycledLeScanCallback, crashResolver);
        }
    }

    /**
     * Tells the cycler the scan rate and whether it is in operating in background mode.
     * Background mode flag  is used only with the Android 5.0 scanning implementations to switch
     * between LOW_POWER_MODE vs. LOW_LATENCY_MODE
     * @param backgroundFlag
     */
    public void setScanPeriods(long scanPeriod, long betweenScanPeriod, boolean backgroundFlag) {
        Logger.d(TAG, "Set scan periods called with %s, %s Background mode must have changed.",
                scanPeriod, betweenScanPeriod);
        if (this.backgroundFlag != backgroundFlag) {
            isRestartNeeded = true;
        }
        this.backgroundFlag = backgroundFlag;
        mScanPeriod = scanPeriod;
        mBetweenScanPeriod = betweenScanPeriod;
        if (backgroundFlag) {
            Logger.d(TAG, "We are in the background.  Setting wakeup alarm");
            setWakeUpAlarm();
        } else {
            Logger.d(TAG, "We are not in the background.  Cancelling wakeup alarm");
            cancelWakeUpAlarm();
        }
        long now = new Date().getTime();
        if (mNextScanCycleStartTime > now) {
            // We are waiting to start scanning.  We may need to adjust the next start time
            // only do an adjustment if we need to make it happen sooner.  Otherwise, it will
            // take effect on the next cycle.
            long proposedNextScanStartTime = (mLastScanCycleEndTime + betweenScanPeriod);
            if (proposedNextScanStartTime < mNextScanCycleStartTime) {
                mNextScanCycleStartTime = proposedNextScanStartTime;
                Logger.i(TAG, "Adjusted nextScanStartTime to be %s", new Date(mNextScanCycleStartTime));
            }
        }
        if (mScanCycleStopTime > now) {
            // we are waiting to stop scanning.  We may need to adjust the stop time
            // only do an adjustment if we need to make it happen sooner.  Otherwise, it will
            // take effect on the next cycle.
            long proposedScanStopTime = (mLastScanCycleStartTime + scanPeriod);
            if (proposedScanStopTime < mScanCycleStopTime) {
                mScanCycleStopTime = proposedScanStopTime;
                Logger.i(TAG, "Adjusted scanStopTime to be %s", mScanCycleStopTime);
            }
        }
    }

    public void start() {
        Logger.d(TAG, "start called");
        isScanningEnabled = true;
        if (!isScanCycleStarted) {
            scanLeDevice(true);
        } else {
            Logger.d(TAG, "scanning already started");
        }
    }

    @SuppressLint("NewApi")
    public void stop() {
        Logger.d(TAG, "stop called");
        isScanningEnabled = false;
        if (isScanCycleStarted) {
            scanLeDevice(false);
        }
        if (bluetoothAdapter != null) {
            stopScan();
            mLastScanCycleEndTime = new Date().getTime();
        }
    }

    protected abstract void stopScan();

    protected abstract boolean deferScanIfNeeded();

    protected abstract void startScan();


    @SuppressLint("NewApi")
    protected void scanLeDevice(final Boolean enable) {
        isScanCycleStarted = true;
        if (getBluetoothAdapter() == null) {
            Logger.e(TAG, "No bluetooth adapter.  beaconService cannot scan.");
        }
        if (enable) {
            if (deferScanIfNeeded()) {
                return;
            }
            Logger.d(TAG, "starting a new scan cycle");
            if (!isScanning || isScanningPaused || isRestartNeeded) {
                isScanning = true;
                isScanningPaused = false;
                try {
                    if (getBluetoothAdapter() != null) {
                        if (getBluetoothAdapter().isEnabled()) {
                            if (bluetoothCrashResolver != null && bluetoothCrashResolver.isRecoveryInProgress()) {
                                Logger.w(TAG, "Skipping scan because crash recovery is in progress.");
                            } else {
                                if (isScanningEnabled) {
                                    if (isRestartNeeded) {
                                        isRestartNeeded = false;
                                        Logger.d(TAG, "restarting a bluetooth le scan");
                                    } else {
                                        Logger.d(TAG, "starting a new bluetooth le scan");
                                    }
                                    try {
                                        startScan();
                                    } catch (Exception e) {
                                        Logger.e(TAG, "Internal Android exception scanning for beacons");
                                    }
                                } else {
                                    Logger.d(TAG, "Scanning unnecessary - no monitoring or ranging active.");
                                }
                            }
                            mLastScanCycleStartTime = new Date().getTime();
                        } else {
                            Logger.w(TAG, "Bluetooth is disabled.  Cannot scan for beacons.");
                        }
                    }
                } catch (Exception e) {
                    Logger.e(TAG, "Exception starting bluetooth scan.  Perhaps bluetooth is disabled or unavailable?");
                }
            } else {
                Logger.d(TAG, "We are already scanning");
            }
            mScanCycleStopTime = (new Date().getTime() + mScanPeriod);
            scheduleScanCycleStop();

            Logger.d(TAG, "Scan started");
        } else {
            Logger.d(TAG, "disabling scan");
            isScanning = false;

            stopScan();
            mLastScanCycleEndTime = new Date().getTime();
        }
    }

    protected void scheduleScanCycleStop() {
        // Stops scanning after a pre-defined scan period.
        long millisecondsUntilStop = mScanCycleStopTime - (new Date().getTime());
        if (millisecondsUntilStop > 0) {
            Logger.d(TAG, "Waiting to stop scan cycle for another %s milliseconds",
                    millisecondsUntilStop);
            if (backgroundFlag) {
                setWakeUpAlarm();
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scheduleScanCycleStop();
                }
            }, millisecondsUntilStop > 1000 ? 1000 : millisecondsUntilStop);
        } else {
            finishScanCycle();
        }
    }

    protected abstract void finishScan();

    private void finishScanCycle() {
        Logger.d(TAG, "Done with scan cycle");
        cycledLeScanCallback.onScanCycleEnd();
        if (isScanning) {
            if (getBluetoothAdapter() != null) {
                if (getBluetoothAdapter().isEnabled()) {
                    try {
                        Logger.d(TAG, "stopping bluetooth le scan");

                        finishScan();

                    } catch (Exception e) {
                        Logger.w(TAG, "Internal Android exception scanning for beacons");
                    }
                    mLastScanCycleEndTime = new Date().getTime();
                } else {
                    Logger.w(TAG, "Bluetooth is disabled.  Cannot scan for beacons.");
                }
            }
            mNextScanCycleStartTime = getNextScanStartTime();
            if (isScanningEnabled) {
                scanLeDevice(true);
            } else {
                Logger.d(TAG, "Scanning disabled.  No ranging or monitoring regions are active.");
                isScanCycleStarted = false;
                cancelWakeUpAlarm();
            }
        }
    }

    protected BluetoothAdapter getBluetoothAdapter() {
        if (bluetoothAdapter == null) {
            // Initializes Bluetooth adapter.
            final BluetoothManager bluetoothManager =
                    (BluetoothManager) mContext.getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
            bluetoothAdapter = bluetoothManager.getAdapter();
            if (bluetoothAdapter == null) {
                Logger.w(TAG, "Failed to construct a BluetoothAdapter");
            }
        }
        return bluetoothAdapter;
    }

    private PendingIntent mWakeUpOperation = null;

    // In case we go into deep sleep, we will set up a wakeup alarm when in the background to kickoff
    // off the scan cycle again
    protected void setWakeUpAlarm() {
        // wake up time will be the maximum of 5 minutes, the scan period, the between scan period
        long milliseconds = 1000l * 60 * 5; /* five minutes */
        if (milliseconds < mBetweenScanPeriod) {
            milliseconds = mBetweenScanPeriod;
        }
        if (milliseconds < mScanPeriod) {
            milliseconds = mScanPeriod;
        }
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent();
        intent.setClassName(mContext, StartupBroadcastReceiver.class.getName());
        intent.putExtra("wakeup", true);
        cancelWakeUpAlarm();
        mWakeUpOperation = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, System.currentTimeMillis() + milliseconds, mWakeUpOperation);
        Logger.d(TAG, "Set a wakeup alarm to go off in %s ms: %s", milliseconds, mWakeUpOperation);
    }

    protected void cancelWakeUpAlarm() {
        Logger.d(TAG, "cancel wakeup alarm: %s", mWakeUpOperation);
        if (mWakeUpOperation != null) {
            AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(mWakeUpOperation);
        }
    }

    private long getNextScanStartTime() {
        // Because many apps may use this library on the same device, we want to try to synchonize
        // scanning as much as possible in order to save battery.  Therefore, we will set the scan
        // intervals to be on a predictable interval using a modulus of the system time.  This may
        // cause scans to start a little earlier than otherwise, but it should be acceptable.
        // This way, if multiple apps on the device are using the default scan periods, then they
        // will all be doing scans at the same time, thereby saving battery when none are scanning.
        // This, of course, won't help at all if people set custom scan periods.  But since most
        // people accept the defaults, this will likely have a positive effect.
        if (mBetweenScanPeriod == 0) {
            return System.currentTimeMillis();
        }
        long fullScanCycle = mScanPeriod + mBetweenScanPeriod;
        long normalizedBetweenScanPeriod = mBetweenScanPeriod-(System.currentTimeMillis() % fullScanCycle);
        Logger.d(TAG, "Normalizing between scan period from %s to %s", mBetweenScanPeriod,
                normalizedBetweenScanPeriod);

        return System.currentTimeMillis()+normalizedBetweenScanPeriod;
    }
}
