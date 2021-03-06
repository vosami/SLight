package com.syncworks.slightblelib.service;

/**
 * Created by vosami on 1/10/15.
 * Detection Tracker
 */
public class DetectionTracker {
    private static DetectionTracker sDetectionTracker = null;
    private long mLastDetectionTime = 0l;
    private DetectionTracker() {

    }
    public static synchronized DetectionTracker getInstance() {
        if (sDetectionTracker == null) {
            sDetectionTracker  = new DetectionTracker();
        }
        return sDetectionTracker;
    }
    public long getLastDetectionTime() {
        return mLastDetectionTime;
    }
    public void recordDetection() {
        mLastDetectionTime = System.currentTimeMillis();
    }
}
