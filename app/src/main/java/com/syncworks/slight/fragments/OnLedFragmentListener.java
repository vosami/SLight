package com.syncworks.slight.fragments;

/**
 * Created by vosami on 2015-03-25.
 */
public interface OnLedFragmentListener {
    // 현재 밝기를 Activity 에 전달
    public void onSingleBrightAction(int currentBright);
    // 현재 색깔을 Activity 에 전달
    public void onColorChangeAction(int red, int green, int blue);
    // 시작 지연 시간 전달
    public void onStartDelayAction(int startDelay);
    // 종료 지연 시간 전달
    public void onEndDelayAction(int endDelay);
    // 효과 지연 비율 전달
    public void onEffectRatioAction(float ratio);
}
