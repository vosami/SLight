package com.syncworks.slight.fragment_easy;

/**
 * Created by vosami on 2015-06-11.
 */
public interface OnEasyFragmentListener {
    public void onModifyName();

    public void onTestConnect();

    public void onScanStart();

    public void onScanStop();

    public void onSetDeviceItem(String devName, String devAddr);

    public void onSelectLed(int ledNum, boolean state);

    public void onSelectRGB(int ledNum, boolean state);

    public void onBrightRGB(int ledNum, int bright);

    public void onBrightLed(int ledNum, int bright);

    public void onEffectStart(boolean isStart);

    public void onEffect(int effect, int delayLong, int randomTime, int startTime, int patternOption);

    public void onRgbEffect(int effect, int delayLong, int randomTime, int startTime);

    public void onColorDialog();

    public void onNotDialog();
    /**SaveFragment 시작*/
    // 참(true)일 경우 휴면 상태 유지, 거짓(false) 일 경우 깨우기
    public void onSleep(boolean isSleep);
    // 동작 시간 설정
    public void onSleepTime(int minute);
    // Fetch Data
    public void onFetchData(int dataNum);
    // Run Mode
    public void onSetRunMode(int runMode, int runPattern);
    // Save Data
    public void onSaveData(int dataNum);
    // 파라미터 설정
    public void onSetParam(int param);
    // 동작시간 설정
    public void onSetSeqTime(int order, int runTime);
    /**SaveFragment 종료*/
    public void onFrag1Start();
    public void onFrag1End();
}
