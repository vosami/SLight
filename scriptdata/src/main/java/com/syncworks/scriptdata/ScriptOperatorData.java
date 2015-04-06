package com.syncworks.scriptdata;

import com.syncworks.define.Define;

/**
 * Created with Android Studio
 * Copyrights (C)SyncWorks All rights reserved by SyncWorks
 * Created by 승현 on 2015-03-15.
 */
public class ScriptOperatorData {
    private int opScript;
	private String opDetail;
	public ScriptOperatorData(int op, String detail) {
        opScript = op;
		opDetail = detail;
	}

	public int getScript() {
		return opScript;
	}

    public String getTitle() {
        String retStr = null;
        switch(opScript) {
            case Define.OP_BRIGHT:

                break;
            case Define.OP_START:
                break;
            case Define.OP_END:
                break;
            case Define.OP_RANDOM_VAL:
                break;
            case Define.OP_RANDOM_DELAY:
                break;
            case Define.OP_NOP:
                break;
            case Define.OP_FOR_START:
                break;
            case Define.OP_FOR_END:
                break;
            case Define.OP_TRANSITION:
                break;
        }
        return retStr;
    }

	public String getDetail() {
		return opDetail;
	}
}
