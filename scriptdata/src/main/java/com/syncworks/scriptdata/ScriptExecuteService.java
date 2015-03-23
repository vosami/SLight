package com.syncworks.scriptdata;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.syncworks.define.Define;

import java.util.List;

/**
 * Created by vosami on 2015-03-20.
 * 스크립트 데이터를 설정하여 실행
 */
public class ScriptExecuteService extends Service implements Runnable{
    private final static String TAG = ScriptExecuteService.class.getSimpleName();

    // 실행 스레드
    Thread scriptExecuteThread = null;
    private boolean runBool = true;

    // 바인더
    private final IBinder sBinder = new ScriptBinder();

    // 스크립트 데이터 리스트
    ScriptDataList[] scriptDataLists = new ScriptDataList[9];
//    List<List<ScriptData>> scriptDataLists;

    public class ScriptBinder extends Binder {
        public ScriptExecuteService getService() {
            return ScriptExecuteService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // 스레드를 이용해 스크립트 실행
        runBool = true;
        scriptExecuteThread = new Thread(this);
        scriptExecuteThread.start();
        return sBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        runBool = false;
        return super.onUnbind(intent);
    }

    // 서비스 처음 생성시 호출
    @Override
    public void onCreate() {
        super.onCreate();
        /*scriptDataLists.add(new ArrayList<ScriptData>());
        scriptDataLists.add(new ArrayList<ScriptData>());
        scriptDataLists.add(new ArrayList<ScriptData>());
        scriptDataLists.add(new ArrayList<ScriptData>());
        scriptDataLists.add(new ArrayList<ScriptData>());
        scriptDataLists.add(new ArrayList<ScriptData>());
        scriptDataLists.add(new ArrayList<ScriptData>());
        scriptDataLists.add(new ArrayList<ScriptData>());
        scriptDataLists.add(new ArrayList<ScriptData>());*/
    }

    // 다른 컴포넌트가 startService() 를 호출해서 서비스가 시작시 호출
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    // 서비스 종료시 호출
    @Override
    public void onDestroy() {
        scriptExecuteThread.interrupt();
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
				scriptActionParser(i);
			}
        }
    }

	// 스크립트 데이터 설정
    public void setScriptDataList(int position, List<ScriptData> scriptDataList) {
//        scriptDataLists.set(position, scriptDataList);
    }

	// 스크립트 데이터 가져오기
    public ScriptDataList getScriptDataList(int position) {
//        return scriptDataLists.get(position);
        return new ScriptDataList(0);
    }



	private void scriptActionParser(int ledNumber) {
//		int val = scriptDataLists.get(ledNumber).get(0).getVal();
	}
}
