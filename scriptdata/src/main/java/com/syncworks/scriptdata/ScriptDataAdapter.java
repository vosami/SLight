package com.syncworks.scriptdata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.syncworks.define.Define;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created with Android Studio
 * Copyrights (C)SyncWorks All rights reserved by SyncWorks
 * Created by 승현 on 2015-03-15.
 */
public class ScriptDataAdapter extends ArrayAdapter<ScriptData> {

	private List<ScriptData> objects;

	private DecimalFormat df = new DecimalFormat("#.##");

	public ScriptDataAdapter(Context context, int resource, List<ScriptData> objects) {
		super(context, resource, objects);
		// 생성자에서 받은 데이터를 현재 데이터로 적용
		this.objects = objects;
	}

	private View getLayout(int position) {
		View retView;
		// 명령어를 확인하여 레이아웃 결정
		int mVal = objects.get(position).getVal();
		int mDelay = objects.get(position).getDuration();

		TextView tvDelay, tvBright;
		float delayVal, brightVal;
		switch (mVal) {
			case Define.OP_START:
				retView = LayoutInflater.from(getContext()).inflate(R.layout.rl_start,null);
				tvDelay = (TextView)retView.findViewById(R.id.delay_val);
				delayVal = (float) ((1+mDelay) * 0.01);
				tvDelay.setText(df.format(delayVal));
				break;
			case Define.OP_END:
				retView = LayoutInflater.from(getContext()).inflate(R.layout.rl_end,null);
				tvDelay = (TextView)retView.findViewById(R.id.delay_val);
				delayVal = (float) ((1+mDelay) * 0.01);
				tvDelay.setText(df.format(delayVal));
				break;
			default:
				retView = LayoutInflater.from(getContext()).inflate(R.layout.rl_default,null);
				tvBright = (TextView) retView.findViewById(R.id.bright_val);
				brightVal = (float) (mVal*100/(Define.OP_CODE_MIN-1));
				tvBright.setText(df.format(brightVal));
				break;
		}
		return retView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = getLayout(position);
		}

		return convertView;
	}
}
