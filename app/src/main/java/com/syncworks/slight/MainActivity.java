package com.syncworks.slight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends ActionBarActivity {
	private final static String TAG = MainActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        // 로그 메시지
        Log.d(TAG,"onCreate");
	}

    @Override
    protected void onResume() {
        super.onResume();
        // 설정된 블루투스 주소 가져오기
        SLightPreference appPref = new SLightPreference(this);
        String bleAddress = appPref.getString(SLightPreference.DEVICE_ADDR);
        // 백그라운드 서비스 시작
        /*Intent bleAutoServiceIntent = new Intent(getBaseContext(), BleAutoService.class);
        bleAutoServiceIntent.putExtra(Define.BLE_ADDRESS,bleAddress);
        startService(bleAutoServiceIntent);*/
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG,"onDestroy");
        super.onDestroy();
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
                intent = new Intent(this, BleSetActivity.class);
                startActivity(intent);
				break;
			// LED 효과 설정 버튼 클릭시
			case R.id.btn_led_effect:
				intent = new Intent(this, LedEffectActivity.class);
				startActivity(intent);
				break;
			// 타이머 설정 버튼 클릭시
			case R.id.btn_timer_set:
//                intent = new Intent(this, TimerSetActivity.class);
                intent = new Intent(this, TestActivity.class);
                startActivity(intent);
				break;
		}
	}
}
