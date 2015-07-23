package com.syncworks.slight;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.syncworks.define.Logger;
import com.syncworks.slight.services.DummyService;
import com.syncworks.vosami.blelib.resolver.BluetoothCrashResolver;

/**
 * Created by Kim on 2015-07-23.
 */
public class SLightApp extends Application {
    private BluetoothCrashResolver bluetoothCrashResolver = null;

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Logger.d(this,"Dummy service connected" );
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Logger.d(this,"Dummy service disconnected");
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d(this, "Application onCreate");
        bluetoothCrashResolver = new BluetoothCrashResolver(this);
        bluetoothCrashResolver.start();
        bindService(new Intent(this, DummyService.class),connection, Context.BIND_AUTO_CREATE);
    }

    public BluetoothCrashResolver getBluetoothCrashResolver() {
        return bluetoothCrashResolver;
    }
}
