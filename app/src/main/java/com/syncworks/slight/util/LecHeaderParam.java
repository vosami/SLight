package com.syncworks.slight.util;

import com.syncworks.define.Logger;
import com.syncworks.slight.TxDatas;

/**
 * Created by vosami on 2015-07-16.
 * LED 헤
 */
public class LecHeaderParam {
    public final static int ALARM_LENGTH = 5;
    public final static int HEADER_ALARM_ORDER = 33;
    public final static int HEADER_SEQ_TIME_ORDER = 50;
    public final static int HEADER_PARAM_ORDER = 54;

    public final static int PARAM_RUN_MODE_DEFAULT = 0;
    public final static int PARAM_RUN_MODE_SEQUENTIAL = 1;
    public final static int PARAM_RUN_MODE_RANDOM = 2;

    private byte[] lecByte = new byte[64];

    public LecHeaderParam() {
        init();
    }

    private void init() {
        for (int i=0;i<64;i++) {
            lecByte[i] = 0;
        }
    }

    public void setLecByte(byte[] data, int srcPos, int dstPos, int length) {
        System.arraycopy(data,srcPos,lecByte,dstPos,length);
        Logger.v(this, "setLecByte", TxDatas.bytesToHex(data));
        Logger.v(this,"setLecByte", TxDatas.bytesToHex(lecByte));
    }

    public void setOffTime(int offTime) {
        this.lecByte[32] = (byte)(offTime);
    }

    public int getOffTime() {
        Logger.v(this,"getOffTime",lecByte[33],lecByte[32]);
        return (lecByte[32]);
    }

    public int getSequenceRunTime(int order) {
        if (order <4) {
            return lecByte[HEADER_SEQ_TIME_ORDER + order];
        } else {
            return 0;
        }
    }

    public void setSequenceRunTime(int order, int runTime) {
        if (order<4) {
            lecByte[HEADER_SEQ_TIME_ORDER + order] = (byte) runTime;
        }
    }

    // 휴면 중 LED 깜박이기
    public void setSleepLedBink(boolean isBlink) {
        if (isBlink) {
            lecByte[HEADER_PARAM_ORDER] = (byte) (lecByte[HEADER_PARAM_ORDER] | 0x80);
        } else {
            lecByte[HEADER_PARAM_ORDER] = (byte) (lecByte[HEADER_PARAM_ORDER] & 0x7F);
        }
    }

    public boolean isSleepLedBlink() {
        return (lecByte[HEADER_PARAM_ORDER] & 0x80) != 0;
    }

    public boolean isMusicMode() {
        return (lecByte[HEADER_PARAM_ORDER] & 0x40) != 0;
    }

    public int getRunMode() {
        return (lecByte[HEADER_PARAM_ORDER] & 0x30)>>4;
    }
    public void setRunMode(int runMode) {
        lecByte[HEADER_PARAM_ORDER] = (byte) ((lecByte[HEADER_PARAM_ORDER] & 0xCF) | ((runMode<<4) & 0x30));
    }

    public int getRunPattern() {
        return (lecByte[HEADER_PARAM_ORDER] & 0x0C)>>2;
    }

    public void setRunPattern(int runPattern) {
        lecByte[HEADER_PARAM_ORDER] = (byte) ((lecByte[HEADER_PARAM_ORDER] & 0xF3) | ((runPattern<<2) & 0x0C));
    }

    public int getParam() {
        return lecByte[HEADER_PARAM_ORDER];
    }

    public void setAlarmDate(int alarmNum, int date) {
        lecByte[HEADER_ALARM_ORDER + ALARM_LENGTH * alarmNum] = (byte)date;
    }

    public int getAlarmDate(int alarmNum) {
        return lecByte[HEADER_ALARM_ORDER + ALARM_LENGTH * alarmNum];
    }

    public void setAlarmHour(int alarmNum, int hour) {
        lecByte[HEADER_ALARM_ORDER + ALARM_LENGTH * alarmNum + 1] = (byte)hour;
    }

    public int getAlarmHour(int alarmNum) {
        return lecByte[HEADER_ALARM_ORDER + ALARM_LENGTH * alarmNum + 1];
    }

    public void setAlarmMinute(int alarmNum, int min) {
        lecByte[HEADER_ALARM_ORDER + ALARM_LENGTH * alarmNum + 2] = (byte)min;
    }

    public int getAlarmMinute(int alarmNum) {
        return lecByte[HEADER_ALARM_ORDER + ALARM_LENGTH * alarmNum + 2];
    }

    public void setAlarmRunTime(int alarmNum, int runTime) {
        lecByte[HEADER_ALARM_ORDER + ALARM_LENGTH * alarmNum + 3] = (byte)runTime;
    }

    public int getAlarmRunTime(int alarmNum) {
        return lecByte[HEADER_ALARM_ORDER + ALARM_LENGTH * alarmNum + 3];
    }

    public void setAlarmOnOff(int alarmNum, boolean onOff) {
        if (onOff) {
            lecByte[HEADER_ALARM_ORDER + ALARM_LENGTH * alarmNum + 4] |= 0x80;
        } else {
            lecByte[HEADER_ALARM_ORDER + ALARM_LENGTH * alarmNum + 4] &= 0x7F;
        }
    }

    public boolean getAlarmOnOff(int alarmNum) {
        return (lecByte[HEADER_ALARM_ORDER + ALARM_LENGTH * alarmNum + 4] & 0x80) != 0;
    }

    public void setAlarmRunMode(int alarmNum, int runMode) {
        byte param = lecByte[HEADER_ALARM_ORDER + ALARM_LENGTH * alarmNum + 4];
        param = (byte) ((param & 0x9F) | (runMode <<5));
        lecByte[HEADER_ALARM_ORDER + ALARM_LENGTH * alarmNum + 4] = param;
    }

    public int getAlarmRunMode(int alarmNum) {
        return (lecByte[HEADER_ALARM_ORDER + ALARM_LENGTH * alarmNum + 4] & 0x60) >> 5;
    }

    public void setAlarmRunPattern(int alarmNum, int runPattern) {
        byte param = lecByte[HEADER_ALARM_ORDER + ALARM_LENGTH * alarmNum + 4];
        param = (byte) ((param & 0xE7) | (runPattern <<3));
        lecByte[HEADER_ALARM_ORDER + ALARM_LENGTH * alarmNum + 4] = param;
    }

    public int getAlarmRunPattern(int alarmNum) {
        return (lecByte[HEADER_ALARM_ORDER + ALARM_LENGTH * alarmNum + 4] & 0x18) >> 3;
    }

    public int getAlarmParam(int alarmNum) {
        return lecByte[HEADER_ALARM_ORDER + ALARM_LENGTH * alarmNum + 4];
    }
}
