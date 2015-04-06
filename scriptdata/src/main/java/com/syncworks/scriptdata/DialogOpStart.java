package com.syncworks.scriptdata;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.syncworks.define.Define;

/**
 * Created with Android Studio
 * Copyrights (C)SyncWorks All rights reserved by SyncWorks
 * Created by 승현 on 2015-04-06.
 */
public class DialogOpStart extends Dialog {

	private int opCode;
	private int duration;
	private Button btnConfirm, btnCancel;
	private TextView tvDuration;
	private SeekBar sbDuration;

	DialogScriptInterface mListener = null;

	public DialogOpStart(Context context) {
		super(context);
	}

	public DialogOpStart(Context context, int opCode, int duration) {
		super(context);
		this.opCode = opCode;
		this.duration = duration;
	}

	public void setListener(DialogScriptInterface lis) {
		mListener = lis;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
		lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		lpWindow.dimAmount = 0.8f;
		getWindow().setAttributes(lpWindow);
		if (opCode == Define.OP_START) {
			setTitle(getContext().getResources().getString(R.string.op_start_txt));
		} else {
			setTitle(getContext().getResources().getString(R.string.op_end_txt));
		}
		setContentView(R.layout.dialog_op_start);
		btnConfirm = (Button) findViewById(R.id.btn_confirm);
		btnCancel = (Button) findViewById(R.id.btn_cancel);
		btnConfirm.setOnClickListener(clickListener);
		btnCancel.setOnClickListener(clickListener);

		tvDuration = (TextView) findViewById(R.id.tv_duration_val);
		tvDuration.setText(getDurationStr(duration));
		sbDuration = (SeekBar) findViewById(R.id.sb_duration_val);
		sbDuration.setOnSeekBarChangeListener(sbListener);
	}

	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int i = v.getId();
			if (i == R.id.btn_confirm) {
				mListener.setScript(opCode,duration);
				dismiss();
			} else if (i == R.id.btn_cancel) {
				cancel();
			}
		}
	};

	private SeekBar.OnSeekBarChangeListener sbListener = new SeekBar.OnSeekBarChangeListener() {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			duration = progress;
			tvDuration.setText(getDurationStr(duration));
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {

		}
	};

	private String getDurationStr(int duration) {
		float sec = (float) (duration<<3) / 100;
		String mStr = String.format("%.2f",sec);
		if (duration == 0xFF) {
			mStr = "무한대";
		}
		return mStr;
	}
}
