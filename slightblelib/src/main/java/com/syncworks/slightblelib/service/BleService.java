package com.syncworks.slightblelib.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.syncworks.define.Logger;
import com.syncworks.slightblelib.resolver.BluetoothCrashResolver;
import com.syncworks.slightblelib.scanner.CycledLeScanner;

import java.lang.ref.WeakReference;

/**
 * Created by vosami on 2015-07-24.
 * 스마트라이트 블루투스 4.0 서비스
 */
public class BleService extends Service {
    private static final Object TAG = BleService.class;

    // Activity 와 통신하는 핸들러 설정
    private Handler handler = new Handler();
    // BleService 가 bind 된 횟수 카운트
    private int bindCount = 0;
    private BluetoothCrashResolver bluetoothCrashResolver;
    private boolean isScanningEnabled = false;
    private CycledLeScanner cycledLeScanner;
    private boolean backgroundFlag = false;

    /*
     * The scan period is how long we wait between restarting the BLE advertisement scans
     * Each time we restart we only see the unique advertisements once (e.g. unique beacons)
     * So if we want updates, we have to restart.  For updates at 1Hz, ideally we
     * would restart scanning that often to get the same update rate.  The trouble is that when you
     * restart scanning, it is not instantaneous, and you lose any beacon packets that were in the
     * air during the restart.  So the more frequently you restart, the more packets you lose.  The
     * frequency is therefore a tradeoff.  Testing with 14 beacons, transmitting once per second,
     * here are the counts I got for various values of the SCAN_PERIOD:
     *
     * Scan period     Avg beacons      % missed
     *    1s               6                 57
     *    2s               10                29
     *    3s               12                14
     *    5s               14                0
     *
     * Also, because beacons transmit once per second, the scan period should not be an even multiple
     * of seconds, because then it may always miss a beacon that is synchronized with when it is stopping
     * scanning.
     *
     */

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class BleBinder extends Binder {
        public BleService getService() {
            Logger.i(TAG, "getService of BeaconBinder called");
            // Return this instance of LocalService so clients can call public methods
            return BleService.this;
        }
    }

    /**
     * Command to the service to display a message
     */
    public static final int MSG_START_RANGING = 2;
    public static final int MSG_STOP_RANGING = 3;
    public static final int MSG_START_MONITORING = 4;
    public static final int MSG_STOP_MONITORING = 5;
    public static final int MSG_SET_SCAN_PERIODS = 6;



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    static class IncomingHandler extends Handler {
        private final WeakReference<BleService> mService;

        IncomingHandler(BleService service) {
            mService = new WeakReference<BleService>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            BleService service = mService.get();
            StartRMData startRMData = (StartRMData) msg.obj;

            if (service != null) {
                switch (msg.what) {
                    case MSG_START_RANGING:
                        Logger.i(TAG, "start ranging received");
                        service.startRangingBeaconsInRegion(startRMData.getRegionData(), new org.altbeacon.beacon.service.Callback(startRMData.getCallbackPackageName()));
                        service.setScanPeriods(startRMData.getScanPeriod(), startRMData.getBetweenScanPeriod(), startRMData.getBackgroundFlag());
                        break;
                    case MSG_STOP_RANGING:
                        Logger.i(TAG, "stop ranging received");
                        service.stopRangingBeaconsInRegion(startRMData.getRegionData());
                        service.setScanPeriods(startRMData.getScanPeriod(), startRMData.getBetweenScanPeriod(), startRMData.getBackgroundFlag());
                        break;
                    case MSG_START_MONITORING:
                        Logger.i(TAG, "start monitoring received");
                        service.startMonitoringBeaconsInRegion(startRMData.getRegionData(), new org.altbeacon.beacon.service.Callback(startRMData.getCallbackPackageName()));
                        service.setScanPeriods(startRMData.getScanPeriod(), startRMData.getBetweenScanPeriod(), startRMData.getBackgroundFlag());
                        break;
                    case MSG_STOP_MONITORING:
                        LogManager.i(TAG, "stop monitoring received");
                        service.stopMonitoringBeaconsInRegion(startRMData.getRegionData());
                        service.setScanPeriods(startRMData.getScanPeriod(), startRMData.getBetweenScanPeriod(), startRMData.getBackgroundFlag());
                        break;
                    case MSG_SET_SCAN_PERIODS:
                        LogManager.i(TAG, "set scan intervals received");
                        service.setScanPeriods(startRMData.getScanPeriod(), startRMData.getBetweenScanPeriod(), startRMData.getBackgroundFlag());
                        break;
                    default:
                        super.handleMessage(msg);
                }
            }
        }
    }
}
