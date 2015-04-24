package com.syncworks.scriptdata;

import com.syncworks.define.Define;

/**
 * Created by vosami on 2015-04-22.
 */
public class ScriptNameData {
    public final static boolean DIR_ASSET = true;
    public final static boolean DIR_FILES = false;

    private boolean scriptType = Define.SINGLE_LED;
    private boolean dirOfFile = DIR_ASSET;
    private String nameOfFile = null;
    private String patternName = null;
    private String patternNameEn = null;
    public ScriptNameData() {
    }
    public ScriptNameData(boolean type, String name, String nameEn) {
        setType(type);
        setName(name);
        setNameEn(nameEn);
    }

    public void setType(boolean type) {
        this.scriptType = type;
    }

    public void setName(String name) {
        this.patternName = name;
    }

    public void setNameEn(String nameEn) {
        this.patternNameEn = nameEn;
    }

    public void setDirOfFile(boolean dir) {
        this.dirOfFile = dir;
    }

    public void setNameOfFile(String name) {
        this.nameOfFile = name;
    }

    public boolean getType() {
        return scriptType;
    }

    public String getName() {
        return patternName;
    }

    public String getNameEn() {
        return patternNameEn;
    }

    public String getNameOfFile() {
        return nameOfFile;
    }
    public boolean getDirType() {
        return dirOfFile;
    }
}
