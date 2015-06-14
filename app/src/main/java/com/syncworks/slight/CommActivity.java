package com.syncworks.slight;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.syncworks.slight.fragment_comm.BleSetFragment;
import com.syncworks.slight.fragment_comm.BrightFragment;
import com.syncworks.slight.fragment_comm.EffectFragment;
import com.syncworks.slight.fragment_comm.LedSelectFragment;
import com.syncworks.slight.fragment_comm.OnCommFragmentListener;
import com.syncworks.slight.widget.StepView;
import com.syncworks.slightpref.SLightPref;
import com.syncworks.vosami.blelib.BleConsumer;
import com.syncworks.vosami.blelib.BleManager;
import com.syncworks.vosami.blelib.BleNotifier;
import com.syncworks.vosami.blelib.BleUtils;
import com.syncworks.vosami.blelib.BluetoothLeService;
import com.syncworks.vosami.blelib.scanner.SlightScanCallback;
import com.syncworks.vosami.blelib.scanner.SlightScanner;


public class CommActivity extends ActionBarActivity implements BleConsumer, OnCommFragmentListener {
    private final static String TAG = "CommActivity";
    private final static int MAX_STEP = 4;  // 4단계가 최고 단계로 설정
    private int curStep;                    // 현재 단계 설정

    /* 장치 설정 확인*/
    SLightPref appPref = null;

    /**
     * 블루투스 관련 정의
     */
    private SlightScanner slightScanner;

    private BleManager bleManager;

    private BluetoothDevice mBluetoothDevice;

    private boolean isBleSupport = false;
    // End

    private BleSetFragment fragment1st;
    private LedSelectFragment fragment2nd;
    private BrightFragment fragment3rd;
    private EffectFragment fragment4th;

    // 단계 설정 View
    private StepView stepView;

    // 이름 설정 Dialog
    private AlertDialog mDialog = null;
    private String modifyName= null;
    private ProgressDialog mProgressDialog;
    // 이름 설정 진행 상태 확인
    private boolean isDisplayDialog = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 장치 설정 초기화
        appPref = new SLightPref(this);

        BleUtils bleUtils = new BleUtils(this);
        // 스마트폰이 Bluetooth LE 를 지원하는지 확인
        isBleSupport = bleUtils.isBluetoothLeSupported();
        if (isBleSupport) {

            // 블루투스가 꺼져 있다면
            if (!bleUtils.isBluetoothOn()) {
                // 블루투스를 켤지 물어본다
                bleUtils.askUserToEnableBluetoothIfNeeded();
            }
            // Bluetooth LE 지원되면 스캔 초기화
            slightScanner = SlightScanner.createScanner(this,10000,slightScanCallback);
            // Bluetooth LE 매니저 초기화
            bleManager = BleManager.getBleManager(this);
        } else {
            // 스마트폰이 Bluetooth LE 를 지원하지 않는다면 메시지 출력
            Toast.makeText(this, getString(R.string.ble_not_support),
                    Toast.LENGTH_LONG).show();
        }

        curStep = 1; // 1단계로 설정
        setContentView(R.layout.activity_comm);
        findView();
        createFragment();
    }

    private void findView() {
        stepView = (StepView) findViewById(R.id.comm_step_view);
        StepView.OnStepViewTouchListener stepViewTouchListener = new StepView.OnStepViewTouchListener() {
            @Override
            public void onStepViewEvent(int clickStep) {

            }
        };
        stepView.setOnStepViewTouchListener(stepViewTouchListener);
    }

    // 스캔 결과를 받아오는 인터페이스
    private SlightScanCallback slightScanCallback = new SlightScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.d(TAG,"Device"+device.getName());
            fragment1st.addList(device,rssi);
        }

        @Override
        public void onEnd() {
            scanStop();
        }
    };

    @Override
    protected void onResume() {
        // Bluetooth LE 를 지원한다면 장치 스캔 시작
        if (isBleSupport) {
            Log.d(TAG,"onResume");
            if (!slightScanner.getStateScanning()) {
                slightScanner.start();
            }
            // 블루투스 연결 매니저 설정
            bleManager = BleManager.getBleManager(this);
            bleManager.bind(this);
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        if (isBleSupport) {
            scanStop();
            bleManager.unbind(this);
        }
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comm, menu);
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

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_comm_back:
                curStep--;
                if (curStep < 1) {
                    curStep = 1;
                }
                changeStep(curStep);
                break;
            case R.id.btn_comm_next:
                curStep++;
                if (curStep > MAX_STEP) {
                    curStep = 2;
                }
                changeStep(curStep);
                break;
        }
    }

    private void createFragment() {
        String deviceName, deviceAddr;
        deviceName = appPref.getString(SLightPref.DEVICE_NAME);
        deviceAddr = appPref.getString(SLightPref.DEVICE_ADDR);
        fragment1st = BleSetFragment.newInstance(deviceName,deviceAddr);
        fragment2nd = LedSelectFragment.newInstance("","");
        fragment3rd = BrightFragment.newInstance("","");
        fragment4th = EffectFragment.newInstance("","");

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.ll_comm_fragment,fragment1st);
        fragmentTransaction.commit();
    }
    // Fragment 초기화
    private void startFragment(int step) {
        Log.d(TAG,"startFragment" + step);
        switch (step) {
            case 1:
                fragment1st.setTvCurrentDevice(
                        appPref.getString(SLightPref.DEVICE_NAME),
                        appPref.getString(SLightPref.DEVICE_ADDR));
                if (bleManager.getBleConnectState() != BluetoothLeService.STATE_DISCONNECTED) {
                    bleManager.bleDisconnect();
                }
                scanStart();
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
        }
    }

    // Fragment 변경 전
    private void endFragment(int step) {

    }

    // 액션 바의 타이틀 변경
    private void changeActionBarText(int step) {
        String titleText;
        switch(step) {
            case 1:
                titleText = getResources().getString(R.string.comm_activity_step_1);
                break;
            case 2:
                titleText = getResources().getString(R.string.comm_activity_step_2);
                break;
            case 3:
                titleText = getResources().getString(R.string.comm_activity_step_3);
                break;
            case 4:
                titleText = getResources().getString(R.string.comm_activity_step_4);
                break;
            default:
                titleText = "";
                break;
        }
        setTitle(titleText);
    }

    // 각 단계별 Fragment 변경
    private void changeFragment(int step) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (step) {
            case 1:
                startFragment(step);
                fragmentTransaction.replace(R.id.ll_comm_fragment, fragment1st);
                break;
            case 2:
                fragmentTransaction.replace(R.id.ll_comm_fragment, fragment2nd);
                break;
            case 3:
                fragmentTransaction.replace(R.id.ll_comm_fragment, fragment3rd);
                break;
            case 4:
                fragmentTransaction.replace(R.id.ll_comm_fragment, fragment4th);
                break;
            default:
                break;
        }
        fragmentTransaction.commit();
    }

    // 하단 단계 설정 View 변경
    private void changeStepView(int step) {
        stepView.setCurStep(step);
    }

    private void changeStep(int step) {
        // 허용 되지 않은 단계에 있다면 1로 초기화
        if (step <1 && step > MAX_STEP) {
            step = 1;
        }
        curStep = step;             // 현재 단계 저장
        changeActionBarText(step);  // 타이틀 바 제목 변경
        changeFragment(step);       // Fragment 변경
        changeStepView(step);       // 하단 단계 설정 View 변경
    }


    // 블루투스 서비스 연결
    @Override
    public void onBleServiceConnect() {
        Log.d(TAG, "서비스 연결됨");
        bleManager.setBleNotifier(new BleNotifier() {
            @Override
            public void bleConnected() {

            }

            @Override
            public void bleDisconnected() {
                Log.d(TAG,"Disconnected");
                if (isDisplayDialog) {
                    fragment1st.setTvCurrentDeviceName(modifyName);
                    scanStart();
                    isDisplayDialog = false;
                }
            }

            @Override
            public void bleServiceDiscovered() {

            }

            @Override
            public void bleDataAvailable() {

            }

            @Override
            public void bleDataWriteComplete() {
                Log.d(TAG,"Data Write Complete");
                if (isDisplayDialog) {
                    bleManager.bleDisconnect();
                }
            }
        });
    }

    // Fragments 와 통신
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onModifyName() {
        Log.d(TAG, "Modify");
        // 이름변경 대화창을 출력한 상태로 표시
        isDisplayDialog = true;
        bleManager.bleConnect(fragment1st.getDevAddr());
        mDialog = creteModifyNameDialog();
        mDialog.show();
    }

    @Override
    public void onScanStart() {
        Log.d(TAG, "ScanStart");
        scanStart();
    }

    @Override
    public void onScanStop() {
        Log.d(TAG, "ScanStop");
        fragment1st.displayScanButton(true);
    }

    @Override
    public void onSetDeviceItem(String devName, String devAddr) {
        Log.d(TAG, "dev" + devName + devAddr);
    }
    // 블루투스 스캔 시작
    private void scanStart() {
        Log.d(TAG,"scanStart");
        fragment1st.displayScanButton(false);
        fragment1st.clearList();
        if (!slightScanner.getStateScanning()) {
            slightScanner.start();
        }
    }
    // 블루투스 스캔 중지
    private void scanStop() {
        fragment1st.displayScanButton(true);
        if (slightScanner.getStateScanning()) {
            slightScanner.stop();
        }
    }

    private AlertDialog creteModifyNameDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle(getResources().getString(R.string.dialog_modify_name));
        final EditText inputName = new EditText(this);
        ab.setView(inputName);
        ab.setCancelable(true);

        ab.setPositiveButton(getString(R.string.dialog_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                modifyName = inputName.getText().toString();
                if (slightScanner.getStateScanning()) {
                    slightScanner.stop();
                }
                setConfirm(mDialog);
            }
        });

        ab.setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                setDismiss(mDialog);
            }
        });

        return ab.create();
    }

    /**
     * 다이얼로그 종료
     * @param dialog
     */
    private void setDismiss(Dialog dialog){
        if(dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    private void setConfirm(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        mProgressDialog = ProgressDialog.show(CommActivity.this,"","잠시만 기다려주세요.",true);
        new Thread(taskModName).start();
    }

    public final Handler handlerModName = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                bleManager.writeName(modifyName);
            } else if (msg.what == 2) {
                isDisplayDialog = false;
            }
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    };

    private Runnable taskModName = new Runnable() {
        @Override
        public void run() {
            for (int i=0;i<5;i++) {
                // 장치와 연결이 되었다면 종료
                if (bleManager.getBleConnectState() == BluetoothLeService.STATE_CONNECTED){
                    break;
                }
                bleManager.getBleConnectState();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (bleManager.getBleConnectState() == BluetoothLeService.STATE_CONNECTED) {
                handlerModName.sendEmptyMessage(1);
                Log.d(TAG,"Send Complete");
            } else {
                handlerModName.sendEmptyMessage(2);
                Log.d(TAG, "Not Send");
            }

        }
    };
}
