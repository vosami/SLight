package com.syncworks.slight.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.syncworks.slight.SLightApp;

public class DummyService extends Service {
    private final IBinder binder = new LocalBinder();
    private static final String TAG = "DummyService";

    public class LocalBinder extends Binder {
        DummyService getService() {
            return DummyService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "Dummy service bound");
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        boolean result = super.onUnbind(intent);
        Log.d(TAG, "Dummy service unbound");
        return result;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Dummy service created");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service (and presumably Application) is being destroyed.  We will stop the crash resolver.");
        ((SLightApp)getApplication()).getBluetoothCrashResolver().stop();
    }
}
