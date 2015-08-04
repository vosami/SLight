package com.syncworks.slight.util;

import com.syncworks.define.Logger;
import com.syncworks.slight.TxDatas;

/**
 * Created by vosami on 2015-07-16.
 * LED 헤
 */
public class LecHeaderParam {
    public final static int HEADER_SEQ_TIME_ORDER = 50;
    public final static int HEADER_PARAM_ORDER = 54;

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
}
