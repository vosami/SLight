package com.syncworks.leddata;

import static com.syncworks.define.Define.*;

/**
 * Created by vosami on 2015-07-01.
 */
public class LedData {
    private int _val;
    private int _duration;

    // 기본 생성자
    public LedData() {
        _val = 0;
        _duration = 0;
    }

    // 기본 밝기 설정 생성자
    public LedData(int val, int duration) {
        // val 값이 0~191 일 경우 기본 밝기 설정
        if (val < OP_CODE_MIN && val >= 0) {
            _val = val;
            _duration = duration;
        }
        // val 값이 instruct 범위일 때 명령어 설정
        else if (val > OP_CODE_MIN && val <= 0xFF) {
            setInstruct(val,duration);
        } else {
            _val = 0;
            _duration = 0;
        }
    }
    // 명령어 설정 생성자
    public LedData(int instruct, Object... args) {
        if (instruct > OP_CODE_MIN && instruct <= 0xFF) {
            setInstruct(instruct,args);
        }
    }

    public void setInstruct(int instruct, Object... args) {
        switch (instruct) {
            case OP_START:
                break;
            case OP_END:
                break;
        }
    }
}
