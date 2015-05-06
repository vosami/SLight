package com.syncworks.endlessadapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vosami on 2015-05-06.
 */
public class HttpListData {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private int writeCount;
    private String writer;
    private String patternName;
    private int patternType;
    private String description;
    private Date date;

    public HttpListData() {
        writeCount = 0;
        writer = "no name";
        patternName = "no pattern";
        patternType = 0;
        description = "no description";
        date = new Date();
        try {
            date = dateFormat.parse("2015-05-01");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getWriteCount() {
        return writeCount;
    }

    public String getWriter() {
        return writer;
    }

    public String getPatternName() {
        return patternName;
    }

    public int getPatternType() {
        return patternType;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public void setWriteCount(int _writeCount) {
        this.writeCount = _writeCount;
    }

    public void setWriter(String _writer) {
        this.writer = _writer;
    }

    public void setPatternName(String _patternName) {
        this.patternName = _patternName;
    }

    public void setPatternType(int _patternType) {
        this.patternType = _patternType;
    }

    public void setDescription(String _description) {
        this.description = _description;
    }

    public void setDate(Date _date) {
        this.date = _date;
    }
}
