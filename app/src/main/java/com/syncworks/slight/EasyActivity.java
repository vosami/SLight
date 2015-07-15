package com.syncworks.slight;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter;
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
import com.syncworks.slight.util.ByteLengthFilter;
import com.syncworks.slight.util.ErrorToast;
import com.syncworks.slight.util.SuccessToast;
import com.syncworks.slight.widget.StepView;
import com.syncworks.slightpref.SLightPref;
import com.syncworks.vosami.blelib.BleConsumer;
import com.syncworks.vosami.blelib.BleManager;
import com.syncworks.vosami.blelib.BleNotifier;
import com.syncworks.vosami.blelib.BleUtils;
import com.syncworks.vosami.blelib.BluetoothLeService;
import com.syncworks.vosami.blelib.LecGattAttributes;
import com.syncworks.vosami.blelib.scanner.SlightScanCallback;
import com.syncworks.vosami.blelib.scanner.SlightScanner;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;


public class EasyActivity extends ActionBarActivity implements OnEasyFragmentListener,BleConsumer {

    private SLightPref appPref  = null;

    private final static int MAX_STEP = 5;  // 5단계가 최고 단계로 설정
    StepView stepView;
    Button btnPrev, btnNext, btnStep5, btnFinish;

    // LED 선택, 패턴 데이터
    private LedDataSeries ledDataSeries;


    private boolean isBleSupported = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appPref = new SLightPref(this);
        ledDataSeries = new LedDataSeries();
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
                changeStep(clickStep);
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
                    changeStep(2);
                } else {
                    changeStep(stepView.getStep() + 1);
                }
                break;
            case R.id.easy_btn_step5:
                // 선택된 LED 완료 표시
                ledComplete();
                // 효과 선택 초기화
                fragment4th.initEffectNum();
                changeStep(5);
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
    private final static int HANDLE_FRAG_CONNECTED = 1;
    private final static int HANDLE_NOT_CONNECTED = 2;

    private final static int HANDLE_MOD_NAME_CONNECT = 5;
    private final static int HANDLE_MOD_NAME=6;
    private final static int HANDLE_MOD_NAME_ERROR = 7;

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
//            Toast.makeText(this,getString(R.string.easy_ble_not_connect),Toast.LENGTH_LONG).show();
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
            EasyActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case HANDLE_FRAG_CONNECTED:
                        // 진행 상태 대화창을 닫음
                        activity.dismissProgressDialog();
                        activity.changeStep(2);
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
                        activity.fragment1st.setTvCurrentDevice(activity.modName, activity.appPref.getString(SLightPref.DEVICE_ADDR));
                        // 진행 상태 대화창을 닫음
                        activity.dismissProgressDialog();
                        activity.scanStart();
                        activity.showSuccessToast(activity.getString(R.string.easy_mod_name_success));
                        break;
                    case HANDLE_MOD_NAME_ERROR:
                        activity.bleManager.bleDisconnect();
                        activity.dismissProgressDialog();
                        activity.showErrorToast(activity.getString(R.string.easy_mod_name_error));
                        activity.scanStart();
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

    // 장치 이름 변경 연결
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
                changeStep(stepView.getStep()- 1);
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
        name = name.replace(name.substring(name.length()-1), "");
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
                modName = inputName.getText().toString();
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
    private void showErrorToast(String txt) {

        ErrorToast errorToast = new ErrorToast(this);
        errorToast.showToast(txt, Toast.LENGTH_LONG);
    }

    private void showSuccessToast(String txt) {
        SuccessToast successToast = new SuccessToast(this);
        successToast.showToast(txt,Toast.LENGTH_SHORT);
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
                stepView.setVisibility(View.GONE);
                btnPrev.setVisibility(View.GONE);
                btnStep5.setVisibility(View.GONE);
                btnFinish.setVisibility(View.GONE);
                break;
            case 1:
                btnNext.setText(getString(R.string.str_next));
                stepView.setVisibility(View.VISIBLE);
                btnPrev.setVisibility(View.VISIBLE);
                btnStep5.setVisibility(View.GONE);
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
        fragment4th.setLedSelect(this.ledDataSeries.ledSelect);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
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
//            fragmentTransaction.add(R.id.easy_ll_fragment, fragment2nd);
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
                fragmentTransaction.replace(R.id.easy_ll_fragment, fragment0th);
                break;
            case 1:
                startFragment(step);
                // LED 선택 초기화
                ledDataSeries.ledSelect.init();
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
        fragmentTransaction.commit();
    }

    private void changeStep(int step) {
        setBtnText(step);       // 다음단계 버튼 텍스트 변경
        changeActionBarText(step);  // 타이틀 바 제목 변경 , 스텝 변경
        changeFragment(step);       // Fragment 변경
    }

    /**********************************************************************************************
     * Fragment 관련 설정 종료
     *********************************************************************************************/

    private final static byte[] offPattern = {(byte)Define.OP_START,0,0,0,(byte)Define.OP_END};

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
            //ledDataSeries.ledSelect.setRgb(ledNum, LedSelect.SelectType.DEFAULT);
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
        ledDataSeries.ledOptions[ledNum].setRatioBright(bright*100/(Define.OP_CODE_MIN-1));
        txPattern(ledNum, ledDataSeries.ledExeDatas[ledNum].toByteArray(ledDataSeries.ledOptions[ledNum]));
        txData(TxDatas.formatInitCount());
    }

    @Override
    public void onEffect(int effect, boolean isDelayLong, boolean isRandom, int startTime) {

        for (int i=0;i<Define.NUMBER_OF_SINGLE_LED;i++) {
            if (ledDataSeries.ledSelect.getLed(i) == LedSelect.SelectType.SELECTED) {
                ledDataSeries.ledOptions[i].setDelayStart(startTime);
                if (isDelayLong) {
                    ledDataSeries.ledOptions[i].setRatioDuration(100);
                } else {
                    ledDataSeries.ledOptions[i].setRatioDuration(200);
                }
                ledDataSeries.ledExeDatas[i].setEffect(effect, isDelayLong, isRandom, startTime);
                // LED 선택 프래그먼트에 효과 값 전달
                fragment2nd.setPattern(false, i, effect);
                txPattern(i, ledDataSeries.ledExeDatas[i].toByteArray(ledDataSeries.ledOptions[i]));
                txData(TxDatas.formatInitCount());
            }
        }
        for (int i=0;i<Define.NUMBER_OF_COLOR_LED;i++) {
            if (ledDataSeries.ledSelect.getRgb(i) == LedSelect.SelectType.SELECTED) {
                ledDataSeries.ledOptions[i*3].setDelayStart(startTime);
                ledDataSeries.ledOptions[i*3 + 1].setDelayStart(startTime);
                ledDataSeries.ledOptions[i*3 + 2].setDelayStart(startTime);
                if (isDelayLong) {
                    ledDataSeries.ledOptions[i*3].setRatioDuration(100);
                    ledDataSeries.ledOptions[i*3 + 1].setRatioDuration(100);
                    ledDataSeries.ledOptions[i*3 + 2].setRatioDuration(100);
                } else {
                    ledDataSeries.ledOptions[i*3].setRatioDuration(200);
                    ledDataSeries.ledOptions[i*3 + 1].setRatioDuration(200);
                    ledDataSeries.ledOptions[i*3 + 2].setRatioDuration(200);
                }
                ledDataSeries.ledExeDatas[i*3].setEffect(effect, isDelayLong, isRandom, startTime);
                ledDataSeries.ledExeDatas[i*3 + 1].setEffect(effect, isDelayLong, isRandom, startTime);
                ledDataSeries.ledExeDatas[i*3 + 2].setEffect(effect, isDelayLong, isRandom, startTime);

                // LED 선택 프래그먼트에 효과 값 전달
                fragment2nd.setPattern(true, i, effect);
                txPattern(i * 3, ledDataSeries.ledExeDatas[i * 3].toByteArray(ledDataSeries.ledOptions[i * 3]));
                txPattern(i*3 + 1,ledDataSeries.ledExeDatas[i*3 + 1].toByteArray(ledDataSeries.ledOptions[i*3 + 1]));
                txPattern(i*3 + 2,ledDataSeries.ledExeDatas[i*3 + 2].toByteArray(ledDataSeries.ledOptions[i*3 + 2]));
                txData(TxDatas.formatInitCount());
            }
        }
    }

    @Override
    public void onColorDialog() {

    }

    @Override
    public void onNotDialog() {

    }

    @Override
    public void onSleepLedCheck(boolean isCheckLed) {

    }

    @Override
    public void onSleep(boolean isSleep) {

    }

    @Override
    public void onRandomPlay(int playTime) {

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
//            Toast.makeText(this, getString(R.string.ble_not_support), Toast.LENGTH_LONG).show();
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
                            if (rxName.contains(modName)) {
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
        }
        txData(data);
    }

    // 데이터 송신 함수
    private void txData(byte[] array) {
        bleManager.writeTxData(array);
    }
    /**
     * 블루투스 관련 설정 종료
     */


}
