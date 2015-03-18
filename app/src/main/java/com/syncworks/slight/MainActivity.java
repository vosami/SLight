package com.syncworks.slight;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.syncworks.vosami.blelib.BluetoothLeService;


public class MainActivity extends ActionBarActivity {
	private final static String TAG = MainActivity.class.getSimpleName();

	// 블루투스 서비스
	private BluetoothLeService bluetoothLeService;
	private BluetoothAdapter bluetoothAdapter;

	// 블루투스 서비스 연결
	private final ServiceConnection lecServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			bluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
			if (!bluetoothLeService.initialize()) {
				Log.e(TAG, "Unable to initialize Bluetooth");
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			bluetoothLeService = null;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 블루투스 장치 검색
		Intent lecServiceIntent = new Intent(MainActivity.this, BluetoothLeService.class);
		bindService(lecServiceIntent, lecServiceConnection, BIND_AUTO_CREATE);
		final BluetoothManager mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		bluetoothAdapter = mBluetoothManager.getAdapter();
		if (bluetoothAdapter == null) {
			Toast.makeText(this, "Ble not supported", Toast.LENGTH_SHORT).show();
			return;
		}
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
				break;
		}
	}
}
