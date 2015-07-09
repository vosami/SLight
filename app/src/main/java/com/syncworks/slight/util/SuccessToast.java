package com.syncworks.slight.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.syncworks.slight.R;

/**
 * Created by vosami on 2015-07-09.
 * 성공 메시지
 */
public class SuccessToast extends Toast {
    Context mContext;

    public SuccessToast(Context context) {
        super(context);
        mContext = context;
    }

    public void showToast(String body, int duration) {
        // http://developer.android.com/guide/topics/ui/notifiers/toasts.html
        LayoutInflater inflater;
        View v;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.toast_success, null);

        TextView text = (TextView) v.findViewById(R.id.texttoast);
        text.setText(body);

        show(this, v, duration);
    }

    private void show(Toast toast, View v, int duration) {
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(duration);
        toast.setView(v);
        toast.show();
    }
}
