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
    public final static int DEFINED_PATTERN =               1;
    public final static int CUSTOM_PATTERN =                0;
    public final static int SINGLE_PATTERN =                0;
    public final static int RED_PATTERN =                   1;
    public final static int GREEN_PATTERN =                 2;
    public final static int BLUE_PATTERN =                  3;
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
     * 랜덤 값의 간격 값
     */
    public final static int RANDOM_GAP_1 =                  0x00;
    public final static int RANDOM_GAP_2 =                  0x01;
    public final static int RANDOM_GAP_4 =                  0x02;
    public final static int RANDOM_GAP_8 =                  0x03;
    public final static int RANDOM_GAP_16 =                 0x04;
    public final static int RANDOM_GAP_32 =                 0x05;
    public final static int RANDOM_GAP_64 =                 0x06;
    public final static int RANDOM_GAP_128 =                0x07;

    public final static int OP_MACRO_MIN_VAL =              0xFF00;
    public final static int OP_MACRO_MAX_VAL =              0xFF01;
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
    // 변수 값으로 밝기 설정 - 명령어(1Byte) + 변수 선택(2bit,i,a,b,c) + 유지시간(6bit)
    public final static int OP_VAR_VAL =                    0xC9;
    public final static int OP_SOUND_VAL =                  0xCA;
    public final static int OP_RANDOM_VAR_C =               0xCB;

    // LED 패턴 설정 스크립트
    // 0~127:사용자 정의 패턴, 128~191:사전 설정 단색 패턴, 192~255:사전 설정 RGB 패턴
    public final static int OP_HEAD_PATTERN =               0xCD;
    // 옵션 설정, 3bit:reserved, 1bit:랜덤 On/Off, 4bit: 효과 시간 설정
    public final static int OP_HEAD_OPTION =                0xCE;
    // 사전 딜레이 설정, 밝기 0, 딜레이 *10-1
    public final static int OP_HEAD_START_DELAY =           0xCF;
    // 변수 A 초기화
    public final static int OP_PUT_VAR_A =                  0xD0;
    // 변수 B 초기화
    public final static int OP_PUT_VAR_B =                  0xD1;
    // 변수 C 초기화
    public final static int OP_PUT_VAR_C =                  0xD2;
    // 변수 연산 - 명령어(1Byte) + 데이터(2bit,A,B,C,for i)연산(2bit,+,-,*,/) + 상수(4bit)
    public final static int OP_CALC_VAR_A =                 0xD5;
    public final static int OP_CALC_VAR_B =                 0xD6;
    public final static int OP_CALC_VAR_C =                 0xD7;

    public final static int OP_SIN_SHIFT =                  0xDA;
    public final static int OP_SIN_COUNT =                  0xDB;
    public final static int OP_SIN_START =                  0xDC;
    //
    public final static int OP_PUT_MSP =                    0xE0;
    public final static int OP_GET_MSP =                    0xE1;
    public final static int OP_PUT_SENSOR =                 0xE2;
    public final static int OP_GET_SENSOR =                 0xE3;

    public final static int OP_TRANS_BRIGHT_START =         0xEA;
    public final static int OP_TRANS_BRIGHT_STOP =          0xEB;
    public final static int OP_TRANS_BRIGHT_COUNT =         0xEC;
    public final static int OP_TRANS_START =                0xED;

	// 반복문 시작 - 명령어(1Byte) + 반복횟수(8bit)
	public final static int OP_FOR_START = 					0xF0;
	// 반복문 종료 - 명령어(1Byte) + Reserved(8bit)
	public final static int OP_FOR_END = 					0xF1;
    // 조건문 - 명령어(1Byte) + 변수1(2bit, a,b,c) + 비교문(2bit, =,>,<) + 변수2(2bit, a,b,c,
    public final static int OP_IF =                         0xF2;
    public final static int OP_ELSE =                       0xF3;
    public final static int OP_END_IF =                     0xF4;
	// 점프(위험하므로 사용 안함) - 명령어(1Byte) + 점프 위치(1Byte)
	public final static int OP_JUMPTO = 					0xF5;

    public final static int OP_PASS_DATA =                  0xF6;

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

    /**
     * VAR DATA 명령어
     */
    public final static int DATA_A =                    0x00;
    public final static int DATA_B =                    0x01;
    public final static int DATA_C =                    0x02;
    public final static int DATA_FOR_I =                0x03;
    // 연산자
    public final static int DATA_PLUS =                 0x00;
    public final static int DATA_MINUS =                0x01;
    public final static int DATA_MULT =                 0x02;
    public final static int DATA_DIV =                  0x03;
    // 비교 연산자
    public final static int DATA_EQUAL =                0x00;
    public final static int DATA_LARGE =                0x01;
    public final static int DATA_SMALL =                0x02;
    public final static int DATA_NOT_EQUAL =            0x03;


	public final static boolean SINGLE_SCRIPT = true;
	public final static boolean DOUBLE_SCRIPT = false;

	/**
	 * 송신 명령어
	 */
    // 이름 정보 기록
    public final static byte TX_NAME_CHANGE =                       0x30;
    // EEPROM 에 데이터 기록(0:명령어, 1~2:주소, 3:길이, 4~19:기록데이터)
    public final static byte TX_EEPROM_WRITE = 0x40;
    // EEPROM 의 데이터 읽음(0:명령어, 1~2:주소, 3:길이)
    public final static byte TX_EEPROM_READ = 0x41;
    // 카운트 초기화(0:명령어, 1~3:Reserved)
    public final static byte TX_INIT_COUNT = 0x42;
    // RAM 의 점멸 패턴을 EEPROM 에 기록
    public final static byte TX_MEM_TO_ROM = 0x43;
    public final static byte TX_MEM_TO_ROM_EACH = 0x44;
    public final static byte TX_MEM_TO_ROM_PLACE = 0x45;
    public final static byte TX_MEM_TO_ROM_COMPLETE = 0x46;
    // I2C Write (0:명령어, 1:Device Address, 2:속도, 3~4:주소, 5:길이, 6~19:기록 데이터)
    public final static byte TX_I2C_WRITE = 0x50;
    // I2C Write (0:명령어, 1:Device Address, 2:속도, 3~4:주소, 5:길이)
    public final static byte TX_I2C_READ = 0x51;
    // LEC_Header 파라미터 읽기
    public final static byte TX_PARAM_READ = 0x57;
    // LEC_Header 파라미터 쓰기
    public final static byte TX_PARAM_WRITE = 0x58;
    // Acknowledge
    public final static byte TX_ACK = 0x5A;
    // 점멸 패턴 기록 (0:명령어, 1:제어할 LED 번호, 2:시작 번지, 3:길이, 4~19:기록데이터)
	public final static byte TX_MEMORY_WRITE = 0x60;
    // 점멸 패턴 읽어오기 (0:명령어, 1:제어할 LED 번호, 2:시작 번지, 3:길이)
    public final static byte TX_MEMORY_READ = 0x61;
    public final static byte TX_SOUND_VAL = 0x62;
    public final static byte TX_MEMORY_WRITE_ACK = 0x63;
    public final static byte TX_MEMORY_PATTERN_ALL = 0x64;
    public final static byte TX_MEMORY_FETCH_DATA = 0x65;
    public final static byte TX_MEMORY_FETCH_COMPLETE = 0x66;
    // 현재 시간 재설정 (0:명령어, 1~4:현재시간)
    public final static byte TX_TIME_RELOAD = 0x70;
    // 현재 시간 읽어오기 (0:명령어, 1~3:Reserved)
    public final static byte TX_TIME_READ = 0x71;
    // 알람 시간 설정 (0:명령어, 1:선택 알람, 2:요일, 3:시간, 4:분, 5:유지시간-0이면 OFF)
    public final static byte TX_ALARM_WRITE = (byte) 0x72;
    // EEPROM 초기화 (0:명령어, 1~3:Reserved)
    public final static byte TX_INIT_SET = (byte) 0x80;
    // 스마트라이트 재우기
    public final static byte TX_WAKE_UP = (byte) 0x90;
    // 타이머 멈추기
    public final static byte TX_MINUTE_TIMER_START = (byte) 0x91;

	/**
	 * 디렉토리 관련
	 */
	public final static boolean DIR_ASSET = true;
	public final static boolean DIR_FILES = false;
	public final static String FILE_DEFAULT = "scriptdata0.xml";
	public final static String FILE_COLOR_DEFAULT = "scriptcolordata0.xml";


}
