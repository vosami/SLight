package com.syncworks.slight;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
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

import com.syncworks.slightpref.SLightPref;
import com.syncworks.vosami.blelib.BluetoothDeviceAdapter;
import com.syncworks.vosami.blelib.BluetoothLeService;
import com.syncworks.vosami.blelib.scanner.SlightScanCallback;
import com.syncworks.vosami.blelib.scanner.SlightScanner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class BleSetActivity1 extends ActionBarActivity {
    // 디버그용 TAG
    private final static String TAG = BleSetActivity1.class.getSimpleName();

    // 다이얼로그
    ProgressDialog progressDialog = null;

    // 기타 정의
    public final static long SCAN_PERIOD = 10000;        // 블루투스 검색 시간 설정 10초
    private static final int REQUEST_ENABLE_BT = 1;
    private List<ScanFilter> scanFilters = new ArrayList<ScanFilter>();
    private ScanSettings scanSettings;

    // 블루투스 스캐너 관련
    private Handler scanHandler = new Handler();
    private boolean isScanning = false;		// 장치 검색 중인지 확인

    private SlightScanner scanner = null;

    // 장치 설정 확인
    SLightPref appPref = null;

    // 블루투스 서비스
    private BluetoothLeService slService;
    private BluetoothAdapter bluetoothAdapter;
    private List<BluetoothDevice> mDevice = new ArrayList<BluetoothDevice>();

    private boolean isEditing = false;		// 에디트 모드인지 확인

    // 장치 이름, 장치 주소 표시 TextView
    TextView tvDeviceName, tvDeviceAddr;
    Button btnBleScan, btnEdit;
    LinearLayout llDevName;//, llRadioPower;
    EditText etDevName;

    // 장치 리스트 표시 ListView
    ListView deviceList;
    BluetoothDeviceAdapter slAdapter;

    // 전송 명령어
    private final static int SEND_CONNECT = 0;
    private final static int SEND_DEVICE_NAME = 1;
    private final static int SEND_TX_POWER = 2;
    private final static int SEND_START = 0;
    private final static int SEND_COMPLETE = 3;
    private int sendCommand = 0;

    private boolean isWriteComplete = false;

    // 블루투스 서비스 연결
    private final ServiceConnection slServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            slService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!slService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
            }
            Log.d(TAG, "I get the SmartLight Service");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            slService = null;
        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ble_set);
        appPref = new SLightPref(this);
        findViews();

        // 블루투스 장치 검색
        Intent slServiceIntent = new Intent(BleSetActivity1.this, BluetoothLeService.class);
        bindService(slServiceIntent, slServiceConnection, BIND_AUTO_CREATE);
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

        registerReceiver(slUpdateReceiver, makeGattUpdateIntentFilter());
    }
    // Activity 가 더이상 보이지 않을 경우
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(slUpdateReceiver);
    }

    // Activity 가 종료될 때
    @Override
    protected void onDestroy() {
        if (isScanning) {       // 장치 검색 중이면...
//            scanLedDevice();    // 검색 중지
//            beginScanning();
            scanner.stop();
        }
        if (slServiceConnection != null)
            unbindService(slServiceConnection);
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
                break;
            case R.id.btn_edit:
                // 현재 수정 모드라면
                if (isEditing) {
                    btnEdit.setText(getString(R.string.btn_edit));
                    isEditing = false;
                    //showProgressDialog();
                    llDevName.setVisibility(View.GONE);
                    // 데이터 전송
                    sendBleSetting();
                }
                // 현재 수정 모드가 아니라면
                else {
                    btnEdit.setText(getString(R.string.btn_edit_complete));
                    isEditing = true;
                    llDevName.setVisibility(View.VISIBLE);
//                    llRadioPower.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_ble_scan:
//                scanLedDevice();
//                beginScanning();
                stopScanning();
                break;
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_WRITE_COMPLETE);

        return intentFilter;
    }

    private final BroadcastReceiver slUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            // 연결 완료 메시지를 받으면
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                Log.d(TAG, "Connected");
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                Log.d(TAG, "Disconnected");
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                Log.d(TAG, "Service Discovered");
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                Log.d(TAG, "Data Available");
            } else if (BluetoothLeService.ACTION_DATA_WRITE_COMPLETE.equals(action)) {
                Log.d(TAG, "Data Write Complete");
                isWriteComplete = true;
            }
        }
    };

    // 블루투스 장치를 스캔한다.
    private void scanLedDevice() {
        // 화면에 표시되는 UI Widget 은 핸들러 사용하여 처리
        final Handler handler = new Handler();
        final Runnable mHandlerRun = new Runnable() {
            @Override
            public void run() {
                if (isScanning) {
                    btnBleScan.setText(getString(R.string.btn_ble_stop));
                } else {
                    btnBleScan.setText(getString(R.string.btn_ble_scan));
                }
            }
        };
        // 쓰레드를 이용하여 장치 검색
        new Thread() {
            @Override
            public void run() {
                if (isScanning) {	// 현재 스캔 중이라면...
                    bluetoothAdapter.stopLeScan(mLeScanCallback);	// 스캔을 멈춘다.
                    isScanning = false;
                    handler.post(mHandlerRun);

                } else {			// 스캔중이 아니라면...
                    UUID[] uuids = new UUID[1];
                    uuids[0] = BluetoothLeService.UUID_LEC_SERVICE;
                    bluetoothAdapter.startLeScan(uuids,mLeScanCallback); // 스캔 시작
                    isScanning = true;
                    handler.post(mHandlerRun);
                    try {
                        Thread.sleep(SCAN_PERIOD);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    bluetoothAdapter.stopLeScan(mLeScanCallback);
                    isScanning = false;
                    handler.post(mHandlerRun);
                }
            }
        }.start();
    }
    // 블루투스 검색 결과 콜백 함수
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi,
                             byte[] scanRecord) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
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
    };


    // 진행 중 다이얼로그 보여주기
    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("전송중입니다.");
        // 다이얼로그 화면에 출력
        progressDialog.show();
    }
    // 진행 중 다이얼로그 해제
    private void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    SendHandler sendHandler = null;
    SendThread sendThread = null;
    private void sendBleSetting() {
        sendHandler = new SendHandler();
        sendThread = new SendThread();
        sendThread.start();
    }

    private class SendHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SEND_START:
//                    showProgressDialog();

                    break;
                case SEND_DEVICE_NAME:
                    if (slService.isAcquireNameService()) {
                        String mDevName = etDevName.getText().toString();
                        slService.writeDeviceName(mDevName);
                        isWriteComplete = false;
                        sendCommand++;
                    }
                    break;
                case SEND_COMPLETE:
                    sendThread.stopThread();
                    dismissProgressDialog();
                    break;
                default:
                    break;
            }
        }
    }

    private class SendThread extends Thread implements Runnable {
        private boolean isPlay = false;
        public SendThread() {
            isPlay = true;
        }
        public void stopThread() {
            isPlay = false;
        }

        @Override
        public void run() {
            super.run();
            int runningCount = 0;
            while (isPlay) {
                if (runningCount == 0) {
                    Message msg = sendHandler.obtainMessage();
                    msg.what = SEND_START;
                    sendHandler.sendMessage(msg);
                    // 스캔 중이라면 스캔 중지
                    if (isScanning) {
                        scanLedDevice();
                    }
                    // 연결 상태가 아니라면 연결 시도
                    if (slService.getStateConnect() != BluetoothLeService.STATE_CONNECTED) {
                        slService.connect(tvDeviceAddr.getText().toString());
                    }
                    runningCount++;
                    Log.d(TAG,"SEND_START");
                }
                else if (runningCount == 1) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (slService.isAcquireNameService()) {
                        String mDevName = etDevName.getText().toString();
                        isWriteComplete = false;
                        slService.writeDeviceName(mDevName);
                        runningCount++;
                    }
                    Log.d(TAG,"SEND_DEVICE_NAME");
                    /*Message msg = sendHandler.obtainMessage();
                    msg.what = SEND_DEVICE_NAME;
                    Log.d(TAG,"SEND_DEVICE_NAME");*/
//                    sendHandler.sendMessage(msg);
//                    runningCount++;
                }
                else if (runningCount == 2) {
                    if (isWriteComplete) {
                        slService.disconnect();
                        sendCommand = 0;
                        isWriteComplete = false;
                        runningCount++;
                        Message msg = sendHandler.obtainMessage();
                        msg.what = SEND_COMPLETE;
                        sendHandler.sendMessage(msg);
                    }
                }
            }
            Log.d(TAG,"SendThread End");
        }
    }

    private void beginScanning() {
        scanner.start();
    }

    private void stopScanning() {
        scanner.stop();
    }

    private SlightScanCallback scanCallback = new SlightScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.d(TAG,"address"+device.getAddress());
            if (device != null) {
                if (mDevice.indexOf(device) == -1) {
                    mDevice.add(device);
                    slAdapter.addRssi(rssi);   // RSSI 데이터 추가
                    slAdapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onEnd() {

        }
    };


}
