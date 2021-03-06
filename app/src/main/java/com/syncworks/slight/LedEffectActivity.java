package com.syncworks.slight;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
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
import android.widget.Spinner;

import com.syncworks.define.Define;
import com.syncworks.ledselectlayout.LedSelectLayout;
import com.syncworks.ledviewlayout.LedViewLayout;
import com.syncworks.scriptdata.ScriptDataList;
import com.syncworks.scriptdata.ScriptDataListSpinnerAdapter;
import com.syncworks.scriptdata.ScriptExecuteService;
import com.syncworks.scriptdata.ScriptFileManager;
import com.syncworks.slight.fragments.ColorAlwaysOn;
import com.syncworks.slight.fragments.LedSettingData;
import com.syncworks.slight.fragments.OnLedFragmentListener;
import com.syncworks.slight.fragments.SingleAlwaysOn;
import com.syncworks.slight.fragments.SingleArrayFragment;
import com.syncworks.slight.fragments.SingleFragment;
import com.syncworks.slight.util.CustomToast;
import com.syncworks.slightpref.SLightPref;
import com.syncworks.soundmeter.SoundMeter;
import com.syncworks.titlebarlayout.TitleBarLayout;
import com.syncworks.verticalseekbar.VerticalSeekBarPlus;
import com.syncworks.vosami.blelib.BleConsumer;
import com.syncworks.vosami.blelib.BleManager;
import com.syncworks.vosami.blelib.BleNotifier;
import com.syncworks.vosami.blelib.BluetoothLeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * TODO 메시지 읽고 오면 에러 발생
 */

public class LedEffectActivity extends ActionBarActivity implements OnLedFragmentListener,BleConsumer {
    private final static String TAG = LedEffectActivity.class.getSimpleName();
	// 이전 LED 선택 상태를 기억
	private int oldEnabledLedGroup;
    // 스크립트 파일 목록 생성 매니저
    private ScriptFileManager scriptFileManager = null;
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

	// 연결 상태 확인
	private boolean connectionState = false;

	private Menu menu = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_led_effect);
        scriptFileManager = new ScriptFileManager(this);
        scriptFileManager.resetData();

        // 설정 정보 초기화
        ledSettingData = LedSettingData.getInstance();
        // View 등록
        findViews();

        // 변수 초기화
        initVar();


	}

    private void initVar() {
		// 이전 활성화된 LED 상태를 Single LED 로 설정
		oldEnabledLedGroup = Define.SINGLE_LED_123_ACTIVATE | Define.SINGLE_LED_456_ACTIVATE | Define.SINGLE_LED_789_ACTIVATE;
		// 현재 선택된 LED 를 Single LED 1로 설정
        thisSelectedLedNumber = Define.SELECTED_LED1;
    }

    @Override
    protected void onStart() {
        super.onStart();
        receiver = new BrightReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ScriptExecuteService.CHANGE_BRIGHT_ACTION);
        registerReceiver(receiver, intentFilter);
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
                Log.d(TAG,"연결되었습니다.");
            } else {
                SLightPref appPref = new SLightPref(this);
                String address = appPref.getString(SLightPref.DEVICE_ADDR);
                bleManager.bleConnect(address);
                CustomToast.middleTop(this,"연결 시도중입니다.");
                Log.d(TAG,"연결 X");
            }

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d(TAG, "onPrepareOptionsMenu");
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

    /**
     * 레이아웃 내의 뷰를 사용할 수 있도록 등록
     */
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
        // 세로 SeekBar 리스너 등록
        verticalSeekBar.setOnVSeekBarListener(onVSeekBarListener);

        createFragments();
        replaceFragments();

    }

    // 점멸 패턴 스피너에 설정
    private void setSpinnerPatternName(boolean ledColor, int pattern) {
        String[] patternName;
        ScriptDataListSpinnerAdapter spinnerAdapter;
        int patternArraySize = 0;
        // SINGLE LED 점멸 패턴 데이터 가져오기
        if (ledColor == Define.SINGLE_LED) {
            patternName = scriptFileManager.getArrayName(Define.SINGLE_LED, "kr");
            spinnerAdapter = new ScriptDataListSpinnerAdapter(this.getApplicationContext(),android.R.layout.simple_spinner_item, patternName);
            spPatternSelect.setAdapter(spinnerAdapter);
            spPatternSelect.setSelection(pattern);
            spPatternSelect.setOnItemSelectedListener(singleItemSelectedListener);
        }
        else {
            patternName = scriptFileManager.getArrayName(Define.COLOR_LED, "kr");
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
                    if (ledSettingData.getPattern(Define.SINGLE_LED,i) != position) {
						parseXml(i, Define.SINGLE_LED, position);

                        /*scriptExecuteService.parseXml(i,Define.SINGLE_LED,
								scriptFileManager.getDirType(Define.SINGLE_LED,position),
								scriptFileManager.getFileName(Define.SINGLE_LED,position));
                        ledSettingData.setPattern(Define.SINGLE_LED,i,position);*/
                    }
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
                    if (ledSettingData.getPattern(Define.COLOR_LED,i) != position) {
						parseXml(i,Define.COLOR_LED,position);
						/*scriptExecuteService.parseXml(i,Define.COLOR_LED,
								scriptFileManager.getDirType(Define.COLOR_LED,position),
								scriptFileManager.getFileName(Define.COLOR_LED,position));
                        ledSettingData.setPattern(Define.COLOR_LED,i,position);*/
                    }
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

	private void parseXml(int ledNum, boolean colorType, int position) {
        if (mBound) {
            scriptExecuteService.parseXml(ledNum, colorType,
                    scriptFileManager.getDirType(colorType, position),
                    scriptFileManager.getFileName(colorType, position));
        } /*else {
            CustomToast.middleBottom(this,"서비스에 연결하지 못했습니다.");
        }*/
		ledSettingData.setPattern(colorType,ledNum,position);
	}

    // LED 선택 레이아웃 리스너 설정
    private LedSelectLayout.OnLedSelectListener onLedSelectListener = new LedSelectLayout.OnLedSelectListener() {
        @Override
        public void onLedSelect(boolean isSingleLed, int enabledLedGroup, int selectedLed) {
            Log.d(TAG, "Single:"+isSingleLed+", LedGroup:"+enabledLedGroup + ", SelectLed:"+selectedLed);
            ledViewLayout.setActivateBool(enabledLedGroup);
            // UI 설정
            selectUI(isSingleLed,enabledLedGroup,selectedLed);
        }

        @Override
        public void onLedCheck(boolean isSingleLed, int enabledLedGroup, int checkedLed) {
            Log.d(TAG, "Single:"+isSingleLed+", LedGroup:"+enabledLedGroup + ", CheckLed:"+checkedLed);
            ledViewLayout.setActivateBool(enabledLedGroup);
            // UI 설정
            checkUI(isSingleLed, enabledLedGroup,checkedLed);
        }
    };

    private VerticalSeekBarPlus.OnVSeekBarListener onVSeekBarListener = new VerticalSeekBarPlus.OnVSeekBarListener() {
        @Override
        public void onEvent(int progress) {
            float ratio = (float)progress / 10;
            scriptExecuteService.setBrightRatio(thisSelectedLedNumber,ratio);
            ledSettingData.setRatioBright(thisSelectedLedNumber,progress);
        }
    };

    // ledSettingData 에 맞춰 UI를 설정
    private void selectUI(boolean isSingle, int enabledLedGroup, int selectedLed) {
        int pattern = 0;
		// 패턴 데이터를 가져온다.
		pattern = ledSettingData.getPattern(selectedLed);
		// 스피너의 점멸 패턴 불러오기
		setSpinnerPatternName(isSingle, pattern);
		// 타이틀 바 설정
		titleBarLayout.setLedNumber(selectedLed);
		// LED 그룹이 바뀌었다면
		if (oldEnabledLedGroup != enabledLedGroup) {
			// Single LED 가 선택되었다면
			if (isSingle) {
				if (selectedLed == Define.SELECTED_LED1 || selectedLed == Define.SELECTED_LED2 || selectedLed == Define.SELECTED_LED3) {
					parseXml(LedSettingData.convertLedNum(Define.SELECTED_LED1),Define.SINGLE_LED,ledSettingData.getPattern(Define.SELECTED_LED1));
					parseXml(LedSettingData.convertLedNum(Define.SELECTED_LED2),Define.SINGLE_LED,ledSettingData.getPattern(Define.SELECTED_LED2));
					parseXml(LedSettingData.convertLedNum(Define.SELECTED_LED3),Define.SINGLE_LED,ledSettingData.getPattern(Define.SELECTED_LED3));
				} else if (selectedLed == Define.SELECTED_LED4 || selectedLed == Define.SELECTED_LED5 || selectedLed == Define.SELECTED_LED6) {
					parseXml(LedSettingData.convertLedNum(Define.SELECTED_LED4),Define.SINGLE_LED,ledSettingData.getPattern(Define.SELECTED_LED4));
					parseXml(LedSettingData.convertLedNum(Define.SELECTED_LED5),Define.SINGLE_LED,ledSettingData.getPattern(Define.SELECTED_LED5));
					parseXml(LedSettingData.convertLedNum(Define.SELECTED_LED6),Define.SINGLE_LED,ledSettingData.getPattern(Define.SELECTED_LED6));
				} else if (selectedLed == Define.SELECTED_LED7 || selectedLed == Define.SELECTED_LED8 || selectedLed == Define.SELECTED_LED9) {
					parseXml(LedSettingData.convertLedNum(Define.SELECTED_LED7),Define.SINGLE_LED,ledSettingData.getPattern(Define.SELECTED_LED7));
					parseXml(LedSettingData.convertLedNum(Define.SELECTED_LED8),Define.SINGLE_LED,ledSettingData.getPattern(Define.SELECTED_LED8));
					parseXml(LedSettingData.convertLedNum(Define.SELECTED_LED9),Define.SINGLE_LED,ledSettingData.getPattern(Define.SELECTED_LED9));
				}
			}
			// Color LED 가 선택되었다면
			else {
				parseXml(LedSettingData.convertLedNum(selectedLed),Define.COLOR_LED,pattern);
			}
		}
		// LED 선택이 바뀌었다면
		if (thisSelectedLedNumber != selectedLed) {

		}
		// 현재 설정된 LED 그룹 기억
		oldEnabledLedGroup = enabledLedGroup;
        // 현재 설정된 LED 번호 기억
        thisSelectedLedNumber = selectedLed;
    }

    // LED 를 여러개 선택할 경우 UI 설정하고 데이터 초기화
    private void checkUI(boolean isSingle, int enabledLedGroup, int checkedLed) {
        // 스피너의 점멸 패턴 불러오기
        setSpinnerPatternName(isSingle, 0);
        // 타이틀 바 설정
        titleBarLayout.setLedNumber(checkedLed);

        // LED 그룹이 바뀌었다면
        if (oldEnabledLedGroup != enabledLedGroup) {
            // Single LED 가 선택되었다면
            if (isSingle) {
                if ((checkedLed & Define.SELECTED_LED1) !=0 || (checkedLed & Define.SELECTED_LED2)!=0 || (checkedLed & Define.SELECTED_LED3)!=0) {
                    parseXml(LedSettingData.convertLedNum(Define.SELECTED_LED1),Define.SINGLE_LED,0);
                    parseXml(LedSettingData.convertLedNum(Define.SELECTED_LED2),Define.SINGLE_LED,0);
                    parseXml(LedSettingData.convertLedNum(Define.SELECTED_LED3),Define.SINGLE_LED,0);
                }
                if ((checkedLed & Define.SELECTED_LED4) !=0 || (checkedLed & Define.SELECTED_LED5)!=0 || (checkedLed & Define.SELECTED_LED6)!=0) {
                    parseXml(LedSettingData.convertLedNum(Define.SELECTED_LED4),Define.SINGLE_LED,0);
                    parseXml(LedSettingData.convertLedNum(Define.SELECTED_LED5),Define.SINGLE_LED,0);
                    parseXml(LedSettingData.convertLedNum(Define.SELECTED_LED6),Define.SINGLE_LED,0);
                }
                if ((checkedLed & Define.SELECTED_LED7) !=0 || (checkedLed & Define.SELECTED_LED8)!=0 || (checkedLed & Define.SELECTED_LED9)!=0) {
                    parseXml(LedSettingData.convertLedNum(Define.SELECTED_LED7),Define.SINGLE_LED,0);
                    parseXml(LedSettingData.convertLedNum(Define.SELECTED_LED8),Define.SINGLE_LED,0);
                    parseXml(LedSettingData.convertLedNum(Define.SELECTED_LED9),Define.SINGLE_LED,0);
                }
            }
            // Color LED 가 선택되었다면
            else {
                if ((checkedLed & Define.SELECTED_COLOR_LED1) != 0) {
                    parseXml(0,Define.COLOR_LED,0);
                }
                if ((checkedLed & Define.SELECTED_COLOR_LED2) != 0) {
                    parseXml(1,Define.COLOR_LED,0);
                }
                if ((checkedLed & Define.SELECTED_COLOR_LED3) != 0) {
                    parseXml(2,Define.COLOR_LED,0);
                }
            }
        }
        // 현재 설정된 LED 그룹 기억
        oldEnabledLedGroup = enabledLedGroup;
        // 현재 설정된 LED 번호 기억
        thisSelectedLedNumber = checkedLed;
    }


    private void createFragments() {
        singleAlwaysOn = SingleAlwaysOn.newInstance(100);
        singleFragment = SingleFragment.newInstance(10,0,0);
        singleArrayFragment = SingleArrayFragment.newInstance(10,0,0);
        coloralwaysOn = ColorAlwaysOn.newInstance(100,100,100);
    }

    private void replaceFragments() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.ll_fragment,singleAlwaysOn);
        fragmentTransaction.commit();
    }

	// 블루투스 서비스 Bind 인터페이스
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
                Log.i(TAG,"Connected");
				// 연결 상태 아이콘 설정 - Connected
				setConnectIcon(true);
            }

            @Override
            public void bleDisconnected() {
                Log.i(TAG,"Disconnected");
				// 연결 상태 아이콘 설정 - disconnect
				setConnectIcon(false);
            }

            @Override
            public void bleServiceDiscovered() {

            }

            @Override
            public void bleDataAvailable(String uuid, byte[] data) {
                Log.i(TAG,"bleDataAvailable");
            }

            @Override
            public void bleDataWriteComplete() {
                Log.i(TAG,"bleDataWriteComplete");
            }
        });
    }

    public enum FragmentType {
        SINGLE_ALWAYS_ON, COLOR_ALWAYS_ON, SINGLE, SINGLE_ARRAY
    }
    private void changeFragments(FragmentType fragmentType, int ledNum) {
        LedSettingData.SetData setData = null;
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (fragmentType) {
            case SINGLE_ALWAYS_ON:
                verticalSeekBar.setVisibility(View.GONE);
				int bright = ledSettingData.getBright(ledNum);
				singleAlwaysOn.setInitBright(bright);
                fragmentTransaction.replace(R.id.ll_fragment, singleAlwaysOn);
                break;
            case COLOR_ALWAYS_ON:
                verticalSeekBar.setVisibility(View.GONE);
                int color = ledSettingData.getColor(ledNum);
                coloralwaysOn.setInitColor(color);
                fragmentTransaction.replace(R.id.ll_fragment, coloralwaysOn);
                break;
            case SINGLE:
                verticalSeekBar.setVisibility(View.VISIBLE);
                setData = ledSettingData.getSetData(ledNum);
                verticalSeekBar.setProgress(setData.ratioBright);
                singleFragment.setInitParam(setData);
                fragmentTransaction.replace(R.id.ll_fragment, singleFragment);
                break;
            case SINGLE_ARRAY:
                verticalSeekBar.setVisibility(View.VISIBLE);
                singleArrayFragment.setInit(10,0,0);
                fragmentTransaction.replace(R.id.ll_fragment, singleArrayFragment);
                break;
        }
        fragmentTransaction.commit();
    }

    private void ledSetPattern(int ledNum, int pattern){

    }
    private void ledSetBright(int ledNum, int bright) {

    }

    /**
     * Single 항상 켜기 - 밝기 값 설정
     * @param currentBright : 밝기 값
     */
    @Override
    public void onSingleBrightAction(int currentBright) {
        Log.d(TAG, "Bright Change: " + currentBright);
        int[] brights = new int[Define.NUMBER_OF_SINGLE_LED];
        for (int i=0;i<Define.NUMBER_OF_SINGLE_LED;i++) {
            if (((thisSelectedLedNumber>>i)& 0x01) == 1) {
                brights[i] = currentBright;
                ledSettingData.setBright(Define.SINGLE_LED,i,currentBright);
            }
            else {
                brights[i] = Define.OP_CODE_MIN;
            }
        }
        scriptExecuteService.setAllDataBright(brights);
        // TODO 데이터 송신
    }

    /**
     * Color 항상 켜기 - 밝기 값 설정
     * @param red 적색
     * @param green 녹색
     * @param blue 청색
     */
    @Override
    public void onColorChangeAction(int red, int green, int blue) {
        Log.d(TAG,"Color Change: #"+Integer.toHexString(red)+Integer.toHexString(green)+Integer.toHexString(blue));
        int[] brights = new int[Define.NUMBER_OF_SINGLE_LED];
        for (int i=0;i<Define.NUMBER_OF_COLOR_LED;i++) {
            if (((thisSelectedLedNumber>>(12+i))&0x01) == 1) {
                brights[i*3] = red;
                brights[i*3+1] = green;
                brights[i*3+2] = blue;
                ledSettingData.setBright(Define.COLOR_LED,i, Color.rgb(red,green,blue));
            }
            else {
                brights[i*3] = Define.OP_CODE_MIN;
                brights[i*3+1] = Define.OP_CODE_MIN;
                brights[i*3+2] = Define.OP_CODE_MIN;
            }
        }
        scriptExecuteService.setAllDataBright(brights);
        // TODO 데이터 송신
    }

    @Override
    public void onStartDelayAction(int startDelay) {
        scriptExecuteService.setStartDelay(thisSelectedLedNumber,startDelay);
        ledSettingData.setStartDelay(thisSelectedLedNumber,startDelay);
    }

    @Override
    public void onEndDelayAction(int endDelay) {
        scriptExecuteService.setEndDelay(thisSelectedLedNumber,endDelay);
        ledSettingData.setEndDelay(thisSelectedLedNumber,endDelay);
    }

    /**
     * 효과 지연 설정
     * @param ratio 효과 지연 비율
     */
    @Override
    public void onEffectRatioAction(float ratio) {
        scriptExecuteService.setEffectRatio(thisSelectedLedNumber,ratio);
        scriptExecuteService.initCount();
        ledSettingData.setEffectRatio(thisSelectedLedNumber, (int) (ratio*10));
    }

    /**
     * LED 배열로 설정하여 순차 점멸 패턴 설정
     * @param gapDelay 중간 지연 값
     * @param endDelay 종료 지연 값
     */
    @Override
    public void onArrayGapDelayAction(int gapDelay, int endDelay) {
        scriptExecuteService.setArrayGapDelay(thisSelectedLedNumber,gapDelay, endDelay);
        scriptExecuteService.initCount();
        ledSettingData.setArrayGapDelay(thisSelectedLedNumber,gapDelay, endDelay);
    }

    private class BrightReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int[] dataPassed = intent.getIntArrayExtra("DATA_PASSED");
//            Log.d(TAG,"onReceive" + dataPassed[0]);
            for (int i=0;i<Define.NUMBER_OF_SINGLE_LED;i++) {
                ledViewLayout.setLedColorData(i,dataPassed[i]);
                ledViewLayout.reDraw();
            }
        }
    }

    // TODO OnClick Method
    public void onClick(View v) {
        switch (v.getId()) {
			case R.id.data_modify:
				int ofLed = 0;
				for (int i=0;i<Define.NUMBER_OF_SINGLE_LED;i++) {
					if (((thisSelectedLedNumber>>i) & 0x01) == 0x01) {
						ofLed = i;
						break;
					}
				}
				ScriptDataList thisList = scriptExecuteService.getScriptDataList(ofLed);
				ScriptListActivity.setScriptDataList(thisList);
				Intent intent = new Intent(this,ScriptListActivity.class);
				startActivityForResult(intent,3);
				break;
            case R.id.led_back:
                this.finish();
                break;
            case R.id.led_menu:
//				txThisData(thisSelectedLedNumber);
                txAllData();
				break;
            case R.id.led_save:
                bleManager.writeTxData(TxDatas.formatMemToRom());
                /*txThisData(thisSelectedLedNumber);
				txCounterInit();*/
				/*recordHandler = new RecordHandler();
				if (enableMic) {
					enableMic = false;
					testStopMic();
				} else {
					enableMic = true;
					testMic();
				}*/

				break;

        }
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 3) {
			Log.d(TAG,"Data Result");
			int ofLed = 0;
			for (int i=0;i<Define.NUMBER_OF_SINGLE_LED;i++) {
				if (((thisSelectedLedNumber>>i) & 0x01) == 0x01) {
					ofLed = i;
					break;
				}
			}
			scriptExecuteService.setDataList(ofLed,ScriptListActivity.getScriptDataList());
		}
	}

	private void txCounterInit() {
		bleManager.writeTxData(TxDatas.formatInitCount());
	}

	private void txThisData(int selectedLedNum) {
		for (int i=0;i<Define.NUMBER_OF_SINGLE_LED;i++) {
			if (((selectedLedNum >> i) & 0x01) == 1) {
				txLedNum(i);
			}
		}
	}

	private void txAllData() {
		for (int i=0;i<Define.NUMBER_OF_SINGLE_LED;i++) {
			txLedNum(i);
		}
		txCounterInit();
	}

	private void txLedNum(int ledNum0to8) {
//		List<byte[]> mDataList = txDataFormatter(ledNum);
		List<byte[]> mDataList = TxDatas.formatMemWrite(ledNum0to8,
				scriptExecuteService.getByteArray(ledNum0to8));
		int dataCount = mDataList.size();
		for (int i=0;i<dataCount;i++) {
			bleManager.writeTxData(mDataList.get(i));
		}
	}

	private List<byte[]> txDataFormatter(int ledNum) {
		List<byte[]> txScriptList = new ArrayList<>();
		byte[] fullScript = scriptExecuteService.getByteArray(ledNum);
		int scriptLength = fullScript.length;
		int scriptCount = (scriptLength / 16) + 1;
		for (int i = 0; i<scriptCount; i++) {
			int txCount = scriptLength - (i * 16);
			if (txCount > 16) {
				txCount = 16;
			}
			byte[] scriptData = new byte[txCount + 4];
			scriptData[0] = Define.TX_MEMORY_WRITE;
			scriptData[1] = (byte) ledNum;
			scriptData[2] = (byte) (i*8);
			scriptData[3] = (byte) (txCount/2);
			for (int j=0;j<txCount;j++) {
				scriptData[4+j] = fullScript[i*16 + j];
			}
			txScriptList.add(scriptData);
			bleManager.writeTxData(scriptData);
		}
		return txScriptList;
	}

	private boolean enableMic = false;

	private void testLevelMeter(int amp) {
		byte[] levelData = new byte[10];
		levelData[0] = Define.TX_MEMORY_WRITE;
		levelData[1] = 0;
		levelData[2] = 0;
		levelData[3] = 3;
		levelData[4] = (byte) Define.OP_START;
		levelData[5] = 0;
		if (amp >= Define.OP_CODE_MIN) {
			amp = Define.OP_CODE_MIN - 1;
		}
		else if (amp <0) {
			amp = 0;
		}
		levelData[6] = (byte)amp;
		levelData[7] = 0;
		levelData[8] = (byte) Define.OP_END;
		levelData[9] = 0;
		bleManager.writeTxData(levelData);
	}

	public SoundMeter sm = null;

	private Timer micTimer = null;
	private void testMic() {
		sm = new SoundMeter();
		sm.start();
		micTimer = new Timer();
		micTimer.scheduleAtFixedRate(new RecorderTask(), 0, 100);
	}

	private void testStopMic() {
		micTimer.cancel();
		micTimer.purge();
		micTimer = null;
		sm.stop();
	}

	public class RecorderTask extends TimerTask {

		@Override
		public void run() {
			double k = sm.getAmplitude();

			Message msg = recordHandler.obtainMessage();
			msg.what = SEND_AMPLITUDE;
			msg.arg1 = (int)k;
			recordHandler.sendMessage(msg);
		}
	}

	private final static int SEND_AMPLITUDE = 1;

	private RecordHandler recordHandler = null;

	private class RecordHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case SEND_AMPLITUDE:
//					Log.d(TAG,"Record"+msg.arg1);
					int txAmp = msg.arg1 /8;
					testLevelMeter(txAmp);
					break;
			}
		}
	}
}
