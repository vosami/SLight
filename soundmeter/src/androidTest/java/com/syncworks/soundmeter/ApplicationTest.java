package com.syncworks.soundmeter;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
	SoundMeter sm = null;

	public ApplicationTest() {
		super(Application.class);
		testMic();
	}

	public void testMic() {
		sm = new SoundMeter();
		sm.start();
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new RecorderTask(), 0, 1000);

	}

	public class RecorderTask extends TimerTask {

		@Override
		public void run() {
			double k = sm.getAmplitude();
			String mStr = "";
			mStr.format("%.2f",k);
			Log.d("Record",mStr);
		}
	}
}