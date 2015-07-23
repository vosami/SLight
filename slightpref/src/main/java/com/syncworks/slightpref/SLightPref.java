package com.syncworks.slightpref;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

/**
 * Created by vosami on 2015-04-10.
 * 스마트 라이트 설정 값 저장
 */
public class SLightPref {
    private final static String PREF_NAME = "com.syncworks.slight";
    // 처음 실행 확인
    private final static String DEVICE_FIRST = "key_first";
    // 장치 이름 저장
    public final static String DEVICE_NAME = "key_name";
    // 장치 주소 저장
    public final static String DEVICE_ADDR = "key_addr";
    // 장치 버전 저장
    public final static String DEVICE_VERSION = "key_version";


    public final static String[] DEVICE_LED_NAME = {"key_led1","key_led2","key_led3","key_led4",
            "key_led5","key_led6","key_led7","key_led8","key_led9"};
    public final static String[] DEVICE_COLOR_LED_NAME = {"key_color_led1","key_color_led2","key_color_led3"};

    public final static String[] EASY_ACTIVITY = {"key_easy1","key_easy2","key_easy3","key_easy4","key_easy5"};

    public final static String FRAG_INSTALL_NOT_SHOW = "key_install_not_show";
    private static Context context;

    private SharedPreferences pref = null;

    /**
     * 생성자
     * @param c 컨텍스트 설정
     */
    public SLightPref(Context c) {
        context = c;
        pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);

        // 처음 실행하는 것이라면 환경 변수 새로 설정
        if (!getBoolean(DEVICE_FIRST)) {
            initPref();
        }
    }

    private void initPref() {
        // 최초 실행 확인 변수 설정
        putBoolean(DEVICE_FIRST, true);
        // 연결 장치 이름 설정
        putString(DEVICE_NAME,"NONE");
        // 연결 장치 주소 설정
        putString(DEVICE_ADDR,"00:00:00:00:00:00");
        // 연결 장치 버전 저장
        putString(DEVICE_VERSION, "");
        // LED 장치 이름 설정
        Resources r = context.getResources();
        putString(DEVICE_LED_NAME[0],r.getString(R.string.led1_txt));
        putString(DEVICE_LED_NAME[1],r.getString(R.string.led2_txt));
        putString(DEVICE_LED_NAME[2],r.getString(R.string.led3_txt));
        putString(DEVICE_LED_NAME[3],r.getString(R.string.led4_txt));
        putString(DEVICE_LED_NAME[4],r.getString(R.string.led5_txt));
        putString(DEVICE_LED_NAME[5],r.getString(R.string.led6_txt));
        putString(DEVICE_LED_NAME[6],r.getString(R.string.led7_txt));
        putString(DEVICE_LED_NAME[7],r.getString(R.string.led8_txt));
        putString(DEVICE_LED_NAME[8],r.getString(R.string.led9_txt));
        putString(DEVICE_COLOR_LED_NAME[0],r.getString(R.string.cled1_txt));
        putString(DEVICE_COLOR_LED_NAME[1],r.getString(R.string.cled2_txt));
        putString(DEVICE_COLOR_LED_NAME[2],r.getString(R.string.cled3_txt));
        putBoolean(FRAG_INSTALL_NOT_SHOW, false);
        putBoolean(EASY_ACTIVITY[0],false);
        putBoolean(EASY_ACTIVITY[1],false);
        putBoolean(EASY_ACTIVITY[2],false);
        putBoolean(EASY_ACTIVITY[3],false);
        putBoolean(EASY_ACTIVITY[4],false);
    }

    public void putString(String key, String val) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, val);
        editor.apply();
    }

    public String getString(String key) {
        // 데이터가 없다면 "NONE"을 반환, 에러 발생하면 "ERROR" 반환
        try {
            return pref.getString(key, "NONE");
        } catch (Exception e) {
            Log.e(PREF_NAME, "Error getString");
            return "ERROR";
        }
    }

    public void putBoolean(String key, boolean val) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, val);
        editor.apply();
    }

    public boolean getBoolean(String key) {
        return pref.getBoolean(key,false);
    }
}
