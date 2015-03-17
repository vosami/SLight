package com.syncworks.scriptdata;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created with Android Studio
 * Copyrights (C)SyncWorks All rights reserved by SyncWorks
 * Created by 승현 on 2015-03-15.
 */
public class ScriptOperatorDialog extends Dialog{
	private Context mContext;
	private ListView listViewOperator;

	ScriptOperateListener mListener;

	public ScriptOperatorDialog(Context context) {
		super(context);
		setContentView(R.layout.dialog_script_operator);
		mContext = context;
		listViewOperator = (ListView) findViewById(R.id.listview_script_operator);
		String txtTitle = context.getResources().getString(R.string.dialog_script_operator_title);
		setTitle(txtTitle);

		final ArrayList<ScriptOperatorData> mListItem = new ArrayList<>();
		Resources r = context.getResources();
		mListItem.add(new ScriptOperatorData(
				r.getString(R.string.op_default_txt),
				r.getString(R.string.op_default_detail)));
		mListItem.add(new ScriptOperatorData(
				r.getString(R.string.op_start_txt),
				r.getString(R.string.op_default_detail)));
		mListItem.add(new ScriptOperatorData(
				r.getString(R.string.op_end_txt),
				r.getString(R.string.op_default_detail)));
		mListItem.add(new ScriptOperatorData(
				r.getString(R.string.op_default_txt),
				r.getString(R.string.op_default_detail)));
		mListItem.add(new ScriptOperatorData(
				r.getString(R.string.op_default_txt),
				r.getString(R.string.op_default_detail)));
		mListItem.add(new ScriptOperatorData(
				r.getString(R.string.op_default_txt),
				r.getString(R.string.op_default_detail)));

		ScriptOperatorAdapter adapter = new ScriptOperatorAdapter(context,0,mListItem);

		listViewOperator.setAdapter(adapter);
		listViewOperator.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mListener.onSetOperateListener(position);
			}
		});

	}



	public void setOnScriptOperateListener(ScriptOperateListener listener) {
		mListener = listener;
	}

	public interface ScriptOperateListener {
		public void onSetOperateListener(int position);
	}
}
