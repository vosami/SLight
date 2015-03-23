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
		testScriptDataList();
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
}