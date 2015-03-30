package com.syncworks.scriptdata;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.syncworks.define.Define;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Created by vosami on 2015-03-20.
 * 스크립트 데이터를 설정하여 실행
 */
public class ScriptExecuteService extends Service implements Runnable{
    private final static String TAG = ScriptExecuteService.class.getSimpleName();

    // 메시지 핸들러
    public final static String CHANGE_BRIGHT_ACTION = "CHANGE_BRIGHT_ACTION";

    // 실행 스레드
    Thread scriptExecuteThread = null;
    private boolean runBool = true;

    // 바인더
    private final IBinder sBinder = new ScriptBinder();

    // 스크립트 데이터 리스트
    ScriptDataList[] scriptDataLists = new ScriptDataList[9];
    // 이전 밝기
    int[] oldBright = new int[9];
    // 현재 밝기
    int[] curBright = new int[9];

    public class ScriptBinder extends Binder {
        public ScriptExecuteService getService() {
            return ScriptExecuteService.this;
        }
    }



    // 서비스 처음 생성시 호출
    @Override
    public void onCreate() {
        super.onCreate();
        for (int i=0;i<Define.NUMBER_OF_SINGLE_LED;i++) {
            parseXml(i,Define.SINGLE_LED,0);
            oldBright[i] = 0;
            curBright[i] = 0;
        }


        Log.d(TAG,"onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        // 스레드를 이용해 스크립트 실행
        runBool = true;
        scriptExecuteThread = new Thread(this);
        scriptExecuteThread.start();
        return sBinder;

    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG,"onUnbind");
        runBool = false;
        return super.onUnbind(intent);
    }



    // 다른 컴포넌트가 startService() 를 호출해서 서비스가 시작시 호출
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    // 서비스 종료시 호출
    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    @Override
    public void run() {
        while(runBool) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
			for (int i=0;i< Define.NUMBER_OF_SINGLE_LED;i++) {
                scriptDataLists[i].DataExecute();
                curBright[i] = scriptDataLists[i].getCurrentBright();
			}
            if (!Arrays.equals(oldBright,curBright)) {
                for (int i=0;i<Define.NUMBER_OF_SINGLE_LED;i++) {
                    oldBright[i] = curBright[i];
                }
//                Log.d(TAG,"curBright" + curBright[0]);
                Intent intent = new Intent();
                intent.setAction(CHANGE_BRIGHT_ACTION);
                intent.putExtra("DATA_PASSED",curBright);
                sendBroadcast(intent);
            }
        }
    }

	// 스크립트 데이터 가져오기
    public ScriptDataList getScriptDataList(int position) {
//        return scriptDataLists.get(position);
        return new ScriptDataList(0);
    }


    /**
     * XML 파일을 Script Data로 변환하는 메소드
     * @param ledNum 스크립트 데이터의 LED 번호
     * @param colorType 불러올 XML 파일의 Color 타입
     * @param pos XML 파일의 포지션
     */
    public void parseXml(int ledNum, boolean colorType, int pos) {
        String[] singleFiles = {"scriptdata0.xml", "scriptdata1.xml", "scriptdata2.xml", "scriptdata3.xml", "scriptdata4.xml"};
        String[] colorFiles = {"scriptcolordata0.xml"};
        AssetManager assetManager = getBaseContext().getAssets();
        try {
            InputStream is;
            if (colorType == Define.SINGLE_LED) {
                is = assetManager.open(singleFiles[pos]);
                scriptDataLists[ledNum] = ScriptXmlParser.parse(is, ledNum);
            }
            else {
                is = assetManager.open(colorFiles[pos]);
                List<ScriptDataList> mData = ScriptXmlParser.parseColor(is,ledNum);
                scriptDataLists[ledNum] = mData.get(0);
                scriptDataLists[ledNum+1] = mData.get(1);
                scriptDataLists[ledNum+2] = mData.get(2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i=0;i<Define.NUMBER_OF_SINGLE_LED;i++) {
            if (scriptDataLists[i] != null) {
                scriptDataLists[i].initCurrentVar();
            }
        }
    }


}
