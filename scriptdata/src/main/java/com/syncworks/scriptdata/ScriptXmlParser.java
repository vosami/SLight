package com.syncworks.scriptdata;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by vosami on 2015-03-20.
 */
public class ScriptXmlParser {
    public static ScriptDataList parse(InputStream is, int ledNum) {
        ScriptDataList scriptDataList = null;
        try {
            // SAXParser 에서 XML Reader 생성
            XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            // SAXXMLHandler 생성
            ScriptXmlHandler scriptXmlHandler = new ScriptXmlHandler();
            // XML Reader 에 Handler 설정
            xmlReader.setContentHandler(scriptXmlHandler);
            // XML Parse 시작
            xmlReader.parse(new InputSource(is));
            // XML 데이터 저장
            scriptDataList = scriptXmlHandler.getScriptList(ledNum);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scriptDataList;
    }

    public static List<ScriptDataList> parseColor(InputStream is, int ledNum) {
        List<ScriptDataList> scriptDataList = null;
        try {
            // SAXParser 에서 XML Reader 생성
            XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            // SAXXMLHandler 생성
            ScriptXmlHandler scriptXmlHandler = new ScriptXmlHandler();
            // XML Reader 에 Handler 설정
            xmlReader.setContentHandler(scriptXmlHandler);
            // XML Parse 시작
            xmlReader.parse(new InputSource(is));
            // XML 데이터 저장
            scriptDataList = scriptXmlHandler.getScriptLists(ledNum);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scriptDataList;
    }

    public static ScriptNameData parseName(InputStream is) {
        ScriptNameData snData = null;

        try {
            // SAXParser 에서 XML Reader 생성
            XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            // 핸들러 생성
            ScriptNameXmlHandler scriptNameXmlHandler = new ScriptNameXmlHandler();
            // XML 리더에 핸들러 설정
            xmlReader.setContentHandler(scriptNameXmlHandler);
            // XML Parse 시작
            xmlReader.parse(new InputSource(is));
            // 이름 데이터 가져오기
            snData = scriptNameXmlHandler.getNameData();

        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return snData;
    }

    public static ScriptNameData testParse(File source) {
        ScriptNameData scriptNameData = null;
        // SAXParser 에서 XML Reader 생성
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            // 핸들러 생성
            ScriptNameXmlHandler scriptNameXmlHandler = new ScriptNameXmlHandler();
            saxParser.parse(source,scriptNameXmlHandler);
            scriptNameData = scriptNameXmlHandler.getNameData();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scriptNameData;
    }

}
