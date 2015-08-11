package com.syncworks.leddata;

import java.io.Serializable;

/**
 * Created by vosami on 2015-07-03.
 * Led 실행 옵션
 */
public class LedOptions implements Serializable{
    private final static int MAX_BRIGHT_RATIO = 100;
    private final static int MAX_DURATION_RATIO = 300;
    private final static int MAX_DELAY = 250;

    private int ratioBright = 100;                              // 밝기 비율 (%단위)
    private int ratioDuration = 100;                            // 지연 비율 (%단위)
    private int delayStart = 0;                                 // 시작 지연 설정
    private int delayEnd = 0;                                   // 마지막 지연 설정
    private int delayMiddle = 0;                                // 중간 지연 설정
    private int delayStartOption = 0;                           // 시작 지연 적용 설정
    private int delayEndOption = 0;                             // 마지막 지연 적용 설정
    private int delayMiddleOption = 0;                          // 중간 지연 적용 설정
    private int patternDelayOption = 0;                         // 적용 패턴 값
    private int randomDelayOption = 0;                          // 랜덤 지연 설정 옵션

    public LedOptions() {
        init();
    }
    // 옵션 설정 초기화
    public void init() {
        ratioBright = 50;
        ratioDuration = 100;
        delayStart = 0;
        delayEnd = 0;
        delayMiddle = 0;
        delayStartOption = 0;
        delayEndOption = 0;
        delayMiddleOption = 0;
        patternDelayOption = 0;
        randomDelayOption = 0;
    }
    // 시작 지연 옵션 값 설정
    public void setDelayStartOption(int delay) {
        delayStartOption = delay;
    }
    // 시작 지연 옵션 값 읽어오기
    public int getDelayStartOption() {
        return delayStartOption;
    }
    // 패턴 딜레이 옵션 설정
    public void addPatternDelayOption() {
        if (patternDelayOption >= 4) {
            patternDelayOption = 0;
        } else {
            patternDelayOption++;
        }
    }
    public void setPatternDelayOption(int option) {
        patternDelayOption = option;
    }
    // 패턴 딜레이 옵션 읽어오기
    public int getPatternDelayOption() {
        return patternDelayOption;
    }
    // 랜덤 지연 옵션 설정
    public void addRandomDelayOption() {
        if (randomDelayOption >= 4) {
            randomDelayOption = 0;
        } else {
            randomDelayOption++;
        }
    }
    public void setRandomDelayOption(int option) {
        randomDelayOption = option;
    }
    // 랜덤 지연 옵션 읽어오기
    public int getRandomDelayOption() {
        return randomDelayOption;
    }

    // 밝기 비율 설정
    public void setRatioBright(int ratio) {
        if (ratio >=0 && ratio<=MAX_BRIGHT_RATIO) {
            this.ratioBright = ratio;
        }
    }
    // 지연 비율 설정
    public void setRatioDuration(int ratio) {
        if (ratio >= 0 && ratio <= MAX_DURATION_RATIO) {
            this.ratioDuration = ratio;
        }
    }
    // 시작 지연 설정
    public void setDelayStart(int delay) {
        if (delay >= 0 && delay <= MAX_DELAY) {
            this.delayStart = delay;
        }
    }
    // 종료 지연 설정
    public void setDelayEnd(int delay) {
        if (delay >= 0 && delay <= MAX_DELAY) {
            this.delayEnd = delay;
        }
    }
    // 중간 지연 설정
    public void setDelayMiddle(int delay) {
        if (delay >= 0 && delay <= MAX_DELAY) {
            this.delayMiddle = delay;
        }
    }

    // 밝기 비율 읽기
    public int getRatioBright() {
        return this.ratioBright;
    }
    // 지연 비율 읽기
    public int getRatioDuration() {
        return this.ratioDuration;
    }
    // 시작 지연 읽기
    public int getDelayStart() {
        return this.delayStart;
    }
    // 종료 지연 읽기
    public int getDelayEnd() {
        return this.delayEnd;
    }
    // 중간 지연 읽기
    public int getDelayMiddle() {
        return this.delayMiddle;
    }

}
