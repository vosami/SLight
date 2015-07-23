package com.syncworks.vosami.blelib;

/**
 * Created by vosami on 2015-07-22.
 */
public class BleDeviceData {
    private String name;
    private String addr;
    private int rssi;
    private String version;

    public BleDeviceData() {
        name = "";
        addr = "";
        rssi = 0;
        version = "";
    }

    public BleDeviceData(String mName, String mAddr, int mRssi, String mVersion) {
        name = mName;
        addr = mAddr;
        rssi = mRssi;
        version = mVersion;
    }

    public String getName() {
        return name;
    }

    public String getAddr() {
        return addr;
    }

    public String getVersion() {
        return version;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int mRssi) {
        rssi = mRssi;
    }
}
