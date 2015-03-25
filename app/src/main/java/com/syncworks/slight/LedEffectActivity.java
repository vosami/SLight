package com.syncworks.slight;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;

import com.syncworks.define.Define;
import com.syncworks.ledselectlayout.LedSelectLayout;
import com.syncworks.ledviewlayout.LedViewLayout;
import com.syncworks.scriptdata.ScriptDataListSpinnerAdapter;
import com.syncworks.slight.fragments.ColorAlwaysOn;
import com.syncworks.slight.fragments.OnLedFragmentListener;
import com.syncworks.slight.fragments.SingleAlwaysOn;
import com.syncworks.slight.fragments.SingleFragment;
import com.syncworks.titlebarlayout.TitleBarLayout;


public class LedEffectActivity extends ActionBarActivity implements OnLedFragmentListener {

    // LED 선택 레이아웃
    LedSelectLayout ledSelectLayout;
    // LED 점멸 패턴 확인 레이아웃
    LedViewLayout ledViewLayout;
    // 타이틀 바 레이아웃
    TitleBarLayout titleBarLayout;
    // 점멸 패턴 선택 스피너
    Spinner spPatternSelect;

    // 단색 LED 항상 켜기용 Fragment
    Fragment curFragment;
    SingleAlwaysOn singleAlwaysOn;
    SingleFragment singleFragment;
    ColorAlwaysOn coloralwaysOn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_led_effect);
        // View 등록
        findViews();
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
        setSpinnerPatternName(Define.SINGLE_LED);

        createFragments();
        replaceFragments();

    }

    // 점멸 패턴 스피너에 설정
    private void setSpinnerPatternName(boolean ledColor) {
        String[] patternName;
        ScriptDataListSpinnerAdapter spinnerAdapter;
        // SINGLE LED 점멸 패턴 데이터 가져오기
        if (ledColor == Define.SINGLE_LED) {
            patternName = getResources().getStringArray(R.array.single_pattern_name);
        }
        else {
            patternName = getResources().getStringArray(R.array.color_pattern_name);
        }
        spinnerAdapter = new ScriptDataListSpinnerAdapter(this.getApplicationContext(),android.R.layout.simple_spinner_item, patternName);
        spPatternSelect.setAdapter(spinnerAdapter);
    }

    // LED 선택 레이아웃 리스너 설정
    private LedSelectLayout.OnLedSelectListener onLedSelectListener = new LedSelectLayout.OnLedSelectListener() {
        @Override
        public void onLedSelect(boolean isSingleLed, int enabledLedGroup, int selectedLed) {
            titleBarLayout.setLedNumber(selectedLed);
        }

        @Override
        public void onLedCheck(boolean isSingleLed, int enabledLedGroup, int checkedLed) {
            titleBarLayout.setLedNumber(checkedLed);
        }
    };


    private void createFragments() {
        singleAlwaysOn = SingleAlwaysOn.newInstance("0","0");
        singleFragment = SingleFragment.newInstance("0","0");
        coloralwaysOn = ColorAlwaysOn.newInstance("0", "0");
    }

    private void replaceFragments() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.ll_fragment,singleAlwaysOn);
//        fragmentTransaction.add(R.id.ll_fragment,singleFragment);
        fragmentTransaction.add(R.id.ll_fragment, coloralwaysOn);
        fragmentTransaction.commit();
//        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
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
}
