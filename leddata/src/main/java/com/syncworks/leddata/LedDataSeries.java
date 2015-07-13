package com.syncworks.leddata;

import java.io.Serializable;

import static com.syncworks.define.Define.*;
/**
 * Created by Kim on 2015-07-03.
 * LED 데이터 시리즈
 */
public class LedDataSeries implements Serializable {
    public LedDataList[] ledExeDatas = new LedDataList[NUMBER_OF_SINGLE_LED];
    public LedOptions[] ledOptions = new LedOptions[NUMBER_OF_SINGLE_LED];
    public LedSelect ledSelect;

    public LedDataSeries() {
        init();
    }

    private void init() {
        for (int i=0;i<NUMBER_OF_SINGLE_LED;i++) {
            ledExeDatas[i] = new LedDataList();
            ledOptions[i] = new LedOptions();
        }
        ledSelect = new LedSelect();
    }
}
