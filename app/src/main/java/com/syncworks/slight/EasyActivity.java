package com.syncworks.slight;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.syncworks.define.Logger;
import com.syncworks.slight.fragment_easy.BleSetFragment;
import com.syncworks.slight.fragment_easy.OnEasyFragmentListener;
import com.syncworks.slight.widget.StepView;




public class EasyActivity extends ActionBarActivity implements OnEasyFragmentListener {

    StepView stepView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        findViews();
        createFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_easy, menu);
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
        stepView = (StepView) findViewById(R.id.easy_step_view);
        StepView.OnStepViewTouchListener stepViewTouchListener = new StepView.OnStepViewTouchListener() {
            @Override
            public void onStepViewEvent(int clickStep) {
                Logger.v(this, "OnStepView", clickStep);
            }
        };
    }


    /**********************************************************************************************
     * Fragment 관련 설정 시작
     *********************************************************************************************/

    private BleSetFragment fragment1st;
//    private LedSelectFragment fragment2nd;
//    private BrightFragment fragment3rd;
//    private EffectFragment fragment4th;

    private void createFragment() {
        fragment1st = BleSetFragment.newInstance();
//        fragment2nd = LedSelectFragment.newInstance();
//        fragment3rd = BrightFragment.newInstance("","");
//        fragment4th = EffectFragment.newInstance();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.easy_ll_fragment, fragment1st);
        fragmentTransaction.commit();
    }
    /**********************************************************************************************
     * Fragment 관련 설정 종료
     *********************************************************************************************/

    @Override
    public void onModifyName() {

    }

    @Override
    public void onScanStart() {

    }

    @Override
    public void onScanStop() {

    }

    @Override
    public void onSetDeviceItem(String devName, String devAddr) {

    }

    @Override
    public void onSelectLed(int ledNum, boolean state) {

    }

    @Override
    public void onSelectRGB(int ledNum, boolean state) {

    }

    @Override
    public void onBrightRGB(int ledNum, int bright) {

    }

    @Override
    public void onBrightLed(int ledNum, int bright) {

    }

    @Override
    public void onEffect(int effect, int param) {

    }

    @Override
    public void onColorDialog() {

    }

    @Override
    public void onNotDialog() {

    }
}
