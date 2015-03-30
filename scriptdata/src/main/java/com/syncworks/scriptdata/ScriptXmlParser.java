package com.syncworks.scriptdata;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
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
}
