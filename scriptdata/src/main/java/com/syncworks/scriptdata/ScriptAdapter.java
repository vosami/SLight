package com.syncworks.scriptdata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.syncworks.define.Define;

import java.util.List;

/**
 * Created with Android Studio
 * Copyrights (C)SyncWorks All rights reserved by SyncWorks
 * Created by 승현 on 2015-04-05.
 */
public class ScriptAdapter extends ArrayAdapter<ScriptData> {

	private List<ScriptData> objects;

	public ScriptAdapter(Context context, int resource) {
		this(context, resource, null);
	}

	public ScriptAdapter(Context context, int resource, List<ScriptData> objects) {
		super(context, resource, objects);
		this.objects = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			int val = objects.get(position).getVal();
			LayoutInflater inflater = LayoutInflater.from(getContext());
			if (val < Define.OP_CODE_MIN) {
				convertView = inflater.inflate(R.layout.rl_default,null);
			}
			else {
				switch (val) {
					case Define.OP_START:
						convertView = inflater.inflate(R.layout.rl_start,null);
						break;
					case Define.OP_END:
						convertView = inflater.inflate(R.layout.rl_end,null);
						break;
					default:
						convertView = inflater.inflate(R.layout.rl_default,null);
						break;
				}
			}
		}
		return convertView;
	}
}
