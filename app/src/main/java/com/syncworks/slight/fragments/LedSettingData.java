package com.syncworks.slight.fragments;

import android.graphics.Color;

import com.syncworks.define.Define;
import com.syncworks.slight.LedEffectActivity;

/**
 * Created by vosami on 2015-03-25.
 */
public class LedSettingData {

    private SetData[] singleDatas = new SetData[Define.NUMBER_OF_SINGLE_LED];
    private SetData[] colorDatas = new SetData[Define.NUMBER_OF_COLOR_LED];

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
            singleDatas[i] = new SetData();
            singleDatas[i].pattern = 0;
            singleDatas[i].bright = 100;
        }
        for (int i=0;i<Define.NUMBER_OF_COLOR_LED;i++) {
            colorDatas[i] = new SetData();
            colorDatas[i].pattern = 0;
            colorDatas[i].rgbData = Color.rgb(100,0,0);
        }
    }
    public boolean getLedType(int ledNum) {
        if ((ledNum & Define.SELECTED_COLOR_LED1) == Define.SELECTED_COLOR_LED1 ||
                (ledNum & Define.SELECTED_COLOR_LED2) == Define.SELECTED_COLOR_LED2 ||
                (ledNum & Define.SELECTED_COLOR_LED3) == Define.SELECTED_COLOR_LED3) {
            return Define.COLOR_LED;
        }
        return Define.SINGLE_LED;
    }

    // 배열의 수와 Color 로 세팅 데이터 가져오기
    public SetData getNaturalSetData(boolean isSingle, int ledNum) {
        if (isSingle) {
            return singleDatas[ledNum];
        }
        else {
            return colorDatas[ledNum];
        }
    }

    /**
     * 점멸 패턴 설정
     * @param isSingle Color or Single LED
     * @param ledNum   led 번호
     * @param pos      점멸 패턴 번호
     */
    public void setPattern(boolean isSingle, int ledNum, int pos) {
        getNaturalSetData(isSingle,ledNum).pattern = pos;
    }

    public SetData getSetData(int ledNum) {
        if (ledNum == Define.SELECTED_LED1) {
            return singleDatas[0];
        }
        else if (ledNum == Define.SELECTED_LED2) {
            return singleDatas[1];
        }
        else if (ledNum == Define.SELECTED_LED3) {
            return singleDatas[2];
        }
        else if (ledNum == Define.SELECTED_LED4) {
            return singleDatas[3];
        }
        else if (ledNum == Define.SELECTED_LED5) {
            return singleDatas[4];
        }
        else if (ledNum == Define.SELECTED_LED6) {
            return singleDatas[5];
        }
        else if (ledNum == Define.SELECTED_LED7) {
            return singleDatas[6];
        }
        else if (ledNum == Define.SELECTED_LED8) {
            return singleDatas[7];
        }
        else if (ledNum == Define.SELECTED_LED9) {
            return singleDatas[8];
        }
        else if (ledNum == Define.SELECTED_COLOR_LED1) {
            return colorDatas[0];
        }
        else if (ledNum == Define.SELECTED_COLOR_LED2) {
            return colorDatas[1];
        }
        else {
            return colorDatas[2];
        }
    }
    public int getPattern(int ledNum) {
        return getSetData(ledNum).pattern;
    }
    public void setPattern(int ledNum, int pattern) {
        getSetData(ledNum).pattern = pattern;
    }

    // 프래그먼트 정보 획득
    public LedEffectActivity.FragmentType getFragmentType(int ledNum) {
        LedEffectActivity.FragmentType retType = LedEffectActivity.FragmentType.SINGLE_ALWAYS_ON;
        switch (ledNum) {
            case Define.SELECTED_COLOR_LED1:
                if (colorDatas[0].pattern == 0) {
                    retType = LedEffectActivity.FragmentType.COLOR_ALWAYS_ON;
                } else {
                    retType = LedEffectActivity.FragmentType.SINGLE;
                }
                break;
            case Define.SELECTED_COLOR_LED2:
                if (colorDatas[1].pattern == 0) {
                    retType = LedEffectActivity.FragmentType.COLOR_ALWAYS_ON;
                } else {
                    retType = LedEffectActivity.FragmentType.SINGLE;
                }
                break;
            case Define.SELECTED_COLOR_LED3:
                if (colorDatas[2].pattern == 0) {
                    retType = LedEffectActivity.FragmentType.COLOR_ALWAYS_ON;
                } else {
                    retType = LedEffectActivity.FragmentType.SINGLE;
                }
                break;
            case Define.SELECTED_LED1:
                if (singleDatas[0].pattern == 0) {
                    retType = LedEffectActivity.FragmentType.SINGLE_ALWAYS_ON;
                } else {
                    retType = LedEffectActivity.FragmentType.SINGLE;
                }
                break;
            case Define.SELECTED_LED2:
                if (singleDatas[1].pattern == 0) {
                    retType = LedEffectActivity.FragmentType.SINGLE_ALWAYS_ON;
                } else {
                    retType = LedEffectActivity.FragmentType.SINGLE;
                }
                break;
            case Define.SELECTED_LED3:
                if (singleDatas[2].pattern == 0) {
                    retType = LedEffectActivity.FragmentType.SINGLE_ALWAYS_ON;
                } else {
                    retType = LedEffectActivity.FragmentType.SINGLE;
                }
                break;
            case Define.SELECTED_LED4:
                if (singleDatas[3].pattern == 0) {
                    retType = LedEffectActivity.FragmentType.SINGLE_ALWAYS_ON;
                } else {
                    retType = LedEffectActivity.FragmentType.SINGLE;
                }
                break;
            case Define.SELECTED_LED5:
                if (singleDatas[4].pattern == 0) {
                    retType = LedEffectActivity.FragmentType.SINGLE_ALWAYS_ON;
                } else {
                    retType = LedEffectActivity.FragmentType.SINGLE;
                }
                break;
            case Define.SELECTED_LED6:
                if (singleDatas[5].pattern == 0) {
                    retType = LedEffectActivity.FragmentType.SINGLE_ALWAYS_ON;
                } else {
                    retType = LedEffectActivity.FragmentType.SINGLE;
                }
                break;
            case Define.SELECTED_LED7:
                if (singleDatas[6].pattern == 0) {
                    retType = LedEffectActivity.FragmentType.SINGLE_ALWAYS_ON;
                } else {
                    retType = LedEffectActivity.FragmentType.SINGLE;
                }
                break;
            case Define.SELECTED_LED8:
                if (singleDatas[7].pattern == 0) {
                    retType = LedEffectActivity.FragmentType.SINGLE_ALWAYS_ON;
                } else {
                    retType = LedEffectActivity.FragmentType.SINGLE;
                }
                break;
            case Define.SELECTED_LED9:
                if (singleDatas[8].pattern == 0) {
                    retType = LedEffectActivity.FragmentType.SINGLE_ALWAYS_ON;
                } else {
                    retType = LedEffectActivity.FragmentType.SINGLE;
                }
                break;
        }

        return retType;
    }

    public int convertLedNumber(int ledNum) {
        int retLedNum = 0;
        if ((ledNum & Define.SELECTED_COLOR_LED1) == Define.SELECTED_COLOR_LED1) {
            retLedNum = 0;
        } else if ((ledNum & Define.SELECTED_COLOR_LED2) == Define.SELECTED_COLOR_LED2) {
            retLedNum = 1;
        } else if ((ledNum & Define.SELECTED_COLOR_LED3) == Define.SELECTED_COLOR_LED3) {
            retLedNum = 2;
        } else if ((ledNum & Define.SELECTED_LED1) == Define.SELECTED_LED1) {
            retLedNum = 0;
        } else if ((ledNum & Define.SELECTED_LED2) == Define.SELECTED_LED2) {
            retLedNum = 1;
        } else if ((ledNum & Define.SELECTED_LED3) == Define.SELECTED_LED3) {
            retLedNum = 2;
        } else if ((ledNum & Define.SELECTED_LED4) == Define.SELECTED_LED4) {
            retLedNum = 3;
        } else if ((ledNum & Define.SELECTED_LED5) == Define.SELECTED_LED5) {
            retLedNum = 4;
        } else if ((ledNum & Define.SELECTED_LED6) == Define.SELECTED_LED6) {
            retLedNum = 5;
        } else if ((ledNum & Define.SELECTED_LED7) == Define.SELECTED_LED7) {
            retLedNum = 6;
        } else if ((ledNum & Define.SELECTED_LED8) == Define.SELECTED_LED8) {
            retLedNum = 7;
        } else if ((ledNum & Define.SELECTED_LED9) == Define.SELECTED_LED9) {
            retLedNum = 8;
        }
        return 0;
    }

    public class SetData {
        public int pattern = 0;
        // 항상 켜기에서 사용됨
        public int bright = 0;
        public int rgbData = 0;

        public int startDelay = 0;
        public int gapDelay = 0;
        public int endDelay = 0;
        public int ratioBright = 0;
        public int ratioDelay = 0;

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
