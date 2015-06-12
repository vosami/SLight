package com.syncworks.slight;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.syncworks.slight.fragment_comm.BleSetFragment;
import com.syncworks.slight.fragment_comm.BrightFragment;
import com.syncworks.slight.fragment_comm.EffectFragment;
import com.syncworks.slight.fragment_comm.LedSelectFragment;
import com.syncworks.slight.fragment_comm.OnCommFragmentListener;
import com.syncworks.vosami.blelib.BleConsumer;


public class CommActivity extends ActionBarActivity implements BleConsumer, OnCommFragmentListener {

    private final static int MAX_STEP = 4;
    private static int fragmentStep = 1;

    private BleSetFragment fragment1st;
    private LedSelectFragment fragment2nd;
    private BrightFragment fragment3rd;
    private EffectFragment fragment4th;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comm, menu);
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

    public void onClick(View view) {
    }

    private void createFragment() {
        String deviceName, deviceAddr;
        deviceName = "";
        deviceAddr = "";
        fragment1st = BleSetFragment.newInstance(deviceName,deviceAddr);
        fragment2nd = LedSelectFragment.newInstance("","");
        fragment3rd = BrightFragment.newInstance("","");
        fragment4th = EffectFragment.newInstance("","");

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.ll_comm_fragment,fragment1st);
        fragmentTransaction.commit();
    }

    // 액션 바의 타이틀 변경
    private void changeActionBarText(int step) {
        String titleText;
        switch(step) {
            case 1:
                titleText = getResources().getString(R.string.comm_activity_step_1);
                break;
            case 2:
                titleText = getResources().getString(R.string.comm_activity_step_2);
                break;
            case 3:
                titleText = getResources().getString(R.string.comm_activity_step_3);
                break;
            case 4:
                titleText = getResources().getString(R.string.comm_activity_step_4);
                break;
            default:
                titleText = "";
                break;
        }
        setTitle(titleText);
    }

    // 각 단계별 Fragment 변경
    private void changeFragment(int stepFragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (stepFragment) {
            case 1:
                fragmentTransaction.replace(R.id.ll_comm_fragment, fragment1st);
                break;
            case 2:
                fragmentTransaction.replace(R.id.ll_comm_fragment, fragment2nd);
                break;
            case 3:
                fragmentTransaction.replace(R.id.ll_comm_fragment, fragment3rd);
                break;
            case 4:
                fragmentTransaction.replace(R.id.ll_comm_fragment, fragment4th);
                break;
            default:
                break;
        }
        fragmentTransaction.commit();
    }

    private void changeStep(int step) {
        // 허용 되지 않은 단계에 있다면 1로 초기화
        if (step <1 && step > MAX_STEP) {
            step = 1;
        }
        changeActionBarText(step);
        changeFragment(fragmentStep);
    }


    // 블루투스 서비스 연결
    @Override
    public void onBleServiceConnect() {

    }

    // Fragments 와 통신
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
