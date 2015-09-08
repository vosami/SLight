package com.syncworks.slight;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.syncworks.define.Define;
import com.syncworks.define.Logger;
import com.syncworks.leddata.LedDataSeries;
import com.syncworks.leddata.LedSelect;
import com.syncworks.slight.fragment_easy.BleSetFragment;
import com.syncworks.slight.fragment_easy.BrightFragment;
import com.syncworks.slight.fragment_easy.EffectFragment;
import com.syncworks.slight.fragment_easy.InstallFragment;
import com.syncworks.slight.fragment_easy.LedSelectFragment;
import com.syncworks.slight.fragment_easy.OnEasyFragmentListener;
import com.syncworks.slight.fragment_easy.SaveFragment;
import com.syncworks.slight.services.MicrophoneTask;
import com.syncworks.slight.util.ByteLengthFilter;
import com.syncworks.slight.util.ErrorToast;
import com.syncworks.slight.util.LecHeaderParam;
import com.syncworks.slight.util.SuccessToast;
import com.syncworks.slight.widget.StepView;
import com.syncworks.slightpref.SLightPref;
import com.syncworks.vosami.blelib.BleConsumer;
import com.syncworks.vosami.blelib.BleManager;
import com.syncworks.vosami.blelib.BleNotifier;
import com.syncworks.vosami.blelib.BleUtils;
import com.syncworks.vosami.blelib.BluetoothLeService;
import com.syncworks.vosami.blelib.LecGattAttributes;
import com.syncworks.vosami.blelib.resolver.BluetoothCrashResolver;
import com.syncworks.vosami.blelib.scanner.SlightScanCallback;
import com.syncworks.vosami.blelib.scanner.SlightScanner;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.List;


public class EasyActivity extends ActionBarActivity implements OnEasyFragmentListener,BleConsumer {

    private SLightPref appPref  = null;

    private BluetoothCrashResolver crashResolver = null;

    private final static int MAX_STEP = 5;  // 5단계가 최고 단계로 설정
    StepView stepView;
    Button btnPrev, btnNext, btnStep5, btnFinish;
    private LecHeaderParam lecHeader  = null;
    //private byte[] lecHeader = new byte[64];

    // LED 선택, 패턴 데이터
    private LedDataSeries ledDataSeries;


    private boolean isBleSupported = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //crashResolver = new BluetoothCrashResolver(this);
        appPref = new SLightPref(this);
        lecHeader = new LecHeaderParam();
        ledDataSeries = new LedDataSeries();
        setContentView(R.layout.activity_easy);
        bleCheck();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        findViews();
        createFragment();

        if (isBleSupported) {
            Logger.d(this, "onResume");
            // 블루투스 연결 매니저 설정
            bleManager.bind(this);
        }
    }

    @Override
    protected void onDestroy() {
        if (isBleSupported) {
            if (slightScanner.getStateScanning()) {
                slightScanner.stop();
            }
            bleManager.unbind(this);
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        //crashResolver.setUpdateNotifier(updateNotifier);
        // 블루투스 LE를 지원한다면 스캔 시작

        super.onResume();
    }

    @Override
    protected void onStop() {

        //crashResolver.setUpdateNotifier(null);
        super.onStop();
    }
/*
    public BluetoothCrashResolver.UpdateNotifier updateNotifier = new BluetoothCrashResolver.UpdateNotifier() {
        public void dataUpdated() {
            runOnUiThread(new Runnable() {
                public void run() {
                    //updateView();
                }
            });
        }
    };
*/
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
            if (isAvailConnect()) {
                bleManager.bleConnect(appPref.getString(SLightPref.DEVICE_ADDR));
            }
        } else if (id == R.id.action_help) {
            Logger.d(this,"action_help");
            switch (stepView.getStep()) {
                case 1:
                    fragment1st.showOverLay();
                    break;
                case 2:
                    fragment2nd.showOverLay();
                    break;
                case 3:
                    fragment3rd.showOverLay();
                    break;
                case 4:
                    fragment4th.showOverLay();
                    break;
                case 5:
                    fragment5th.showOverLay();
            }
        } else if (id == android.R.id.home) {
            this.finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.d(this, "ActivityResult");
        if (requestCode == BleUtils.REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                bleCheck();
                if (isBleSupported) {
                    Logger.d(this, "onResume");
                    // 블루투스 연결 매니저 설정
                    bleManager.bind(this);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 뷰 등록
    private void findViews() {
        stepView = (StepView) findViewById(R.id.easy_step_view);
        btnPrev = (Button) findViewById(R.id.easy_btn_back);
        btnNext = (Button) findViewById(R.id.easy_btn_next);
        btnStep5 = (Button) findViewById(R.id.easy_btn_step5);
        btnFinish = (Button) findViewById(R.id.easy_btn_finish);
        StepView.OnStepViewTouchListener stepViewTouchListener = new StepView.OnStepViewTouchListener() {
            @Override
            public void onStepViewEvent(int clickStep) {
                Logger.v(this, "OnStepView", clickStep);
                switch (clickStep) {
                    case 1:
                        if (bleManager.getBleConnectState() == BluetoothLeService.STATE_DISCONNECTED) {
                            changeStep(1);
                        } else {
                            showBackDialog();
                        }
                        break;
                    case 2:
                        if (bleManager.getBleConnectState() == BluetoothLeService.STATE_DISCONNECTED) {
                            showDefaultToast(getString(R.string.easy_press_next_btn));
                        } else {
                            changeStep(2);
                        }
                        break;
                    case 3:
                        if (bleManager.getBleConnectState() == BluetoothLeService.STATE_DISCONNECTED) {
                            showDefaultToast(getString(R.string.easy_press_next_btn));
                        } else {
                            changeStep(3);
                        }
                        break;
                    case 4:
                        if (bleManager.getBleConnectState() == BluetoothLeService.STATE_DISCONNECTED) {
                            showDefaultToast(getString(R.string.easy_press_next_btn));
                        } else {
                            changeStep(4);
                        }
                        break;
                    case 5:
                        if (bleManager.getBleConnectState() == BluetoothLeService.STATE_DISCONNECTED) {
                            showDefaultToast(getString(R.string.easy_press_next_btn));
                        } else {
                            showProgressDialog();
                            new Thread(taskReadOption).start();
                        }
                        break;
                }
            }
        };
        stepView.setOnStepViewTouchListener(stepViewTouchListener);
    }

    // 4단계에서 2단계로 넘어갈 때 선택되었던 LED 를 초기화하여 완료 상태로 설정
    private void ledComplete() {
        for (int i=0;i<Define.NUMBER_OF_SINGLE_LED;i++) {
            if (ledDataSeries.ledSelect.getLed(i) == LedSelect.SelectType.SELECTED) {
                ledDataSeries.ledSelect.setLed(i, LedSelect.SelectType.COMPLETED);
            }
        }
        for (int i=0;i<Define.NUMBER_OF_COLOR_LED;i++) {
            if (ledDataSeries.ledSelect.getRgb(i) == LedSelect.SelectType.SELECTED) {
                ledDataSeries.ledSelect.setRgb(i, LedSelect.SelectType.COMPLETED);
            }
        }
    }


    public void onClick(View v) {
        int curStep = stepView.getStep();
        switch (v.getId()) {
            case R.id.easy_btn_back:
                Logger.d(this,"btn_back");
                if (curStep == 2) {
                    showBackDialog();
                } else {
                    changeStep(stepView.getStep() - 1);
                }
                break;
            case R.id.easy_btn_next:
                Logger.d(this,"btn_next");
                if (curStep == 1) {
                    scanStop();
                    // 대기 창 띄움
                    showProgressDialog();
                    new Thread(taskConnect).start();
                } else if (curStep == 4) {
                    // 완료 하면 버튼 초기화
                    ledComplete();
                    // 효과 선택 초기화
                    fragment4th.initEffectNum();
                    changeStep(2);
                } else if (curStep == 5) {
                    // 완료 하면 버튼 초기화
                    ledComplete();
                    // 효과 선택 초기화
                    fragment4th.initEffectNum();
                    changeStep(2);
                } else {
                    changeStep(stepView.getStep() + 1);
                }
                break;
            case R.id.easy_btn_step5:
                showProgressDialog();
                new Thread(taskReadOption).start();
                /*// 선택된 LED 완료 표시
                ledComplete();
                // 효과 선택 초기화
                fragment4th.initEffectNum();
                changeStep(5);*/
                break;
            case R.id.easy_btn_finish:
                this.finish();
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
    private static String versionName = null;

    //마이크 입력 태스크
    private MicrophoneTask micTask = null;

    private final static int HANDLE_FRAG_START = 1;
    private final static int HANDLE_FRAG_CONNECTED = 2;
    private final static int HANDLE_NOT_CONNECTED = 3;

    private final static int HANDLE_MOD_NAME_CONNECT = 5;
    private final static int HANDLE_MOD_NAME=6;
    private final static int HANDLE_MOD_NAME_ERROR = 7;
    private final static int HANDLE_NOT_AVAILABLE_CONNECT = 8;
    private final static int HANDLE_COMPLETE_CONNECT_TEST = 9;
    private final static int HANDLE_ERROR_CONNECT_TEST = 10;
    private final static int HANDLE_CHANGE_STEP_5 = 12;
    private final static int HANDLE_CHANGE_STEP_5_ERROR = 13;
    private final static int HANDLE_SAVE_DATA = 15;
    private final static int HANDLE_SAVE_DATA_ERROR = 16;
    private final static int HANDLE_FETCH_DATA = 20;
    private final static int HANDLE_FETCH_DATA_ERROR = 21;
    private final static int HANDLE_RUN_MODE = 25;
    private final static int HANDLE_RUN_MODE_ERROR = 26;

    private final static int HANDLE_ACKNOWLEDGE = 50;
    private final static int HANDLE_SCAN_START = 60;
    private final static int HANDLE_SCAN_STOP = 61;
    private final static int HANDLE_SCAN_RESULT = 62;

    private final static int HANDLE_ICON_INVALIDATE = 99;

    private void txCounterInit() {
        txData(TxDatas.formatInitCount());
    }

    private void txBrightAll(int bright) {
        txData(TxDatas.formatBrightAll(bright));
    }


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TOAST_NOT_CONNECT,TOAST_TEST})
    public @interface EasyToast {}
    public final static int TOAST_NOT_CONNECT = 0;
    public final static int TOAST_TEST = 1;

    private void displayToast(@EasyToast int id) {
        if (id == TOAST_NOT_CONNECT) {
            showErrorToast(getString(R.string.easy_ble_not_connect));
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
            final EasyActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case HANDLE_FRAG_START:
                        activity.changeStep(2);
                        break;
                    case HANDLE_FRAG_CONNECTED:
                        // 진행 상태 대화창을 닫음
                        activity.dismissProgressDialog();
                        /*
                        activity.micTask = new MicrophoneTask();
                        activity.micTask.setOnSoundListener(new MicrophoneTask.OnSoundListener() {
                            @Override
                            public void onSoundChange(int sound) {
                                if (sound > 191) {
                                    sound = 191;
                                }
                                activity.txData(TxDatas.formatTxSound(sound));
                                Logger.d(this,"Sound",sound);
                            }
                        });
                        activity.micTask.micStart();
                        */
                        break;
                    case HANDLE_NOT_CONNECTED:
                        // 진행 상태 대화창을 닫음
                        activity.dismissProgressDialog();
                        // 연결 안됨 표시
                        activity.displayToast(TOAST_NOT_CONNECT);
                        break;
                    case HANDLE_MOD_NAME_CONNECT:
                        // 진행 상태 대화창을 닫음
                        activity.dismissProgressDialog();
                        // 이름 변경 대화창 오픈
                        activity.showModNameDialog();
                        break;
                    case HANDLE_MOD_NAME:
                        activity.bleManager.bleDisconnect();
                        activity.fragment1st.setTvCurrentDevice(activity.modName,
                                activity.appPref.getString(SLightPref.DEVICE_ADDR),
                                activity.appPref.getString(SLightPref.DEVICE_VERSION));
                        // 진행 상태 대화창을 닫음
                        activity.dismissProgressDialog();
                        activity.scanStart();
                        activity.showSuccessToast(activity.getString(R.string.easy_mod_name_success));
                        break;
                    case HANDLE_MOD_NAME_ERROR:
                        activity.bleManager.bleDisconnect();
                        activity.dismissProgressDialog();
                        activity.showErrorToast(activity.getString(R.string.easy_mod_name_error));
                        //activity.scanStart();
                        break;
                    case HANDLE_NOT_AVAILABLE_CONNECT:
                        activity.bleManager.bleDisconnect();
                        activity.dismissProgressDialog();
                        String showMsg = versionName + "\n" +activity.getString(R.string.easy_connect_unavailable);
                        activity.showErrorToast(showMsg);
                        break;
                    case HANDLE_COMPLETE_CONNECT_TEST:
                        activity.bleManager.bleDisconnect();
                        activity.dismissProgressDialog();
                        String showMsg2 = versionName + "\n" +activity.getString(R.string.easy_connect_available);
                        activity.showSuccessToast(showMsg2);
                        break;
                    case HANDLE_ERROR_CONNECT_TEST:
                        activity.bleManager.bleDisconnect();
                        activity.dismissProgressDialog();
                        activity.showErrorToast(activity.getString(R.string.easy_ble_not_connect));
                        break;
                    case HANDLE_CHANGE_STEP_5:
                        activity.dismissProgressDialog();
                        activity.changeStep(5);
                        Logger.d(this, "ChangeStep5", activity.lecHeader.getOffTime());
                        Logger.d(this,"ChangeStep5",activity.lecHeader.isSleepLedBlink());
                        break;
                    case HANDLE_CHANGE_STEP_5_ERROR:
                        activity.dismissProgressDialog();
                        activity.showErrorToast(activity.getString(R.string.easy_save_step_5_error));
                        break;
                    case HANDLE_SAVE_DATA:
                        activity.dismissProgressDialog();
                        activity.showSuccessToast(activity.getString(R.string.easy_save_complete));
                        break;
                    case HANDLE_SAVE_DATA_ERROR:
                        activity.dismissProgressDialog();
                        activity.showErrorToast(activity.getString(R.string.easy_save_data_error));
                        break;
                    case HANDLE_FETCH_DATA:
                        activity.dismissProgressDialog();
                        activity.showSuccessToast(activity.getString(R.string.easy_load_complete));
                        break;
                    case HANDLE_FETCH_DATA_ERROR:
                        activity.dismissProgressDialog();
                        activity.showErrorToast(activity.getString(R.string.easy_save_data_error));
                        break;
                    case HANDLE_RUN_MODE:
                        activity.dismissProgressDialog();
                        //Logger.d(this, "HANDLE_RUN_MODE", activity.dataNum);
                        if (activity.trmRunMode == 0) {
                            activity.showSuccessToast(activity.getString(R.string.easy_radio_success_default));
                        } else if (activity.trmRunMode == 1) {
                            activity.showSuccessToast(activity.getString(R.string.easy_radio_success_sequential));
                        } else {
                            activity.showSuccessToast(activity.getString(R.string.easy_radio_success_random));
                        }
                        //activity.showSuccessToast(activity.getString(R.string.easy_load_complete));
                        break;
                    case HANDLE_RUN_MODE_ERROR:
                        activity.dismissProgressDialog();
                        activity.showErrorToast(activity.getString(R.string.easy_save_data_error));
                        break;
                    case HANDLE_ACKNOWLEDGE:
                        activity.showDefaultToast(activity.getString(R.string.easy_ack));
                        break;
                    case HANDLE_SCAN_START:
                        if (activity.fragment1st.isVisible()) {
                            activity.fragment1st.displayScanButton(false);
                            activity.fragment1st.clearList();
                        }
                        break;
                    case HANDLE_SCAN_STOP:
                        if (activity.fragment1st.isVisible()) {
                            activity.fragment1st.displayScanButton(true);
                        }
                        break;
                    case HANDLE_SCAN_RESULT:
                        if (activity.fragment1st.isVisible()) {
                            String addr = msg.getData().getString("addr");
                            activity.fragment1st.addList((byte[]) msg.obj,addr,msg.arg1);
                        }
                        break;
                    case HANDLE_ICON_INVALIDATE:
                        if (activity.bleManager.getBleConnectState() == BluetoothLeService.STATE_DISCONNECTED) {
                            activity.changeStep(1);
                        }
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
            Logger.d(this, "Try Connect");
            if (isAvailConnect()) {
                bleManager.bleConnect(appPref.getString(SLightPref.DEVICE_ADDR));
                // 5초간 연결 상태 확인
                for (int i=0;i<10;i++) {
                    connectState = bleManager.getBleConnectState();
                    // 10초간 연결 상태 확인
                    if (connectState == BluetoothLeService.STATE_CONNECTED) {
                        if (bleManager.isAcquireServices()) {
                            // 연결되었으면 연결상태 확인 종료
                            break;
                        } else {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (connectState == BluetoothLeService.STATE_CONNECTING){
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        break;
                    }
                }
            }

            connectState = bleManager.getBleConnectState();
            if (connectState == BluetoothLeService.STATE_CONNECTED && bleManager.isAcquireServices()) {
                Logger.d(this, "Connected");
                uiHandler.sendEmptyMessage(HANDLE_FRAG_START);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Calendar calendar = Calendar.getInstance();
                byte[] c = TxDatas.formatReloadTime(calendar);
                txData(c);
                // LED 깨우기
                txData(TxDatas.formatSleep(true));
                // 분 이벤트 종료
                txData(TxDatas.formatMinuteTimerStart(false));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 타이머 초기화 명령으로 수정
                txBrightAll(Define.OP_CODE_MIN - 1);
                txCounterInit();
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

    // 장치 이름 변경 연결
    private Runnable taskModifyNameConnect = new Runnable() {
        @Override
        public void run() {
            int connectState;
            Logger.d(this,"Try Connect");
            if (isAvailConnect()) {
                bleManager.bleConnect(fragment1st.getDevAddr());
                // 10초간 연결 상태 확인
                for (int i = 0; i < 10; i++) {
                    connectState = bleManager.getBleConnectState();
                    // 10초간 연결 상태 확인
                    if (connectState == BluetoothLeService.STATE_CONNECTED) {
                        // 연결되었으면 연결상태 확인 종료
                        break;
                    } else if (connectState == BluetoothLeService.STATE_CONNECTING) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        break;
                    }
                }
            }
            connectState = bleManager.getBleConnectState();
            if (connectState == BluetoothLeService.STATE_CONNECTED) {
                uiHandler.sendEmptyMessage(HANDLE_MOD_NAME_CONNECT);
            } else {
                uiHandler.sendEmptyMessage(HANDLE_NOT_CONNECTED);
            }
        }
    };
    // 장치 이름 변경 태스크에서 호출되었는지 확인
    private boolean isCalledModifyName = false;
    private void setIsCalledModifyName() {
        isCalledModifyName = true;
    }
    private boolean getIsCalledModifyName() {
        if (isCalledModifyName) {
            isCalledModifyName = false;
            return true;
        } else {
            return false;
        }
    }

    // 장치 이름 변경
    private Runnable taskModifyName = new Runnable() {
        @Override
        public void run() {
            if (bleManager.getBleConnectState() == BluetoothLeService.STATE_CONNECTED) {
                bleManager.writeName(modName);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 장치 이름 변경 태스크에서 호출되었음을 설정
                setIsCalledModifyName();
                bleManager.getName();
                for (int i=0;i<5;i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!isCalledModifyName) {
                        break;
                    }
                }
                if (getIsCalledModifyName()) {
                    uiHandler.sendEmptyMessage(HANDLE_MOD_NAME_ERROR);
                }
            } else {
                // 연결 종료 이름 변경 할 수 없음
                showErrorToast(getString(R.string.easy_ble_not_connect));
            }
        }
    };

    private boolean isStartTestConnect = false;

    // 연결 테스트
    private Runnable taskTestConnect = new Runnable() {
        @Override
        public void run() {
            int connectState;
            if (isAvailConnect()) {
                bleManager.bleConnect(fragment1st.getDevAddr());
                // 10초간 연결 상태 확인
                for (int i = 0; i < 10; i++) {
                    connectState = bleManager.getBleConnectState();
                    // 10초간 연결 상태 확인
                    if (connectState == BluetoothLeService.STATE_CONNECTED) {
                        // 연결되었으면 연결상태 확인 종료
                        break;
                    } else if (connectState == BluetoothLeService.STATE_CONNECTING) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        break;
                    }
                }
            }
            if (bleManager.getBleConnectState() == BluetoothLeService.STATE_CONNECTED) {
                isStartTestConnect = true;
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                bleManager.getDevVersion();
                for (int i=0;i<50;i++) {
                    if (isStartTestConnect) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        break;
                    }
                }
                if (isStartTestConnect) {
                    uiHandler.sendEmptyMessage(HANDLE_ERROR_CONNECT_TEST);
                }
            } else {
                uiHandler.sendEmptyMessage(HANDLE_ERROR_CONNECT_TEST);
            }
        }
    };


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

    // 스마트라이트 옵션 읽어오기
    private Runnable taskReadOption = new Runnable() {
        @Override
        public void run() {
            if (bleManager.getBleConnectState() == BluetoothLeService.STATE_CONNECTED) {
                // 파라미터 읽어오기 시작
                setIsCalledReadParam();
                txData(TxDatas.formatParamRead(16, 16));
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
                if (isCalledReadParam) {
                    // 읽어올 수 없음
                    uiHandler.sendEmptyMessage(HANDLE_CHANGE_STEP_5_ERROR);
                } else {
                    // 파라미터 읽기 완료
                    setIsCalledReadParam();
                    txData(TxDatas.formatParamRead(16,32));
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
                    if (isCalledReadParam) {
                        // 읽어올 수 없음
                        uiHandler.sendEmptyMessage(HANDLE_CHANGE_STEP_5_ERROR);
                    } else {
                        // 파라미터 읽기 완료
                        setIsCalledReadParam();
                        txData(TxDatas.formatParamRead(16,48));
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
                        if (isCalledReadParam) {
                            // 읽어올 수 없음
                            uiHandler.sendEmptyMessage(HANDLE_CHANGE_STEP_5_ERROR);
                        } else {
                            // 파라미터 읽기 완료
                            uiHandler.sendEmptyMessage(HANDLE_CHANGE_STEP_5);
                        }
                    }
                }
            } else {
                //TODO 연결 안되었을 경우
            }
        }
    };

    private int dataNum = 0;
    private boolean isRxSaveData = false;

    private Runnable taskSaveData = new Runnable() {
        @Override
        public void run() {
            if (bleManager.getBleConnectState() == BluetoothLeService.STATE_CONNECTED) {
                Calendar calendar = Calendar.getInstance();
                int option = 0x80 | ((dataNum<<3) & 0x18);
                txData(TxDatas.formatAlarmWrite(2,option,60,0x7F,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE)));
                //txData(TxDatas.formatAlarmWrite(0x88,60,0xFF,2,17));
                txData(TxDatas.formatSaveDataPlace(dataNum));
                for (int i=0;i<50;i++) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (isRxSaveData) {
                        break;
                    }
                }
                /*for (int i=0;i<Define.NUMBER_OF_SINGLE_LED;i++) {
                    for (int j=0;j<4;j++) {
                        byte[] txDatas = TxDatas.formatSaveData(i, dataNum * Define.NUMBER_OF_SINGLE_LED + i,j);
                        isRxSaveData = false;
                        txData(txDatas);
                        //txData(TxDatas.formatSaveData(i,dataNum*Define.NUMBER_OF_SINGLE_LED+i));
                        for (int k = 0; k < 20; k++) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            // 수신 데이터가 있다면 중지
                            if (isRxSaveData) {
                                break;
                            }
                        }
                        // 수신 데이터가 없다면 중지 후 에러 발생
                        if (!isRxSaveData) {
                            break;
                        }
                    }
                    // 수신 데이터가 없다면 중지 후 에러 발생
                    if (!isRxSaveData) {
                        break;
                    }
                }*/
                if (isRxSaveData) {
                    uiHandler.sendEmptyMessage(HANDLE_SAVE_DATA);
                } else {
                    uiHandler.sendEmptyMessage(HANDLE_SAVE_DATA_ERROR);
                }
            } else {
                uiHandler.sendEmptyMessage(HANDLE_SAVE_DATA_ERROR);
            }

        }
    };


    private boolean isRxFetchData = false;

    private Runnable taskFetchData = new Runnable() {

        @Override
        public void run() {
            if (bleManager.getBleConnectState() == BluetoothLeService.STATE_CONNECTED) {
                /*for (int i = 0; i < Define.NUMBER_OF_SINGLE_LED; i++) {
                    byte[] txDatas = TxDatas.formatFetchData(dataNum, i);
                    isRxFetchData = false;
                    txData(txDatas);
                    for (int k = 0; k < 20; k++) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // 수신 데이터가 있다면 중지
                        if (isRxFetchData) {
                            break;
                        }
                    }
                    // 수신 데이터가 없다면 중지 후 에러 발생
                    if (!isRxFetchData) {
                        break;
                    }
                }*/
                txData(TxDatas.formatFetchDataPlace(dataNum));
                for (int k = 0; k < 50; k++) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 수신 데이터가 있다면 중지
                    if (isRxFetchData) {
                        break;
                    }
                }
                if (isRxFetchData) {
                    txCounterInit();
                    uiHandler.sendEmptyMessage(HANDLE_FETCH_DATA);
                } else {
                    uiHandler.sendEmptyMessage(HANDLE_FETCH_DATA_ERROR);
                }
            } else {
                uiHandler.sendEmptyMessage(HANDLE_FETCH_DATA_ERROR);
            }
        }
    };

    private int trmRunMode = 0;

    private Runnable taskRunMode = new Runnable() {
        @Override
        public void run() {
            if (bleManager.getBleConnectState() == BluetoothLeService.STATE_CONNECTED) {
                txData(TxDatas.formatSleep(true));
                txData(TxDatas.formatMinuteTimerStart(false));
                if (trmRunMode == 0) {
                    txData(TxDatas.formatFetchDataPlace(dataNum));
                    txCounterInit();
                    for (int k = 0; k < 50; k++) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // 수신 데이터가 있다면 중지
                        if (isRxFetchData) {
                            break;
                        }
                    }
                } else {
                    txData(TxDatas.formatFetchDataPlace(0));
                    txCounterInit();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    txData(TxDatas.formatFetchDataPlace(1));
                    txCounterInit();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    txData(TxDatas.formatFetchDataPlace(2));
                    txCounterInit();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    txData(TxDatas.formatFetchDataPlace(dataNum));
                    for (int k = 0; k < 50; k++) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // 수신 데이터가 있다면 중지
                        if (isRxFetchData) {
                            break;
                        }
                    }
                }
                if (isRxFetchData) {
                    txCounterInit();
                    txData(TxDatas.formatMinuteTimerStart(true));
                    //Logger.d(this, "HANDLE_RUN_MODE Start", trmRunMode);
                    uiHandler.sendEmptyMessage(HANDLE_RUN_MODE);
                } else {
                    uiHandler.sendEmptyMessage(HANDLE_RUN_MODE_ERROR);
                }
            } else {
                uiHandler.sendEmptyMessage(HANDLE_RUN_MODE_ERROR);
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

    private AlertDialog backDialog = null;
    private void showBackDialog() {
        backDialog = backDialog();
        backDialog.show();
    }

    private AlertDialog backDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle(getString(R.string.easy_back_notify));
        ab.setCancelable(true);
        ab.setPositiveButton(getString(R.string.str_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeStep(1);
                backDialog.dismiss();
            }
        });

        ab.setNegativeButton(getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                backDialog.dismiss();
            }
        });
        return ab.create();
    }

    /** 이름 변경 Dialog 시작
     */
    // 이름 변경 변수 전달
    private String modName = null;
    private AlertDialog modNameDialog = null;
    private void showModNameDialog() {
        modNameDialog = modifyNameDialog();
        modNameDialog.show();
    }
    private AlertDialog modifyNameDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle(getString(R.string.easy_mod_name_dialog));
        final EditText inputName = new EditText(this);
        String name = appPref.getString(SLightPref.DEVICE_NAME);
        name = name.replace(" ", "");
        inputName.setText(name);
        inputName.setSingleLine();
        inputName.requestFocus();
        InputFilter[] filterArray = new InputFilter[]{new ByteLengthFilter(13,"KSC5601")};
        //filterArray[0] = new InputFilter.LengthFilter(14);
        inputName.setFilters(filterArray);
        LinearLayout llName = new LinearLayout(this);
        llName.setOrientation(LinearLayout.VERTICAL);
        llName.addView(inputName);
        //ab.setView(inputName);
        TextView tvLengthFilter = new TextView(this);
        tvLengthFilter.setText(getString(R.string.easy_name_length_filter));
        tvLengthFilter.setPadding(5,5,5,5);
        llName.addView(tvLengthFilter);

        ab.setView(llName);
        ab.setCancelable(true);
        ab.setPositiveButton(getString(R.string.str_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                modName = inputName.getText().toString() + " ";
                modNameDialog.dismiss();
                showProgressDialog();
                new Thread(taskModifyName).start();
            }
        });

        ab.setNegativeButton(getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                modNameDialog.dismiss();
                if (bleManager.getBleConnectState() != BluetoothLeService.STATE_DISCONNECTED) {
                    bleManager.bleDisconnect();
                }
            }
        });

        return ab.create();
    }

    /** 이름 변경 Dialog 종료
     */

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
        successToast.showToast(txt,Toast.LENGTH_SHORT);
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
    private EffectFragment fragment4th;
    private SaveFragment fragment5th;

    // 다음단계 버튼 텍스트 변경
    private void setBtnText(int step) {
        switch (step) {
            case 0:
                btnNext.setText(getString(R.string.str_next));
                stepView.clearAnimation();
                stepView.setVisibility(View.GONE);
                btnPrev.clearAnimation();
                btnPrev.setVisibility(View.GONE);
                btnPrev.invalidate();
                btnStep5.clearAnimation();
                btnStep5.setVisibility(View.GONE);
                btnFinish.clearAnimation();
                btnFinish.setVisibility(View.GONE);
                break;
            case 1:
                btnNext.setText(getString(R.string.str_next));
                stepView.clearAnimation();
                stepView.setVisibility(View.VISIBLE);
                btnPrev.clearAnimation();
                btnPrev.setVisibility(View.VISIBLE);
                btnStep5.clearAnimation();
                btnStep5.setVisibility(View.GONE);
                btnFinish.clearAnimation();
                btnFinish.setVisibility(View.GONE);
                break;
            case 2:
                btnNext.setText(getString(R.string.str_next));
                stepView.setVisibility(View.VISIBLE);
                btnPrev.setVisibility(View.VISIBLE);
                btnStep5.setVisibility(View.GONE);
                btnFinish.setVisibility(View.GONE);
                break;
            case 3:
                btnNext.setText(getString(R.string.str_next));
                stepView.setVisibility(View.VISIBLE);
                btnPrev.setVisibility(View.VISIBLE);
                btnStep5.setVisibility(View.GONE);
                btnFinish.setVisibility(View.GONE);
                break;
            case 4:
                btnNext.setText(getString(R.string.easy_step_complete));
                stepView.setVisibility(View.VISIBLE);
                btnPrev.setVisibility(View.VISIBLE);
                btnStep5.setVisibility(View.VISIBLE);
                btnFinish.setVisibility(View.GONE);
                break;
            case 5:
                btnNext.setText(getString(R.string.easy_step_complete));
                stepView.setVisibility(View.VISIBLE);
                btnPrev.setVisibility(View.GONE);
                btnStep5.setVisibility(View.GONE);
                btnFinish.setVisibility(View.VISIBLE);
                break;
        }
    }

    FragmentManager fragmentManager = null;
    FragmentTransaction fragmentTransaction = null;

    private void createFragment() {
        fragment0th = InstallFragment.newInstance();
        fragment1st = BleSetFragment.newInstance();
        fragment2nd = LedSelectFragment.newInstance();
        fragment3rd = BrightFragment.newInstance();
        fragment4th = EffectFragment.newInstance();
        fragment5th = SaveFragment.newInstance();

        fragment2nd.setLedSelect(this.ledDataSeries.ledSelect);
        fragment2nd.setLedOptions(this.ledDataSeries.ledOptions);
        fragment3rd.setLedSelect(this.ledDataSeries.ledSelect);
        fragment3rd.setLedOptions(this.ledDataSeries.ledOptions);
        fragment4th.setLedSelect(this.ledDataSeries.ledSelect);
        //fragment4th.setLedOption(this.ledDataSeries.ledOptions);
        fragment5th.setLecHeader(this.lecHeader);

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        // 0단계를 보지 않겠다고 설정되어 있다면
        if (appPref.getBoolean(SLightPref.FRAG_INSTALL_NOT_SHOW)) {
            setBtnText(1);
            changeActionBarText(1);
            fragmentTransaction.add(R.id.easy_ll_fragment, fragment1st);
        }
        // 0단계를 보겠다고 설정되어 있다면
        else {
            setBtnText(0);
            changeActionBarText(0);
            fragmentTransaction.add(R.id.easy_ll_fragment, fragment0th);
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
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        /*if (fragment0th.isVisible()) {
            fragmentTransaction.remove(fragment0th);
        } else if (fragment1st.isVisible()) {
            fragmentTransaction.remove(fragment1st);
        } else if (fragment2nd.isVisible()) {
            fragmentTransaction.remove(fragment2nd);
        } else if (fragment3rd.isVisible()) {
            fragmentTransaction.remove(fragment3rd);
        } else if (fragment4th.isVisible()) {
            fragmentTransaction.remove(fragment4th);
        } else if (fragment5th.isVisible()) {
            fragmentTransaction.remove(fragment5th);
        }*/
        switch (step) {
            case 0:
                fragmentTransaction.replace(R.id.easy_ll_fragment, fragment0th);
                break;
            case 1:
                startFragment(step);
                // LED 선택 초기화
                ledDataSeries.ledSelect.init();
                for (int i=0;i<Define.NUMBER_OF_SINGLE_LED;i++) {
                    ledDataSeries.ledOptions[i].init();
                }
                fragmentTransaction.replace(R.id.easy_ll_fragment, fragment1st);
                break;
            case 2:
                fragmentTransaction.replace(R.id.easy_ll_fragment, fragment2nd);
                break;
            case 3:
                fragmentTransaction.replace(R.id.easy_ll_fragment, fragment3rd);
                break;
            case 4:
                fragmentTransaction.replace(R.id.easy_ll_fragment, fragment4th);
                break;
            case 5:
                fragmentTransaction.replace(R.id.easy_ll_fragment, fragment5th);
                break;
            default:
                break;
        }

        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        Logger.d(this, "fragmentTransaction");
    }

    private void changeStep(int step) {
        setBtnText(step);       // 다음단계 버튼 텍스트 변경
        changeActionBarText(step);  // 타이틀 바 제목 변경 , 스텝 변경
        changeFragment(step);       // Fragment 변경
    }

    /**********************************************************************************************
     * Fragment 관련 설정 종료
     *********************************************************************************************/

    private final static byte[] offPattern = {(byte)Define.OP_START,0,0,0,(byte)Define.OP_END,0};

    @Override
    public void onTestConnect() {
        if (slightScanner.getStateScanning()) {
            scanStop();
        }
        showProgressDialog();
        new Thread(taskTestConnect).start();
    }

    @Override
    public void onModifyName() {
        showProgressDialog();
        // 스캔 중이라면 중지
        if (slightScanner.getStateScanning()) {
            scanStop();
        }
        // 연결 쓰레드 시작
        new Thread(taskModifyNameConnect).start();
    }

    @Override
    public void onScanStart() {
        scanStart();
    }

    @Override
    public void onScanStop() {
        scanStop();
    }

    @Override
    public void onSetDeviceItem(String devName, String devAddr) {

    }

    @Override
    public void onSelectLed(int ledNum, boolean state) {
        Logger.d(this,"onSelectLed",state);
        if (state) {
            //ledDataSeries.ledSelect.setLed(ledNum, LedSelect.SelectType.SELECTED);
            ledDataSeries.ledExeDatas[ledNum].init();
            ledDataSeries.ledOptions[ledNum].init();
            txPattern(ledNum, ledDataSeries.ledExeDatas[ledNum].toByteArray(ledDataSeries.ledOptions[ledNum]));
            txData(TxDatas.formatInitCount());
        } else {
            //ledDataSeries.ledSelect.setLed(ledNum, LedSelect.SelectType.DEFAULT);
            txPattern(ledNum, offPattern);
            txData(TxDatas.formatInitCount());
        }
    }

    @Override
    public void onSelectRGB(int ledNum, boolean state) {
        int redLedNum = ledNum * 3;
        int greenLedNum = ledNum * 3 + 1;
        int blueLedNum = ledNum * 3 + 2;
        Logger.d(this,"onSelectRGB",state);
        if (state) {
            //ledDataSeries.ledSelect.setRgb(ledNum, LedSelect.SelectType.SELECTED);
            // 3개 LED 를 모두 초기화
            ledDataSeries.ledExeDatas[redLedNum].init();
            ledDataSeries.ledExeDatas[greenLedNum].init();
            ledDataSeries.ledExeDatas[blueLedNum].init();
            // 옵션 초기화
            ledDataSeries.ledOptions[redLedNum].init();
            ledDataSeries.ledOptions[greenLedNum].init();
            ledDataSeries.ledOptions[blueLedNum].init();
            // 3개 LED 밝기 데이터 전달
            txPattern(redLedNum, ledDataSeries.ledExeDatas[redLedNum].toByteArray(ledDataSeries.ledOptions[ledNum]));
            txPattern(greenLedNum, ledDataSeries.ledExeDatas[greenLedNum].toByteArray(ledDataSeries.ledOptions[ledNum]));
            txPattern(blueLedNum, ledDataSeries.ledExeDatas[blueLedNum].toByteArray(ledDataSeries.ledOptions[ledNum]));

        } else {
            // 3개 LED 밝기 데이터 전달
            txPattern(redLedNum, offPattern);
            txPattern(greenLedNum, offPattern);
            txPattern(blueLedNum, offPattern);
            txData(TxDatas.formatInitCount());
        }
    }

    @Override
    public void onBrightRGB(int ledNum, int bright) {

    }

    @Override
    public void onBrightLed(int ledNum, int bright) {
        ledDataSeries.ledOptions[ledNum].setRatioBright(bright * 100 / (Define.OP_CODE_MIN - 1));
        List<byte[]> mDataList = TxDatas.formatMemWrite(ledNum,
                ledDataSeries.ledExeDatas[ledNum].toByteArray(ledDataSeries.ledOptions[ledNum]));
        int dataCount = mDataList.size();
        for (int i=0;i<dataCount;i++) {
            txData(mDataList.get(i));
        }
        //txPattern(ledNum, ledDataSeries.ledExeDatas[ledNum].toByteArray(ledDataSeries.ledOptions[ledNum]));
        txData(TxDatas.formatInitCount());
    }

    @Override
    public void onEffectStart(boolean isStart) {
        if (isStart) {
            txData(TxDatas.formatSleep(false));
            // 분 이벤트 종료
            txData(TxDatas.formatMinuteTimerStart(false));
        } else {
            txData(TxDatas.formatSleep(true));
        }
    }

    @Override
    public void onEffect(int effect, int patternTime, int randomTime, int startTime) {
        txData(TxDatas.formatSleep(false));
        // 분 이벤트 종료
        txData(TxDatas.formatMinuteTimerStart(false));
        for (int i=0;i<Define.NUMBER_OF_SINGLE_LED;i++) {
            if (ledDataSeries.ledSelect.getLed(i) == LedSelect.SelectType.SELECTED) {
                ledDataSeries.ledOptions[i].setDelayStartOption(startTime);
                ledDataSeries.ledOptions[i].setPatternDelayOption(patternTime);
                ledDataSeries.ledOptions[i].setRandomDelayOption(randomTime);
                ledDataSeries.ledExeDatas[i].setEffect(effect, patternTime, randomTime, startTime);
                // LED 선택 프래그먼트에 효과 값 전달
                fragment2nd.setPattern(false, i, effect);
                List<byte[]> mDataList = TxDatas.formatMemWrite(i,
                        ledDataSeries.ledExeDatas[i].toByteArray(ledDataSeries.ledOptions[i]));
                int dataCount = mDataList.size();
                for (int j=0;j<dataCount;j++) {
                    txData(mDataList.get(j));
                }
                //txPattern(i, ledDataSeries.ledExeDatas[i].toByteArray(ledDataSeries.ledOptions[i]));
                txData(TxDatas.formatInitCount());

            }
        }
        for (int i=0;i<Define.NUMBER_OF_COLOR_LED;i++) {
            if (ledDataSeries.ledSelect.getRgb(i) == LedSelect.SelectType.SELECTED) {
                ledDataSeries.ledOptions[i*3].setDelayStartOption(startTime);
                ledDataSeries.ledOptions[i*3 + 1].setDelayStartOption(startTime);
                ledDataSeries.ledOptions[i*3 + 2].setDelayStartOption(startTime);
                ledDataSeries.ledOptions[i*3].setPatternDelayOption(patternTime);
                ledDataSeries.ledOptions[i*3 + 1].setPatternDelayOption(patternTime);
                ledDataSeries.ledOptions[i*3 + 2].setPatternDelayOption(patternTime);
                ledDataSeries.ledOptions[i*3].setRandomDelayOption(randomTime);
                ledDataSeries.ledOptions[i*3 + 1].setRandomDelayOption(randomTime);
                ledDataSeries.ledOptions[i*3 + 2].setRandomDelayOption(randomTime);
                List<byte[]> mDataList;
                int dataCount;
                ledDataSeries.ledExeDatas[i*3].setRgbEffect(effect, i * 3, patternTime, randomTime, startTime);
                // LED 선택 프래그먼트에 효과 값 전달
                fragment2nd.setPattern(true, i, effect);
                mDataList = TxDatas.formatMemWrite(i*3,
                        ledDataSeries.ledExeDatas[i*3].toByteArray(ledDataSeries.ledOptions[i*3]));
                dataCount = mDataList.size();
                for (int j=0;j<dataCount;j++) {
                    txData(mDataList.get(j));
                }
                ledDataSeries.ledExeDatas[i*3 + 1].setRgbEffect(effect, i * 3 +1, patternTime, randomTime, startTime);
                // LED 선택 프래그먼트에 효과 값 전달
                fragment2nd.setPattern(true, i, effect);
                mDataList = TxDatas.formatMemWrite(i*3 + 1,
                        ledDataSeries.ledExeDatas[i*3 + 1].toByteArray(ledDataSeries.ledOptions[i*3 + 1]));
                dataCount = mDataList.size();
                for (int j=0;j<dataCount;j++) {
                    txData(mDataList.get(j));
                }
                ledDataSeries.ledExeDatas[i*3 + 2].setRgbEffect(effect, i * 3 +2, patternTime, randomTime, startTime);
                // LED 선택 프래그먼트에 효과 값 전달
                fragment2nd.setPattern(true, i, effect);
                mDataList = TxDatas.formatMemWrite(i*3 + 2,
                        ledDataSeries.ledExeDatas[i*3 + 2].toByteArray(ledDataSeries.ledOptions[i*3 + 2]));
                dataCount = mDataList.size();
                for (int j=0;j<dataCount;j++) {
                    txData(mDataList.get(j));
                }
                txData(TxDatas.formatInitCount());
            }
        }
        txData(TxDatas.formatSleep(true));
    }

    @Override
    public void onRgbEffect(int effect, int delayLong, int randomTime, int startTime) {

    }

    @Override
    public void onColorDialog() {

    }

    @Override
    public void onNotDialog() {

    }

    @Override
    public void onSleep(boolean isSleep) {
        Logger.d(this, "onSleep");
        txData(TxDatas.formatSleep(isSleep));
    }

    @Override
    public void onSleepTime(int time) {
        txData(TxDatas.formatSleepTime(time));
    }

    @Override
    public void onFetchData(int dataNum) {
        showProgressDialog();
        this.dataNum = dataNum;
        new Thread(taskFetchData).start();
        //txData(TxDatas.formatFetchData(dataNum));
    }

    @Override
    public void onSetRunMode(int runMode, int runPattern) {
        showProgressDialog();
        this.trmRunMode = runMode;
        this.dataNum = runPattern;
        new Thread(taskRunMode).start();
    }

    @Override
    public void onSaveData(int dataNum) {
        //txData(TxDatas.formatSaveData(dataNum));
        /*if (dataNum == 2) {
            txData(TxDatas.formatReadTime());
        } else {
            showProgressDialog();
            this.dataNum = dataNum;
            new Thread(taskSaveData).start();
        }*/
        showProgressDialog();
        this.dataNum = dataNum;
        new Thread(taskSaveData).start();
    }

    @Override
    public void onSetParam(int param) {
        Logger.d(this,"onSetParam");
        txData(TxDatas.formatSetParam(param));
    }

    @Override
    public void onSetSeqTime(int order, int runTime) {
        Logger.d(this,"onSetSeqTime");
        txData(TxDatas.formatSetSeqTime(order, runTime));
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
            showErrorToast(getString(R.string.ble_not_support));
        }
    }


    // 스캔 결과를 받아오는 인터페이스
    private SlightScanCallback slightScanCallback = new SlightScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            Logger.d(this,"scanResult");
            Message msg = uiHandler.obtainMessage(HANDLE_SCAN_RESULT);
            msg.obj = scanRecord;
            msg.arg1 = rssi;
            Bundle mac = new Bundle();
            mac.putString("addr",device.getAddress());
            msg.setData(mac);
            uiHandler.sendMessage(msg);
            //fragment1st.addList(device,rssi);
        }

        @Override
        public void onEnd() {
            scanStop();
        }
    };

    // 블루투스 스캔 시작
    private void scanStart() {
        Logger.d(this, "scanStart");
        uiHandler.sendEmptyMessage(HANDLE_SCAN_START);
        slightScanner.start();
        /*if (!slightScanner.getStateScanning()) {
            slightScanner.start();
        }*/
    }
    // 블루투스 스캔 중지
    private void scanStop() {
        Logger.d(this,"scanStop");
        uiHandler.sendEmptyMessage(HANDLE_SCAN_STOP);
        if (slightScanner.getStateScanning()) {
            slightScanner.stop();
        }
    }

    @Override
    public void onBleServiceConnect() {
        bleManager.setBleNotifier(new BleNotifier() {
            @Override
            public void bleConnected() {
                Logger.d(this, "Connected");
                uiHandler.sendEmptyMessage(HANDLE_ICON_INVALIDATE);
            }

            @Override
            public void bleDisconnected() {
                Logger.d(this, "Disconnected");
                uiHandler.sendEmptyMessage(HANDLE_ICON_INVALIDATE);
            }

            @Override
            public void bleServiceDiscovered() {

            }

            @Override
            public void bleDataAvailable(String uuid, byte[] data) {
                Logger.d(this, "bleDataAvailable");
                if (uuid.equals(LecGattAttributes.LEC_DEV_NAME_UUID)) {
                    if (getIsCalledModifyName()) {
                        String rxName = null;
                        try {
                            rxName = new String(data, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        if (rxName != null) {
                            if (rxName.contains(modName.replace(" ", ""))) {
                                // 이름 설정
                                modName = rxName;
                                // 이름 변경 실행 핸들러 동작
                                uiHandler.sendEmptyMessage(HANDLE_MOD_NAME);
                            } else {
                                // 에러 발생 핸들러 동작
                                uiHandler.sendEmptyMessage(HANDLE_MOD_NAME_ERROR);
                            }
                        } else {
                            // 에러 발생 핸들러 동작
                            uiHandler.sendEmptyMessage(HANDLE_MOD_NAME_ERROR);
                        }

                    }
                } else if (uuid.equals(LecGattAttributes.LEC_RX_UUID)) {
                    switch (data[0]) {
                        case Define.TX_MEM_TO_ROM_EACH:
                            //isRxSaveData = true;
                            Logger.d(this, "TX_MEM_TO_ROM_EACH");
                            break;
                        case Define.TX_MEM_TO_ROM_COMPLETE:
                            isRxSaveData = true;
                            Logger.d(this, "TX_MEM_TO_ROM_COMPLETE", data[3]);
                            break;
                        case Define.TX_PARAM_READ:
                            lecHeader.setLecByte(data, 4, data[2], data[1]);
                            getIsCalledReadParam();
                            break;
                        case Define.TX_ACK:
                            switch (data[1]) {
                                case Define.TX_MEMORY_FETCH_DATA:
                                    isRxFetchData = true;
                                    //uiHandler.sendEmptyMessage(HANDLE_ACKNOWLEDGE);
                                    break;
                                case Define.TX_PARAM_WRITE:
                                    uiHandler.sendEmptyMessage(HANDLE_ACKNOWLEDGE);
                                    break;
                            }
                            break;
                        case Define.TX_MEMORY_FETCH_COMPLETE:
                            isRxFetchData = true;
                            Logger.d(this, "TX_MEMORY_FETCH_COMPLETE", data[3]);
                            break;
                    }
                } else if (uuid.equals(LecGattAttributes.LEC_VERSION_UUID)) {
                    isStartTestConnect = false;
                    try {
                        versionName = new String(data, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        versionName = "NONE";
                    }
                    String verNum = versionName.replaceAll("[^0-9,.]", "");
                    float verFloat = Float.parseFloat(verNum);
                    if (verFloat >= 1.15f) {
                        uiHandler.sendEmptyMessage(HANDLE_COMPLETE_CONNECT_TEST);
                    } else {
                        uiHandler.sendEmptyMessage(HANDLE_NOT_AVAILABLE_CONNECT);
                    }
                }


            }

            @Override
            public void bleDataWriteComplete() {

            }
        });
    }
    // 패턴 데이터 송신 함수
    private void txPattern(int ledNum, byte[] array) {
        int size = array.length;
        byte[] data = new byte[size + 4];
        data[0] = Define.TX_MEMORY_WRITE;
        data[1] = (byte) ledNum;
        data[2] = (byte) 0;
        data[3] = (byte) (size/2);
        for (int i=0;i<size;i++) {
            data[4+i] = array[i];
            Logger.d(this,"txPattern",ledNum,array[i]);
        }
        txData(data);
    }

    // 데이터 송신 함수
    private void txData(byte[] array) {
        Logger.d(this,"txData",TxDatas.bytesToHex(array));
        bleManager.writeTxData(array);
    }
    /**
     * 블루투스 관련 설정 종료
     */


    /** 설정된 장치가 초기상태(NONE, 00:00:00:00:00:00)인지 확인하여 스마트라이트 장치를 설정하였다면 연결 가능(true) 반환하고
     * 연결 가능하지 않다면(false) 에러 토스트 출력
     * @return 연결 가능상태 반환
     */
    private boolean isAvailConnect() {
        String initName = getString(R.string.str_none);
        String initAddr = getString(R.string.cur_device_addr);
        String devName = appPref.getString(SLightPref.DEVICE_NAME);
        String devAddr = appPref.getString(SLightPref.DEVICE_ADDR);
        if (devName.equals(initName) || devAddr.equals(initAddr)) {
            showErrorToast(getString(R.string.easy_ble_not_available_connect));
            return false;
        }
        return true;
    }
}
