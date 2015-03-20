package com.syncworks.scriptdata;

import android.util.Log;

import com.syncworks.define.Define;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vosami on 2015-03-20.
 * XML 파일에서 스크립트 데이터 추출
 */
public class ScriptXmlHandler extends DefaultHandler {
    private final static String TAG = ScriptXmlHandler.class.getSimpleName();
    // 스크립트 표시 순서
    private int scriptListOrder = 0;
    // 스크립트 타입
    private boolean scriptListType = Define.SINGLE_LED;
    // 스크립트 데이터
    List<List<ScriptData>> scriptDataList = new ArrayList<>();
    private int curListPos = 0;
    // XML 오류 확인용
    boolean elementOn = false;
    // 임시 데이터
    private int bright = 0;
    private int duration = 0;
    private int data1 = 0;
    private int data2 = 0;

    String currentValue = "";

    public ScriptXmlHandler() {

    }

    // 해당 스크립트 리스트의 순서 획득
    public int getScriptListOrder() {
        return scriptListOrder;
    }
    // 해당 스크립트 리스트의 데이터 획득
    public List<ScriptData> getScriptList(int selListPos) {
        if (scriptDataList.size() > selListPos) {
            return scriptDataList.get(selListPos);
        }
        return scriptDataList.get(0);
    }


    // 태그가 시작될 때
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        elementOn = true;
        if (qName.equals("ScriptDataList")) {
            scriptDataList.add(new ArrayList<ScriptData>());
        } else if (qName.equals("ScriptData")) {
            bright = 0;
            duration = 0;
            data1 = 0;
            data2 = 0;
        }
    }
    // 태그 안에 값 확인
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (elementOn) {
            currentValue = new String(ch, start, length);
        }
    }
    // 태그가 종료될 때
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        elementOn = false;
        // 스크립트가 표시되는 순서 설정(이름 설정)
        if (qName.equalsIgnoreCase("ScriptOrder")) {
            scriptListOrder = Integer.parseInt(currentValue);
        }
        // 스크립트 타입 설정 (Single / Color)
        else if (qName.equalsIgnoreCase("ScriptType")) {
            if (currentValue.contains("Color") || currentValue.contains("color")) {
                scriptListType = Define.COLOR_LED;
            } else {
                scriptListType = Define.SINGLE_LED;
            }
        }
        // 스크립트 리스트 추가
        else if (qName.equalsIgnoreCase("ScriptDataList")) {
            curListPos++;
        }
        // 스크립트 데이터 추가
        else if (qName.equalsIgnoreCase("ScriptData")) {
            scriptDataList.get(curListPos).add(getScriptData());
            Log.d(TAG,"ScriptData:"+bright+","+duration);
        }
        // 밝기 값 설정
        else if (qName.equalsIgnoreCase("ScriptBright")) {
            bright = getBright(currentValue);
        }
        // 지연 값 설정
        else if (qName.equalsIgnoreCase("ScriptDuration")) {
            duration = Integer.parseInt(currentValue);
        }
        // 추가 데이터 1 설정
        else if (qName.equalsIgnoreCase("ScriptData1")) {
            data1 = Integer.parseInt(currentValue);
        }
        // 추가 데이터 2 설정
        else if (qName.equalsIgnoreCase("ScriptData2")) {
            data2 = Integer.parseInt(currentValue);
        }
        // 변수 초기화
        currentValue = null;
    }

    // 2바이트 데이터인지 4바이트 데이터인지 확인
    private ScriptData getScriptData() {
        // 4Byte 데이터라면
        if (bright == Define.OP_TRANSITION) {
            return new ScriptData(bright, duration, data1, data2);
        }
        // 2Byte 데이터라면
        return new ScriptData(bright,duration);
    }

    // 명령어 확인
    private int getBright(String str) {
        int retInt;
        if (str.contains("OP")) {
            switch (str) {
                case "OP_START":
                    retInt = Define.OP_START;
                    break;
                case "OP_END":
                    retInt = Define.OP_END;
                    break;
                case "OP_FETCH":
                    retInt = Define.OP_FETCH;
                    break;
                case "OP_RANDOM_VAL":
                    retInt = Define.OP_RANDOM_VAL;
                    break;
                case "OP_RANDOM_DELAY":
                    retInt = Define.OP_RANDOM_DELAY;
                    break;
                case "OP_NOP":
                    retInt = Define.OP_NOP;
                    break;
                case "OP_LONG_DELAY":
                    retInt = Define.OP_LONG_DELAY;
                    break;
                case "OP_FOR_START":
                    retInt = Define.OP_FOR_START;
                    break;
                case "OP_FOR_END":
                    retInt = Define.OP_FOR_END;
                    break;
                case "OP_JUMPTO":
                    retInt = Define.OP_JUMPTO;
                    break;
                case "OP_PASS_DATA":
                    retInt = Define.OP_PASS_DATA;
                    break;
                case "OP_TRANSITION":
                    retInt = Define.OP_TRANSITION;
                    break;
                default:
                    retInt = Define.OP_NOP;
                    break;
            }
        } else {
            retInt = Integer.parseInt(str);
        }
        return retInt;
    }
}
