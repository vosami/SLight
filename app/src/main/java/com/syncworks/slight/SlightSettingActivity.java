package com.syncworks.slight;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.syncworks.slight.util.CustomToast;
import com.syncworks.slightpref.SLightPref;
import com.syncworks.vosami.blelib.BleConsumer;
import com.syncworks.vosami.blelib.BleManager;
import com.syncworks.vosami.blelib.BleNotifier;
import com.syncworks.vosami.blelib.BluetoothLeService;


public class SlightSettingActivity extends ActionBarActivity implements BleConsumer {
    private final static String TAG = SlightSettingActivity.class.getSimpleName();



    // 액션 바 메뉴
    private Menu menu = null;
    //블루투스 매니저
    private BleManager bleManager;
    // 연결 상태 확인
    private boolean connectionState = false;

    // View 등록
    private Button btnDeviceName;
    private EditText etDeviceName;
    private Button btnCloseTime;
    private EditText etCloseTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slight_setting);

        btnDeviceName = (Button) findViewById(R.id.bt_set_device_name);
        etDeviceName = (EditText) findViewById(R.id.et_set_device_name);
        btnCloseTime = (Button) findViewById(R.id.bt_set_close_time);
        etCloseTime = (EditText) findViewById(R.id.et_set_close_time);
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
                Log.d(TAG, "연결되었습니다.");
            } else {
                SLightPref appPref = new SLightPref(this);
                String address = appPref.getString(SLightPref.DEVICE_ADDR);
                bleManager.bleConnect(address);
                CustomToast.middleTop(this, "연결 시도중입니다.");
                Log.d(TAG,"연결 X");
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (connectionState) {
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_connect));
        } else {
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_disconnect));
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
            }

            @Override
            public void bleDataWriteComplete() {
                Log.i(TAG, "bleDataWriteComplete");
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_set_device_name:
                String deviceName = etDeviceName.getText().toString();
                if (deviceName.length() > 0) {
                    if (connectionState) {
                        bleManager.writeName(deviceName);
                        CustomToast.middleBottom(this, "SLight 이름을 재설정하였습니다.");
                    } else {
                        CustomToast.middleBottom(this, "SLight 와 연결할 수 없습니다.");
                    }
                } else {
                    CustomToast.middleBottom(this, "장치 이름을 입력하세요.");
                }
                break;
            case R.id.bt_set_close_time:
                break;
        }
    }
}
