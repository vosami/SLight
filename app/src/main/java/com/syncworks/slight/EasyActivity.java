package com.syncworks.slight;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.syncworks.define.Logger;
import com.syncworks.slight.fragment_easy.BleSetFragment;
import com.syncworks.slight.fragment_easy.BrightFragment;
import com.syncworks.slight.fragment_easy.LedSelectFragment;
import com.syncworks.slight.fragment_easy.OnEasyFragmentListener;
import com.syncworks.slight.widget.StepView;
import com.syncworks.vosami.blelib.BleConsumer;
import com.syncworks.vosami.blelib.BleManager;
import com.syncworks.vosami.blelib.BleNotifier;
import com.syncworks.vosami.blelib.BleUtils;
import com.syncworks.vosami.blelib.BluetoothLeService;
import com.syncworks.vosami.blelib.scanner.SlightScanCallback;
import com.syncworks.vosami.blelib.scanner.SlightScanner;


public class EasyActivity extends ActionBarActivity implements OnEasyFragmentListener,BleConsumer {

    private final static int MAX_STEP = 5;  // 4단계가 최고 단계로 설정
    StepView stepView;

    private boolean isBleSupported = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            scanStart();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // 뷰 등록
    private void findViews() {
        stepView = (StepView) findViewById(R.id.easy_step_view);
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
                break;
            case R.id.easy_btn_next:
                Logger.d(this,"btn_next");
                break;
        }
    }




    /**********************************************************************************************
     * Fragment 관련 설정 시작
     *********************************************************************************************/

    private BleSetFragment fragment1st;
    private LedSelectFragment fragment2nd;
    private BrightFragment fragment3rd;
//    private EffectFragment fragment4th;

    private void createFragment() {
        fragment1st = BleSetFragment.newInstance();
        fragment2nd = LedSelectFragment.newInstance();
        fragment3rd = BrightFragment.newInstance();
//        fragment4th = EffectFragment.newInstance();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.easy_ll_fragment, fragment1st);
        fragmentTransaction.commit();
    }

    // Fragment 초기화
    private void startFragment(int step) {
        switch (step) {
            case 1:
                if (bleManager.getBleConnectState() != BluetoothLeService.STATE_DISCONNECTED) {
                    bleManager.bleDisconnect();
                }
                //fragment2nd.initToggleBtn();
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

    // 액션바의 타이틀 변경
    private void changeActionBarText(int step) {
        String[] titleText;
        titleText = getResources().getStringArray(R.array.easy_activity_step);
        setTitle(titleText[step]);
    }

    // 하단 단계 설정 View 변경
    private void changeStepView(int step) {
        stepView.setStep(step);
    }

    // 각 단계별 Fragment 변경
    private void changeFragment(int step) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (step) {
            case 1:
                startFragment(step);
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
        // 허용 되지 않은 단계에 있다면 1로 초기화
        if (step <1 && step > MAX_STEP) {
            step = 1;
        }
        changeActionBarText(step);  // 타이틀 바 제목 변경
        changeFragment(step);       // Fragment 변경
        changeStepView(step);       // 하단 단계 설정 View 변경
    }

    /**********************************************************************************************
     * Fragment 관련 설정 종료
     *********************************************************************************************/

    @Override
    public void onModifyName() {

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
            fragment1st.addList(device,rssi);
        }

        @Override
        public void onEnd() {
            scanStop();
        }
    };

    // 블루투스 스캔 시작
    private void scanStart() {
        fragment1st.displayScanButton(false);
        fragment1st.clearList();
        slightScanner.start();
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

    @Override
    public void onBleServiceConnect() {
        bleManager.setBleNotifier(new BleNotifier() {
            @Override
            public void bleConnected() {

            }

            @Override
            public void bleDisconnected() {

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

    /** 진행상태 확인 Dialog 관련 시작
     **/
    private ProgressDialog mProgressDialog;
    private void showProgressDialog() {
        mProgressDialog = ProgressDialog.show(this,"test",getString(R.string.easy_progress_dialog));
    }
    private void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
    /** 진행상태 확인 Dialog 관련 종료
     **/
}
