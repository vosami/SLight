package com.syncworks.scriptdata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created with Android Studio
 * Copyrights (C)SyncWorks All rights reserved by SyncWorks
 * Created by 승현 on 2015-03-15.
 */
public class ScriptOperatorAdapter extends ArrayAdapter<ScriptOperatorData>{

	private List<ScriptOperatorData> objects;

	public ScriptOperatorAdapter(Context context, int resource,  List<ScriptOperatorData> objects) {
		super(context, resource, objects);
		this.objects = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.li_operator,null);
			((TextView)(convertView.findViewById(R.id.tv_title_operator))).setText(
					objects.get(position).getTitle());
			((TextView)(convertView.findViewById(R.id.tv_detail_operator))).setText(
					objects.get(position).getDetail());
		}

		return convertView;
	}
}
