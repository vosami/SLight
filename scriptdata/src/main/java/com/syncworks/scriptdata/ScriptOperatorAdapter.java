package com.syncworks.scriptdata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.syncworks.define.Define;

import java.util.List;

/**
 * Created with Android Studio
 * Copyrights (C)SyncWorks All rights reserved by SyncWorks
 * Created by 승현 on 2015-03-15.
 */
public class ScriptOperatorAdapter extends ArrayAdapter<Integer>{

	private List<Integer> objects;

	public ScriptOperatorAdapter(Context context, int resource,  List<Integer> objects) {
		super(context, resource, objects);
		this.objects = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        String mTitle = null;
        String mDetail = null;
        int mScript;
		convertView = LayoutInflater.from(getContext()).inflate(R.layout.li_operator,null);
		mScript = objects.get(position);
		switch (mScript) {
			case Define.OP_BRIGHT:
				mTitle = getContext().getResources().getString(R.string.op_default_txt);
				mDetail = getContext().getResources().getString(R.string.op_default_detail);
				break;
			case Define.OP_START:
				mTitle = getContext().getResources().getString(R.string.op_start_txt);
				mDetail = getContext().getResources().getString(R.string.op_start_detail);
				break;
			case Define.OP_END:
				mTitle = getContext().getResources().getString(R.string.op_end_txt);
				mDetail = getContext().getResources().getString(R.string.op_end_detail);
				break;
			case Define.OP_RANDOM_VAL:
				mTitle = getContext().getResources().getString(R.string.op_random_val_txt);
				mDetail = getContext().getResources().getString(R.string.op_random_val_detail);
				break;
			case Define.OP_RANDOM_DELAY:
				mTitle = getContext().getResources().getString(R.string.op_random_delay_txt);
				mDetail = getContext().getResources().getString(R.string.op_random_delay_detail);
				break;
			case Define.OP_NOP:
				mTitle = getContext().getResources().getString(R.string.op_nop_txt);
				mDetail = getContext().getResources().getString(R.string.op_nop_detail);
				break;
			case Define.OP_FOR_START:
				mTitle = getContext().getResources().getString(R.string.op_for_start_txt);
				mDetail = getContext().getResources().getString(R.string.op_for_start_detail);
				break;
			case Define.OP_FOR_END:
				mTitle = getContext().getResources().getString(R.string.op_for_end_txt);
				mDetail = getContext().getResources().getString(R.string.op_for_end_detail);
				break;
			case Define.OP_PASS_DATA:
				mTitle = getContext().getResources().getString(R.string.op_pass_data_txt);
				mDetail = getContext().getResources().getString(R.string.op_pass_data_detail);
				break;
			case Define.OP_TRANSITION:
				mTitle = getContext().getResources().getString(R.string.op_transition_txt);
				mDetail = getContext().getResources().getString(R.string.op_transition_detail);
				break;
			default:
				mTitle = getContext().getResources().getString(R.string.op_default_txt);
				mDetail = getContext().getResources().getString(R.string.op_default_detail);
				break;
		}
		((TextView)(convertView.findViewById(R.id.tv_title_operator))).setText(
				mTitle);
		((TextView)(convertView.findViewById(R.id.tv_detail_operator))).setText(
				mDetail);


		return convertView;
	}
}
