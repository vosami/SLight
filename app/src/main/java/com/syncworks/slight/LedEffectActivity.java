package com.syncworks.slight;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.syncworks.ledselectlayout.LedSelectLayout;


public class LedEffectActivity extends ActionBarActivity {

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
        LedSelectLayout ledSelectLayout = (LedSelectLayout) findViewById(R.id.led_select_layout);

        // LED 선택 레이아웃 리스너 등록
        ledSelectLayout.setOnLedSelectListener(onLedSelectListener);
    }

    // LED 선택 레이아웃 리스너 설정
    private LedSelectLayout.OnLedSelectListener onLedSelectListener = new LedSelectLayout.OnLedSelectListener() {
        @Override
        public void onLedSelect(boolean isSingleLed, int enabledLedGroup, int selectedLed) {

        }

        @Override
        public void onLedCheck(boolean isSingleLed, int enabledLedGroup, int checkedLed) {

        }
    };

}
