package com.syncworks.slight.fragments;

import android.graphics.Color;

import com.syncworks.define.Define;
import com.syncworks.slight.LedEffectActivity;

/**
 * Created by vosami on 2015-03-25.
 */
public class LedSettingData {

    private SingleData[] singleDatas = new SingleData[Define.NUMBER_OF_SINGLE_LED];
    private ColorData[] colorDatas = new ColorData[Define.NUMBER_OF_COLOR_LED];

    protected static LedSettingData ledSettingData = null;

    public static LedSettingData getInstance() {
        if (ledSettingData == null) {
            ledSettingData = new LedSettingData();
        }
        return ledSettingData;
    }

    public LedSettingData() {
        initVar();
    }
    private void initVar() {
        for (int i=0;i< Define.NUMBER_OF_SINGLE_LED;i++) {
            singleDatas[i] = new SingleData();
            singleDatas[i].setSelectPattern(0);
            singleDatas[i].setStartDelay(0);
            singleDatas[i].setEndDelay(0);
            singleDatas[i].setBright(100);
            singleDatas[i].setRatioBright(10);
            singleDatas[i].setRatioDelay(10);
        }
        for (int i=0;i<Define.NUMBER_OF_COLOR_LED;i++) {
            colorDatas[i] = new ColorData();
            colorDatas[i].setSelectPattern(0);
            colorDatas[i].setStartDelay(0);
            colorDatas[i].setEndDelay(0);
            colorDatas[i].setColor(100,0,0);
            colorDatas[i].setRatioBright(10);
            colorDatas[i].setRatioDelay(10);
        }
    }
    public LedEffectActivity.FragmentType getFragmentType(int ledNum) {
        LedEffectActivity.FragmentType retType = LedEffectActivity.FragmentType.SINGLE_ALWAYS_ON;
        switch (ledNum) {
            case Define.SELECTED_COLOR_LED1:
                if (colorDatas[0].getSelectPattern() == 0) {
                    retType = LedEffectActivity.FragmentType.COLOR_ALWAYS_ON;
                } else {
                    retType = LedEffectActivity.FragmentType.SINGLE;
                }
                break;
            case Define.SELECTED_COLOR_LED2:
                if (colorDatas[1].getSelectPattern() == 0) {
                    retType = LedEffectActivity.FragmentType.COLOR_ALWAYS_ON;
                } else {
                    retType = LedEffectActivity.FragmentType.SINGLE;
                }
                break;
            case Define.SELECTED_COLOR_LED3:
                if (colorDatas[2].getSelectPattern() == 0) {
                    retType = LedEffectActivity.FragmentType.COLOR_ALWAYS_ON;
                } else {
                    retType = LedEffectActivity.FragmentType.SINGLE;
                }
                break;
            case Define.SELECTED_LED1:
                if (singleDatas[0].getSelectPattern() == 0) {
                    retType = LedEffectActivity.FragmentType.SINGLE_ALWAYS_ON;
                } else {
                    retType = LedEffectActivity.FragmentType.SINGLE;
                }
                break;
            case Define.SELECTED_LED2:
                if (singleDatas[1].getSelectPattern() == 0) {
                    retType = LedEffectActivity.FragmentType.SINGLE_ALWAYS_ON;
                } else {
                    retType = LedEffectActivity.FragmentType.SINGLE;
                }
                break;
            case Define.SELECTED_LED3:
                if (singleDatas[2].getSelectPattern() == 0) {
                    retType = LedEffectActivity.FragmentType.SINGLE_ALWAYS_ON;
                } else {
                    retType = LedEffectActivity.FragmentType.SINGLE;
                }
                break;
            case Define.SELECTED_LED4:
                if (singleDatas[3].getSelectPattern() == 0) {
                    retType = LedEffectActivity.FragmentType.SINGLE_ALWAYS_ON;
                } else {
                    retType = LedEffectActivity.FragmentType.SINGLE;
                }
                break;
            case Define.SELECTED_LED5:
                if (singleDatas[4].getSelectPattern() == 0) {
                    retType = LedEffectActivity.FragmentType.SINGLE_ALWAYS_ON;
                } else {
                    retType = LedEffectActivity.FragmentType.SINGLE;
                }
                break;
            case Define.SELECTED_LED6:
                if (singleDatas[5].getSelectPattern() == 0) {
                    retType = LedEffectActivity.FragmentType.SINGLE_ALWAYS_ON;
                } else {
                    retType = LedEffectActivity.FragmentType.SINGLE;
                }
                break;
            case Define.SELECTED_LED7:
                if (singleDatas[6].getSelectPattern() == 0) {
                    retType = LedEffectActivity.FragmentType.SINGLE_ALWAYS_ON;
                } else {
                    retType = LedEffectActivity.FragmentType.SINGLE;
                }
                break;
            case Define.SELECTED_LED8:
                if (singleDatas[7].getSelectPattern() == 0) {
                    retType = LedEffectActivity.FragmentType.SINGLE_ALWAYS_ON;
                } else {
                    retType = LedEffectActivity.FragmentType.SINGLE;
                }
                break;
            case Define.SELECTED_LED9:
                if (singleDatas[8].getSelectPattern() == 0) {
                    retType = LedEffectActivity.FragmentType.SINGLE_ALWAYS_ON;
                } else {
                    retType = LedEffectActivity.FragmentType.SINGLE;
                }
                break;
        }

        return retType;
    }

    public int convertLedNumber(int ledNum) {
        return 0;
    }

    public class SingleData {
        private int singleSelectPattern = 0;
        private int singleStartDelay = 0;
        private int singleEndDelay = 0;
        private int singleRatioBright = 10;
        private int singleRatioDelay = 10;
        private int singleBright = 0;

        public void setSelectPattern(int pattern) {
            singleSelectPattern = pattern;
        }
        public void setStartDelay(int delay) {
            singleStartDelay = delay;
        }
        public void setEndDelay(int delay) {
            singleEndDelay = delay;
        }
        public void setRatioBright(int ratio) {
            singleRatioBright = ratio;
        }
        public void setRatioDelay(int ratio) {
            singleRatioDelay = ratio;
        }
        public void setBright(int bright) {
            singleBright = bright;
        }
        public int getSelectPattern() {
            return singleSelectPattern;
        }
        public int getStartDelay() {
            return singleStartDelay;
        }
        public int getEndDelay() {
            return singleEndDelay;
        }
        public int getRatioBright() {
            return singleRatioBright;
        }
        public int getRatioDelay() {
            return singleRatioDelay;
        }
        public int getBright() {
            return singleBright;
        }
    }


    public class ColorData {
        private int colorSelectPattern = 0;
        private int colorStartDelay = 0;
        private int colorEndDelay = 0;
        private int colorRatioBright = 10;
        private int colorRatioDelay = 10;
        private int colorRedBright = 0;
        private int colorGreenBright = 0;
        private int colorBlueBright = 0;
        public void setSelectPattern(int pattern) {
            colorSelectPattern = pattern;
        }
        public void setStartDelay(int delay) {
            colorStartDelay = delay;
        }
        public void setEndDelay(int delay) {
            colorEndDelay = delay;
        }
        public void setRatioBright(int ratio) {
            colorRatioDelay = ratio;
        }
        public void setRatioDelay(int ratio) {
            colorStartDelay = ratio;
        }
        public void setColor(int red, int green, int blue) {
            colorRedBright = red;
            colorGreenBright = green;
            colorBlueBright = blue;
        }
        public int getSelectPattern() {
            return colorSelectPattern;
        }
        public int getStartDelay() {
            return colorStartDelay;
        }
        public int getEndDelay() {
            return colorEndDelay;
        }
        public int getRatioBright() {
            return colorRatioBright;
        }
        public int getRatioDelay() {
            return colorRatioDelay;
        }
        public int getBright() {
            return Color.rgb(colorRedBright,colorGreenBright,colorBlueBright);
        }
    }
}
