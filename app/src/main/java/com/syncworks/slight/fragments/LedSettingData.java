package com.syncworks.slight.fragments;

import android.graphics.Color;

import com.syncworks.define.Define;

/**
 * Created by vosami on 2015-03-25.
 */
public class LedSettingData {

    private int enabledLedGroup;
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
        enabledLedGroup = 7;
        for (int i=0;i< Define.NUMBER_OF_SINGLE_LED;i++) {
            singleDatas[i] = new SetData();
            singleDatas[i].pattern = 0;
            singleDatas[i].bright = 100;
        }
        for (int i=0;i<Define.NUMBER_OF_COLOR_LED;i++) {
            colorDatas[i] = new SetData();
            colorDatas[i].pattern = 0;
            colorDatas[i].rgbData = Color.rgb(100,100,100);
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
     * @param ledNumNatural   led 번호
     * @param pos      점멸 패턴 번호
     */
    public void setPattern(boolean isSingle, int ledNumNatural, int pos) {
        getNaturalSetData(isSingle,ledNumNatural).pattern = pos;
    }

    public int getPattern(boolean isSingle, int ledNumNatural) {
        return getNaturalSetData(isSingle,ledNumNatural).pattern;
    }
    public int getPattern(int ledNum) {
        return getSetData(ledNum).pattern;
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

    /*
    public void setPattern(int ledNum, int pattern) {
        getSetData(ledNum).pattern = pattern;
    }*/
    /*public int getBright(int ledNum) {
        return getSetData(ledNum).bright;
    }
    public void setBright(int ledNum, int bright) {
        getSetData(ledNum).bright = bright;
    };*/
    // 밝기 설정
    public void setBright(boolean isSingle, int ledNumNatural, int bright) {
        if (isSingle) {
            getNaturalSetData(isSingle, ledNumNatural).bright = bright;
        }
        else {
            getNaturalSetData(isSingle,ledNumNatural).rgbData = bright;
        }
    }
    // Effect 지연 비율 설정
    public void setEffectRatio(int ledNum, int ratio) {
        // Color LED Array 선택시
        if ((ledNum & 0x7000) != 0) {
            for (int i=0; i<Define.NUMBER_OF_COLOR_LED;i++) {
                if (((ledNum>>(12+i)) & 0x01) == 1) {
                    colorDatas[i].ratioDelay = ratio;
                }
            }
        }
        // Single LED Array 선택시
        else {
            for (int i=0; i<Define.NUMBER_OF_SINGLE_LED;i++) {
                if (((ledNum>>i)& 0x01) == 1) {
                    singleDatas[i].ratioDelay = ratio;
                }
            }
        }
    }
    // 시작 지연 시간 설정
    public void setStartDelay(int ledNum, int delay) {
        // Color LED Array 선택시
        if ((ledNum & 0x7000) != 0) {
            for (int i=0; i<Define.NUMBER_OF_COLOR_LED;i++) {
                if (((ledNum>>(12+i)) & 0x01) == 1) {
                    colorDatas[i].startDelay = delay;
                }
            }
        }
        // Single LED Array 선택시
        else {
            for (int i=0; i<Define.NUMBER_OF_SINGLE_LED;i++) {
                if (((ledNum>>i)& 0x01) == 1) {
                    singleDatas[i].startDelay = delay;
                }
            }
        }
    }
    // 종료 지연 시간 설정
    public void setEndDelay(int ledNum, int delay) {
        // Color LED Array 선택시
        if ((ledNum & 0x7000) != 0) {
            for (int i=0; i<Define.NUMBER_OF_COLOR_LED;i++) {
                if (((ledNum>>(12+i)) & 0x01) == 1) {
                    colorDatas[i].endDelay = delay;
                }
            }
        }
        // Single LED Array 선택시
        else {
            for (int i=0; i<Define.NUMBER_OF_SINGLE_LED;i++) {
                if (((ledNum>>i)& 0x01) == 1) {
                    singleDatas[i].endDelay = delay;
                }
            }
        }
    }

    // LED 배열 사이 지연 시간 설정
    public void setArrayGapDelay(int ledNum, int gapDelay, int endDelay) {
        // Color LED Array 선택시
        if ((ledNum & 0x7000) != 0) {
            for (int i=0; i<Define.NUMBER_OF_COLOR_LED;i++) {
                if (((ledNum>>(12+i)) & 0x01) == 1) {
                    colorDatas[i].gapDelay = gapDelay;
                    colorDatas[i].arrayEndDelay = endDelay;
                    // TODO 시작, 종료 지연 시간 설정
                }
            }
        }
        // Single LED Array 선택시
        else {
            for (int i=0; i<Define.NUMBER_OF_SINGLE_LED;i++) {
                if (((ledNum>>i)& 0x01) == 1) {
                    singleDatas[i].gapDelay = gapDelay;
                    singleDatas[i].arrayEndDelay = endDelay;
                    // TODO 시작, 종료 지연 시간 설정
                }
            }
        }
    }

    public void setRatioBright(int ledNum, int ratioBright) {
        // Color LED Array 선택시
        if ((ledNum & 0x7000) != 0) {
            for (int i=0; i<Define.NUMBER_OF_COLOR_LED;i++) {
                if (((ledNum>>(12+i)) & 0x01) == 1) {
                    colorDatas[i].ratioBright = ratioBright;
                }
            }
        }
        // Single LED Array 선택시
        else {
            for (int i=0; i<Define.NUMBER_OF_SINGLE_LED;i++) {
                if (((ledNum>>i)& 0x01) == 1) {
                    singleDatas[i].ratioBright = ratioBright;
                }
            }
        }
    }


    public class SetData {
        public int pattern = 0;
        // 항상 켜기에서 사용됨
        public int bright = 0;
        public int rgbData = 0;

        public int startDelay = 0;
        public int gapDelay = 0;
        public int arrayEndDelay = 0;
        public int endDelay = 0;
        public int ratioBright = 0;
        public int ratioDelay = 0;

    }
}
