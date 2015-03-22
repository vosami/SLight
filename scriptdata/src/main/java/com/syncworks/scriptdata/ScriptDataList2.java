package com.syncworks.scriptdata;

import android.util.Log;

import com.syncworks.define.Define;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio
 * Copyrights (C)SyncWorks All rights reserved by SyncWorks
 * Created by 승현 on 2015-03-19.
 */
public class ScriptDataList2 {
	// 태그 데이터
	private final static String TAG = ScriptDataList2.class.getSimpleName();

	private List<ScriptData> scriptDataList;
	private int ledNumber = 0;

	public ScriptDataList2(int ledNum) {
		scriptDataList = new ArrayList<>();
		// 시작 명령어
		scriptDataList.add(new ScriptData(Define.OP_START,0));
		scriptDataList.add(new ScriptData(Define.OP_END,0));
		ledNumber = ledNum;
	}

	public int getSize() {
		int dataSize = 0;
		for (int i=0;i<scriptDataList.size();i++){
			// 1열(2Byte) 스크립트라면 사이즈를 1 추가
			if (scriptDataList.get(i).getSize() == Define.SINGLE_SCRIPT) {
				dataSize++;
			}
			// 2열(4Byte) 스크립트라면 사이즈를 2 추가
			else {
				dataSize = dataSize + 2;
			}
		}
		return dataSize;
	}

	public void addScriptData(ScriptData scriptdata) {
		int size = getSize();
		// 단일 스크립트라면...
		if (scriptdata.getSize() == Define.SINGLE_SCRIPT && size < Define.DATA_ARRAY_MAX) {
			scriptDataList.add(scriptDataList.size()-1,scriptdata);
		}
		// 더블 스크립트라면...
		else if (scriptdata.getSize() == Define.DOUBLE_SCRIPT && size < Define.DATA_ARRAY_MAX-1) {
			scriptDataList.add(scriptDataList.size()-1,scriptdata);
		} else {
			Log.d(TAG,"ScriptData Overload");
		}
	}

	public ScriptData getScriptData(int position) {
		return scriptDataList.get(position);
	}

	public void setScriptDataList(List<ScriptData> dataList) {
		scriptDataList = dataList;
	}

	public int getStartPosition() {
		int startPos = 0;
		for (int i=0;i<scriptDataList.size();i++) {
			if (scriptDataList.get(i).getVal() == Define.OP_START) {
				startPos = i;
			}
            if (scriptDataList.get(i).getVal() == Define.OP_END) {
                break;
            }
		}
		return startPos;
	}

	public int getEndPosition() {
		int endPos = 0;
		for (int i=0;i<scriptDataList.size();i++) {
			if (scriptDataList.get(i).getVal() == Define.OP_END) {
				endPos = i;
				break;
			}
		}
		return endPos;
	}
}
