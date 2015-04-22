package com.syncworks.scriptdata;

import com.syncworks.define.Define;

/**
 * Created by vosami on 2015-04-22.
 */
public class ScriptNameData {
    private boolean scriptType = Define.SINGLE_LED;
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

    public boolean getType() {
        return scriptType;
    }

    public String getName() {
        return patternName;
    }

    public String getNameEn() {
        return patternNameEn;
    }
}
