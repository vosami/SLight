package com.syncworks.slight;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.syncworks.slight.util.CustomToast;
import com.syncworks.slightpref.SLightPref;
import com.syncworks.vosami.blelib.BluetoothDeviceAdapter;
import com.syncworks.vosami.blelib.scanner.SlightScanCallback;
import com.syncworks.vosami.blelib.scanner.SlightScanner;

import java.util.ArrayList;
import java.util.List;


public class BleSetActivity extends ActionBarActivity {
    // 디버그용 TAG
    private final static String TAG = BleSetActivity.class.getSimpleName();


    // 기타 정의
    public final static long SCAN_PERIOD = 10000;        // 블루투스 검색 시간 설정 10초
    private static final int REQUEST_ENABLE_BT = 1;

    // 블루투스 스캐너 관련
    private Handler scanHandler = new Handler();

    private SlightScanner scanner = null;

    // 장치 설정 확인
    SLightPref appPref = null;

    // 블루투스 서비스
    private BluetoothAdapter bluetoothAdapter;
    private List<BluetoothDevice> mDevice = new ArrayList<>();


    // 장치 이름, 장치 주소 표시 TextView
    TextView tvDeviceName, tvDeviceAddr;
    Button btnBleScan, btnEdit;
    LinearLayout llDevName;//, llRadioPower;
    EditText etDevName;

    // 장치 리스트 표시 ListView
    ListView deviceList;
    BluetoothDeviceAdapter slAdapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ble_set);
        appPref = new SLightPref(this);
        findViews();

        // 블루투스 장치 검색

        final BluetoothManager mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = mBluetoothManager.getAdapter();
        if (bluetoothAdapter == null) {
            String strNotSupport = getResources().getString(R.string.ble_not_support);
            Toast.makeText(this, strNotSupport, Toast.LENGTH_LONG).show();
            return;
        }
        scanner = SlightScanner.createScanner(this, 10000, scanCallback);

        // 장치 검색 시작
//        scanLedDevice();
        beginScanning();
	}
    // Activity 가 사용자가 보이도록 앞으로 올 경우
    @Override
    protected void onResume() {
        super.onResume();
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

    }
    // Activity 가 더이상 보이지 않을 경우
    @Override
    protected void onStop() {
        super.onStop();
    }

    // Activity 가 종료될 때
    @Override
    protected void onDestroy() {
        if (scanner.getStateScanning()) {       // 장치 검색 중이면...
            scanner.stop();
        }

        super.onDestroy();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_ble_set, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

    // 현재 레이아웃에서 위젯 등록
    private void findViews() {
        tvDeviceName = (TextView) findViewById(R.id.cur_device_name);
        tvDeviceAddr = (TextView) findViewById(R.id.cur_device_address);
        deviceList = (ListView) findViewById(R.id.list_device);
        btnBleScan = (Button) findViewById(R.id.btn_ble_scan);
        btnEdit = (Button) findViewById(R.id.btn_edit);
        llDevName = (LinearLayout) findViewById(R.id.device_name_ll);
        //llRadioPower = (LinearLayout) findViewById(R.id.radio_ll);
        etDevName = (EditText) findViewById(R.id.et_device_name);

        // 설정 읽어오기, 장치 이름, 장치 주소를 TextView 에 표시
        String mDeviceName = appPref.getString(SLightPref.DEVICE_NAME);
        String mDeviceAddr = appPref.getString(SLightPref.DEVICE_ADDR);
        tvDeviceName.setText(mDeviceName);
        tvDeviceAddr.setText(mDeviceAddr);

        // listView
        slAdapter = new BluetoothDeviceAdapter(this, R.layout.device_list_item, mDevice);
        deviceList.setAdapter(slAdapter);
        deviceList.setOnItemClickListener(itemClickListener);
    }

    /**
     * 검색된 장치 목록에서 아이템 클릭시 호출
     */
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String mDevName = ((BluetoothDevice)parent.getAdapter().getItem(position)).getName();
            String mDevAddr = ((BluetoothDevice)parent.getAdapter().getItem(position)).getAddress();
            tvDeviceName.setText(mDevName);
            tvDeviceAddr.setText(mDevAddr);
            savePreference();
        }
    };

    // 현재 설정된 장치 이름과, 주소를 Preference 에 저장
    private void savePreference() {
        appPref.putString(SLightPref.DEVICE_NAME, tvDeviceName.getText().toString());
        appPref.putString(SLightPref.DEVICE_ADDR, tvDeviceAddr.getText().toString());
    }


    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_back:
                this.finish();
                break;
            case R.id.btn_save:
                savePreference();
                CustomToast.middleBottom(this, getString(R.string.ble_set_save_preference));
                this.finish();
                break;
            case R.id.btn_edit:

                break;
            case R.id.btn_ble_scan:
                if (scanner.getStateScanning()) {
                    stopScanning();
                } else {
                    beginScanning();
                    btnBleScan.setText(getString(R.string.btn_ble_stop));
                }

                break;
        }
    }

    private void beginScanning() {
        slAdapter.clear();
        scanner.start();
    }

    private void stopScanning() {
        scanner.stop();
    }

    private SlightScanCallback scanCallback = new SlightScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG,"address"+device.getAddress());
                    if (device != null) {
                        if (mDevice.indexOf(device) == -1) {
                            mDevice.add(device);
                            slAdapter.addRssi(rssi);   // RSSI 데이터 추가
                            slAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });


        }

        @Override
        public void onEnd() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btnBleScan.setText(getString(R.string.btn_ble_scan));
                }
            });
        }
    };


}
