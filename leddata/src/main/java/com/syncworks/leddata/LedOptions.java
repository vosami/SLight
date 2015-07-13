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
    private boolean isApplyDelayStart = false;                  // 시작 지연 적용 설정
    private boolean isApplyDelayEnd = false;                    // 마지막 지연 적용 설정
    private boolean isApplyDelayMiddle = false;                 // 중간 지연 적용 설정
    private int idOfPattern = 0;                                // 적용 패턴 값

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
        isApplyDelayStart = false;
        isApplyDelayEnd = false;
        isApplyDelayMiddle = false;
        idOfPattern = 0;
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
    // 시작 지연 설정
    public void applyDelayStart(boolean bool) {
        isApplyDelayStart = bool;
    }
    // 종료 지연 설정
    public void applyDelayEnd(boolean bool) {
        isApplyDelayEnd = bool;
    }
    // 중간 지연 설정
    public void applyDelayMiddle(boolean bool) {
        isApplyDelayMiddle = bool;
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
        if (isApplyDelayStart) {
            return this.delayStart;
        } else {
            return 0;
        }
    }
    // 종료 지연 읽기
    public int getDelayEnd() {
        if (isApplyDelayEnd) {
            return this.delayEnd;
        } else {
            return 0;
        }
    }
    // 중간 지연 읽기
    public int getDelayMiddle() {
        if (isApplyDelayMiddle) {
            return this.delayMiddle;
        } else {
            return 0;
        }
    }
    //
    public boolean isApplyDelayStart() {
        return isApplyDelayStart;
    }

    public boolean isApplyDelayEnd() {
        return isApplyDelayEnd;
    }
    public boolean isApplyDelayMiddle() {
        return isApplyDelayMiddle;
    }
}
