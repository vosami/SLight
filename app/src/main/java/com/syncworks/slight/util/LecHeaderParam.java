package com.syncworks.slight.util;

import com.syncworks.define.Logger;
import com.syncworks.slight.TxDatas;

/**
 * Created by vosami on 2015-07-16.
 * LED 헤
 */
public class LecHeaderParam {
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
        this.lecByte[32] = (byte)(offTime & 0x00FF);
        this.lecByte[33] = (byte)(offTime>>8 & 0x00FF);
    }

    public int getOffTime() {
        Logger.v(this,"getOffTime",lecByte[33],lecByte[32]);
        return ((lecByte[33]<<8 & 0xFF00) | (lecByte[32] & 0x00FF));
    }

    // 휴면 중 LED 깜박이기
    public void setSleepLedBink(boolean isBlink) {
        if (isBlink) {
            lecByte[58] = 1;
        } else {
            lecByte[58] = 0;
        }
    }
    public boolean isSleepLedBlink() {
        if (lecByte[58] == 0) {
            return false;
        } else {
            return true;
        }
    }

    // 패턴 A,B,C 가 랜덤하게 플레이
    public void setRandomDataTime(int randomTime) {
        lecByte[59] = (byte) randomTime;
    }

    public int getRandomDataTime() {
        return lecByte[59];
    }
}
