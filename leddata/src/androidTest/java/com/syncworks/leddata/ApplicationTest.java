package com.syncworks.leddata;

import android.app.Application;
import android.test.ApplicationTestCase;

import static com.syncworks.define.Define.*;
/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    LedData ledData = new LedData(OP_START,10);


}