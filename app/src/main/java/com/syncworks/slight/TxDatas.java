package com.syncworks.slight;

import com.syncworks.define.Define;
import com.syncworks.slight.util.LecHeaderParam;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

	public static byte[] formatAlarmWrite(int alarmNum, int option,int runTime ,int enumDay, int hour, int min) {
		return new byte[]{Define.TX_ALARM_WRITE, (byte) alarmNum,(byte)option,(byte)runTime,(byte)enumDay,(byte)hour, (byte)min};
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

	public static byte[] formatMinuteTimerStart(boolean isTimerStart) {
		byte timerStart = 0;
		if (isTimerStart) {
			timerStart = 1;
		}
		return new byte[]{Define.TX_MINUTE_TIMER_START,timerStart,0,0};
	}

	// 동작 시간 설정
	public static byte[] formatSleepTime(int time) {
		byte lByte;
		lByte = (byte) (time & 0xFF);
		return new byte[]{Define.TX_PARAM_WRITE,32,1,lByte};
	}


	// EEPROM 에 있는 데이터 실행
	public static byte[] formatFetchData(int dataNum,int ledNum) {
		return new byte[]{Define.TX_MEMORY_FETCH_DATA,(byte)dataNum,(byte)ledNum,0};
	}

	// EEPROM 에 데이터 저장
	public static byte[] formatSaveData(int ledNum, int dataNum, int num) {
		return new byte[]{Define.TX_MEM_TO_ROM_EACH,(byte)ledNum,(byte)dataNum,(byte)num};
	}
	// EEPROM 초기화
	public static byte[] formatInitEEPROM() {
		return new byte[]{Define.TX_INIT_SET,0,0,0};
	}

	// 사운드 값 전송
	public static byte[] formatTxSound(int sound) {
		return new byte[]{Define.TX_SOUND_VAL,(byte)sound};
	}

	public static byte[] formatReloadTime(Calendar c) {
		return new byte[]{Define.TX_TIME_RELOAD,(byte)(c.get(Calendar.YEAR)-2000),(byte)c.get(Calendar.MONTH),(byte)(c.get(Calendar.DAY_OF_MONTH)-1),
				(byte)(c.get(Calendar.HOUR_OF_DAY)),(byte)c.get(Calendar.MINUTE),(byte)c.get(Calendar.SECOND)};
	}

	public static byte[] formatReadTime() {
		return new byte[]{Define.TX_TIME_READ,0,0,0};
	}

	public static byte[] formatSaveDataPlace(int dataPlace) {
		return new byte[]{Define.TX_MEM_TO_ROM_PLACE,(byte)dataPlace,0,0};
	}

	public static byte[] formatFetchDataPlace(int dataPlace) {
		return new byte[]{Define.TX_MEMORY_FETCH_DATA,(byte)dataPlace,0,0};
	}

	public static byte[] formatSetParam(int param) {
		return new byte[]{Define.TX_PARAM_WRITE, LecHeaderParam.HEADER_PARAM_ORDER,1, (byte) param};
	}

	public static byte[] formatSetSeqTime(int order, int runTime) {
		return new byte[]{Define.TX_PARAM_WRITE, (byte) (LecHeaderParam.HEADER_SEQ_TIME_ORDER + order),1, (byte) runTime};
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


	public static int getDaysDifference(Date fromDate,Date toDate)
	{
		if(fromDate==null||toDate==null)
			return 0;

		return (int)( (toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
	}
}
