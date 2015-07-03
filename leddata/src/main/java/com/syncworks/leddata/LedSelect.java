package com.syncworks.leddata;


import static com.syncworks.define.Define.*;
/**
 * Created by vosami on 2015-07-03.
 * Led 선택 클래스
 */
public class LedSelect {
    public enum SelectType {
        DEFAULT(0), SELECTED(1), COMPLETED(2), DISABLED(3);
        private int value;

        private SelectType(int value) {
            this.value = value;
        }
    };

    private SelectType[] rgb = new SelectType[NUMBER_OF_COLOR_LED];
    private SelectType[] led = new SelectType[NUMBER_OF_SINGLE_LED];

    private int[] ratioBright = new int[NUMBER_OF_SINGLE_LED];
    private int[] ratioDuration = new int[NUMBER_OF_SINGLE_LED];
    private int[] delayStart = new int[NUMBER_OF_SINGLE_LED];
    private int[] delayMiddle = new int[NUMBER_OF_SINGLE_LED];
    private int[] delayEnd = new int[NUMBER_OF_SINGLE_LED];
    private boolean[] isApplyDelayStart = new boolean[NUMBER_OF_SINGLE_LED];
    private boolean[] isApplyDelayMiddle = new boolean[NUMBER_OF_SINGLE_LED];
    private boolean[] isApplyDelayEnd = new boolean[NUMBER_OF_SINGLE_LED];
    private int[] idOfPattern = new int[NUMBER_OF_SINGLE_LED];

    public LedSelect() {

    }
    // RGB LED 의 선택 사항 설정
    public void setRgb(int ledNum, SelectType type) {
        rgb[ledNum] = type;
    }
    // 단색 LED 의 선택 사항 설정
    public void setLed(int ledNum, SelectType type) {
        led[ledNum] = type;
    }

}
