package com.syncworks.scriptdata;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
	private final static String TAG = "ScriptDataList";
	public ApplicationTest() {
		super(Application.class);
		testByte();
	}

	public void testScriptDataList() {
		ScriptDataList dataList = new ScriptDataList(0);
		dataList.add(new ScriptData(0, 0));
		dataList.add(new ScriptData(1, 2));
		dataList.getDataListSize();
		byte[] bytes = dataList.toByteArray();
		for (int i=0;i<bytes.length;i++) {
			Log.d(TAG,"tt"+bytes[i]);
		}
	}

	public void testByte() {
		byte[] arrayByte = new byte[110];

		for (int i =0;i<110;i++) {
			arrayByte[i] = (byte)i;

		}

		for (int i=0;i<110;i++) {
			Log.d(TAG,"array"+arrayByte[i]);
		}
	}
}