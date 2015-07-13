package com.syncworks.leddata;

import java.io.Serializable;
import java.util.Comparator;

import static com.syncworks.define.Define.*;

/**
 * Created by vosami on 2015-07-01.
 * Led Data 클래스
 */
public class LedData implements Serializable,Comparator<LedData> {
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
        // 임시 변수 생성
        int param1 = 0;
        int param2 = 0;
        int param3 = 0;
        switch (instruct) {
            case OP_START:
            case OP_END:
            case OP_FETCH:
                if (args.length == 1) {
                    _val = instruct;
                    _duration = (int) args[0];//Integer.valueOf((String) args[0]);
                }
                break;
            case OP_RANDOM_VAL:
            case OP_RANDOM_DELAY:
                if (args.length == 2) {
                    _val = instruct;
                    // 기준 값은 6의 배수 0~186
                    param1 = Integer.valueOf((String) args[0]);
                    // 간격 값은 쉬프트 0~7 (1~128)
                    param2 = Integer.valueOf((String) args[1]) & 0x07;
                    param1 = (param1 / 6) & 0xF8;
                    _duration = param1 | param2;
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
                    param1 = Integer.valueOf((String) args[0]) & 0x03;
                    param2 = Integer.valueOf((String) args[1]) & 0x03;
                    param3 = Integer.valueOf((String) args[2]) & 0x0F;
                    _duration = (param1 << 6) | (param2 << 4) | param3;
                }
                break;
            case OP_PUT_MSP:
                if (args.length == 1) {
                    _val = instruct;
                    _duration = Integer.valueOf((String) args[0]);
                }
                break;
            case OP_GET_MSP:
                if (args.length == 2) {
                    _val = instruct;
                    param1 = Integer.valueOf((String) args[0]) & 0x03;
                    param3 = Integer.valueOf((String) args[1]) & 0x3F;
                    _duration = (param1<<6) | param3;
                }
                break;
            // 센서 데이터 읽기 (DATA_A,B,C(2bit),
            case OP_GET_SENSOR:
                if (args.length == 3) {
                    _val = instruct;
                    param1 = Integer.valueOf((String) args[0]) & 0x03;
                    param2 = Integer.valueOf((String) args[1]) & 0x03;
                    param3 = Integer.valueOf((String) args[2]) & 0x0F;
                    _duration = (param1 << 6) | (param2 << 4) | param3;
                }
                break;
            case OP_FOR_START:
            case OP_FOR_END:
                if (args.length == 1) {
                    _val = instruct;
                    _duration = Integer.valueOf((String) args[0]);
                }
                break;
            case OP_IF:
                if (args.length == 3) {
                    _val = instruct;
                    param1 = Integer.valueOf((String) args[0]) & 0x03;
                    param2 = Integer.valueOf((String) args[1]) & 0x03;
                    param3 = Integer.valueOf((String) args[2]) & 0x0F;
                    _duration = (param1 << 6) | (param2 << 4) | param3;
                }
                break;
            case OP_ELSE:
            case OP_END_IF:
            case OP_JUMPTO:
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

    // 밝기를 퍼센트 단위로 조절
    public void modBrightPercent(int percent) {
        int val1, val2;
        if (percent >100 || percent < 0) {
            return;
        }
        // 기본 밝기 값이면 percent 단위 조절
        if (_val < OP_CODE_MIN) {
            _val = _val * percent / 100;
        }
        // 랜덤 밝기 명령어이면 기준값 조절
        else if (_val == OP_RANDOM_VAL) {
            val1 = (_duration) & 0xF8;
            val2 = (_duration & 0x07);
            val1 = (val1 * percent / 100) & 0xF8;
            _duration = val1 | val2;
        }
        // 소리 조절 값이면 비율 조절
        else if (_val == OP_SOUND_VAL) {
            _duration = _duration * percent / 100;
        }
    }
    // 지연을 퍼센트 단위로 조절
    public void modDurationPercent(int percent) {
        int val1, val2;
        if (percent >300 || percent < 0) {
            return;
        }
        if (_val < OP_CODE_MIN) {
            setDurationRatio(percent);
        } else if (_val == OP_START || _val == OP_END || _val == OP_NOP || _val == OP_LONG_DELAY) {
            setDurationRatio(percent);
        } else if (_val == OP_RANDOM_DELAY) {
            val1 = (_duration) & 0xF8;
            val2 = (_duration & 0x07);
            val1 = (val1 * percent / 100) & 0xF8;
            _duration = val1 | val2;
        }
    }

    private void setDurationRatio(int percent) {
        if (_duration != 0xFF) {
            _duration = _duration * percent / 100;
            if (_duration > 0xF0) {
                _duration = 0xF0;
            }
        }
    }

    // val 변경
    public void modBright(int val) {
        _val = val;
    }
    // duration 변경
    public void modDuration(int duration) {
        _duration = duration;
    }

    // 밝기 정보 가져오기
    public int getVal() {
        return _val;
    }
    // 지연 정보 가져오기
    public int getDuration() {
        return _duration;
    }
    // 명령어인지 확인
    public boolean isInstruct() {
        if (_val < OP_CODE_MIN) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public int compare(LedData x, LedData y) {
        if (x.getVal() == y.getVal()) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        String retStr = "Led Data Val:" + _val + ", Duration:" + _duration;
        return retStr;
    }
}
