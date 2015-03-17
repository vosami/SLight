package com.syncworks.scriptdata;

/**
 * Created with Android Studio
 * Copyrights (C)SyncWorks All rights reserved by SyncWorks
 * Created by 승현 on 2015-03-15.
 */
public class ScriptOperatorData {
	private String opTitle;
	private String opDetail;
	public ScriptOperatorData(String title, String detail) {
		opTitle = title;
		opDetail = detail;
	}

	public String getTitle() {
		return opTitle;
	}

	public String getDetail() {
		return opDetail;
	}
}
