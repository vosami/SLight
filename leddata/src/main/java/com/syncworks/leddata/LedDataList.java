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

    private final static int EF_ALWAYS =    0; // 항상켜기
    private final static int EF_PULSE =     1; // 깜박이기
    private final static int EF_FLASH =     2; // 번쩍이기
    private final static int EF_DOUBLE =    3; // 두번번쩍
    private final static int EF_UP_DOWN =   4; // 오르내리
    private final static int EF_FIREFLY =   5; // 반딧불이 효과
    private final static int EF_TORCH =     6; // 횃불효과
    private final static int EF_SIN =       7; // 정현파
    private final static int EF_LASER =     8; // 레이저파
    private final static int EF_BREATHE =   9; // 숨쉬기파
    private final static int EF_FALSE =     10; // 고장효과
    private final static int EF_LIGHTENING = 11; // 번개효과
    //private final static int EF_WELDING =   12; // 용접효과
    private final static int EF_CANNON =    12; // 대포효과
    private final static int EF_EXPLOSION = 13; // 폭발효과
    private final static int EF_ENERGY =    14; // 에네르기파
    private final static int EF_RAINBOW =   15; // 숨쉬기파


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
    public void setRgbEffect(int effect, int rgbNum, int patternTime, int randomTime, int startTime, int patternOption) {
        int rgbPer = rgbNum % 3;
        switch (effect) {
            case EF_ALWAYS:
                ledDatas.clear();
                if (patternOption == 0) {
                    add(new LedData(0, 0));
                    add(new LedData(OP_NOP,startTime));
                    add(new LedData(OP_START,0));
                    add(new LedData(191, 0));
                    add(new LedData(OP_END, 0));
                } else if (patternOption == 1) {
                    add(new LedData(0, 0));
                    add(new LedData(OP_NOP,startTime));
                    add(new LedData(OP_TRANS_BRIGHT_START, 0));
                    add(new LedData(OP_TRANS_BRIGHT_STOP, OP_MACRO_MAX_VAL));
                    add(new LedData(OP_TRANS_BRIGHT_COUNT,250));
                    add(new LedData(OP_TRANS_START,patternTime));
                    add(new LedData(OP_START,0));
                    add(new LedData(191, 0));
                    add(new LedData(OP_END, 0));
                } else if (patternOption == 2) {
                    add(new LedData(191, 0));
                    add(new LedData(OP_NOP,startTime));
                    add(new LedData(OP_START,0));
                    add(new LedData(0, 0));
                    add(new LedData(OP_END, 0));
                } else if (patternOption == 3) {
                    add(new LedData(191, 0));
                    add(new LedData(OP_NOP,startTime));
                    add(new LedData(OP_TRANS_BRIGHT_START, OP_MACRO_MAX_VAL));
                    add(new LedData(OP_TRANS_BRIGHT_STOP, 0));
                    add(new LedData(OP_TRANS_BRIGHT_COUNT,250));
                    add(new LedData(OP_TRANS_START,patternTime));
                    add(new LedData(OP_START,0));
                    add(new LedData(0, 0));
                    add(new LedData(OP_END, 0));
                }
                break;
            case EF_PULSE:
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
            case EF_FLASH:
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
            case EF_UP_DOWN:
                int patternTime3 = 2*(patternTime + 1);
                ledDatas.clear();
                add(new LedData(0, 0));
                add(new LedData(OP_NOP, startTime));
                add(new LedData(OP_START, 0));
                add(new LedData(OP_TRANS_BRIGHT_START, 0));
                add(new LedData(OP_TRANS_BRIGHT_STOP, OP_MACRO_MAX_VAL));
                add(new LedData(OP_TRANS_BRIGHT_COUNT,patternTime3));
                add(new LedData(OP_TRANS_START,patternTime));
                add(new LedData(OP_TRANS_BRIGHT_START, OP_MACRO_MAX_VAL));
                add(new LedData(OP_TRANS_BRIGHT_STOP, 0));
                add(new LedData(OP_TRANS_BRIGHT_COUNT,patternTime3));
                add(new LedData(OP_TRANS_START,patternTime));
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
                add(new LedData(OP_END,0));
                break;
            case EF_FIREFLY:
                ledDatas.clear();
                add(new LedData(0, 0));
                add(new LedData(OP_NOP, startTime));
                add(new LedData(OP_START, 0));
                add(new LedData(OP_TRANS_BRIGHT_START, 0));
                add(new LedData(OP_TRANS_BRIGHT_STOP, OP_MACRO_MAX_VAL));
                add(new LedData(OP_TRANS_BRIGHT_COUNT,10* (patternTime + 1)));
                add(new LedData(OP_TRANS_START,patternTime));
                add(new LedData(OP_TRANS_BRIGHT_START, OP_MACRO_MAX_VAL));
                add(new LedData(OP_TRANS_BRIGHT_STOP, 0));
                add(new LedData(OP_TRANS_BRIGHT_COUNT,20* (patternTime + 1)));
                add(new LedData(OP_TRANS_START,patternTime));
                add(new LedData(OP_NOP,(patternTime+1)*20 + 50));
                switch (randomTime) {
                    case 0:
                        break;
                    case 1:
                        add(new LedData(OP_RANDOM_DELAY,18));
                        break;
                    case 2:
                        add(new LedData(OP_RANDOM_DELAY,20));
                        break;
                    case 3:
                        add(new LedData(OP_RANDOM_DELAY,21));
                        break;
                    case 4:
                        add(new LedData(OP_RANDOM_DELAY,27));
                        break;
                }
                add(new LedData(OP_END,0));
                break;
            case EF_TORCH:
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
            case EF_SIN:
                ledDatas.clear();
                add(new LedData(OP_HEAD_PATTERN, EF_SIN, DEFINED_PATTERN, effect));
                add(new LedData(OP_HEAD_OPTION, randomTime, patternTime));
                add(new LedData(OP_HEAD_START_DELAY, startTime));
                add(new LedData(OP_START,0));
                add(new LedData(OP_SIN_SHIFT,0x9F));
                add(new LedData(OP_SIN_COUNT,0xFF));
                add(new LedData(OP_SIN_START,10 - (patternTime*2)));
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
            case EF_LASER:
                int pattern6 = (patternTime+1) * 5;
                ledDatas.clear();
                add(new LedData(OP_HEAD_PATTERN, EF_LASER, DEFINED_PATTERN, effect));
                add(new LedData(OP_HEAD_OPTION, randomTime, patternTime));
                add(new LedData(OP_HEAD_START_DELAY, startTime));
                add(new LedData(OP_START,0));
                add(new LedData(OP_TRANS_BRIGHT_START, 0));
                add(new LedData(OP_TRANS_BRIGHT_STOP, 100));
                add(new LedData(OP_TRANS_BRIGHT_COUNT,pattern6));
                add(new LedData(OP_TRANS_START,0));
                add(new LedData(120,1));
                add(new LedData(191,1));
                add(new LedData(120,1));
                add(new LedData(0,0));
                add(new LedData(OP_NOP,30 + patternTime*10));
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
            case EF_BREATHE:
                int pattern7 = (patternTime + 1) * 30;
                ledDatas.clear();
                add(new LedData(OP_HEAD_PATTERN, EF_LASER, DEFINED_PATTERN, effect));
                add(new LedData(OP_HEAD_OPTION, randomTime, patternTime));
                add(new LedData(OP_HEAD_START_DELAY, startTime));
                add(new LedData(OP_START, 0));
                add(new LedData(OP_TRANS_BRIGHT_START, 100));
                add(new LedData(OP_TRANS_BRIGHT_STOP, 191));
                add(new LedData(OP_TRANS_BRIGHT_COUNT,pattern7));
                add(new LedData(OP_TRANS_START,0));
                add(new LedData(OP_TRANS_BRIGHT_START, 191));
                add(new LedData(OP_TRANS_BRIGHT_STOP, 40));
                add(new LedData(OP_TRANS_BRIGHT_COUNT,pattern7));
                add(new LedData(OP_TRANS_START,0));
                add(new LedData(OP_TRANS_BRIGHT_START, 40));
                add(new LedData(OP_TRANS_BRIGHT_STOP, 100));
                add(new LedData(OP_TRANS_BRIGHT_COUNT,pattern7/2));
                add(new LedData(OP_TRANS_START,0));
                add(new LedData(OP_NOP,30 + 10*patternTime));
                switch (randomTime) {
                    case 0:
                        break;
                    case 1:
                        add(new LedData(OP_RANDOM_DELAY,2));
                        break;
                    case 2:
                        add(new LedData(OP_RANDOM_DELAY,18));
                        break;
                    case 3:
                        add(new LedData(OP_RANDOM_DELAY,27));
                        break;
                    case 4:
                        add(new LedData(OP_RANDOM_DELAY,36));
                        break;
                }
                add(new LedData(OP_END,0));
                break;
            case EF_FALSE:
                int randomForCount = 40 * (patternTime + 1);
                ledDatas.clear();
                add(new LedData(OP_HEAD_PATTERN, EF_LASER, DEFINED_PATTERN, effect));
                add(new LedData(OP_HEAD_OPTION, randomTime, patternTime));
                add(new LedData(OP_HEAD_START_DELAY, startTime));
                add(new LedData(OP_START, 0));
                add(new LedData(OP_FOR_START,randomForCount));
                add(new LedData(OP_RANDOM_VAL,252));
                add(new LedData(OP_RANDOM_DELAY,2));
                add(new LedData(OP_FOR_END, 0));
                add(new LedData(191,randomForCount));
                add(new LedData(OP_LONG_DELAY,randomForCount));
                add(new LedData(OP_RANDOM_DELAY,62));
                add(new LedData(OP_RANDOM_DELAY,randomForCount));
                add(new LedData(OP_FOR_START,randomForCount));
                add(new LedData(OP_RANDOM_VAL,252));
                add(new LedData(OP_RANDOM_DELAY,2));
                add(new LedData(OP_FOR_END, 0));
                add(new LedData(OP_TRANS_BRIGHT_START, 191));
                add(new LedData(OP_TRANS_BRIGHT_STOP, 0));
                add(new LedData(OP_TRANS_BRIGHT_COUNT,30));
                add(new LedData(OP_TRANS_START,0));
                add(new LedData(0,randomForCount));
                add(new LedData(OP_RANDOM_DELAY,62));
                add(new LedData(OP_FOR_START,randomForCount));
                add(new LedData(OP_RANDOM_VAL,60));
                add(new LedData(OP_RANDOM_DELAY,2));
                add(new LedData(OP_FOR_END, 0));
                add(new LedData(OP_END,0));
                break;
            case EF_LIGHTENING:
                int lightTime = (((patternTime+1)*2)<<3)&0xF8 + 4;
                ledDatas.clear();
                //add(new LedData(OP_HEAD_PATTERN, EF_LIGHTENING, DEFINED_PATTERN, effect));
                //add(new LedData(OP_HEAD_OPTION, randomTime, patternTime));
                //add(new LedData(OP_HEAD_START_DELAY, startTime));
                add(new LedData(OP_START, 0));
                add(new LedData(OP_NOP,startTime));
                add(new LedData(0,10));
                add(new LedData(OP_RANDOM_DELAY,lightTime));
                add(new LedData(OP_RANDOM_DELAY,lightTime));
                add(new LedData(OP_RANDOM_DELAY,lightTime));
                add(new LedData(OP_RANDOM_VAR_C,2));
                add(new LedData(OP_IF,DATA_C,  DATA_LARGE, 2));
                add(new LedData(191,0));
                add(new LedData(0,10));
                add(new LedData(OP_RANDOM_DELAY,lightTime));
                add(new LedData(OP_RANDOM_DELAY,lightTime));
                add(new LedData(OP_END_IF,0));
                add(new LedData(OP_RANDOM_DELAY,lightTime));
                add(new LedData(OP_RANDOM_VAR_C,2));
                add(new LedData(OP_IF,DATA_C, DATA_LARGE, 2));
                add(new LedData(OP_FOR_START,30));
                add(new LedData(OP_RANDOM_VAL,135));
                add(new LedData(OP_RANDOM_DELAY,1));
                add(new LedData(OP_FOR_END, 0));
                add(new LedData(0,10));
                add(new LedData(OP_RANDOM_DELAY,lightTime));
                add(new LedData(OP_RANDOM_DELAY,lightTime));
                add(new LedData(OP_END_IF,0));
                add(new LedData(OP_RANDOM_DELAY,lightTime));
                add(new LedData(OP_RANDOM_VAR_C,2));
                add(new LedData(OP_IF,DATA_C,  DATA_LARGE, 2));
                add(new LedData(OP_FOR_START,20));
                add(new LedData(OP_RANDOM_VAL,135));
                add(new LedData(OP_RANDOM_DELAY,1));
                add(new LedData(OP_FOR_END, 0));
                add(new LedData(0,10));
                add(new LedData(OP_RANDOM_DELAY,lightTime));
                add(new LedData(OP_RANDOM_DELAY,lightTime));
                add(new LedData(OP_END_IF,0));
                add(new LedData(OP_RANDOM_DELAY,lightTime));
                add(new LedData(OP_RANDOM_DELAY,lightTime));
                add(new LedData(OP_END, 0));
                break;
            case EF_CANNON:
                ledDatas.clear();
                add(new LedData(0, 0));
                add(new LedData(OP_NOP, startTime));
                add(new LedData(OP_START, 0));
                add(new LedData(0,1));
                add(new LedData(50,1));
                add(new LedData(100,1));
                add(new LedData(150,1));
                add(new LedData(191,5));
                add(new LedData(150,5));
                add(new LedData(100,5));
                add(new LedData(50,5));
                add(new LedData(0,5));
                add(new LedData(OP_PUT_VAR_C,100));
                add(new LedData(OP_LONG_DELAY,20*(patternTime + 1)));
                switch (randomTime) {
                    case 0:
                        break;
                    case 1:
                        add(new LedData(OP_RANDOM_DELAY,18));
                        break;
                    case 2:
                        add(new LedData(OP_RANDOM_DELAY,27));
                        break;
                    case 3:
                        add(new LedData(OP_RANDOM_DELAY,36));
                        break;
                    case 4:
                        add(new LedData(OP_RANDOM_DELAY,69));
                        break;
                }
                add(new LedData(OP_END,0));
                break;
            case EF_EXPLOSION:
                int pattern10 = (patternTime + 1) * 10;
                ledDatas.clear();
                add(new LedData(0, 0));
                add(new LedData(OP_START, 0));
                add(new LedData(OP_IF, DATA_C, DATA_NOT_EQUAL, 0));
                add(new LedData(OP_PUT_VAR_C,0));
                add(new LedData(OP_NOP,startTime));
                add(new LedData(OP_FOR_START,pattern10));
                add(new LedData(OP_RANDOM_VAL,253));
                add(new LedData(OP_FOR_END,0));
                add(new LedData(OP_FOR_START,pattern10));
                add(new LedData(OP_RANDOM_VAL,245));
                add(new LedData(OP_FOR_END,0));
                add(new LedData(OP_FOR_START,pattern10));
                add(new LedData(OP_RANDOM_VAL,197));
                add(new LedData(OP_FOR_END,0));
                add(new LedData(OP_FOR_START,pattern10));
                add(new LedData(OP_RANDOM_VAL,133));
                add(new LedData(OP_FOR_END,0));
                add(new LedData(OP_ELSE,0));
                add(new LedData(0, 0));
                add(new LedData(OP_END_IF,0));
                add(new LedData(OP_END,0));
                break;
            case EF_ENERGY:
                ledDatas.clear();
                add(new LedData(OP_HEAD_PATTERN, EF_ENERGY, DEFINED_PATTERN, effect));
                add(new LedData(OP_HEAD_OPTION, randomTime, patternTime));
                add(new LedData(OP_HEAD_START_DELAY, startTime));
                add(new LedData(OP_START, 0));
                add(new LedData(150, 0));
                add(new LedData(OP_FOR_START, 10 + patternTime * 2));
                add(new LedData(20, 5 + patternTime));
                add(new LedData(50, 5 + patternTime));
                add(new LedData(OP_FOR_END, 0));
                add(new LedData(160, 0));
                add(new LedData(OP_FOR_START, 16 + patternTime * 2));
                add(new LedData(60, 4 + patternTime));
                add(new LedData(90, 4 + patternTime));
                add(new LedData(OP_FOR_END, 0));
                add(new LedData(170, 0));
                add(new LedData(OP_FOR_START, 20 + patternTime * 2));
                add(new LedData(90, 3 + patternTime));
                add(new LedData(110, 3 + patternTime));
                add(new LedData(OP_FOR_END, 0));
                add(new LedData(180, 0));
                add(new LedData(OP_FOR_START, 26 + patternTime * 2));
                add(new LedData(120, 1+patternTime));
                add(new LedData(140, 1+patternTime));
                add(new LedData(OP_FOR_END, 0));
                add(new LedData(OP_FOR_START, 200));
                add(new LedData(150, 0));
                add(new LedData(190, 0));
                add(new LedData(OP_FOR_END, 0));
                add(new LedData(OP_TRANS_BRIGHT_START, 190));
                add(new LedData(OP_TRANS_BRIGHT_STOP, 0));
                add(new LedData(OP_TRANS_BRIGHT_COUNT, 50));
                add(new LedData(OP_TRANS_START, 0));
                add(new LedData(0, 200));
                switch (randomTime) {
                    case 0:
                        break;
                    case 1:
                        add(new LedData(OP_RANDOM_DELAY,18));
                        break;
                    case 2:
                        add(new LedData(OP_RANDOM_DELAY,27));
                        break;
                    case 3:
                        add(new LedData(OP_RANDOM_DELAY,36));
                        break;
                    case 4:
                        add(new LedData(OP_RANDOM_DELAY,69));
                        break;
                }
                add(new LedData(OP_END, 0));
                break;
            case EF_RAINBOW:
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

        }
    }

    public void setEffect(int effect, int patternTime, int randomTime, int startTime, int patternOption) {
        switch (effect) {
            case EF_ALWAYS:
                ledDatas.clear();
                if (patternOption == 0) {
                    add(new LedData(0, 0));
                    add(new LedData(OP_NOP,startTime));
                    add(new LedData(OP_START,0));
                    add(new LedData(191, 0));
                    add(new LedData(OP_END, 0));
                } else if (patternOption == 1) {
                    add(new LedData(0, 0));
                    add(new LedData(OP_NOP,startTime));
                    add(new LedData(OP_TRANS_BRIGHT_START, 0));
                    add(new LedData(OP_TRANS_BRIGHT_STOP, OP_MACRO_MAX_VAL));
                    add(new LedData(OP_TRANS_BRIGHT_COUNT,250));
                    add(new LedData(OP_TRANS_START,0));
                    add(new LedData(OP_START,0));
                    add(new LedData(191, 0));
                    add(new LedData(OP_END, 0));
                } else if (patternOption == 2) {
                    add(new LedData(191, 0));
                    add(new LedData(OP_NOP,startTime));
                    add(new LedData(OP_START,0));
                    add(new LedData(0, 0));
                    add(new LedData(OP_END, 0));
                } else if (patternOption == 3) {
                    add(new LedData(191, 0));
                    add(new LedData(OP_NOP,startTime));
                    add(new LedData(OP_TRANS_BRIGHT_START, OP_MACRO_MAX_VAL));
                    add(new LedData(OP_TRANS_BRIGHT_STOP, 0));
                    add(new LedData(OP_TRANS_BRIGHT_COUNT,250));
                    add(new LedData(OP_TRANS_START,0));
                    add(new LedData(OP_START,0));
                    add(new LedData(0, 0));
                    add(new LedData(OP_END, 0));
                }
                break;
            case EF_PULSE:
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
            case EF_FLASH:
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
            // 두번깜박
            case EF_DOUBLE:
                ledDatas.clear();
                add(new LedData(0, 0));
                add(new LedData(OP_NOP, startTime));
                add(new LedData(OP_START, 0));
                add(new LedData(191,0));
                add(new LedData(0,10));
                add(new LedData(191,0));
                add(new LedData(0,40*(patternTime + 1) + 5));
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
            // 오르내리 효과
            case EF_UP_DOWN:
                int patternTime3 = 40*(patternTime+1);
                ledDatas.clear();
                add(new LedData(0, 0));
                add(new LedData(OP_NOP, startTime));
                add(new LedData(OP_START, 0));
                add(new LedData(OP_TRANS_BRIGHT_START, 0));
                add(new LedData(OP_TRANS_BRIGHT_STOP, OP_MACRO_MAX_VAL));
                add(new LedData(OP_TRANS_BRIGHT_COUNT,patternTime3));
                add(new LedData(OP_TRANS_START,patternTime));
                add(new LedData(OP_TRANS_BRIGHT_START, OP_MACRO_MAX_VAL));
                add(new LedData(OP_TRANS_BRIGHT_STOP, 0));
                add(new LedData(OP_TRANS_BRIGHT_COUNT,patternTime3));
                add(new LedData(OP_TRANS_START,patternTime));
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
            case EF_FIREFLY:
                ledDatas.clear();
                add(new LedData(0, 0));
                add(new LedData(OP_NOP, startTime));
                add(new LedData(OP_START, 0));
                add(new LedData(OP_TRANS_BRIGHT_START, 0));
                add(new LedData(OP_TRANS_BRIGHT_STOP, OP_MACRO_MAX_VAL));
                add(new LedData(OP_TRANS_BRIGHT_COUNT,10* (patternTime + 1)));
                add(new LedData(OP_TRANS_START,patternTime));
                add(new LedData(OP_TRANS_BRIGHT_START, OP_MACRO_MAX_VAL));
                add(new LedData(OP_TRANS_BRIGHT_STOP, 0));
                add(new LedData(OP_TRANS_BRIGHT_COUNT,20* (patternTime + 1)));
                add(new LedData(OP_TRANS_START,patternTime));
                add(new LedData(OP_NOP,(patternTime+1)*20 + 50));
                switch (randomTime) {
                    case 0:
                        break;
                    case 1:
                        add(new LedData(OP_RANDOM_DELAY,18));
                        break;
                    case 2:
                        add(new LedData(OP_RANDOM_DELAY,20));
                        break;
                    case 3:
                        add(new LedData(OP_RANDOM_DELAY,21));
                        break;
                    case 4:
                        add(new LedData(OP_RANDOM_DELAY,27));
                        break;
                }
                add(new LedData(OP_END,0));
                break;
            case EF_TORCH:
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
            case EF_SIN:
                ledDatas.clear();
                add(new LedData(OP_HEAD_PATTERN, EF_SIN, DEFINED_PATTERN, effect));
                add(new LedData(OP_HEAD_OPTION, randomTime, patternTime));
                add(new LedData(OP_HEAD_START_DELAY, startTime));
                add(new LedData(OP_START,0));
                add(new LedData(OP_SIN_SHIFT,0x9F));
                add(new LedData(OP_SIN_COUNT,0xFF));
                add(new LedData(OP_SIN_START,10 - (patternTime*2)));
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
            case EF_LASER:
                int pattern6 = (patternTime+1) * 50;
                ledDatas.clear();
                add(new LedData(OP_HEAD_PATTERN, EF_LASER, DEFINED_PATTERN, effect));
                add(new LedData(OP_HEAD_OPTION, randomTime, patternTime));
                add(new LedData(OP_HEAD_START_DELAY, startTime));
                add(new LedData(OP_START,0));
                add(new LedData(OP_TRANS_BRIGHT_START, 0));
                add(new LedData(OP_TRANS_BRIGHT_STOP, 100));
                add(new LedData(OP_TRANS_BRIGHT_COUNT,pattern6));
                add(new LedData(OP_TRANS_START,0));
                add(new LedData(120,1));
                add(new LedData(191,1));
                add(new LedData(120,1));
                add(new LedData(0,0));
                add(new LedData(OP_NOP,30 + patternTime*10));
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
            case EF_BREATHE:
                int pattern7 = (patternTime + 1) * 30;
                ledDatas.clear();
                add(new LedData(OP_HEAD_PATTERN, EF_BREATHE, DEFINED_PATTERN, effect));
                add(new LedData(OP_HEAD_OPTION, randomTime, patternTime));
                add(new LedData(OP_HEAD_START_DELAY, startTime));
                add(new LedData(OP_START, 0));
                add(new LedData(OP_TRANS_BRIGHT_START, 100));
                add(new LedData(OP_TRANS_BRIGHT_STOP, 191));
                add(new LedData(OP_TRANS_BRIGHT_COUNT,pattern7));
                add(new LedData(OP_TRANS_START,0));
                add(new LedData(OP_TRANS_BRIGHT_START, 191));
                add(new LedData(OP_TRANS_BRIGHT_STOP, 40));
                add(new LedData(OP_TRANS_BRIGHT_COUNT,pattern7));
                add(new LedData(OP_TRANS_START,0));
                add(new LedData(OP_TRANS_BRIGHT_START, 40));
                add(new LedData(OP_TRANS_BRIGHT_STOP, 100));
                add(new LedData(OP_TRANS_BRIGHT_COUNT,pattern7/2));
                add(new LedData(OP_TRANS_START,0));
                add(new LedData(OP_NOP,30 + 10*patternTime));
                switch (randomTime) {
                    case 0:
                        break;
                    case 1:
                        add(new LedData(OP_RANDOM_DELAY,2));
                        break;
                    case 2:
                        add(new LedData(OP_RANDOM_DELAY,18));
                        break;
                    case 3:
                        add(new LedData(OP_RANDOM_DELAY,27));
                        break;
                    case 4:
                        add(new LedData(OP_RANDOM_DELAY,36));
                        break;
                }
                add(new LedData(OP_END,0));
                break;
            case EF_FALSE:
                int randomForCount = 40 * (patternTime + 1);
                ledDatas.clear();
                add(new LedData(OP_HEAD_PATTERN, EF_LASER, DEFINED_PATTERN, effect));
                add(new LedData(OP_HEAD_OPTION, randomTime, patternTime));
                add(new LedData(OP_HEAD_START_DELAY, startTime));
                add(new LedData(OP_START, 0));
                add(new LedData(OP_FOR_START,randomForCount));
                add(new LedData(OP_RANDOM_VAL,252));
                add(new LedData(OP_RANDOM_DELAY,2));
                add(new LedData(OP_FOR_END, 0));
                add(new LedData(191,randomForCount));
                add(new LedData(OP_LONG_DELAY,randomForCount));
                add(new LedData(OP_RANDOM_DELAY,62));
                add(new LedData(OP_RANDOM_DELAY,randomForCount));
                add(new LedData(OP_FOR_START,randomForCount));
                add(new LedData(OP_RANDOM_VAL,252));
                add(new LedData(OP_RANDOM_DELAY,2));
                add(new LedData(OP_FOR_END, 0));
                add(new LedData(OP_TRANS_BRIGHT_START, 191));
                add(new LedData(OP_TRANS_BRIGHT_STOP, 0));
                add(new LedData(OP_TRANS_BRIGHT_COUNT,30));
                add(new LedData(OP_TRANS_START,0));
                add(new LedData(0,randomForCount));
                add(new LedData(OP_RANDOM_DELAY,62));
                add(new LedData(OP_FOR_START,randomForCount));
                add(new LedData(OP_RANDOM_VAL,60));
                add(new LedData(OP_RANDOM_DELAY,2));
                add(new LedData(OP_FOR_END, 0));
                add(new LedData(OP_END,0));
                break;
            case EF_LIGHTENING:
                int lightTime = (((patternTime+1)*2)<<3)&0xF8 + 4;
                ledDatas.clear();
                //add(new LedData(OP_HEAD_PATTERN, EF_LIGHTENING, DEFINED_PATTERN, effect));
                //add(new LedData(OP_HEAD_OPTION, randomTime, patternTime));
                //add(new LedData(OP_HEAD_START_DELAY, startTime));
                add(new LedData(OP_START, 0));
                add(new LedData(OP_NOP,startTime));
                add(new LedData(0,10));
                add(new LedData(OP_RANDOM_DELAY,lightTime));
                add(new LedData(OP_RANDOM_DELAY,lightTime));
                add(new LedData(OP_RANDOM_DELAY,lightTime));
                add(new LedData(OP_RANDOM_VAR_C,2));
                add(new LedData(OP_IF,DATA_C, DATA_LARGE, 2));
                add(new LedData(191,0));
                add(new LedData(0,10));
                add(new LedData(OP_RANDOM_DELAY,lightTime));
                add(new LedData(OP_RANDOM_DELAY,lightTime));
                add(new LedData(OP_END_IF,0));
                add(new LedData(OP_RANDOM_DELAY,lightTime));
                add(new LedData(OP_RANDOM_VAR_C,2));
                add(new LedData(OP_IF,DATA_C, DATA_LARGE, 2));
                add(new LedData(OP_FOR_START,30));
                add(new LedData(OP_RANDOM_VAL,135));
                add(new LedData(OP_RANDOM_DELAY,1));
                add(new LedData(OP_FOR_END, 0));
                add(new LedData(0,10));
                add(new LedData(OP_RANDOM_DELAY,lightTime));
                add(new LedData(OP_RANDOM_DELAY,lightTime));
                add(new LedData(OP_END_IF,0));
                add(new LedData(OP_RANDOM_DELAY,lightTime));
                add(new LedData(OP_RANDOM_VAR_C,2));
                add(new LedData(OP_IF,DATA_C,  DATA_LARGE, 2));
                add(new LedData(OP_FOR_START,20));
                add(new LedData(OP_RANDOM_VAL,135));
                add(new LedData(OP_RANDOM_DELAY,1));
                add(new LedData(OP_FOR_END, 0));
                add(new LedData(0,10));
                add(new LedData(OP_RANDOM_DELAY,lightTime));
                add(new LedData(OP_RANDOM_DELAY,lightTime));
                add(new LedData(OP_END_IF,0));
                add(new LedData(OP_RANDOM_DELAY,lightTime));
                add(new LedData(OP_RANDOM_DELAY,lightTime));
                add(new LedData(OP_END, 0));
                break;
            /*
            case EF_WELDING:
                ledDatas.clear();
                add(new LedData(OP_HEAD_PATTERN, EF_WELDING, DEFINED_PATTERN, effect));
                add(new LedData(OP_HEAD_OPTION, randomTime, patternTime));
                add(new LedData(OP_HEAD_START_DELAY, startTime));
                add(new LedData(OP_START, 0));
                add(new LedData(191,patternTime));
                add(new LedData(0,patternTime*2 + 1));
                add(new LedData(OP_FOR_START,50));
                add(new LedData(OP_RANDOM_VAL,252));
                add(new LedData(OP_FOR_END, 0));
                add(new LedData(0,patternTime*10));
                add(new LedData(OP_RANDOM_DELAY,62));
                add(new LedData(OP_FOR_START,10));
                add(new LedData(OP_RANDOM_VAL,60));
                add(new LedData(OP_FOR_END, 0));
                add(new LedData(OP_FOR_START,10));
                add(new LedData(OP_RANDOM_VAL,174));
                add(new LedData(OP_FOR_END, 0));
                add(new LedData(OP_FOR_START,10));
                add(new LedData(OP_RANDOM_VAL,252));
                add(new LedData(OP_FOR_END, 0));
                add(new LedData(OP_FOR_START,10));
                add(new LedData(OP_RANDOM_VAL,174));
                add(new LedData(OP_FOR_END, 0));
                add(new LedData(OP_FOR_START,10));
                add(new LedData(OP_RANDOM_VAL,60));
                add(new LedData(OP_FOR_END, 0));
                add(new LedData(0,patternTime*2 + 1));
                add(new LedData(OP_END, 0));
                break;
                */
            case EF_CANNON:
                ledDatas.clear();
                add(new LedData(0, 0));
                add(new LedData(OP_NOP, startTime));
                add(new LedData(OP_START, 0));
                add(new LedData(0,1));
                add(new LedData(50,1));
                add(new LedData(100,1));
                add(new LedData(150,1));
                add(new LedData(191,5));
                add(new LedData(150,5));
                add(new LedData(100,5));
                add(new LedData(50,5));
                add(new LedData(0,5));
                add(new LedData(OP_PUT_VAR_C,100));
                add(new LedData(OP_LONG_DELAY,20*(patternTime + 1)));
                switch (randomTime) {
                    case 0:
                        break;
                    case 1:
                        add(new LedData(OP_RANDOM_DELAY,18));
                        break;
                    case 2:
                        add(new LedData(OP_RANDOM_DELAY,27));
                        break;
                    case 3:
                        add(new LedData(OP_RANDOM_DELAY,36));
                        break;
                    case 4:
                        add(new LedData(OP_RANDOM_DELAY,69));
                        break;
                }
                add(new LedData(OP_END,0));
                break;
            case EF_EXPLOSION:
                int pattern10 = (patternTime + 1) * 10;
                ledDatas.clear();
                add(new LedData(0, 0));
                add(new LedData(OP_START, 0));
                add(new LedData(OP_IF, DATA_C, DATA_NOT_EQUAL, 0));
                add(new LedData(OP_PUT_VAR_C,0));
                add(new LedData(OP_NOP,startTime));
                add(new LedData(OP_FOR_START,pattern10));
                add(new LedData(OP_RANDOM_VAL,253));
                add(new LedData(OP_FOR_END,0));
                add(new LedData(OP_FOR_START,pattern10));
                add(new LedData(OP_RANDOM_VAL,245));
                add(new LedData(OP_FOR_END,0));
                add(new LedData(OP_FOR_START,pattern10));
                add(new LedData(OP_RANDOM_VAL,197));
                add(new LedData(OP_FOR_END,0));
                add(new LedData(OP_FOR_START,pattern10));
                add(new LedData(OP_RANDOM_VAL,133));
                add(new LedData(OP_FOR_END,0));
                add(new LedData(OP_ELSE,0));
                add(new LedData(0, 0));
                add(new LedData(OP_END_IF,0));
                add(new LedData(OP_END,0));
                break;
            case EF_ENERGY:
                ledDatas.clear();
                add(new LedData(OP_HEAD_PATTERN, EF_ENERGY, DEFINED_PATTERN, effect));
                add(new LedData(OP_HEAD_OPTION, randomTime, patternTime));
                add(new LedData(OP_HEAD_START_DELAY, startTime));
                add(new LedData(OP_START, 0));
                add(new LedData(150, 0));
                add(new LedData(OP_FOR_START, 10 + patternTime * 2));
                add(new LedData(20, 5 + patternTime));
                add(new LedData(50, 5 + patternTime));
                add(new LedData(OP_FOR_END, 0));
                add(new LedData(160, 0));
                add(new LedData(OP_FOR_START, 16 + patternTime * 2));
                add(new LedData(60, 4 + patternTime));
                add(new LedData(90, 4 + patternTime));
                add(new LedData(OP_FOR_END, 0));
                add(new LedData(170, 0));
                add(new LedData(OP_FOR_START, 20 + patternTime * 2));
                add(new LedData(90, 3 + patternTime));
                add(new LedData(110, 3 + patternTime));
                add(new LedData(OP_FOR_END, 0));
                add(new LedData(180, 0));
                add(new LedData(OP_FOR_START, 26 + patternTime * 2));
                add(new LedData(120, 1+patternTime));
                add(new LedData(140, 1+patternTime));
                add(new LedData(OP_FOR_END, 0));
                add(new LedData(OP_FOR_START, 200));
                add(new LedData(150, 0));
                add(new LedData(190, 0));
                add(new LedData(OP_FOR_END, 0));
                add(new LedData(OP_TRANS_BRIGHT_START, 190));
                add(new LedData(OP_TRANS_BRIGHT_STOP, 0));
                add(new LedData(OP_TRANS_BRIGHT_COUNT, 50));
                add(new LedData(OP_TRANS_START, 0));
                add(new LedData(0, 200));
                switch (randomTime) {
                    case 0:
                        break;
                    case 1:
                        add(new LedData(OP_RANDOM_DELAY,18));
                        break;
                    case 2:
                        add(new LedData(OP_RANDOM_DELAY,27));
                        break;
                    case 3:
                        add(new LedData(OP_RANDOM_DELAY,36));
                        break;
                    case 4:
                        add(new LedData(OP_RANDOM_DELAY,69));
                        break;
                }
                add(new LedData(OP_END, 0));
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
                        temp1 = (delay >> 3) & 0x1F;
                        temp2 = delay & 0x07;
                        temp1 = temp1 * lo.getRatioBright() / 100;
                        delay = ((temp1 << 3) & 0xF8) | (temp2);
                        break;
                    case OP_RANDOM_DELAY:
                        temp1 = (delay >> 3) & 0x1F;
                        temp2 = delay & 0x07;
                        temp1 = temp1 * lo.getRatioDuration() / 100;
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
                    case OP_SIN_SHIFT:
                        temp1 = (delay >> 4) & 0x0F;
                        temp2 = (delay & 0x0F);
                        temp1 = temp1 * lo.getRatioBright() / 100;
                        temp2 = temp2 * lo.getRatioBright() / 100;
                        delay = ((temp1 << 4) & 0xF0) | (temp2 & 0x0F);
                        break;
                    case OP_TRANS_BRIGHT_START:
                    case OP_TRANS_BRIGHT_STOP:
                        if (delay > 0xFF) {
                            if (delay == OP_MACRO_MIN_VAL) {
                                delay = 0;
                            } else if (delay == OP_MACRO_MAX_VAL) {
                                delay = 191 * lo.getRatioBright() / 100;
                            }
                        } else {
                            delay = delay * lo.getRatioBright() / 100;
                        }
                        break;
                }
            }
            byteArray[2*i] = (byte)val;
            byteArray[2*i +1] = (byte)delay;
        }
        return byteArray;
    }
}
