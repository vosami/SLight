package com.syncworks.slight;

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

import com.syncworks.scriptdata.ScriptExecuteService;


public class TestActivity extends ActionBarActivity {
    private final static String TAG = TestActivity.class.getSimpleName();

    // 메시지 수신 리시버
    BrightReceiver receiver;
    // LED 실행 서비스
    ScriptExecuteService scriptExecuteService;
    boolean mBound = false;         // 서비스 연결 여부

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }
    // 액티비티가 시작되면 서비스에 연결
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
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
        if (mBound) {
            unbindService(scriptExecuteServiceConnection);
            mBound = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
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

    public void onTest(View v) {
        switch (v.getId()) {
            case R.id.btn_test:

                break;
        }
    }

    /*private void parseXml() {
        AssetManager assetManager = getBaseContext().getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
            for (int i=0;i<files.length;i++) {
                if (files[i].contains(".xml")) {
                    InputStream is = assetManager.open(files[i]);
                    Log.d(TAG,"File Name is "+files[i] );
                    ScriptXmlParser.parse(is);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }*/

    private class BrightReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int[] dataPassed = intent.getIntArrayExtra("DATA_PASSED");
            Log.d(TAG,"onReceive" + dataPassed[0]);
        }
    }
}
