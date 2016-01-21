package com.syncworks.slight.util;

/**
 * Created by vosami on 2015-08-11.
 */
public class EffectOptionData {
    public boolean isShowStartDelay = false;
    public boolean isShowEffectDelay = false;
    public boolean isShowRandomDelay = false;
    public boolean isShowPatternOption = false;
    public boolean isShowType = false;
    public int timeStartDelay = 0;
    public int timeEffectDelay = 0;
    public int timeRandomDelay = 0;
    public int patternOption = 0;
    public int patternType = 0;
    public String randomString = "";

    public EffectOptionData() {
        isShowStartDelay = false;
        isShowEffectDelay = false;
        isShowRandomDelay = false;
        isShowPatternOption = false;
        isShowType = false;
        timeStartDelay = 0;
        timeEffectDelay = 0;
        timeRandomDelay = 0;
        patternOption = 0;
        patternType = 0;
        randomString = "랜덤시간";
    }

}
