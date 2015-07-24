package com.syncworks.slight;

import android.app.Application;

import com.syncworks.define.Logger;
import com.syncworks.vosami.blelib.BleManager;

/**
 * Created by Kim on 2015-07-23.
 * 스마트라이트 공용 Application
 */
public class SLightApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BleManager bleManager = BleManager.getBleManager(this);
        Logger.d(this, "Application onCreate");
    }

}
