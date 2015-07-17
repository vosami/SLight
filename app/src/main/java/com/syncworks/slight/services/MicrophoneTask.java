package com.syncworks.slight.services;

import com.syncworks.soundmeter.SoundMeter;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by vosami on 2015-07-17.
 */
public class MicrophoneTask extends TimerTask {

    private static SoundMeter sm = null;
    private Timer smTimer = null;

    @Override
    public void run() {
        doSoundChange(sm.getAmplitude());
    }

    public void micStart() {
        TimerTask timerTask = new MicrophoneTask();
        sm = new SoundMeter();
        sm.start();
        smTimer = new Timer();
        smTimer.scheduleAtFixedRate(timerTask, 0, 100);
    }

    public void micCancel() {
        smTimer.cancel();
        smTimer.purge();
        smTimer = null;
        sm.stop();
    }

    private static OnSoundListener listener = null;

    public interface OnSoundListener {
        public void onSoundChange(int sound);
    }

    public void setOnSoundListener(OnSoundListener mListener) {
        this.listener = mListener;
    }

    private void doSoundChange(int sound) {
        if (listener != null) {
            listener.onSoundChange(sound);
        }
    }
}
