package com.syncworks.slight;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.syncworks.define.Define;
import com.syncworks.define.Logger;
import com.syncworks.slight.fragment_easy.BleSetFragment;
import com.syncworks.slight.fragment_easy.BrightFragment;
import com.syncworks.slight.fragment_easy.InstallFragment;
import com.syncworks.slight.fragment_easy.LedSelectFragment;
import com.syncworks.slight.fragment_easy.OnEasyFragmentListener;
import com.syncworks.slight.widget.StepView;
import com.syncworks.slightpref.SLightPref;
import com.syncworks.vosami.blelib.BleConsumer;
import com.syncworks.vosami.blelib.BleManager;
import com.syncworks.vosami.blelib.BleNotifier;
import com.syncworks.vosami.blelib.BleUtils;
import com.syncworks.vosami.blelib.BluetoothLeService;
import com.syncworks.vosami.blelib.scanner.SlightScanCallback;
import com.syncworks.vosami.blelib.scanner.SlightScanner;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;


public class EasyActivity extends ActionBarActivity implements OnEasyFragmentListener,BleConsumer {

    private SLightPref appPref  = null;

    private final static int MAX_STEP = 5;  // 5단계가 최고 단계로 설정
    StepView stepView;
    Button btnPrev, btnNext;


    private boolean isBleSupported = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appPref = new SLightPref(this);
        setContentView(R.layout.activity_easy);
        bleCheck();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        findViews();
        createFragment();
    }

    @Override
    protected void onResume() {
        // 블루투스 LE를 지원한다면 스캔 시작
        if (isBleSupported) {
            Logger.d(this, "onResume");
            // 블루투스 연결 매니저 설정
            bleManager.bind(this);
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        if (isBleSupported) {
            scanStop();
            bleManager.unbind(this);
        }
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_easy, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_connect) {
            bleManager.bleDisconnect();
        } else if (id == R.id.action_disconnect) {
            bleManager.bleConnect(appPref.getString(SLightPref.DEVICE_ADDR));
        } else if (id == R.id.action_help) {
            Logger.d(this,"action_help");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (bleManager.getBleConnectState() == BluetoothLeService.STATE_CONNECTED) {
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(false);
        } else {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    // 뷰 등록
    private void findViews() {
        stepView = (StepView) findViewById(R.id.easy_step_view);
        btnPrev = (Button) findViewById(R.id.easy_btn_back);
        btnNext = (Button) findViewById(R.id.easy_btn_next);
        StepView.OnStepViewTouchListener stepViewTouchListener = new StepView.OnStepViewTouchListener() {
            @Override
            public void onStepViewEvent(int clickStep) {
                Logger.v(this, "OnStepView", clickStep);
            }
        };
        stepView.setOnStepViewTouchListener(stepViewTouchListener);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.easy_btn_back:
                Logger.d(this,"btn_back");
                changeStep(stepView.getStep()- 1);
                break;
            case R.id.easy_btn_next:
                Logger.d(this,"btn_next");
                int curStep = stepView.getStep();
                if (curStep == 1) {
                    showProgressDialog();
                    new Thread(taskConnect).start();
                } else {
                    changeStep(stepView.getStep() + 1);
                }
                break;
        }
    }
    // ActionBar 의 연결 상태 아이콘 설정
    private void setConnectIcon() {
        invalidateOptionsMenu();
    }



    /**********************************************************************************************
     * 쓰레드 관련 설정 시작
     *********************************************************************************************/
    private final static int HANDLE_FRAG_CONNECTED = 1;
    private final static int HANDLE_NOT_CONNECTED = 2;

    private final static int HANDLE_ICON_INVALIDATE = 99;

    private void txCounterInit() {
        bleManager.writeTxData(TxDatas.formatInitCount());
    }

    private void txBrightAll(int bright) {
        bleManager.writeTxData(TxDatas.formatBrightAll(bright));
    }



    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TOAST_NOT_CONNECT,TOAST_TEST})
    public @interface EasyToast {}
    public final static int TOAST_NOT_CONNECT = 0;
    public final static int TOAST_TEST = 1;

    private void displayToast(@EasyToast int id) {
        if (id == TOAST_NOT_CONNECT) {
            Toast.makeText(this,getString(R.string.easy_ble_not_connect),Toast.LENGTH_LONG).show();
        }
    }

    // UI 제어 핸들러
    private UIHandler uiHandler = new UIHandler(this);

    private static class UIHandler extends Handler {
        private WeakReference<EasyActivity> mActivity = null;

        UIHandler(EasyActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            EasyActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case HANDLE_FRAG_CONNECTED:
                        // 진행 상태 대화창을 닫음
                        activity.dismissProgressDialog();
                        //activity.changeStep(2);
                        break;
                    case HANDLE_NOT_CONNECTED:
                        // 진행 상태 대화창을 닫음
                        activity.dismissProgressDialog();
                        // 연결 안됨 표시
                        activity.displayToast(TOAST_NOT_CONNECT);
                        break;
                    case HANDLE_ICON_INVALIDATE:
                        activity.setConnectIcon();
                        break;
                }
            }

        }
    }

    private Runnable taskConnect = new Runnable() {
        @Override
        public void run() {
            int connectState;
            Logger.d(this,"Try Connect");
            bleManager.bleConnect(fragment1st.getDevAddr());
            // 10초간 연결 상태 확인
            for (int i=0;i<10;i++) {
                connectState = bleManager.getBleConnectState();
                // 10초간 연결 상태 확인
                if (connectState == BluetoothLeService.STATE_CONNECTED) {
                    // 연결되었으면 연결상태 확인 종료
                    break;
                } else if (connectState == BluetoothLeService.STATE_CONNECTING){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    break;
                }
            }

            connectState = bleManager.getBleConnectState();
            if (connectState == BluetoothLeService.STATE_CONNECTED) {
                Logger.d(this, "Connected");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // TODO 타이머 초기화 명령으로 수정
                txCounterInit();
                txBrightAll(Define.OP_CODE_MIN - 1);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                txBrightAll(0);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                txBrightAll(Define.OP_CODE_MIN-1);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                txBrightAll(0);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                txBrightAll(Define.OP_CODE_MIN-1);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                txBrightAll(0);

                uiHandler.sendEmptyMessage(HANDLE_FRAG_CONNECTED);
            } else {
                Logger.d(this,"Not connected");
                bleManager.bleDisconnect();
                uiHandler.sendEmptyMessage(HANDLE_NOT_CONNECTED);
            }
        }
    };

    private Runnable taskModifyNameConnect = new Runnable() {
        @Override
        public void run() {
            int connectState;
            Logger.d(this,"Try Connect");
            bleManager.bleConnect(fragment1st.getDevAddr());
            // 10초간 연결 상태 확인
            for (int i=0;i<10;i++) {
                connectState = bleManager.getBleConnectState();
                // 10초간 연결 상태 확인
                if (connectState == BluetoothLeService.STATE_CONNECTED) {
                    // 연결되었으면 연결상태 확인 종료
                    break;
                } else if (connectState == BluetoothLeService.STATE_CONNECTING){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    break;
                }
            }
            connectState = bleManager.getBleConnectState();
            if (connectState == BluetoothLeService.STATE_CONNECTED) {

            } else {

            }
        }
    };



    /**********************************************************************************************
     * 쓰레드 관련 설정 종료
     *********************************************************************************************/

    /**********************************************************************************************
     * 다이알로그 설정 시작
     *********************************************************************************************/

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

    /** 이름 변경 Dialog 시작
     */
    private AlertDialog modifyNameDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle(getString(R.string.easy_mod_name_dialog));
        final EditText inputName = new EditText(this);
        ab.setView(inputName);

        return ab.create();
    }

    /** 이름 변경 Dialog 종료
     */


    /**********************************************************************************************
     * 다이알로그 설정 종료
     *********************************************************************************************/

    /**********************************************************************************************
     * Fragment 관련 설정 시작
     *********************************************************************************************/

    private InstallFragment fragment0th;
    private BleSetFragment fragment1st;
    private LedSelectFragment fragment2nd;
    private BrightFragment fragment3rd;
//    private EffectFragment fragment4th;

    private void toggleView(boolean isVisible) {
        if (isVisible) {
            stepView.setVisibility(View.VISIBLE);
            btnPrev.setVisibility(View.VISIBLE);
        } else {
            stepView.setVisibility(View.GONE);
            btnPrev.setVisibility(View.GONE);
        }
    }

    private void createFragment() {
        fragment0th = InstallFragment.newInstance();
        fragment1st = BleSetFragment.newInstance();
        fragment2nd = LedSelectFragment.newInstance();
        fragment3rd = BrightFragment.newInstance();
//        fragment4th = EffectFragment.newInstance();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // 0단계를 보지 않겠다고 설정되어 있다면
        if (appPref.getBoolean(SLightPref.FRAG_INSTALL_NOT_SHOW)) {
            toggleView(true);
            changeActionBarText(1);
            fragmentTransaction.add(R.id.easy_ll_fragment, fragment1st);
        }
        // 0단계를 보겠다고 설정되어 있다면
        else {
            toggleView(false);
            changeActionBarText(0);
            //fragmentTransaction.add(R.id.easy_ll_fragment, fragment0th);
            fragmentTransaction.add(R.id.easy_ll_fragment, fragment2nd);
        }

        fragmentTransaction.commit();
    }

    // Fragment 초기화
    private void startFragment(int step) {
        switch (step) {
            case 1:
                if (bleManager.getBleConnectState() != BluetoothLeService.STATE_DISCONNECTED) {
                    bleManager.bleDisconnect();
                }
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
        }
    }

    // 액션바의 타이틀 변경
    private void changeActionBarText(int step) {
        if (step >= 0 && step <=5) {
            String titleText = getResources().getStringArray(R.array.easy_activity_step)[step];
            // 타이틀 변경
            setTitle(titleText);
            // 스텝 변경
            stepView.setStep(step);
        }
    }


    // 각 단계별 Fragment 변경
    private void changeFragment(int step) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (step) {
            case 0:
                // 뒤로 버튼, 스텝뷰 안보이게 설정
                toggleView(false);
                fragmentTransaction.replace(R.id.easy_ll_fragment, fragment0th);
                break;
            case 1:
                startFragment(step);
                // 뒤로 버튼, 스텝뷰 보이게 설정
                toggleView(true);
                fragmentTransaction.replace(R.id.easy_ll_fragment, fragment1st);
                break;
            case 2:
                fragmentTransaction.replace(R.id.easy_ll_fragment, fragment2nd);
                break;
            case 3:
                /*if (fragment2nd.isRGB()) {
                    fragment3rd.setRGB(true);
                    fragment3rd.setParamRGB(fragment2nd.getRGBSelect());
                } else {
                    fragment3rd.setRGB(false);
                    fragment3rd.setParamLED(fragment2nd.getLedSelect());
                }*/
                fragmentTransaction.replace(R.id.easy_ll_fragment, fragment3rd);
                break;
            case 4:
                /*if (fragment2nd.isRGB()) {
                    fragment4th.setRGB(true);
                    fragment4th.setParamRGB(fragment2nd.getRGBSelect());
                } else {
                    fragment4th.setRGB(false);
                    fragment4th.setParamLED(fragment2nd.getLedSelect());
                }
                fragment4th.setInit();
                fragmentTransaction.replace(R.id.easy_ll_fragment, fragment4th);*/
                break;
            default:
                break;
        }
        fragmentTransaction.commit();
    }

    private void changeStep(int step) {
        changeActionBarText(step);  // 타이틀 바 제목 변경 , 스텝 변경
        changeFragment(step);       // Fragment 변경
    }

    /**********************************************************************************************
     * Fragment 관련 설정 종료
     *********************************************************************************************/

    @Override
    public void onModifyName() {
        new Thread(taskModifyNameConnect).start();
    }

    @Override
    public void onScanStart() {
        scanStart();
    }

    @Override
    public void onScanStop() {

    }

    @Override
    public void onSetDeviceItem(String devName, String devAddr) {

    }

    @Override
    public void onSelectLed(int ledNum, boolean state) {

    }

    @Override
    public void onSelectRGB(int ledNum, boolean state) {

    }

    @Override
    public void onBrightRGB(int ledNum, int bright) {

    }

    @Override
    public void onBrightLed(int ledNum, int bright) {

    }

    @Override
    public void onEffect(int effect, int param) {

    }

    @Override
    public void onColorDialog() {

    }

    @Override
    public void onNotDialog() {

    }

    @Override
    public void onFrag1Start() {
        scanStart();
    }

    @Override
    public void onFrag1End() {
        scanStop();
    }

    /**
     * 블루투스 관련 설정 시작
     */
    private BleManager bleManager;

    private SlightScanner slightScanner;

    private void bleCheck() {
        // 스마트폰이 블루투스 LE 를 지원하는지 확인
        BleUtils bleUtils = new BleUtils(this);
        isBleSupported = bleUtils.isBluetoothLeSupported();
        if (isBleSupported) {
            // 블루투스가 꺼져 있다면
            if (!bleUtils.isBluetoothOn()) {
                // 블루투스를 켤지 물어본다.
                bleUtils.askUserToEnableBluetoothIfNeeded();
            }
            // 블루투스 LE 지원되면 스캔 초기화
            slightScanner = SlightScanner.createScanner(this,10000,slightScanCallback );
            // Bluetooth LE 매니저 초기화
            bleManager = BleManager.getBleManager(this);
        } else {
            // 스마트폰이 Bluetooth LE 를 지원하지 않는다면 메시지 출력
            Toast.makeText(this, getString(R.string.ble_not_support),
                    Toast.LENGTH_LONG).show();
        }
    }

    // 스캔 결과를 받아오는 인터페이스
    private SlightScanCallback slightScanCallback = new SlightScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            Logger.d(this,"scanResult");
            fragment1st.addList(device,rssi);
        }

        @Override
        public void onEnd() {
            scanStop();
        }
    };

    // 블루투스 스캔 시작
    private void scanStart() {
        Logger.d(this, "scanStart");
        if (fragment1st.isResumed()) {
            fragment1st.displayScanButton(false);
            fragment1st.clearList();
        }
        if (!slightScanner.getStateScanning()) {
            slightScanner.start();
        }
    }
    // 블루투스 스캔 중지
    private void scanStop() {
        Logger.d(this,"scanStop");
        if (fragment1st.isResumed()) {
            fragment1st.displayScanButton(true);
        }
        if (slightScanner.getStateScanning()) {
            slightScanner.stop();
        }
    }

    @Override
    public void onBleServiceConnect() {
        bleManager.setBleNotifier(new BleNotifier() {
            @Override
            public void bleConnected() {
                Logger.d(this,"Connected");
                uiHandler.sendEmptyMessage(HANDLE_ICON_INVALIDATE);
            }

            @Override
            public void bleDisconnected() {
                Logger.d(this,"Disconnected");
                uiHandler.sendEmptyMessage(HANDLE_ICON_INVALIDATE);
            }

            @Override
            public void bleServiceDiscovered() {

            }

            @Override
            public void bleDataAvailable() {

            }

            @Override
            public void bleDataWriteComplete() {

            }
        });
    }
    /**
     * 블루투스 관련 설정 종료
     */


}
