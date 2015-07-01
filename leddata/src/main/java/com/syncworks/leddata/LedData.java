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
        setBright(val, duration);
    }
    // 명령어 설정 생성자
    public LedData(int instruct, Object... args) {
        if (instruct > OP_CODE_MIN && instruct <= 0xFF) {
            setInstruct(instruct,args);
        }
    }
    // 밝기 설정
    public void setBright(int val, int duration) {
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
    // 명령어 설정
    public void setInstruct(int instruct, Object... args) {
        int middleVal = 0;
        int gapVal = 0;
        int data = 0;
        int operation = 0;
        int var = 0;
        switch (instruct) {
            case OP_START:
            case OP_END:
            case OP_FETCH:
                if (args.length == 1) {
                    _val = instruct;
                    _duration = Integer.valueOf((String) args[0]);
                }
                break;
            case OP_RANDOM_VAL:
            case OP_RANDOM_DELAY:
                if (args.length == 2) {
                    _val = instruct;
                    // 기준 값은 6의 배수 0~186
                    middleVal = Integer.valueOf((String) args[0]);
                    // 간격 값은 쉬프트 0~7 (1~128)
                    gapVal = Integer.valueOf((String) args[1]) & 0x07;
                    middleVal = (middleVal / 6) & 0xF8;
                    _duration = middleVal | gapVal;
                }
                break;
            case OP_NOP:
            case OP_LONG_DELAY:
                if (args.length == 1) {
                    _val = instruct;
                    _duration = Integer.valueOf((String) args[0]);
                }
                break;
            case OP_VAR_VAL:
                if (args.length == 1) {
                    _val = instruct;
                    _duration = (Integer.valueOf((String) args[0]) << 6) & 0xC0;
                }
                break;
            case OP_SOUND_VAL:
            case OP_PUT_VAR_A:
            case OP_PUT_VAR_B:
            case OP_PUT_VAR_C:
                if (args.length == 1) {
                    _val = instruct;
                    _duration = Integer.valueOf((String) args[0]);
                }
                break;
            case OP_CALC_VAR_A:
            case OP_CALC_VAR_B:
            case OP_CALC_VAR_C:
                if (args.length == 3) {
                    _val = instruct;
                    data = Integer.valueOf((String) args[0]) & 0x03;
                    operation = Integer.valueOf((String) args[1]) & 0x03;
                    var = Integer.valueOf((String) args[2]) & 0x0F;
                    _duration = (data << 6) | (operation << 4) | var;
                }
                break;
            case OP_PUT_MSP:
                if (args.length == 1) {
                    _val = instruct;
                    _duration = Integer.valueOf((String) args[0]);
                }
                break;
            default:
                _val = 0;
                _duration = 0;
                break;

        }
    }

    // 밝기 정보 가져오기
    public int getVal() {
        return _val;
    }
    // 지연 정보 가져오기
    public int getDuration() {
        return _duration;
    }
}
