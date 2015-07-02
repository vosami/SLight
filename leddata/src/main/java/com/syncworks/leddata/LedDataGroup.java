package com.syncworks.leddata;

import static com.syncworks.define.Define.*;


/**
 * Created by vosami on 2015-07-02.
 * 9개의 LED 그룹
 */
public class LedDataGroup {
    //
    private int curIndex[] = new int[NUMBER_OF_SINGLE_LED];
    private int curDuration[] = new int[NUMBER_OF_SINGLE_LED];
    private int indexOfStartPosition[] = new int[NUMBER_OF_SINGLE_LED];
    private int indexOfForPosition[] = new int[NUMBER_OF_SINGLE_LED];
    private int ledForRef[] = new int[NUMBER_OF_SINGLE_LED];
    private int ledForCount[] = new int[NUMBER_OF_SINGLE_LED];
    private int ledBright[] = new int[NUMBER_OF_SINGLE_LED];
    private LedDataList[] exeDataList = new LedDataList[NUMBER_OF_SINGLE_LED];
    private LedDataList[] ledDataLists;
    private static int dataA, dataB, dataC;
    private static int ratioBright, ratioDuration;
    private static int ledSoundVal;

    public LedDataGroup() {
        ledDataLists = new LedDataList[NUMBER_OF_SINGLE_LED];
        for (int i=0;i<NUMBER_OF_SINGLE_LED;i++) {
            ledDataLists[i] = new LedDataList(i);
        }
        ledClear();
    }


    public void DataExecute(int ledNum) {
        // 지속 시간이 끝났으면 실행
        if (isEndDuration(ledNum)) {
            LedData ledData = getCurData(ledNum);
            int val = ledData.getVal();
            int duration = ledData.getDuration();
            if (val < OP_CODE_MIN) {
                // LED 밝기 설정
                setCurBright(ledNum,val);
                // 지연 설정
                setCurDuration(ledNum,duration);
                // 인덱스 증가
                indexIncrease(ledNum);
            }
/***************************************************************************************************
 * 명령 실행 시작
 **************************************************************************************************/
            else {
                switch(val) {
                    // 시작
                    case OP_START:
                        // 시작 지점 설정
                        indexOfStartPosition[ledNum] = getCurIndex(ledNum);
                        // 지속 시간 갱신(시작과 종료 명령은 지속 시간을 x8 한다)
                        setLongDuration(ledNum, duration, 3);
                        // 인덱스 증가
                        indexIncrease(ledNum);
                        break;
                    // 마지막
                    case OP_END:
                        // 지속 시간 갱신
                        setLongDuration(ledNum, duration, 3);
                        // 시작 위치로 복귀
                        setCurIndex(ledNum, indexOfStartPosition[ledNum]);
                        break;
                    // EEPROM 데이터 불러오기
                    case OP_FETCH:
                        // 인덱스 증가
                        indexIncrease(ledNum);
                        break;
                    // 랜덤 밝기
                    case OP_RANDOM_VAL:
                        // 랜덤 밝기 설정
                        setCurBright(ledNum, getRandom(duration,OP_CODE_MIN));
                        // 인덱스 증가
                        indexIncrease(ledNum);
                        break;
                    case OP_RANDOM_DELAY:
                        // 랜덤 지연 설정
                        setCurDuration(ledNum,getRandom(duration,0xFF));
                        // 인덱스 증가
                        indexIncrease(ledNum);
                        break;
                    case OP_NOP:
                        // 지연 설정
                        setCurDuration(ledNum,duration);
                        // 인덱스 증가
                        indexIncrease(ledNum);
                        break;
                    case OP_LONG_DELAY:
                        // 긴 지연 설정
                        setLongDuration(ledNum,duration,4);
                        // 인덱스 증가
                        indexIncrease(ledNum);
                        break;
                    case OP_VAR_VAL:
                        setCurBright(ledNum,getVarVal(ledNum,duration));
                        // 인덱스 증가
                        indexIncrease(ledNum);
                        break;
                    case OP_SOUND_VAL:
                        setCurBright(ledNum,getSoundVal(duration));
                        // 인덱스 증가
                        indexIncrease(ledNum);
                        break;
                    case OP_PUT_VAR_A:
                        dataA = duration;
                        // 인덱스 증가
                        indexIncrease(ledNum);
                        break;
                    case OP_PUT_VAR_B:
                        dataB = duration;
                        // 인덱스 증가
                        indexIncrease(ledNum);
                        break;
                    case OP_PUT_VAR_C:
                        dataC = duration;
                        // 인덱스 증가
                        indexIncrease(ledNum);
                        break;
                    case OP_CALC_VAR_A:
                        break;
                    case OP_CALC_VAR_B:
                        break;
                    case OP_CALC_VAR_C:
                        break;
                }
            }
        }
    }

    // 지속 시간이 끝났는지 확인
    private boolean isEndDuration(int ledNum) {
        return (curDuration[ledNum] == 0);
    }
    // 지속 시간 설정
    private void setCurDuration(int ledNum, int duration) {
        if (duration < 0 || duration > 0xFF) {
            return;
        }
        if (duration == 0xFF) {
            curDuration[ledNum] = DELAY_INFINITE;
        } else {
            curDuration[ledNum] = duration * ratioDuration / 100;
        }
    }
    // 지속 시간 설정
    private void setLongDuration(int ledNum, int duration, int shift) {
        if (duration < 0 || duration > 0xFF) {
            return;
        }
        if (duration == 0xFF) {
            curDuration[ledNum] = DELAY_INFINITE;
        } else {
            curDuration[ledNum] = (duration<<shift) * ratioDuration / 100;
        }
    }
    // 현재 LedData 명령 확인
    private LedData getCurData(int ledNum) {
        return exeDataList[ledNum].get(getCurIndex(ledNum));
    }
    // 현재 Index 확인
    private int getCurIndex(int ledNum) {
        return curIndex[ledNum];
    }
    private void setCurIndex(int ledNum ,int index) {
        curIndex[ledNum] = index;
    }
    // 현재 Index 증가
    private void indexIncrease(int ledNum) {
        curIndex[ledNum]++;
        // 0~63 으로 제한
        curIndex[ledNum] &= 0x3F;
    }
    // 현재 밝기 설정
    private void setCurBright(int ledNum, int bright) {
        if (bright < 0 || bright > OP_CODE_MIN) {
            return;
        }
        ledBright[ledNum] = bright * ratioBright / 100;
    }



    // 밝기 비율 설정
    private void setRatioBright(int ratio) {
        if (ratio < 0 || ratio > 100) {
            return;
        }
        ratioBright = ratio;
    }
    // 지연 비율 설정
    private void setRatioDuration(int ratio) {
        if (ratio < 0 || ratio > 300) {
            return;
        }
        ratioDuration = ratio;
    }
    // 실행 LED 의 카운트를 초기화한다.
    private void ledInitCountVar(int ledNum) {
        curIndex[ledNum] = 0;
        curDuration[ledNum] = 0;
        indexOfForPosition[ledNum] = 0;
        indexOfStartPosition[ledNum] = 0;
        ledForCount[ledNum] = 0;
        ledForRef[ledNum] = 0;
        ledBright[ledNum] = 0;
    }
    // LED 의 밝기를 모두 0으로 만들고 카운트 관련 변수를 모두 초기화한다.
    private void ledClear() {
        // 데이터 초기화
        dataA = 0;
        dataB = 0;
        dataC = 0;
        ledSoundVal = 0;
        ratioBright = 100;
        ratioDuration = 100;
        for (int i=0;i<NUMBER_OF_SINGLE_LED;i++) {
            ledInitCountVar(i);
        }
    }

    private int getRandom(int data, int max) {
        int refVal = ((data>>3) & 0x001F) * 6;
        int gapVal = 1 << (data & 0x0007);
        int rand = (int) (Math.random() * gapVal * 2);
        int retVal = refVal - gapVal + rand;
        if (retVal < 0) {
            retVal = 0;
        } else if (retVal >= max) {
            retVal = max - 1;
        }
        return retVal;
    }

    // 밝기 설정용 변수 확인
    private int getVarVal(int ledNum, int data) {
        int retVal = 0;
        int selectData = (data >> 6) & 0x03;
        switch (selectData) {
            case DATA_A:
                retVal = dataA;
                break;
            case DATA_B:
                retVal = dataB;
                break;
            case DATA_C:
                retVal = dataC;
                break;
            case DATA_FOR_I:
                retVal = ledForCount[ledNum];
                break;
        }
        if (retVal < 0) {
            retVal = 0;
        } else if (retVal >= OP_CODE_MIN) {
            retVal = OP_CODE_MIN - 1;
        }
        return retVal;
    }

    // 스마트폰 소리에 따른 밝기 설정
    private int getSoundVal(int data) {
        int retVal = ledSoundVal / (data + 1);
        if (retVal < 0) {
            retVal = 0;
        } else if (retVal >= OP_CODE_MIN) {
            retVal = OP_CODE_MIN - 1;
        }
        return retVal;
    }

}
