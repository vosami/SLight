package com.syncworks.slight.util;

/**
 * Created by Kim on 2015-08-11.
 */
public interface BaseExpandableInterface {
    public void onStartTime(int curGroup, int startTime);
    public void onEffectTime(int curGroup, int effectTime);
    public void onRandomTime(int curGroup, int randomTime);
    public void onPatternOption(int curGroup, int patternOption);
    public void onType(int curGroup, int type);
}
