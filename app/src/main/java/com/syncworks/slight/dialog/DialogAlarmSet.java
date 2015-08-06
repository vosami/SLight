package com.syncworks.slight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.syncworks.slight.R;
import com.syncworks.slight.util.LecHeaderParam;

/**
 * Created by Kim on 2015-07-27.
 */
public class DialogAlarmSet extends Dialog {

    private AlarmDialogData alarmDialogData;
    private TimePicker timePicker;
    private ToggleButton tbDate[];
    private SeekBar sbRunTime;
    private RadioButton rbRunMode[];
    private TextView tvRunTime;
    private Button btnCancel, btnConfirm;

    public DialogAlarmSet(Context context) {
        super(context);
    }

    public DialogAlarmSet(Context context, AlarmDialogData data) {
        super(context);
        this.alarmDialogData = data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_alarm_set);
        findViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        timePicker.setCurrentHour(alarmDialogData.getHourOfDay());
        timePicker.setCurrentMinute(alarmDialogData.getMinutes());
        int dateParam = alarmDialogData.getDateParam();
        tbDate[0].setChecked((dateParam & 0x01) != 0);
        tbDate[1].setChecked((dateParam & 0x02) != 0);
        tbDate[2].setChecked((dateParam & 0x04) != 0);
        tbDate[3].setChecked((dateParam & 0x08) != 0);
        tbDate[4].setChecked((dateParam & 0x10) != 0);
        tbDate[5].setChecked((dateParam & 0x20) != 0);
        tbDate[6].setChecked((dateParam & 0x40) != 0);
        sbRunTime.setProgress(alarmDialogData.getRunTime() - 1);
        int runMode = alarmDialogData.getRunMode();
        rbRunMode[runMode].setChecked(true);
    }

    public void setAlarmData(AlarmDialogData data) {
        this.alarmDialogData = data;
    }

    private void findViews() {
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        tbDate = new ToggleButton[7];
        tbDate[0] = (ToggleButton) findViewById(R.id.alarm_date_1);
        tbDate[1] = (ToggleButton) findViewById(R.id.alarm_date_2);
        tbDate[2] = (ToggleButton) findViewById(R.id.alarm_date_3);
        tbDate[3] = (ToggleButton) findViewById(R.id.alarm_date_4);
        tbDate[4] = (ToggleButton) findViewById(R.id.alarm_date_5);
        tbDate[5] = (ToggleButton) findViewById(R.id.alarm_date_6);
        tbDate[6] = (ToggleButton) findViewById(R.id.alarm_date_7);
        tvRunTime = (TextView) findViewById(R.id.run_time_time);
        sbRunTime = (SeekBar) findViewById(R.id.sb_runtime);
        rbRunMode = new RadioButton[3];
        rbRunMode[0] = (RadioButton) findViewById(R.id.rb_run_mode_default);
        rbRunMode[1] = (RadioButton) findViewById(R.id.rb_run_mode_sequence);
        rbRunMode[2] = (RadioButton) findViewById(R.id.rb_run_mode_random);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCancel();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doConfirm(getDateParam(), timePicker.getCurrentHour(), timePicker.getCurrentMinute(), sbRunTime.getProgress() + 1, getRunMode());
            }
        });

        sbRunTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvRunTime.setText(Integer.toString(progress+1) + getContext().getString(R.string.str_minute));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private int getDateParam() {
        int param = 0;
        for (int i=0;i<7;i++) {
            if (tbDate[i].isChecked()) {
                param = param | (1 << i);
            }
        }
        return param;
    }

    private int getRunMode() {
        if (rbRunMode[1].isChecked()) {
            return LecHeaderParam.PARAM_RUN_MODE_SEQUENTIAL;
        } else if (rbRunMode[2].isChecked()){
            return LecHeaderParam.PARAM_RUN_MODE_RANDOM;
        } else {
            return LecHeaderParam.PARAM_RUN_MODE_DEFAULT;
        }
    }

    private OnAlarmSet onAlarmSet = null;

    public interface OnAlarmSet {
        void onCancel();
        void onConfirm(int alarmNum, int date, int hour, int minute, int runTime, int runMode);
    }

    public void setOnAlarmSet(OnAlarmSet alarmSet) {
        this.onAlarmSet = alarmSet;
    }

    private void doCancel() {
        if (onAlarmSet != null) {
            onAlarmSet.onCancel();
        }
    }

    private void doConfirm(int date, int hour, int minute, int runTime, int runMode) {
        if (onAlarmSet != null) {
            onAlarmSet.onConfirm(alarmDialogData.getAlarmNum(),date, hour, minute, runTime, runMode);
        }
    }

}
