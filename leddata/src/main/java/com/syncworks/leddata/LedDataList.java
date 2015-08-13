package com.syncworks.leddata;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static com.syncworks.define.Define.*;

/**
 * Created by vosami on 2015-07-02.
 * LED 데이타 리스트
 */
public class LedDataList implements List<LedData>, Serializable {

    // 스크립트 데이터
    private List<LedData> ledDatas;


    // 기본 생성자 - LED 번호가 0으로 설정
    public LedDataList() {
        ledDatas = new ArrayList<>();
        init();
    }
    // LED 번호와 데이터 리스트 생성자
    public LedDataList(List<LedData> dataList) {
        ledDatas = dataList;
        init();
    }
    // 기본 변수 초기화
    public void init() {
        ledDatas.clear();
        add(new LedData(OP_START,0));
        add(new LedData(191,0));
        add(new LedData(OP_END, 0));
    }

    // RGB 효과 데이터 설정 함수
    public void setRgbEffect(int effect, int rgbNum, int patternTime, int randomTime, int startTime) {
        int rgbPer = rgbNum % 3;
        switch (effect) {
            case 0:
                ledDatas.clear();
                add(new LedData(0, 0));
                add(new LedData(OP_NOP,startTime));
                add(new LedData(OP_START,0));
                add(new LedData(191, 0));
                add(new LedData(OP_END, 0));
                break;
            case 1:
                ledDatas.clear();
                add(new LedData(0, 0));
                add(new LedData(OP_NOP, startTime));
                add(new LedData(OP_START, 0));
                switch (patternTime) {
                    case 0:
                        add(new LedData(191,30));
                        add(new LedData(0,18));
                        break;
                    case 1:
                        add(new LedData(191,30));
                        add(new LedData(0,48));
                        break;
                    case 2:
                        add(new LedData(191,50));
                        add(new LedData(0,48));
                        break;
                    case 3:
                        add(new LedData(191,50));
                        add(new LedData(0,68));
                        break;
                    case 4:
                        add(new LedData(191,50));
                        add(new LedData(0,88));
                        break;
                }
                switch (randomTime) {
                    case 0:
                        break;
                    case 1:
                        add(new LedData(OP_RANDOM_DELAY,2));
                        break;
                    case 2:
                        add(new LedData(OP_RANDOM_DELAY,4));
                        break;
                    case 3:
                        add(new LedData(OP_RANDOM_DELAY,5));
                        break;
                    case 4:
                        add(new LedData(OP_RANDOM_DELAY,6));
                        break;
                }
                add(new LedData(OP_END,0));
                break;
            case 2:
                ledDatas.clear();
                add(new LedData(0, 0));
                add(new LedData(OP_NOP, startTime));
                add(new LedData(OP_START, 0));
                add(new LedData(191,0));
                add(new LedData(0,40*(patternTime + 1)));
                switch (randomTime) {
                    case 0:
                        break;
                    case 1:
                        add(new LedData(OP_RANDOM_DELAY,2));
                        break;
                    case 2:
                        add(new LedData(OP_RANDOM_DELAY,4));
                        break;
                    case 3:
                        add(new LedData(OP_RANDOM_DELAY,5));
                        break;
                    case 4:
                        add(new LedData(OP_RANDOM_DELAY,6));
                        break;
                }
                add(new LedData(OP_END,0));
                break;
            case 3:
                int patternTime3 = 2*(patternTime + 1);
                ledDatas.clear();
                add(new LedData(0, 0));
                add(new LedData(OP_NOP, startTime));
                add(new LedData(OP_START, 0));
                add(new LedData(0,patternTime3));
                add(new LedData(20,patternTime3));
                add(new LedData(35,patternTime3));
                add(new LedData(50,patternTime3));
                add(new LedData(75,patternTime3));
                add(new LedData(100,patternTime3));
                add(new LedData(130,patternTime3));
                add(new LedData(160,patternTime3));
                add(new LedData(191,patternTime3));
                add(new LedData(160,patternTime3));
                add(new LedData(130,patternTime3));
                add(new LedData(100,patternTime3));
                add(new LedData(75,patternTime3));
                add(new LedData(50,patternTime3));
                add(new LedData(35,patternTime3));
                add(new LedData(20,patternTime3));
                add(new LedData(0,patternTime3));
                switch (randomTime) {
                    case 0:
                        break;
                    case 1:
                        add(new LedData(OP_RANDOM_DELAY,2));
                        break;
                    case 2:
                        add(new LedData(OP_RANDOM_DELAY,4));
                        break;
                    case 3:
                        add(new LedData(OP_RANDOM_DELAY,5));
                        break;
                    case 4:
                        add(new LedData(OP_RANDOM_DELAY,6));
                        break;
                }
                add(new LedData(OP_END,0));
                break;
            case 4:
                ledDatas.clear();
                add(new LedData(0, 0));
                add(new LedData(OP_NOP, startTime));
                add(new LedData(OP_START, 0));
                add(new LedData(OP_RANDOM_VAL,252));
                switch (patternTime) {
                    case 0:
                        break;
                    case 1:
                        add(new LedData(OP_NOP, patternTime));
                        break;
                    case 2:
                        add(new LedData(OP_NOP, patternTime));
                        break;
                    case 3:
                        add(new LedData(OP_NOP, patternTime));
                        break;
                    case 4:
                        add(new LedData(OP_NOP, patternTime));
                        break;
                }
                switch (randomTime) {
                    case 0:
                        break;
                    case 1:
                        add(new LedData(OP_RANDOM_DELAY,2));
                        break;
                    case 2:
                        add(new LedData(OP_RANDOM_DELAY,4));
                        break;
                    case 3:
                        add(new LedData(OP_RANDOM_DELAY,5));
                        break;
                    case 4:
                        add(new LedData(OP_RANDOM_DELAY,6));
                        break;
                }
                add(new LedData(OP_END,0));
                break;
            case 5:
                int patternTime5 = 30*(patternTime + 1);
                int patternNop5 = 20*(patternTime + 1);
                if (rgbPer == 0) {
                    ledDatas.clear();
                    add(new LedData(OP_HEAD_PATTERN, rgbNum, DEFINED_PATTERN, effect));
                    add(new LedData(OP_HEAD_OPTION, randomTime, patternTime));
                    add(new LedData(OP_HEAD_START_DELAY, startTime));
                    add(new LedData(OP_START,0));
                    add(new LedData(OP_TRANS_BRIGHT_START, 191));   // 빨강
                    add(new LedData(OP_TRANS_BRIGHT_STOP,191));
                    add(new LedData(OP_TRANS_BRIGHT_COUNT,patternTime5));
                    add(new LedData(OP_NOP,patternNop5));
                    add(new LedData(OP_TRANS_START,0));
                    add(new LedData(OP_TRANS_BRIGHT_START,191));    // 주황
                    add(new LedData(OP_TRANS_BRIGHT_STOP,191));
                    add(new LedData(OP_TRANS_BRIGHT_COUNT,patternTime5));
                    add(new LedData(OP_NOP,patternNop5));
                    add(new LedData(OP_TRANS_START,0));
                    add(new LedData(OP_TRANS_BRIGHT_START,191));    // 노랑
                    add(new LedData(OP_TRANS_BRIGHT_STOP,0));
                    add(new LedData(OP_TRANS_BRIGHT_COUNT,patternTime5));
                    add(new LedData(OP_NOP,patternNop5));
                    add(new LedData(OP_TRANS_START,0));
                    add(new LedData(OP_TRANS_BRIGHT_START,0));     // 초록
                    add(new LedData(OP_TRANS_BRIGHT_STOP,0));
                    add(new LedData(OP_TRANS_BRIGHT_COUNT,patternTime5));
                    add(new LedData(OP_NOP,patternNop5));
                    add(new LedData(OP_TRANS_START,0));
                    add(new LedData(OP_TRANS_BRIGHT_START,0));    // 파랑
                    add(new LedData(OP_TRANS_BRIGHT_STOP,56));
                    add(new LedData(OP_TRANS_BRIGHT_COUNT,patternTime5));
                    add(new LedData(OP_NOP,patternNop5));
                    add(new LedData(OP_TRANS_START,0));
                    add(new LedData(OP_TRANS_BRIGHT_START,56));     // 남색
                    add(new LedData(OP_TRANS_BRIGHT_STOP,96));
                    add(new LedData(OP_TRANS_BRIGHT_COUNT,patternTime5));
                    add(new LedData(OP_NOP,patternNop5));
                    add(new LedData(OP_TRANS_START,0));
                    add(new LedData(OP_TRANS_BRIGHT_START,96));     // 보라
                    add(new LedData(OP_TRANS_BRIGHT_STOP,191));
                    add(new LedData(OP_TRANS_BRIGHT_COUNT,patternTime5));
                    add(new LedData(OP_NOP,patternNop5));
                    add(new LedData(OP_TRANS_START,0));
                    switch (randomTime) {
                        case 0:
                            break;
                        case 1:
                            add(new LedData(OP_RANDOM_DELAY,2));
                            break;
                        case 2:
                            add(new LedData(OP_RANDOM_DELAY,4));
                            break;
                        case 3:
                            add(new LedData(OP_RANDOM_DELAY,5));
                            break;
                        case 4:
                            add(new LedData(OP_RANDOM_DELAY,6));
                            break;
                    }
                    add(new LedData(OP_END,0));
                } else if (rgbPer == 1){
                    ledDatas.clear();
                    add(new LedData(OP_HEAD_PATTERN, rgbNum, DEFINED_PATTERN, effect));
                    add(new LedData(OP_HEAD_OPTION, randomTime, patternTime));
                    add(new LedData(OP_HEAD_START_DELAY, startTime));
                    add(new LedData(OP_START,0));
                    add(new LedData(OP_TRANS_BRIGHT_START, 0));   // 빨강
                    add(new LedData(OP_TRANS_BRIGHT_STOP,105));
                    add(new LedData(OP_TRANS_BRIGHT_COUNT,patternTime5));
                    add(new LedData(OP_NOP,patternNop5));
                    add(new LedData(OP_TRANS_START,0));
                    add(new LedData(OP_TRANS_BRIGHT_START,105));    // 주황
                    add(new LedData(OP_TRANS_BRIGHT_STOP,191));
                    add(new LedData(OP_TRANS_BRIGHT_COUNT,patternTime5));
                    add(new LedData(OP_NOP,patternNop5));
                    add(new LedData(OP_TRANS_START,0));
                    add(new LedData(OP_TRANS_BRIGHT_START,191));    // 노랑
                    add(new LedData(OP_TRANS_BRIGHT_STOP,96));
                    add(new LedData(OP_TRANS_BRIGHT_COUNT,patternTime5));
                    add(new LedData(OP_NOP,patternNop5));
                    add(new LedData(OP_TRANS_START,0));
                    add(new LedData(OP_TRANS_BRIGHT_START,96));     // 초록
                    add(new LedData(OP_TRANS_BRIGHT_STOP,0));
                    add(new LedData(OP_TRANS_BRIGHT_COUNT,patternTime5));
                    add(new LedData(OP_NOP,patternNop5));
                    add(new LedData(OP_TRANS_START,0));
                    add(new LedData(OP_TRANS_BRIGHT_START,0));    // 파랑
                    add(new LedData(OP_TRANS_BRIGHT_STOP,0));
                    add(new LedData(OP_TRANS_BRIGHT_COUNT,patternTime5));
                    add(new LedData(OP_NOP,patternNop5));
                    add(new LedData(OP_TRANS_START,0));
                    add(new LedData(OP_TRANS_BRIGHT_START,0));     // 남색
                    add(new LedData(OP_TRANS_BRIGHT_STOP,0));
                    add(new LedData(OP_TRANS_BRIGHT_COUNT,patternTime5));
                    add(new LedData(OP_NOP,patternNop5));
                    add(new LedData(OP_TRANS_START,0));
                    add(new LedData(OP_TRANS_BRIGHT_START,0));     // 보라
                    add(new LedData(OP_TRANS_BRIGHT_STOP,0));
                    add(new LedData(OP_TRANS_BRIGHT_COUNT,patternTime5));
                    add(new LedData(OP_NOP,patternNop5));
                    add(new LedData(OP_TRANS_START,0));
                    switch (randomTime) {
                        case 0:
                            break;
                        case 1:
                            add(new LedData(OP_RANDOM_DELAY,2));
                            break;
                        case 2:
                            add(new LedData(OP_RANDOM_DELAY,4));
                            break;
                        case 3:
                            add(new LedData(OP_RANDOM_DELAY,5));
                            break;
                        case 4:
                            add(new LedData(OP_RANDOM_DELAY,6));
                            break;
                    }
                    add(new LedData(OP_END,0));
                } else {
                    ledDatas.clear();
                    add(new LedData(OP_HEAD_PATTERN, rgbNum, DEFINED_PATTERN, effect));
                    add(new LedData(OP_HEAD_OPTION, randomTime, patternTime));
                    add(new LedData(OP_HEAD_START_DELAY, startTime));
                    add(new LedData(OP_START,0));
                    add(new LedData(OP_TRANS_BRIGHT_START, 0));   // 빨강
                    add(new LedData(OP_TRANS_BRIGHT_STOP,0));
                    add(new LedData(OP_TRANS_BRIGHT_COUNT,patternTime5));
                    add(new LedData(OP_NOP,patternNop5));
                    add(new LedData(OP_TRANS_START,0));
                    add(new LedData(OP_TRANS_BRIGHT_START,0));    // 주황
                    add(new LedData(OP_TRANS_BRIGHT_STOP,0));
                    add(new LedData(OP_TRANS_BRIGHT_COUNT,patternTime5));
                    add(new LedData(OP_NOP,patternNop5));
                    add(new LedData(OP_TRANS_START,0));
                    add(new LedData(OP_TRANS_BRIGHT_START,0));    // 노랑
                    add(new LedData(OP_TRANS_BRIGHT_STOP,0));
                    add(new LedData(OP_TRANS_BRIGHT_COUNT,patternTime5));
                    add(new LedData(OP_NOP,patternNop5));
                    add(new LedData(OP_TRANS_START,0));
                    add(new LedData(OP_TRANS_BRIGHT_START,0));     // 초록
                    add(new LedData(OP_TRANS_BRIGHT_STOP,191));
                    add(new LedData(OP_TRANS_BRIGHT_COUNT,patternTime5));
                    add(new LedData(OP_NOP,patternNop5));
                    add(new LedData(OP_TRANS_START,0));
                    add(new LedData(OP_TRANS_BRIGHT_START,191));    // 파랑
                    add(new LedData(OP_TRANS_BRIGHT_STOP,97));
                    add(new LedData(OP_TRANS_BRIGHT_COUNT,patternTime5));
                    add(new LedData(OP_NOP,patternNop5));
                    add(new LedData(OP_TRANS_START,0));
                    add(new LedData(OP_TRANS_BRIGHT_START,97));     // 남색
                    add(new LedData(OP_TRANS_BRIGHT_STOP,96));
                    add(new LedData(OP_TRANS_BRIGHT_COUNT,patternTime5));
                    add(new LedData(OP_NOP,patternNop5));
                    add(new LedData(OP_TRANS_START,0));
                    add(new LedData(OP_TRANS_BRIGHT_START,96));     // 보라
                    add(new LedData(OP_TRANS_BRIGHT_STOP,0));
                    add(new LedData(OP_TRANS_BRIGHT_COUNT,patternTime5));
                    add(new LedData(OP_NOP,patternNop5));
                    add(new LedData(OP_TRANS_START,0));
                    switch (randomTime) {
                        case 0:
                            break;
                        case 1:
                            add(new LedData(OP_RANDOM_DELAY,2));
                            break;
                        case 2:
                            add(new LedData(OP_RANDOM_DELAY,4));
                            break;
                        case 3:
                            add(new LedData(OP_RANDOM_DELAY,5));
                            break;
                        case 4:
                            add(new LedData(OP_RANDOM_DELAY,6));
                            break;
                    }
                    add(new LedData(OP_END,0));
                }
                break;
            case 6:
                int patternTime6 = (5-patternTime) * 16;
                int patternGap6 = 8*(patternTime + 1);
                if (rgbPer == 0) {
                    ledDatas.clear();
                    add(new LedData(OP_HEAD_PATTERN, rgbNum, DEFINED_PATTERN, effect));
                    add(new LedData(OP_HEAD_OPTION, randomTime, patternTime));
                    add(new LedData(OP_HEAD_START_DELAY, startTime));
                    add(new LedData(OP_START,0));
                    add(new LedData(OP_SIN_SHIFT,0x9F));
                    add(new LedData(OP_SIN_COUNT,0xFF));
                    add(new LedData(OP_SIN_START,patternTime6));
                    add(new LedData(OP_END,0));
                } else if (rgbPer == 1) {
                    ledDatas.clear();
                    add(new LedData(OP_HEAD_PATTERN, rgbNum, DEFINED_PATTERN, effect));
                    add(new LedData(OP_HEAD_OPTION, randomTime, patternTime));
                    add(new LedData(OP_HEAD_START_DELAY, startTime));
                    add(new LedData(OP_START,0));
                    add(new LedData(OP_SIN_SHIFT,0x9F));
                    add(new LedData(OP_SIN_COUNT,0xFF));
                    add(new LedData(OP_SIN_START,patternTime6 + patternGap6));
                    add(new LedData(OP_END,0));
                } else {
                    ledDatas.clear();
                    add(new LedData(OP_HEAD_PATTERN, rgbNum, DEFINED_PATTERN, effect));
                    add(new LedData(OP_HEAD_OPTION, randomTime, patternTime));
                    add(new LedData(OP_HEAD_START_DELAY, startTime));
                    add(new LedData(OP_START,0));
                    add(new LedData(OP_SIN_SHIFT,0x9F));
                    add(new LedData(OP_SIN_COUNT,0xFF));
                    add(new LedData(OP_SIN_START,patternTime6 + patternGap6*2));
                    add(new LedData(OP_END,0));
                }
                break;
        }
    }

    public void setEffect(int effect, int patternTime, int randomTime, int startTime) {
        switch (effect) {
            case 0:
                ledDatas.clear();
                add(new LedData(0, 0));
                add(new LedData(OP_NOP,startTime));
                add(new LedData(OP_START,0));
                add(new LedData(191, 0));
                add(new LedData(OP_END, 0));
                break;
            case 1:
                ledDatas.clear();
                add(new LedData(0, 0));
                add(new LedData(OP_NOP, startTime));
                add(new LedData(OP_START, 0));
                switch (patternTime) {
                    case 0:
                        add(new LedData(191,30));
                        add(new LedData(0,18));
                        break;
                    case 1:
                        add(new LedData(191,30));
                        add(new LedData(0,48));
                        break;
                    case 2:
                        add(new LedData(191,50));
                        add(new LedData(0,48));
                        break;
                    case 3:
                        add(new LedData(191,50));
                        add(new LedData(0,68));
                        break;
                    case 4:
                        add(new LedData(191,50));
                        add(new LedData(0,88));
                        break;
                }
                switch (randomTime) {
                    case 0:
                        break;
                    case 1:
                        add(new LedData(OP_RANDOM_DELAY,2));
                        break;
                    case 2:
                        add(new LedData(OP_RANDOM_DELAY,4));
                        break;
                    case 3:
                        add(new LedData(OP_RANDOM_DELAY,5));
                        break;
                    case 4:
                        add(new LedData(OP_RANDOM_DELAY,6));
                        break;
                }
                add(new LedData(OP_END,0));
                break;
            case 2:
                ledDatas.clear();
                add(new LedData(0, 0));
                add(new LedData(OP_NOP, startTime));
                add(new LedData(OP_START, 0));
                add(new LedData(191,0));
                add(new LedData(0,40*(patternTime + 1)));
                switch (randomTime) {
                    case 0:
                        break;
                    case 1:
                        add(new LedData(OP_RANDOM_DELAY,2));
                        break;
                    case 2:
                        add(new LedData(OP_RANDOM_DELAY,4));
                        break;
                    case 3:
                        add(new LedData(OP_RANDOM_DELAY,5));
                        break;
                    case 4:
                        add(new LedData(OP_RANDOM_DELAY,6));
                        break;
                }
                add(new LedData(OP_END,0));
                break;
            case 3:
                int patternTime3 = 2*(patternTime + 1);
                ledDatas.clear();
                add(new LedData(0, 0));
                add(new LedData(OP_NOP, startTime));
                add(new LedData(OP_START, 0));
                add(new LedData(0,patternTime3));
                add(new LedData(20,patternTime3));
                add(new LedData(35,patternTime3));
                add(new LedData(50,patternTime3));
                add(new LedData(75,patternTime3));
                add(new LedData(100,patternTime3));
                add(new LedData(130,patternTime3));
                add(new LedData(160,patternTime3));
                add(new LedData(191,patternTime3));
                add(new LedData(160,patternTime3));
                add(new LedData(130,patternTime3));
                add(new LedData(100,patternTime3));
                add(new LedData(75,patternTime3));
                add(new LedData(50,patternTime3));
                add(new LedData(35,patternTime3));
                add(new LedData(20,patternTime3));
                add(new LedData(0,patternTime3));
                switch (randomTime) {
                    case 0:
                        break;
                    case 1:
                        add(new LedData(OP_RANDOM_DELAY,2));
                        break;
                    case 2:
                        add(new LedData(OP_RANDOM_DELAY,4));
                        break;
                    case 3:
                        add(new LedData(OP_RANDOM_DELAY,5));
                        break;
                    case 4:
                        add(new LedData(OP_RANDOM_DELAY,6));
                        break;
                }
                add(new LedData(OP_END,0));
                break;
            case 4:
                ledDatas.clear();
                add(new LedData(0, 0));
                add(new LedData(OP_NOP, startTime));
                add(new LedData(OP_START, 0));
                add(new LedData(OP_RANDOM_VAL,252));
                switch (patternTime) {
                    case 0:
                        break;
                    case 1:
                        add(new LedData(OP_NOP, patternTime));
                        break;
                    case 2:
                        add(new LedData(OP_NOP, patternTime));
                        break;
                    case 3:
                        add(new LedData(OP_NOP, patternTime));
                        break;
                    case 4:
                        add(new LedData(OP_NOP, patternTime));
                        break;
                }
                switch (randomTime) {
                    case 0:
                        break;
                    case 1:
                        add(new LedData(OP_RANDOM_DELAY,2));
                        break;
                    case 2:
                        add(new LedData(OP_RANDOM_DELAY,4));
                        break;
                    case 3:
                        add(new LedData(OP_RANDOM_DELAY,5));
                        break;
                    case 4:
                        add(new LedData(OP_RANDOM_DELAY,6));
                        break;
                }
                add(new LedData(OP_END,0));
                break;

        }
    }

    @Override
    public void add(int i, LedData ledData) {
        if (ledDatas.size() <= DATA_ARRAY_MAX) {
            this.ledDatas.add(i,ledData);
        }
    }

    @Override
    public boolean add(LedData ledData) {
        if (ledDatas.size() <= DATA_ARRAY_MAX) {
            this.ledDatas.add(ledData);
            return true;
        }
        return false;
    }

    @Override
    public boolean addAll(int i, Collection<? extends LedData> collection) {
        return ledDatas.addAll(i,collection);
    }

    @Override
    public boolean addAll(Collection<? extends LedData> collection) {
        return ledDatas.addAll(collection);
    }

    @Override
    public void clear() {
        ledDatas.clear();
        init();
    }

    @Override
    public boolean contains(Object o) {
        int val = (int) o;
        int compareVal;
        if (val > OP_CODE_MIN) {
            for (int i = 0; i < ledDatas.size(); i++) {
                compareVal = ledDatas.get(i).getVal();
                if (compareVal == val) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return ledDatas.containsAll(collection);
    }

    @Override
    public LedData get(int i) {
        return ledDatas.get(i);
    }

    @Override
    public int indexOf(Object o) {
        int val = (int) o;
        int compareVal;
        if (val>OP_CODE_MIN) {
            for (int i=0;i < ledDatas.size();i++) {
                compareVal = ledDatas.get(i).getVal();
                if (compareVal == val) {
                    return i;
                }
            }
        }
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return ledDatas.isEmpty();
    }

    @NonNull
    @Override
    public Iterator<LedData> iterator() {
        return ledDatas.iterator();
    }

    @Override
    public int lastIndexOf(Object o) {
        return ledDatas.lastIndexOf(o);
    }

    @NonNull
    @Override
    public ListIterator<LedData> listIterator() {
        return ledDatas.listIterator();
    }

    @NonNull
    @Override
    public ListIterator<LedData> listIterator(int i) {
        return ledDatas.listIterator(i);
    }

    @Override
    public LedData remove(int i) {
        return ledDatas.remove(i);
    }

    @Override
    public boolean remove(Object o) {
        return ledDatas.remove(o);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return ledDatas.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return ledDatas.retainAll(collection);
    }

    @Override
    public LedData set(int i, LedData ledData) {
        return ledDatas.set(i, ledData);
    }

    @Override
    public int size() {
        return ledDatas.size();
    }

    @NonNull
    @Override
    public List<LedData> subList(int i, int i1) {
        return ledDatas.subList(i, i1);
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return ledDatas.toArray();
    }

    @NonNull
    @Override
    public <T> T[] toArray(T[] ts) {
        return ledDatas.toArray(ts);
    }

    public byte[] toByteArray() {
        int size = ledDatas.size();
        int byteLength = size*2;
        LedData subData;
        byte[] byteArray = new byte[byteLength];
        for (int i=0;i<size;i++) {
            subData = ledDatas.get(i);
            byteArray[2*i] = (byte)subData.getVal();
            byteArray[2*i +1] = (byte) subData.getDuration();
        }
        return byteArray;
    }

    public byte[] toByteArray(LedOptions lo) {
        int size = ledDatas.size();
        int byteLength = size*2;
        LedData subData;
        int temp1, temp2;
        byte[] byteArray = new byte[byteLength];
        for (int i=0;i<size;i++) {
            subData = ledDatas.get(i);
            int val = subData.getVal();
            int delay = subData.getDuration();
            if (val < OP_CODE_MIN) {
                val = val * lo.getRatioBright() / 100;
                delay = delay * lo.getRatioDuration() / 100;
                if (delay > 254) {
                    delay = 254;
                }
            } else {
                switch (val) {
                    case OP_START:
                        delay = lo.getDelayStart();
                        break;
                    case OP_END:
                        delay = lo.getDelayEnd();
                        break;
                    case OP_RANDOM_VAL:
                    case OP_RANDOM_DELAY:
                        temp1 = (delay >> 3) & 0x1F;
                        temp2 = delay & 0x07;
                        temp1 = temp1 * lo.getRatioBright() / 100;
                        delay = ((temp1 << 3) & 0xF8) | (temp2);
                        break;
                    case OP_NOP:
                    case OP_LONG_DELAY:
                        delay = delay * lo.getRatioDuration() / 100;
                        if (delay > 254) {
                            delay = 254;
                        }
                        break;
                    case OP_SOUND_VAL:
                        delay = 10 - lo.getRatioBright()/10;
                        break;
                }
            }
            byteArray[2*i] = (byte)val;
            byteArray[2*i +1] = (byte)delay;
        }
        return byteArray;
    }
}
