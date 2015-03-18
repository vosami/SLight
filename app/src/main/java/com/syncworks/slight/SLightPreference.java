package com.syncworks.slight;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by vosami on 2015-03-18.
 * 스마트 라이트 설정 값
 */
public class SLightPreference {
    private final static String PREF_NAME = "com.syncworks.slight";

    public final static String DEVICE_NAME = "key_lec_name";
    public final static String DEVICE_ADDR = "key_lec_addr";

    private static Context context;

    /**
     * 생성자
     * @param c 컨텍스트 설정
     */
    public SLightPreference(Context c) {
        context = c;
    }

    /**
     * 환경 설정 값 저장 (String)
     * @param key
     * @param val
     */
    public void putString(String key, String val) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, val);
        editor.commit();
    }

    public void putInt(String key, int val) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, val);
        editor.commit();
    }

    public void putFloat(String key, float val) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(key, val);
        editor.commit();
    }

    public String getString(String key) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        // 데이터가 없다면 "NONE"을 반환, 에러 발생하면 "ERROR" 반환
        try {
            return pref.getString(key, "NONE");
        } catch (Exception e) {
            Log.e(PREF_NAME, "Error getString");
            return "ERROR";
        }
    }

    public int getInt(String key, int defVal) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        // 데이터가 없다면 "NONE"을 반환, 에러 발생하면 "ERROR" 반환
        try {
            return pref.getInt(key, defVal);
        } catch (Exception e) {
            Log.e(PREF_NAME, "Error getInt");
            return 0;
        }
    }

    public float getFloat(String key, float defVal) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        // 데이터가 없다면 "NONE"을 반환, 에러 발생하면 "ERROR" 반환
        try {
            return pref.getFloat(key, defVal);
        } catch (Exception e) {
            Log.e(PREF_NAME, "Error getInt");
            return 0;
        }
    }
}
