package com.syncworks.slight;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.syncworks.define.Define;
import com.syncworks.ledselectlayout.LedSelectLayout;
import com.syncworks.ledviewlayout.LedViewLayout;
import com.syncworks.scriptdata.ScriptDataListSpinnerAdapter;
import com.syncworks.scriptdata.ScriptExecuteService;
import com.syncworks.slight.fragments.ColorAlwaysOn;
import com.syncworks.slight.fragments.LedSettingData;
import com.syncworks.slight.fragments.OnLedFragmentListener;
import com.syncworks.slight.fragments.SingleAlwaysOn;
import com.syncworks.slight.fragments.SingleArrayFragment;
import com.syncworks.slight.fragments.SingleFragment;
import com.syncworks.titlebarlayout.TitleBarLayout;
import com.syncworks.verticalseekbar.VerticalSeekBarPlus;
import com.syncworks.vosami.blelib.BleConsumer;
import com.syncworks.vosami.blelib.BleManager;
import com.syncworks.vosami.blelib.BleNotifier;


public class LedEffectActivity extends ActionBarActivity implements OnLedFragmentListener,BleConsumer {
    private final static String TAG = LedEffectActivity.class.getSimpleName();

    // 메시지 수신 리시버
    private BrightReceiver receiver;
    // LED 실행 서비스
    private ScriptExecuteService scriptExecuteService;
    private boolean mBound = false;         // 서비스 연결 여부

    private final ServiceConnection scriptExecuteServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "Service Connected");
            ScriptExecuteService.ScriptBinder binder = (ScriptExecuteService.ScriptBinder) service;
            scriptExecuteService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    // 설정 정보 기록
    LedSettingData ledSettingData;
    // 현재 선택된 LED
    private int thisSelectedLedNumber;

    // LED 선택 레이아웃
    LedSelectLayout ledSelectLayout;
    // LED 점멸 패턴 확인 레이아웃
    LedViewLayout ledViewLayout;
    // 타이틀 바 레이아웃
    TitleBarLayout titleBarLayout;
    // 점멸 패턴 선택 스피너
    Spinner spPatternSelect;
    // 세로 SeekBar
    VerticalSeekBarPlus verticalSeekBar;

    // 단색 LED 항상 켜기용 Fragment
    SingleAlwaysOn singleAlwaysOn;
    SingleFragment singleFragment;
    SingleArrayFragment singleArrayFragment;
    ColorAlwaysOn coloralwaysOn;
    //블루투스 매니저
    private BleManager bleManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_led_effect);
        // 설정 정보 초기화
        ledSettingData = LedSettingData.getInstance();
        // View 등록
        findViews();
        // 변수 초기화
        initVar();
	}

    private void initVar() {
        thisSelectedLedNumber = Define.SELECTED_LED1;
    }

    @Override
    protected void onStart() {
        super.onStart();
        receiver = new BrightReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ScriptExecuteService.CHANGE_BRIGHT_ACTION);
        registerReceiver(receiver,intentFilter);
        // 바인드 서비스
        Intent intent = new Intent(this, ScriptExecuteService.class);
        bindService(intent, scriptExecuteServiceConnection, Context.BIND_AUTO_CREATE);
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
        unregisterReceiver(receiver);
        if (mBound) {
            unbindService(scriptExecuteServiceConnection);
            mBound = false;
        }
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_led_effect, menu);
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

    private void findViews() {
        // LED 선택 레이아웃
        ledSelectLayout = (LedSelectLayout) findViewById(R.id.led_select_layout);
        // LED 점멸 패턴 확인 레이아웃
        ledViewLayout = (LedViewLayout) findViewById(R.id.led_view_layout);
        // 타이틀 바 레이아웃
        titleBarLayout = (TitleBarLayout) findViewById(R.id.title_bar_layout);
        // LED 선택 레이아웃 리스너 등록
        ledSelectLayout.setOnLedSelectListener(onLedSelectListener);
        // 점멸 패턴 선택 레이아웃
        spPatternSelect = (Spinner) findViewById(R.id.sp_pattern_select);
        // 단색 LED 점멸 패턴 설정
        setSpinnerPatternName(Define.SINGLE_LED, 0);
        // 세로 SeekBar
        verticalSeekBar = (VerticalSeekBarPlus) findViewById(R.id.vertical_seekbar);

        createFragments();
        replaceFragments();

    }

    // 점멸 패턴 스피너에 설정
    private void setSpinnerPatternName(boolean ledColor, int pattern) {
        String[] patternName;
        ScriptDataListSpinnerAdapter spinnerAdapter;
        // SINGLE LED 점멸 패턴 데이터 가져오기
        if (ledColor == Define.SINGLE_LED) {
            patternName = getResources().getStringArray(R.array.single_pattern_name);
            spinnerAdapter = new ScriptDataListSpinnerAdapter(this.getApplicationContext(),android.R.layout.simple_spinner_item, patternName);
            spPatternSelect.setAdapter(spinnerAdapter);
            spPatternSelect.setSelection(pattern);
            spPatternSelect.setOnItemSelectedListener(singleItemSelectedListener);
        }
        else {
            patternName = getResources().getStringArray(R.array.color_pattern_name);
            spinnerAdapter = new ScriptDataListSpinnerAdapter(this.getApplicationContext(),android.R.layout.simple_spinner_item, patternName);
            spPatternSelect.setAdapter(spinnerAdapter);
            spPatternSelect.setSelection(pattern);
            spPatternSelect.setOnItemSelectedListener(colorItemSelectedListener);
        }


    }

    private AdapterView.OnItemSelectedListener singleItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int tempLedNum = thisSelectedLedNumber;
            int tempLedCount = 0;
            for (int i=0;i<Define.NUMBER_OF_SINGLE_LED;i++) {
                if (((tempLedNum>>i) & 0x01) != 0) {
                    ledSettingData.setPattern(Define.SINGLE_LED,i,position);
                    scriptExecuteService.parseXml(i,Define.SINGLE_LED,position);
                    tempLedCount++;
                }
            }
            // 프래그먼트 설정
            if (position == 0) {
                changeFragments(FragmentType.SINGLE_ALWAYS_ON,thisSelectedLedNumber);
            } else if (tempLedCount > 1) {
                changeFragments(FragmentType.SINGLE_ARRAY,thisSelectedLedNumber);
            } else {
                changeFragments(FragmentType.SINGLE,thisSelectedLedNumber);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener colorItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int tempLedNum = thisSelectedLedNumber>>12;
            int tempLedCount = 0;
            for (int i=0;i<Define.NUMBER_OF_COLOR_LED;i++) {
                if (((tempLedNum>>i)&0x01) != 0) {
                    ledSettingData.setPattern(Define.COLOR_LED,i,position);
                    scriptExecuteService.parseXml(i,Define.COLOR_LED,position);
                    tempLedCount++;
                }
                // 프래그먼트 설정
                if (position == 0) {
                    changeFragments(FragmentType.COLOR_ALWAYS_ON,thisSelectedLedNumber);
                } else if (tempLedCount > 1) {
                    changeFragments(FragmentType.SINGLE_ARRAY,thisSelectedLedNumber);
                } else {
                    changeFragments(FragmentType.SINGLE, thisSelectedLedNumber);
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    // LED 선택 레이아웃 리스너 설정
    private LedSelectLayout.OnLedSelectListener onLedSelectListener = new LedSelectLayout.OnLedSelectListener() {
        @Override
        public void onLedSelect(boolean isSingleLed, int enabledLedGroup, int selectedLed) {
            Log.d(TAG, "Single:"+isSingleLed+", LedGroup:"+enabledLedGroup + ", SelectLed:"+selectedLed);
            // UI 설정
            selectUI(isSingleLed,selectedLed);
        }

        @Override
        public void onLedCheck(boolean isSingleLed, int enabledLedGroup, int checkedLed) {
            Log.d(TAG, "Single:"+isSingleLed+", LedGroup:"+enabledLedGroup + ", CheckLed:"+checkedLed);
            checkUI(isSingleLed,checkedLed);
            /*titleBarLayout.setLedNumber(checkedLed);
            if (isSingleLed == Define.SINGLE_LED) {
                changeFragments(FragmentType.SINGLE,checkedLed);
            }
            else {
                changeFragments(FragmentType.SINGLE_ARRAY,checkedLed);
            }*/
        }
    };
    // ledSettingData 에 맞춰 UI를 설정
    private void selectUI(boolean isSingle, int selectedLed) {
        int pattern = ledSettingData.getPattern(selectedLed);
        // 스피너의 점멸 패턴 불러오기
        setSpinnerPatternName(isSingle, pattern);
        // 타이틀 바 설정
        titleBarLayout.setLedNumber(selectedLed);
        // 프래그먼트 설정
//        changeFragments(ledSettingData.getFragmentType(selectedLed),selectedLed);
        // 현재 설정된 LED 번호 기억
        thisSelectedLedNumber = selectedLed;
    }

    // LED 를 여러개 선택할 경우 UI 설정하고 데이터 초기화
    private void checkUI(boolean isSingle, int checkedLed) {
        // 스피너의 점멸 패턴 불러오기
        setSpinnerPatternName(isSingle, 0);
        // 타이틀 바 설정
        titleBarLayout.setLedNumber(checkedLed);
        // 현재 설정된 LED 번호 기억
        thisSelectedLedNumber = checkedLed;
    }


    private void createFragments() {
        singleAlwaysOn = SingleAlwaysOn.newInstance("0","0");
        singleFragment = SingleFragment.newInstance(10,0,0);
        singleArrayFragment = SingleArrayFragment.newInstance(10,0,0);
        coloralwaysOn = ColorAlwaysOn.newInstance("0", "0");
    }

    private void replaceFragments() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.ll_fragment,singleAlwaysOn);
        fragmentTransaction.add(R.id.ll_fragment,singleArrayFragment);
//        fragmentTransaction.add(R.id.ll_fragment, coloralwaysOn);
        fragmentTransaction.commit();
//        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    }

    @Override
    public void onBleServiceConnect() {
        Log.d(TAG,"서비스 연결됨");

        bleManager.setBleNotifier(new BleNotifier() {
            @Override
            public void bleConnected() {
                Log.d(TAG,"Connected");
            }

            @Override
            public void bleDisconnected() {
                Log.d(TAG,"Disconnected");

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

    public enum FragmentType {
        SINGLE_ALWAYS_ON, COLOR_ALWAYS_ON, SINGLE, SINGLE_ARRAY
    }
    private void changeFragments(FragmentType fragmentType, int ledNum) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (fragmentType) {
            case SINGLE_ALWAYS_ON:
                verticalSeekBar.setVisibility(View.GONE);
                fragmentTransaction.replace(R.id.ll_fragment, singleAlwaysOn);
                break;
            case COLOR_ALWAYS_ON:
                verticalSeekBar.setVisibility(View.GONE);
                fragmentTransaction.replace(R.id.ll_fragment, coloralwaysOn);
                break;
            case SINGLE:
                verticalSeekBar.setVisibility(View.VISIBLE);
                fragmentTransaction.replace(R.id.ll_fragment, singleFragment);
                break;
            case SINGLE_ARRAY:
                verticalSeekBar.setVisibility(View.VISIBLE);
                fragmentTransaction.replace(R.id.ll_fragment, singleArrayFragment);
                break;
        }
        fragmentTransaction.commit();
    }

    private void ledSetPattern(int ledNum, int pattern){

    }
    private void ledSetBright(int ledNum, int bright) {

    }


    @Override
    public void onSingleBrightAction(int currentBright) {

    }

    @Override
    public void onColorChangeAction(int red, int green, int blue) {

    }

    @Override
    public void onStartDelayAction(int startDelay) {

    }

    @Override
    public void onEndDelayAction(int endDelay) {

    }

    @Override
    public void onEffectRatioAction(float ratio) {

    }

    private class BrightReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int[] dataPassed = intent.getIntArrayExtra("DATA_PASSED");
            Log.d(TAG,"onReceive" + dataPassed[0]);
            for (int i=0;i<Define.NUMBER_OF_SINGLE_LED;i++) {
                ledViewLayout.setLedColorData(i,dataPassed[i]);
                ledViewLayout.reDraw();
            }
        }
    }
}
