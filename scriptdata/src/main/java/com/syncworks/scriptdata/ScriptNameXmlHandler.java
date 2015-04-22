package com.syncworks.scriptdata;

import com.syncworks.define.Define;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by vosami on 2015-04-20.
 */
public class ScriptNameXmlHandler extends DefaultHandler {

    private ScriptNameData snData;

    /*private boolean scriptType = Define.SINGLE_LED;
    // 한글 패턴 설명
    private String patternName = null;
    // 영문 패턴 설명
    private String patternNameEn = null;*/

    private boolean elementOn = false;
    private String currentValue = null;

    public ScriptNameXmlHandler() {
        snData = new ScriptNameData();
    }

    public ScriptNameData getNameData() {
        return snData;
    }


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        elementOn = true;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (elementOn) {
            currentValue = new String(ch,start,length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        elementOn = false;
        // 스크립트 타입 설정 (Single / Color)
        if (qName.equalsIgnoreCase("ScriptType")) {
            if (currentValue.contains("Color") || currentValue.contains("color")) {
                snData.setType(Define.COLOR_LED);
            } else {
                snData.setType(Define.SINGLE_LED);
            }
        }

        else if (qName.equalsIgnoreCase("ScriptName")) {
            snData.setName(currentValue);
        }

        else if (qName.equalsIgnoreCase("ScriptNameEn")) {
            snData.setNameEn(currentValue);
        }
    }
}
