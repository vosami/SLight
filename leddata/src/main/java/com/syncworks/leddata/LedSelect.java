package com.syncworks.leddata;


import java.io.Serializable;

import static com.syncworks.define.Define.*;
/**
 * Created by vosami on 2015-07-03.
 * Led 선택 클래스
 */
public class LedSelect implements Serializable{
    public enum SelectType {
        DEFAULT(0), SELECTED(1), COMPLETED(2), DISABLED(3);
        private int value;

        private SelectType(int value) {
            this.value = value;
        }
    };

    private SelectType[] rgb = new SelectType[NUMBER_OF_COLOR_LED];
    private SelectType[] led = new SelectType[NUMBER_OF_SINGLE_LED];




    public LedSelect() {
        init();
    }

    public void init() {
        for (int i=0;i<NUMBER_OF_COLOR_LED;i++) {
            rgb[i] = SelectType.DISABLED;
        }
        for (int i=0;i<NUMBER_OF_SINGLE_LED;i++) {
            led[i] = SelectType.DEFAULT;
        }
    }
    // RGB LED 의 선택 사항 설정
    public void setRgb(int ledNum, SelectType type) {
        rgb[ledNum] = type;

    }
    // RGB LED 의 선택 사항 설정
    public void setRgb(int ledNum, boolean type) {
        if (type) {
            rgb[ledNum] = SelectType.SELECTED;
        } else {
            rgb[ledNum] = SelectType.DEFAULT;
        }
    }
    // 단색 LED 의 선택 사항 설정
    public void setLed(int ledNum, SelectType type) {
        led[ledNum] = type;
    }

    public void setLed(int ledNum, boolean type) {
        if (type) {
            led[ledNum] = SelectType.SELECTED;
        } else {
            led[ledNum] = SelectType.DEFAULT;
        }
    }


    public SelectType getRgb(int ledNum) {
        return rgb[ledNum];
    }
    public SelectType getLed(int ledNum) {
        return led[ledNum];
    }

}
