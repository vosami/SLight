package com.syncworks.define;

/**
 * Created with Android Studio
 * Copyrights (C)SyncWorks All rights reserved by SyncWorks
 * Created by 승현 on 2015-03-16.
 */
public class Define {
    // 블루투스 관련 설정
    public final static String BLE_ADDRESS = "BluetoothAddress";

	// 데이터 배열의 최대 길이는 64로 정함
	public final static int DATA_ARRAY_MAX = 64;

	// Smart Light Single LED 갯수
	public final static int NUMBER_OF_SINGLE_LED = 9;
	// Smart Light Color Led 갯수
	public final static int NUMBER_OF_COLOR_LED = 3;

	//
	public final static int SINGLE_LED_123_ACTIVATE =       1;
	public final static int SINGLE_LED_456_ACTIVATE =       2;
	public final static int SINGLE_LED_789_ACTIVATE =       4;
	public final static boolean SINGLE_LED =               	true;
	public final static boolean COLOR_LED =                	false;
    public final static int SELECTED_LED1 =       			0x0001;
    public final static int SELECTED_LED2 =       			0x0002;
    public final static int SELECTED_LED3 =       			0x0004;
    public final static int SELECTED_LED4 =       			0x0008;
    public final static int SELECTED_LED5 =       			0x0010;
    public final static int SELECTED_LED6 =       			0x0020;
    public final static int SELECTED_LED7 =       			0x0040;
    public final static int SELECTED_LED8 =       			0x0080;
    public final static int SELECTED_LED9 =       			0x0100;
    public final static int SELECTED_COLOR_LED1 =     		0x1007;
    public final static int SELECTED_COLOR_LED2 =     		0x2038;
    public final static int SELECTED_COLOR_LED3 =     		0x41C0;


	/**
	 * 명령어 리스트
	 */
    // 기본 밝기 명령어
    public final static int OP_BRIGHT =                     0x64;
	// 밝기 값은 OP_CODE_MIN 이하의 값을 가짐
	public final static int OP_CODE_MIN = 					0xC0;
	// 시작 - 시작 명령어(1Byte) + 유지 시간(1Byte)
	public final static int OP_START = 						0xC1;
	// 종료 - 종료 명령어(1Byte) + 유지 시간(1Byte)
	public final static int OP_END = 						0xC2;
	// 데이터(EEPROM) 불러오기 - 불러오기 명령어(1Byte) + 위치(1Byte)
	public final static int OP_FETCH = 						0xC3;
	// 랜덤 밝기 - 명령어(1Byte) + 기준 값(5bit) + 격차 값(3bit)
	public final static int OP_RANDOM_VAL = 				0xC5;
	// 랜덤 유지 시간 - 명령어(1Byte) + 기준 값(5bit) + 격차 값(3bit)
	public final static int OP_RANDOM_DELAY = 				0xC6;
	// 지연(밝기 변화 없음) - 명령어(1Byte) + 유지 시간(1Byte)
	public final static int OP_NOP = 						0xC7;
	// 긴 지연(밝기 변화 없음) - 명령어(1Byte) + 유지시간(1Byte) * 128
	public final static int OP_LONG_DELAY = 				0xC8;
	// 반복문 시작 - 명령어(1Byte) +
	public final static int OP_FOR_START = 					0xF0;
	// 반복문 종료 - 명령어(1Byte) + 반복횟수(4bit) + 카운트(4bit)
	public final static int OP_FOR_END = 					0xF1;
	// 점프(위험하므로 사용 안함) - 명령어(1Byte) + 점프 위치(1Byte)
	public final static int OP_JUMPTO = 					0xF4;
	// 다른 LED 로 데이터 전달
	public final static int OP_PASS_DATA = 					0xF5;
	// 페이드(4Byte) - 명령어(1Byte)
	public final static int OP_TRANSITION = 				0xF6;
	/**
	 * 지연 명령어
	 */
	public final static int DELAY_INFINITE = 				0xFFFF;

	/**
	 * PASS DATA 명령어
 	 */
	public final static int PASS_DATA_NONE =				0x00;
	public final static int PASS_DATA_INCREASE_INDEX = 		0x01;
	public final static int PASS_DATA_ENABLE = 				0x02;
	/*
	#define PASS_DATA_NONE                  0x00
			#define PASS_DATA_INCREASE_INDEX        0x01
			#define PASS_DATA_ENABLE                0x02*/


	public final static boolean SINGLE_SCRIPT = true;
	public final static boolean DOUBLE_SCRIPT = false;

	/**
	 * 송신 명령어
	 */
	public final static byte TX_MEMORY_WRITE = 0x60;
}
