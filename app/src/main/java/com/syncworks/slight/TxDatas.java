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

	public static byte[] formatParamRead(int length, int start) {
		// 명령어(1byte) + 길이(1byte), 시작위치(1byte)
		return new byte[]{Define.TX_PARAM_READ,(byte)length,(byte)start,0};
	}

	public static byte[] formatSleep(boolean isSleep) {
		byte wakeUpParam = 0;
		if (isSleep) {
			wakeUpParam = 1;
		}
		return new byte[]{Define.TX_WAKE_UP,wakeUpParam,0,0};
	}

	public static byte[] formatSleepBlinkCheck(boolean isBlink) {
		byte blinkParam = 0;
		if (isBlink) {
			blinkParam = 1;
		}
		return new byte[]{Define.TX_PARAM_WRITE,58,1,blinkParam};
	}
	// 동작 시간 설정
	public static byte[] formatSleepTime(int time) {
		byte hByte, lByte;
		hByte = (byte) ((time >> 8) & 0xFF);
		lByte = (byte) (time & 0xFF);
		return new byte[]{Define.TX_PARAM_WRITE,32,2,lByte,hByte};
	}

	// 랜덤 플레이
	public static byte[] formatRandomPlay(int playTime) {
		return new byte[]{Define.TX_PARAM_WRITE,59,1,(byte)playTime,0};
	}

	// EEPROM 에 있는 데이터 실행
	public static byte[] formatFetchData(int dataNum) {
		return new byte[]{Define.TX_MEMORY_FETCH_DATA,(byte)dataNum,0,0};
	}

	// EEPROM 에 데이터 저장
	public static byte[] formatSaveData(int ledNum, int dataNum) {
		return new byte[]{Define.TX_MEM_TO_ROM_EACH,(byte)ledNum,(byte)dataNum,0};
	}
	// EEPROM 초기화
	public static byte[] formatInitEEPROM() {
		return new byte[]{Define.TX_INIT_SET,0,0,0};
	}

	// 사운드 값 전송
	public static byte[] formatTxSound(int sound) {
		return new byte[]{Define.TX_SOUND_VAL,(byte)sound};
	}

	/**
	 * 바이트 to 헥스 변환
	 */
	public static final char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for ( int j = 0; j < bytes.length; j++ ) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
}
