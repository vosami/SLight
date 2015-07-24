package com.syncworks.vosami.blelib.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.syncworks.vosami.blelib.resolver.BluetoothCrashResolver;

/**
 * Created by vosami on 2015-07-24.
 * 블루투스 LE 연결 서비스
 */
public class BleService extends Service {
    // Activity 와 통신하는 핸들러 설정
    private Handler handler = new Handler();
    // BleService 가 bind 된 횟수 카운트
    private int bindCount = 0;
    private BluetoothCrashResolver bluetoothCrashResolver;
    private boolean isScanningEnabled = false;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
