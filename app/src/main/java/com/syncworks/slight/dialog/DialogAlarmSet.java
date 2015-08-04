package com.syncworks.slight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.syncworks.slight.R;

/**
 * Created by Kim on 2015-07-27.
 */
public class DialogAlarmSet extends Dialog {
    public DialogAlarmSet(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
        setTitle("알람 설정");
        setContentView(R.layout.dialog_alarm_set);
    }
}
