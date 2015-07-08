package com.syncworks.slight;

import com.syncworks.define.Define;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio
 * Copyrights (C)SyncWorks All rights reserved by SyncWorks
 * Created by 승현 on 2015-04-26.
 */
public class TxDatas {

	public TxDatas() {
	}

	public static List<byte[]> format(byte txInstruct, byte[] arrayByte) {
		List<byte[]> txScriptList = null;

		return txScriptList;
	}

	public static List<byte[]> formatMemWrite(int ledNum, byte[] arrayByte) {
		List<byte[]> txScriptList = new ArrayList<>();
		int scriptLength = arrayByte.length;
		int scriptCount = (scriptLength / 16) + 1;
		for (int i = 0; i<scriptCount; i++) {
			int txCount = scriptLength - (i * 16);
			if (txCount > 16) {
				txCount = 16;
			}
			byte[] scriptData = new byte[txCount + 4];
			scriptData[0] = Define.TX_MEMORY_WRITE;
			scriptData[1] = (byte) ledNum;
			scriptData[2] = (byte) (i*8);
			scriptData[3] = (byte) (txCount/2);
			for (int j=0;j<txCount;j++) {
				scriptData[4+j] = arrayByte[i*16 + j];
			}
			txScriptList.add(scriptData);
		}
		return txScriptList;
	}

	public static byte[] formatInitCount() {
		return new byte[]{Define.TX_INIT_COUNT,0,0,0};
	}

	public static byte[] formatBrightAll(int bright) {
		return new byte[]{Define.TX_MEMORY_PATTERN_ALL,0,0,3,(byte)Define.OP_START,0,(byte)bright,0,(byte)Define.OP_END,0};
	}

	public static byte[] formatTimeRead() {
		return new byte[]{Define.TX_TIME_READ,0,0,0};
	}

	public static byte[] formatAlarmWrite(int enumAlarm, int enumDay, int hour, int min, int hold) {
		return new byte[]{Define.TX_ALARM_WRITE,(byte)enumAlarm,(byte)enumDay,(byte)hour, (byte)min, (byte)hold};
	}

    public static byte[] formatMemToRom() {
		return new byte[]{Define.TX_MEM_TO_ROM, 0, 0, 0};
    }
}
