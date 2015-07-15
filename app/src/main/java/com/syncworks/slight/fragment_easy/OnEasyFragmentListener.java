package com.syncworks.slight.fragment_easy;

/**
 * Created by vosami on 2015-06-11.
 */
public interface OnEasyFragmentListener {
    public void onModifyName();

    public void onScanStart();

    public void onScanStop();

    public void onSetDeviceItem(String devName, String devAddr);

    public void onSelectLed(int ledNum, boolean state);

    public void onSelectRGB(int ledNum, boolean state);

    public void onBrightRGB(int ledNum, int bright);

    public void onBrightLed(int ledNum, int bright);

    public void onEffect(int effect, boolean isDelayLong, boolean isRandom, int startTime);

    public void onColorDialog();

    public void onNotDialog();
    /**SaveFragment 시작*/
    // 휴면중일 때 LED 체크를 하느냐 마느냐? 설정
    public void onSleepLedCheck(boolean isCheckLed);
    // 참(true)일 경우 휴면 상태 유지, 거짓(false) 일 경우 깨우기
    public void onSleep(boolean isSleep);
    // 랜덤 플레이 설정
    public void onRandomPlay(int playTime);
    /**SaveFragment 종료*/
    public void onFrag1Start();
    public void onFrag1End();
}
