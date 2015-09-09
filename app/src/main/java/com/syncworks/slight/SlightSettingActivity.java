package com.syncworks.slight;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.syncworks.define.Define;
import com.syncworks.define.Logger;
import com.syncworks.slight.dialog.AlarmDialogData;
import com.syncworks.slight.dialog.DialogAlarmSet;
import com.syncworks.slight.util.CustomToast;
import com.syncworks.slight.util.ErrorToast;
import com.syncworks.slight.util.LecHeaderParam;
import com.syncworks.slight.util.SuccessToast;
import com.syncworks.slightpref.SLightPref;
import com.syncworks.vosami.blelib.BleConsumer;
import com.syncworks.vosami.blelib.BleManager;
import com.syncworks.vosami.blelib.BleNotifier;
import com.syncworks.vosami.blelib.BluetoothLeService;
import com.syncworks.vosami.blelib.LecGattAttributes;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;



public class SlightSettingActivity extends ActionBarActivity implements BleConsumer {
    private final static String TAG = SlightSettingActivity.class.getSimpleName();

    // 액션 바 메뉴
    private Menu menu = null;
    //블루투스 매니저
    private BleManager bleManager;
    // 연결 상태 확인
    private boolean connectionState = false;
    // 알람 설정 파라미터
    private LecHeaderParam headerParam;

    private TextView alarmAPM[], alarmHour[], alarmMin[];
    private TextView alarmDate[][];
    private View alarmDateBar[][];
    private Switch alarmOnOff[];
    private TextView alarmRunTime[];
    private TextView alarmRunMode[];
    private TextView alarmDevName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        headerParam = new LecHeaderParam();
        setContentView(R.layout.activity_slight_setting);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        findViews();
    }

    private void findViews() {
        alarmAPM = new TextView[3];
        alarmHour = new TextView[3];
        alarmMin = new TextView[3];
        alarmDate = new TextView[3][7];
        alarmDateBar = new View[3][7];
        alarmOnOff = new Switch[3];
        alarmRunTime = new TextView[3];
        alarmRunMode = new TextView[3];
        alarmDevName = new TextView(this);
        alarmAPM[0] = (TextView) findViewById(R.id.alarm_1_apm);
        alarmAPM[1] = (TextView) findViewById(R.id.alarm_2_apm);
        alarmAPM[2] = (TextView) findViewById(R.id.alarm_3_apm);
        alarmHour[0] = (TextView) findViewById(R.id.alarm_1_hour);
        alarmHour[1] = (TextView) findViewById(R.id.alarm_2_hour);
        alarmHour[2] = (TextView) findViewById(R.id.alarm_3_hour);
        alarmMin[0] = (TextView) findViewById(R.id.alarm_1_minute);
        alarmMin[1] = (TextView) findViewById(R.id.alarm_2_minute);
        alarmMin[2] = (TextView) findViewById(R.id.alarm_3_minute);
        alarmDate[0][0] = (TextView) findViewById(R.id.alarm_1_date_1);
        alarmDate[0][1] = (TextView) findViewById(R.id.alarm_1_date_2);
        alarmDate[0][2] = (TextView) findViewById(R.id.alarm_1_date_3);
        alarmDate[0][3] = (TextView) findViewById(R.id.alarm_1_date_4);
        alarmDate[0][4] = (TextView) findViewById(R.id.alarm_1_date_5);
        alarmDate[0][5] = (TextView) findViewById(R.id.alarm_1_date_6);
        alarmDate[0][6] = (TextView) findViewById(R.id.alarm_1_date_7);
        alarmDate[1][0] = (TextView) findViewById(R.id.alarm_2_date_1);
        alarmDate[1][1] = (TextView) findViewById(R.id.alarm_2_date_2);
        alarmDate[1][2] = (TextView) findViewById(R.id.alarm_2_date_3);
        alarmDate[1][3] = (TextView) findViewById(R.id.alarm_2_date_4);
        alarmDate[1][4] = (TextView) findViewById(R.id.alarm_2_date_5);
        alarmDate[1][5] = (TextView) findViewById(R.id.alarm_2_date_6);
        alarmDate[1][6] = (TextView) findViewById(R.id.alarm_2_date_7);
        alarmDate[2][0] = (TextView) findViewById(R.id.alarm_3_date_1);
        alarmDate[2][1] = (TextView) findViewById(R.id.alarm_3_date_2);
        alarmDate[2][2] = (TextView) findViewById(R.id.alarm_3_date_3);
        alarmDate[2][3] = (TextView) findViewById(R.id.alarm_3_date_4);
        alarmDate[2][4] = (TextView) findViewById(R.id.alarm_3_date_5);
        alarmDate[2][5] = (TextView) findViewById(R.id.alarm_3_date_6);
        alarmDate[2][6] = (TextView) findViewById(R.id.alarm_3_date_7);
        alarmDateBar[0][0] = findViewById(R.id.alarm_1_date_1_bar);
        alarmDateBar[0][1] = findViewById(R.id.alarm_1_date_2_bar);
        alarmDateBar[0][2] = findViewById(R.id.alarm_1_date_3_bar);
        alarmDateBar[0][3] = findViewById(R.id.alarm_1_date_4_bar);
        alarmDateBar[0][4] = findViewById(R.id.alarm_1_date_5_bar);
        alarmDateBar[0][5] = findViewById(R.id.alarm_1_date_6_bar);
        alarmDateBar[0][6] = findViewById(R.id.alarm_1_date_7_bar);
        alarmDateBar[1][0] = findViewById(R.id.alarm_2_date_1_bar);
        alarmDateBar[1][1] = findViewById(R.id.alarm_2_date_2_bar);
        alarmDateBar[1][2] = findViewById(R.id.alarm_2_date_3_bar);
        alarmDateBar[1][3] = findViewById(R.id.alarm_2_date_4_bar);
        alarmDateBar[1][4] = findViewById(R.id.alarm_2_date_5_bar);
        alarmDateBar[1][5] = findViewById(R.id.alarm_2_date_6_bar);
        alarmDateBar[1][6] = findViewById(R.id.alarm_2_date_7_bar);
        alarmDateBar[2][0] = findViewById(R.id.alarm_3_date_1_bar);
        alarmDateBar[2][1] = findViewById(R.id.alarm_3_date_2_bar);
        alarmDateBar[2][2] = findViewById(R.id.alarm_3_date_3_bar);
        alarmDateBar[2][3] = findViewById(R.id.alarm_3_date_4_bar);
        alarmDateBar[2][4] = findViewById(R.id.alarm_3_date_5_bar);
        alarmDateBar[2][5] = findViewById(R.id.alarm_3_date_6_bar);
        alarmDateBar[2][6] = findViewById(R.id.alarm_3_date_7_bar);
        alarmOnOff[0] = (Switch) findViewById(R.id.alarm_1_onoff);
        alarmOnOff[1] = (Switch) findViewById(R.id.alarm_2_onoff);
        alarmOnOff[2] = (Switch) findViewById(R.id.alarm_3_onoff);
        alarmRunTime[0] = (TextView) findViewById(R.id.alarm_1_run_minute);
        alarmRunTime[1] = (TextView) findViewById(R.id.alarm_2_run_minute);
        alarmRunTime[2] = (TextView) findViewById(R.id.alarm_3_run_minute);
        alarmRunMode[0] = (TextView) findViewById(R.id.alarm_1_run_mode);
        alarmRunMode[1] = (TextView) findViewById(R.id.alarm_2_run_mode);
        alarmRunMode[2] = (TextView) findViewById(R.id.alarm_3_run_mode);
        alarmDevName = (TextView) findViewById(R.id.alarm_device_name);

        alarmOnOff[0].setOnCheckedChangeListener(onCheckedChangeListener);
        alarmOnOff[1].setOnCheckedChangeListener(onCheckedChangeListener);
        alarmOnOff[2].setOnCheckedChangeListener(onCheckedChangeListener);
        alarmOnOff[0].setOnTouchListener(switchTouchListener);
        alarmOnOff[1].setOnTouchListener(switchTouchListener);
        alarmOnOff[2].setOnTouchListener(switchTouchListener);
        SLightPref appPref = new SLightPref(this);
        String devName = appPref.getString(SLightPref.DEVICE_NAME);
        String devAddr = appPref.getString(SLightPref.DEVICE_ADDR);
        alarmDevName.setText(devName + "(" +devAddr + ")");
    }

    private void invalidate() {
        setAlarmDisplay(0);
        setAlarmDisplay(1);
        setAlarmDisplay(2);
    }

    private void setAlarmDisplay(int alarmNum) {
        NumberFormat formatter = new DecimalFormat("00");
        Calendar calendar = Calendar.getInstance();
        String apm, hour, minute;
        calendar.set(0, 0, 0, headerParam.getAlarmHour(alarmNum), headerParam.getAlarmMinute(alarmNum));
        if (calendar.get(Calendar.AM_PM) == 0) {
            apm = getString(R.string.ss_am);
        }else {
            apm = getString(R.string.ss_pm);
        }
        hour = formatter.format(calendar.get(Calendar.HOUR));
        minute = formatter.format(calendar.get(Calendar.MINUTE));

        alarmAPM[alarmNum].setText(apm);
        alarmHour[alarmNum].setText(hour);
        alarmMin[alarmNum].setText(minute);
        alarmOnOff[alarmNum].setChecked(headerParam.getAlarmOnOff(alarmNum));
        alarmRunTime[alarmNum].setText(Integer.toString((headerParam.getAlarmRunTime(alarmNum)&0xFF)) + getString(R.string.str_minute));
        setAlarmMode(alarmNum, headerParam.getAlarmRunMode(alarmNum));
        setAlarmDate(alarmNum,headerParam.getAlarmDate(alarmNum));
    }

    private void setAlarmMode(int alarmNum, int alarmMode) {
        if (alarmMode == LecHeaderParam.PARAM_RUN_MODE_DEFAULT) {
            String pattern = "";
            alarmRunMode[alarmNum].setText(getString(R.string.ss_alarm_run_mode_default)+pattern);
        } else if (alarmMode == LecHeaderParam.PARAM_RUN_MODE_SEQUENTIAL) {
            alarmRunMode[alarmNum].setText(getString(R.string.ss_alarm_run_mode_sequence));
        } else if (alarmMode == LecHeaderParam.PARAM_RUN_MODE_RANDOM) {
            alarmRunMode[alarmNum].setText(getString(R.string.ss_alarm_run_mode_random));
        } else {
            alarmRunMode[alarmNum].setText("");
        }
    }

    private void setAlarmDate(int alarmNum, int date) {
        for (int i=0;i<7;i++) {
            if ((date & (1<<i)) != 0) {
                alarmDate[alarmNum][i].setTextColor(getResources().getColor(R.color.Black));
                alarmDateBar[alarmNum][i].setVisibility(View.VISIBLE);
            } else {
                alarmDate[alarmNum][i].setTextColor(getResources().getColor(R.color.LightGrey));
                alarmDateBar[alarmNum][i].setVisibility(View.INVISIBLE);
            }
        }
    }

    private boolean isFromUser = false;

    private View.OnTouchListener switchTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Logger.d(this,"Touch");
                isFromUser = true;
            }
            return false;
        }
    };

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch(buttonView.getId()) {
                case R.id.alarm_1_onoff:
                    setSwitchText(0,isChecked);
                    headerParam.setAlarmOnOff(0,isChecked);
                    break;
                case R.id.alarm_2_onoff:
                    setSwitchText(1,isChecked);
                    headerParam.setAlarmOnOff(1, isChecked);
                    break;
                case R.id.alarm_3_onoff:
                    setSwitchText(2,isChecked);
                    headerParam.setAlarmOnOff(2, isChecked);
                    break;
            }
            if (isFromUser) {
                isFromUser = false;
                switch (buttonView.getId()) {
                    case R.id.alarm_1_onoff:
                        txData(TxDatas.formatAlarmWrite(0,headerParam.getAlarmParam(0),headerParam.getAlarmRunTime(0),
                                headerParam.getAlarmDate(0),headerParam.getAlarmHour(0),headerParam.getAlarmMinute(0)));
                        break;
                    case R.id.alarm_2_onoff:
                        txData(TxDatas.formatAlarmWrite(1,headerParam.getAlarmParam(1),headerParam.getAlarmRunTime(1),
                                headerParam.getAlarmDate(1),headerParam.getAlarmHour(1),headerParam.getAlarmMinute(1)));
                        break;
                    case R.id.alarm_3_onoff:
                        txData(TxDatas.formatAlarmWrite(2,headerParam.getAlarmParam(2),headerParam.getAlarmRunTime(2),
                                headerParam.getAlarmDate(2),headerParam.getAlarmHour(2),headerParam.getAlarmMinute(2)));
                        break;
                }
            }
        }
    };


    // 스위치 텍스트 설정
    private void setSwitchText(int num, boolean isChecked) {
        if (isChecked) {
            alarmOnOff[num].setText(getString(R.string.ss_on));
        } else {
            alarmOnOff[num].setText(getString(R.string.ss_off));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        bleManager = BleManager.getBleManager(this);
        bleManager.bind(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        bleManager.unbind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_slight_setting, menu);
        this.menu = menu;
        setConnectIcon(connectionState);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_connect) {
            if (connectionState) {
                bleManager.bleDisconnect();
                Toast.makeText(this,getString(R.string.ble_connect_complete),Toast.LENGTH_SHORT).show();
                Logger.d(TAG, "연결되었습니다.");
            } else {
                SLightPref appPref = new SLightPref(this);
                String address = appPref.getString(SLightPref.DEVICE_ADDR);
                bleManager.bleConnect(address);
                CustomToast.middleTop(this, "연결 시도중입니다.");
                Logger.d(TAG,"연결 X");
            }

            return true;
        } else if (id== android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (connectionState) {
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_bluetooth_on));
        } else {
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_bluetooth_off));
        }
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * ActionBar 의 연결 상태 아이콘 설정
     * @param stateConnect 연결상태(true:연결, false:끊김)
     */
    private void setConnectIcon(boolean stateConnect) {
        connectionState = stateConnect;
        invalidateOptionsMenu();
    }

    @Override
    public void onBleServiceConnect() {
        Log.d(TAG,"서비스 연결됨");
        // 블루투스 연결 상태 확인후 연결 시도
        if (bleManager.getBleConnectState() != BluetoothLeService.STATE_CONNECTED) {
            SLightPref appPref = new SLightPref(this);
            String address = appPref.getString(SLightPref.DEVICE_ADDR);
            if (address.contains(getString(R.string.ble_default_device_address))) {
                uiHandler.sendEmptyMessage(HANDLE_IS_NOT_ADDRESS);
            } else {
                bleManager.bleConnect(address);
            }
            // 연결 상태 설정 - disconnect
            connectionState = false;
        }
        else {
            // 연결 상태 설정 - Connected
            connectionState = true;
        }

        bleManager.setBleNotifier(new BleNotifier() {
            @Override
            public void bleConnected() {
                Log.i(TAG, "Connected");
                // 연결 상태 아이콘 설정 - Connected
                //setConnectIcon(true);
                uiHandler.sendEmptyMessage(HANDLE_ICON_INVALIDATE);
                new Thread(taskReadParam).start();
            }

            @Override
            public void bleDisconnected() {
                Log.i(TAG, "Disconnected");
                // 연결 상태 아이콘 설정 - disconnect
                uiHandler.sendEmptyMessage(HANDLE_ICON_INVALIDATE);
            }

            @Override
            public void bleServiceDiscovered() {

            }

            @Override
            public void bleDataAvailable(String uuid, byte[] data) {
                Log.i(TAG, "bleDataAvailable");
                if (uuid.equals(LecGattAttributes.LEC_RX_UUID)) {
                    switch (data[0]) {
                        case Define.TX_PARAM_READ:
                            headerParam.setLecByte(data, 4, data[2], data[1]);
                            getIsCalledReadParam();
                            break;
                        case Define.TX_ACK:
                            switch (data[1]) {
                                case Define.TX_ALARM_WRITE:
                                    uiHandler.sendEmptyMessage(HANDLE_ALARM_WRITE_SUCCESS);
                                    break;
                                case Define.TX_INIT_SET:
                                    //uiHandler.sendEmptyMessage(HANDLE_INIT_EEPROM);
                                    break;
                            }
                            break;
                    }
                }
            }

            @Override
            public void bleDataWriteComplete() {
                Log.i(TAG, "bleDataWriteComplete");
            }
        });
    }

    private DialogAlarmSet dialog = null;

    public void onSleep(View v) {
        switch (v.getId()) {
            case R.id.ss_btn_sleep:
                txData(TxDatas.formatSleep(false));
                break;
            case R.id.ss_btn_wakeup:
                txData(TxDatas.formatSleep(true));
                break;
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alarm_1_modify:
                dialog = new DialogAlarmSet(this, new AlarmDialogData(0,headerParam.getAlarmHour(0),
                        headerParam.getAlarmMinute(0),headerParam.getAlarmDate(0),headerParam.getAlarmRunTime(0)&0xFF,headerParam.getAlarmRunMode(0)));
                dialog.show();
                break;
            case R.id.alarm_2_modify:
                dialog = new DialogAlarmSet(this, new AlarmDialogData(1,headerParam.getAlarmHour(1),
                        headerParam.getAlarmMinute(1),headerParam.getAlarmDate(1),headerParam.getAlarmRunTime(1)&0xFF,headerParam.getAlarmRunMode(1)));
                dialog.show();
                break;
            case R.id.alarm_3_modify:
                dialog = new DialogAlarmSet(this, new AlarmDialogData(2,headerParam.getAlarmHour(2),
                        headerParam.getAlarmMinute(2),headerParam.getAlarmDate(2),headerParam.getAlarmRunTime(2)&0xFF,headerParam.getAlarmRunMode(2)));
                dialog.show();
                break;

        }
            dialog.setOnAlarmSet(new DialogAlarmSet.OnAlarmSet() {
                @Override
                public void onCancel() {
                    dialog.cancel();
                }

                @Override
                public void onConfirm(int alarmNum, int date, int hour, int minute, int runTime, int runMode) {
                    headerParam.setAlarmDate(alarmNum, date);
                    headerParam.setAlarmHour(alarmNum, hour);
                    headerParam.setAlarmMinute(alarmNum, minute);
                    headerParam.setAlarmRunTime(alarmNum, runTime);
                    headerParam.setAlarmRunMode(alarmNum, runMode);
                    headerParam.setAlarmRunPattern(alarmNum, 0);
                    headerParam.setAlarmOnOff(alarmNum, true);
                    txData(TxDatas.formatAlarmWrite(alarmNum, headerParam.getAlarmParam(alarmNum), runTime, date, hour, minute));
                    invalidate();
                    dialog.cancel();
                }
            });
    }

    private void txData(byte[] array) {
        bleManager.writeTxData(array);
    }
    /** 토스트 시작
     */
    ErrorToast errorToast;
    SuccessToast successToast;
    Toast defaultToast;
    private void showErrorToast(String txt) {
        if (errorToast != null) {
            errorToast.cancel();
        }
        errorToast = new ErrorToast(this);
        errorToast.showToast(txt, Toast.LENGTH_LONG);
    }

    private void showSuccessToast(String txt) {
        if (successToast != null) {
            successToast.cancel();
        }
        successToast = new SuccessToast(this);
        successToast.showToast(txt, Toast.LENGTH_SHORT);
    }

    private void showDefaultToast(String txt) {
        if (defaultToast != null) {
            defaultToast.cancel();
        }
        defaultToast = Toast.makeText(this,txt,Toast.LENGTH_SHORT);
        defaultToast.setGravity(Gravity.CENTER,0,0);
        defaultToast.show();
    }

    /** 토스트 종료
     */


    //private final static int HANDLE_INIT_EEPROM = 10;
    private final static int HANDLE_ICON_INVALIDATE = 0;
    private final static int HANDLE_IS_NOT_ADDRESS = 1;
    private final static int HANDLE_READ_PARAM_START = 4;
    private final static int HANDLE_READ_PARAM_SUCCEED = 5;
    private final static int HANDLE_READ_PARAM_FAIL = 6;
    private final static int HANDLE_ALARM_WRITE_SUCCESS = 10;


    // UI 제어 핸들러
    private UIHandler uiHandler = new UIHandler(this);
    private static class UIHandler extends Handler {
        private WeakReference<SlightSettingActivity> mActivity = null;
        UIHandler(SlightSettingActivity activity) {
            mActivity = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            SlightSettingActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case HANDLE_ICON_INVALIDATE:
                        activity.setConnectIcon(activity.bleManager.isBleServiceConnected());
                        break;
                    case HANDLE_IS_NOT_ADDRESS:
                        activity.showErrorToast(activity.getString(R.string.ss_alarm_try_init_ble));
                        break;
                    case HANDLE_READ_PARAM_START:
                        activity.showProgressDialog();
                        break;
                    case HANDLE_READ_PARAM_SUCCEED:
                        activity.invalidate();
                        activity.dismissProgressDialog();
                        break;
                    case HANDLE_READ_PARAM_FAIL:
                        activity.showErrorToast(activity.getString(R.string.ss_alarm_read_error));
                        activity.dismissProgressDialog();
                        break;
                    case HANDLE_ALARM_WRITE_SUCCESS:
                        activity.showSuccessToast(activity.getString(R.string.ss_alarm_write_success));
                        break;
                }
            }
        }
    }

    /** 진행상태 확인 Dialog 시작
     **/
    private ProgressDialog mProgressDialog;
    private void showProgressDialog() {
        mProgressDialog = ProgressDialog.show(this,"",getString(R.string.easy_progress_dialog));
    }
    private void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
    /** 진행상태 확인 Dialog 관련 종료
     **/

    private boolean isCalledReadParam = false;
    private void setIsCalledReadParam() {
        isCalledReadParam = true;
    }
    private boolean getIsCalledReadParam() {
        if (isCalledReadParam) {
            isCalledReadParam = false;
            return true;
        } else {
            return false;
        }
    }

    private Runnable taskReadParam = new Runnable() {
        @Override
        public void run() {
            if (bleManager.isBleServiceConnected()) {
                uiHandler.sendEmptyMessage(HANDLE_READ_PARAM_START);
                setIsCalledReadParam();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                txData(TxDatas.formatParamRead(LecHeaderParam.ALARM_LENGTH*3,LecHeaderParam.HEADER_ALARM_ORDER));
                // 2초간 파라미터를 읽었는지 확인
                for (int i=0;i<20;i++) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!isCalledReadParam) {
                        break;
                    }
                }
                Calendar calendar = Calendar.getInstance();
                txData(TxDatas.formatReloadTime(calendar));
                if (isCalledReadParam) {
                    // 파라미터 읽기 실패
                    uiHandler.sendEmptyMessage(HANDLE_READ_PARAM_FAIL);
                } else {
                    uiHandler.sendEmptyMessage(HANDLE_READ_PARAM_SUCCEED);
                }
            } else {
                uiHandler.sendEmptyMessage(HANDLE_READ_PARAM_FAIL);
            }
        }
    };
}
