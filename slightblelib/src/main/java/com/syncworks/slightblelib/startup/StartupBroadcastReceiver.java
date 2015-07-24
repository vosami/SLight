package com.syncworks.slightblelib.startup;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.syncworks.define.Logger;
import com.syncworks.slightblelib.BleManager;

@TargetApi(4)
public class StartupBroadcastReceiver extends BroadcastReceiver
{
    private static final String TAG = "StartupBroadcastReceiver";

	@Override
    public void onReceive(Context context, Intent intent) {
        Logger.d(TAG, "onReceive called in startup broadcast receiver");
        if (android.os.Build.VERSION.SDK_INT < 18) {
            Logger.w(TAG, "Not starting up beacon service because we do not have SDK version 18 (Android 4.3).  We have:", android.os.Build.VERSION.SDK_INT);
            return;
        }
        BleManager beaconManager = BleManager.getBleManager(context.getApplicationContext());
        if (beaconManager.isAnyConsumerBound()) {
            if (intent.getBooleanExtra("wakeup", false)) {
                Logger.d(TAG, "got wake up intent");
            }
            else {
                Logger.d(TAG, "Already started.  Ignoring intent: ", intent,
                        intent.getStringExtra("wakeup"));
            }
        }
     }
}
