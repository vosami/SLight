package com.syncworks.slight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.syncworks.slight.R;

/**
 * Created by vosami on 2015-07-17.
 */
public class DialogStartTime extends Dialog{

    Button btnConfirm;
    SeekBar sbTime;
    TextView tvTime;

    private int effectNum = 0;

    private int progress = 0;

    public DialogStartTime(Context context) {
        super(context);
        progress = 0;
    }

    public DialogStartTime(Context context,int progress) {
        super(context);
        this.progress = progress;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
        setTitle(getContext().getString(R.string.easy_effect_select_start_time));
        setContentView(R.layout.dialog_start_time);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        sbTime = (SeekBar) findViewById(R.id.sb_start_time);
        sbTime.setProgress(this.progress);
        tvTime = (TextView) findViewById(R.id.tv_second);
        setSecondText(progress);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doConfirm();
            }
        });

        sbTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setSecondText(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                doStartTime(seekBar.getProgress());
            }
        });
    }

    private void setSecondText(int progress) {
        double sec = progress * 0.15;
        sec = Math.round(sec*10d)/10d;
        String txt = Double.toString(sec) + getContext().getString(R.string.str_sec);
        tvTime.setText(txt);
    }

    private OnStartTimeListener listener = null;

    public interface OnStartTimeListener {
        void onStartTime(int effectNum, int time);
        void onConfirm();
    }

    private void doStartTime(int time) {
        if (listener != null) {
            listener.onStartTime(effectNum , time);
        }
    }

    private void doConfirm() {
        if (listener != null) {
            listener.onConfirm();
        }
    }

    public void setOnStartTimeListener(OnStartTimeListener mListener) {
        this.listener = mListener;
    }
}
