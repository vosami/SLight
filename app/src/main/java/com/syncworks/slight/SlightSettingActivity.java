package com.syncworks.slight;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.syncworks.define.Define;
import com.syncworks.define.Logger;
import com.syncworks.slight.dialog.DialogAlarmSet;
import com.syncworks.slight.util.CustomToast;
import com.syncworks.slightpref.SLightPref;
import com.syncworks.vosami.blelib.BleConsumer;
import com.syncworks.vosami.blelib.BleManager;
import com.syncworks.vosami.blelib.BleNotifier;
import com.syncworks.vosami.blelib.BluetoothLeService;
import com.syncworks.vosami.blelib.LecGattAttributes;

import java.lang.ref.WeakReference;


public class SlightSettingActivity extends ActionBarActivity implements BleConsumer {
    private final static String TAG = SlightSettingActivity.class.getSimpleName();

    // 액션 바 메뉴
    private Menu menu = null;
    //블루투스 매니저
    private BleManager bleManager;
    // 연결 상태 확인
    private boolean connectionState = false;

    private TextView alarmAPM[], alarmHour[], alarmMin[];
    private TextView alarmDate[][];
    private Switch alarmOnOff[];
    private TextView alarmRunTime[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        alarmOnOff = new Switch[3];
        alarmRunTime = new TextView[3];
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
        alarmOnOff[0] = (Switch) findViewById(R.id.alarm_1_onoff);
        alarmOnOff[1] = (Switch) findViewById(R.id.alarm_2_onoff);
        alarmOnOff[2] = (Switch) findViewById(R.id.alarm_3_onoff);
        alarmRunTime[0] = (TextView) findViewById(R.id.alarm_1_run_minute);
        alarmRunTime[1] = (TextView) findViewById(R.id.alarm_2_run_minute);
        alarmRunTime[2] = (TextView) findViewById(R.id.alarm_3_run_minute);
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
            bleManager.bleConnect(address);
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
                setConnectIcon(true);
            }

            @Override
            public void bleDisconnected() {
                Log.i(TAG, "Disconnected");
                // 연결 상태 아이콘 설정 - disconnect
                setConnectIcon(false);
            }

            @Override
            public void bleServiceDiscovered() {

            }

            @Override
            public void bleDataAvailable(String uuid, byte[] data) {
                Log.i(TAG, "bleDataAvailable");
                if (uuid.equals(LecGattAttributes.LEC_RX_UUID)) {
                    if (data[0] == 113) {
                        Logger.d(this,"test" );
                        Logger.d(this,"bleDataAvailable",data[1],data[2],data[3],data[4],data[5]);
                    }
                    switch (data[0]) {
                        case Define.TX_ACK:
                            switch (data[1]) {
                                case Define.TX_INIT_SET:
                                    uiHandler.sendEmptyMessage(HANDLE_INIT_EEPROM);
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

    public void onClick(View v) {
        DialogAlarmSet dialog;
        switch (v.getId()) {
            case R.id.alarm_1_modify:
                dialog = new DialogAlarmSet(this);
                dialog.show();
                break;
            case R.id.alarm_2_modify:
                dialog = new DialogAlarmSet(this);
                dialog.show();
                break;
            case R.id.alarm_3_modify:
                dialog = new DialogAlarmSet(this);
                dialog.show();
                break;
            /*case R.id.btn_init_rom:
                if (bleManager.getBleConnectState() == BluetoothLeService.STATE_CONNECTED) {
                    txData(TxDatas.formatInitEEPROM());
                }
                break;
            case R.id.btn_test_1:
                if (bleManager.getBleConnectState() == BluetoothLeService.STATE_CONNECTED) {
                    txData(TxDatas.formatSleep(false));
                    Calendar c = Calendar.getInstance();
                    txData(TxDatas.formatReloadTime(c));
                    txData(TxDatas.formatAlarmWrite(0,10,0xFF,14,17));
                    //txData(TxDatas.formatReadTime());
                }
                break;
            case R.id.btn_test_2:
                if (bleManager.getBleConnectState() == BluetoothLeService.STATE_CONNECTED) {
                    //txData(TxDatas.formatSaveDataPlace(1));
                    txData(TxDatas.formatFetchDataPlace(2));
                }
                break;*/
        }
    }

    private void txData(byte[] array) {
        bleManager.writeTxData(array);
    }
    private void showDefaultToast(String txt) {
        Toast toast = Toast.makeText(this,txt,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }


    private final static int HANDLE_INIT_EEPROM = 10;

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
                    case HANDLE_INIT_EEPROM:
                        activity.showDefaultToast("내부 저장소가 초기화되었습니다.");
                        break;
                }
            }
        }
    }
}
