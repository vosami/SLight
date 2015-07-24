package com.syncworks.slightblelib.scanner;

import android.bluetooth.BluetoothDevice;

/**
 * Created by vosami on 2015-07-24.
 * 블루투스 LE 반복 스캐너 콜백 함수 (인터페이스)
 */
public interface CycledLeScanCallback {
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord);
    public void onScanCycleEnd();
}
