package com.syncworks.slightblelib.scanner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.syncworks.define.Logger;
import com.syncworks.slightblelib.resolver.BluetoothCrashResolver;

import java.util.Date;

/**
 * Created by vosami on 2015-07-24.
 *
 */
public class CycledLeScannerForJellyBeanMr2 extends CycledLeScanner {
    private static final String TAG = "CycledLeScannerForJellyBeanMr2";
    private BluetoothAdapter.LeScanCallback leScanCallback;

    public CycledLeScannerForJellyBeanMr2(Context context, long scanPeriod, long betweenScanPeriod, boolean backgroundFlag, CycledLeScanCallback cycledLeScanCallback, BluetoothCrashResolver crashResolver) {
        super(context, scanPeriod, betweenScanPeriod, backgroundFlag, cycledLeScanCallback, crashResolver);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void stopScan() {
        try {
            BluetoothAdapter bluetoothAdapter = getBluetoothAdapter();
            if (bluetoothAdapter != null) {
                bluetoothAdapter.stopLeScan(getLeScanCallback());
            }
        } catch (Exception e) {
            Logger.e(e, TAG, "Internal Android exception scanning for beacons");
        }
    }

    @Override
    protected boolean deferScanIfNeeded() {
        long millisecondsUntilStart = mNextScanCycleStartTime - (new Date().getTime());
        if (millisecondsUntilStart > 0) {
            Logger.d(TAG, "Waiting to start next bluetooth scan for another %s milliseconds",
                    millisecondsUntilStart);
            // Don't actually wait until the next scan time -- only wait up to 1 second.  this
            // allows us to start scanning sooner if a consumer enters the foreground and expects
            // results more quickly
            if (backgroundFlag) {
                setWakeUpAlarm();
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scanLeDevice(true);
                }
            }, millisecondsUntilStart > 1000 ? 1000 : millisecondsUntilStart);
            return true;
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void startScan() {
        getBluetoothAdapter().startLeScan(getLeScanCallback());
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void finishScan() {
        getBluetoothAdapter().stopLeScan(getLeScanCallback());
        isScanningPaused = true;
    }

    private BluetoothAdapter.LeScanCallback getLeScanCallback() {
        if (leScanCallback == null) {
            leScanCallback =
                    new BluetoothAdapter.LeScanCallback() {

                        @Override
                        public void onLeScan(final BluetoothDevice device, final int rssi,
                                             final byte[] scanRecord) {
                            Logger.d(TAG, "got record");
                            cycledLeScanCallback.onLeScan(device, rssi, scanRecord);
                            bluetoothCrashResolver.notifyScannedDevice(device, getLeScanCallback());
                        }
                    };
        }
        return leScanCallback;
    }
}
