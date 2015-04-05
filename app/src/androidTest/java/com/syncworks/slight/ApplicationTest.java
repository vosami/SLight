package com.syncworks.slight;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
	public ApplicationTest() {
		super(Application.class);
		testByte();
	}

	public void testByte() {
		byte[] arrayByte = new byte[110];

		for (int i =0;i<110;i++) {
			arrayByte[i] = (byte)i;
			Log.d("test","array"+arrayByte[i]);
		}
	}
}