package com.syncworks.slight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
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

	// 버튼 클릭시 호출
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
			// 블루투스 설정 버튼 클릭시
			case R.id.btn_ble_set:
				break;
			// LED 효과 설정 버튼 클릭시
			case R.id.btn_led_effect:
				intent = new Intent(this, LedEffectActivity.class);
				startActivity(intent);
				break;
			// 타이머 설정 버튼 클릭시
			case R.id.btn_timer_set:
				break;
		}
	}
}
