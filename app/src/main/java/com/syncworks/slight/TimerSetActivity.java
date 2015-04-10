package com.syncworks.slight;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.syncworks.slightpref.SLightPref;
import com.syncworks.vosami.blelib.BleConsumer;
import com.syncworks.vosami.blelib.BleManager;
import com.syncworks.vosami.blelib.BleNotifier;


public class TimerSetActivity extends ActionBarActivity implements BleConsumer{
    private final static String TAG = TimerSetActivity.class.getSimpleName();
    private BleManager bleManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timer_set);


	}

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
        bleManager = BleManager.getBleManager(this);
        bleManager.bind(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
        bleManager.unbind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_timer_set, menu);
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

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_connect:
                SLightPref appPref = new SLightPref(this);
                String addr = appPref.getString(SLightPref.DEVICE_ADDR);
                bleManager.bleConnect(addr);
                break;
            case R.id.btn_disconnect:
                bleManager.bleDisconnect();
                break;
        }
    }
}
