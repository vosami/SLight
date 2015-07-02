package com.syncworks.leddata;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import static com.syncworks.define.Define.OP_START;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
        test();
    }




    public void test() {
        LedData ledData = new LedData(OP_START,10);
        Log.d("slight","1:"+ledData.getVal()+", 2:"+ledData.getDuration());
    }
}