package com.syncworks.slight;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TabHost;

import com.syncworks.endlessadapter.HttpListData;
import com.syncworks.slight.fragments.FragmentColorScriptList;
import com.syncworks.slight.fragments.FragmentHttpDataList;
import com.syncworks.slight.fragments.FragmentSingleScriptList;
import com.syncworks.slight.fragments.TabPagerAdapter;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;


public class ScriptDataActivity extends FragmentActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {

    private TabHost mTabHost;
    private ViewPager mViewPager;
    private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, ScriptDataActivity.TabInfo>();
    private TabPagerAdapter mPagerAdapter;

    /**
     * Maintains extrinsic info of a tab's construct
     */
    private class TabInfo {
        private String tag;
        private Class<?> clss;
        private Bundle args;
        private Fragment fragment;
        TabInfo(String tag, Class<?> clazz, Bundle args) {
            this.tag = tag;
            this.clss = clazz;
            this.args = args;
        }
    }

    /**
     * A simple factory that returns dummy views to the TabHost
     */
    class TabFactory implements TabHost.TabContentFactory {

        private final Context mContext;

        /**
         * @param context
         */
        public TabFactory(Context context) {
            mContext = context;
        }

        /** (non-Javadoc)
         * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
         */
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
    }

    /** (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout
        setContentView(R.layout.activity_script_data);
        // Initialise the TabHost
        this.initialiseTabHost(savedInstanceState);
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
        }
        // Initialise ViewPager
        this.initialiseViewPager();


    }

    /** (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
     */
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("tab", mTabHost.getCurrentTabTag()); //save the tab selected
        super.onSaveInstanceState(outState);
    }

    /**
     * Initialise ViewPager
     */
    private void initialiseViewPager() {

        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, FragmentSingleScriptList.class.getName()));
        fragments.add(Fragment.instantiate(this, FragmentColorScriptList.class.getName()));
        fragments.add(Fragment.instantiate(this, FragmentHttpDataList.class.getName()));
        this.mPagerAdapter  = new TabPagerAdapter(super.getSupportFragmentManager(), fragments);
        //
        this.mViewPager = (ViewPager)super.findViewById(R.id.viewpager);
        this.mViewPager.setAdapter(this.mPagerAdapter);
        this.mViewPager.setOnPageChangeListener(this);
    }

    /**
     * Initialise the Tab Host
     */
    private void initialiseTabHost(Bundle args) {
        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();
        TabInfo tabInfo = null;
        String strOfTab1 = getResources().getString(R.string.tab1_script_data);
        String strOfTab2 = getResources().getString(R.string.tab2_script_data);
        String strOfTab3 = getResources().getString(R.string.tab3_script_data);

        ScriptDataActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec(strOfTab1).setIndicator(strOfTab1), ( tabInfo = new TabInfo(strOfTab1, FragmentSingleScriptList.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        ScriptDataActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec(strOfTab2).setIndicator(strOfTab2), (tabInfo = new TabInfo(strOfTab2, FragmentColorScriptList.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        ScriptDataActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec(strOfTab3).setIndicator(strOfTab3), (tabInfo = new TabInfo(strOfTab3, FragmentHttpDataList.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        // Default to first tab
        //this.onTabChanged("Tab1");
        //
        mTabHost.setOnTabChangedListener(this);
    }

    /**
     *
     * @param activity
     * @param tabHost
     * @param tabSpec
     * @param tabInfo
     */
    private static void AddTab(ScriptDataActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
        // Attach a Tab view factory to the spec
        tabSpec.setContent(activity.new TabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onPageSelected(int position) {
        // TODO Auto-generated method stub
        this.mTabHost.setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onTabChanged(String tabId) {
        //TabInfo newTab = this.mapTabInfo.get(tag);
        int pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
    }


}
