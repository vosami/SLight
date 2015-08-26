package com.syncworks.slight;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.syncworks.slightpref.SLightPref;


public class MainActivity extends ActionBarActivity {
	private final static String TAG = MainActivity.class.getSimpleName();

	private Dialog mDialog = null;

	private ImageView ivVisible;
	private Button btnBleSet, btnLedEffect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnBleSet = (Button) findViewById(R.id.btn_ble_set);
		btnLedEffect = (Button) findViewById(R.id.btn_led_effect);
		ivVisible = (ImageView) findViewById(R.id.iv_long_click);
		ivVisible.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				btnBleSet.setVisibility(View.VISIBLE);
				btnLedEffect.setVisibility(View.VISIBLE);
				return false;
			}

			});
        // 로그 메시지
        Log.d(TAG,"onCreate");
	}

    @Override
    protected void onResume() {
        super.onResume();
        // 설정된 블루투스 주소 가져오기
        SLightPref appPref = new SLightPref(this);
        String bleAddress = appPref.getString(SLightPref.DEVICE_ADDR);
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
			case R.id.btn_comm_set:
				intent = new Intent(this,EasyActivity.class);
				startActivity(intent);
				break;
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
                //intent = new Intent(this, TestActivity.class);
				intent = new Intent(this, SlightSettingActivity.class);
                startActivity(intent);
				break;
			case R.id.btn_gallery_web:
				intent = new Intent(this, GalleryActivity.class);
				startActivity(intent);
				break;
			// SLight 설치 방법 버튼 클릭시
			case R.id.btn_slight_setting:
				createDialog();
				break;
			case R.id.btn_script_list:
				intent = new Intent(this, ScriptDataActivity.class);
				startActivity(intent);
				break;
		}
	}

	/**
	 * SLight 타입별 설치 방법 선택 다이얼로그
	 */
	private void createDialog() {
		// custom dialog
		mDialog = new Dialog(this);
		mDialog.setContentView(R.layout.dialog_slight_select);
		String dlgTitle = getResources().getString(R.string.select_slight_type);
		mDialog.setTitle(dlgTitle);
		Button btnScm100 = (Button) mDialog.findViewById(R.id.btn_scm100);
		Button btnSpm100 = (Button) mDialog.findViewById(R.id.btn_spm100);
		btnScm100.setOnClickListener(dlgListener);
		btnSpm100.setOnClickListener(dlgListener);

		mDialog.show();
	}
	// SLight 설치 방법 선택 다이얼로그 리스너
	private View.OnClickListener dlgListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = null;
			switch (v.getId()) {
				case R.id.btn_scm100:
					intent = new Intent(getApplicationContext(), SLightSettingInfoActivity.class);
					startActivity(intent);
					mDialog.dismiss();
					break;
				case R.id.btn_spm100:
					intent = new Intent(getApplicationContext(), SLightSettingInfoActivity.class);
					startActivity(intent);
					mDialog.dismiss();
					break;
			}
		}
	};



}
