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
public class DialogOpBright extends Dialog{

	int bright = 0;
	int duration = 0;

	Button btnConfirm, btnCancel;
	SeekBar sbBright, sbDuration;
	TextView tvBright, tvDuration;

	DialogScriptInterface mListener = null;

	public DialogOpBright(Context context) {
		super(context);
		bright = 0;
		duration = 0;
	}

	public DialogOpBright(Context context, int bright, int duration) {
		super(context);
		this.bright = bright;
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
		setTitle(getContext().getResources().getString(R.string.op_default_txt));
		setContentView(R.layout.dialog_op_bright);
		btnConfirm = (Button) findViewById(R.id.btn_confirm);
		btnCancel = (Button) findViewById(R.id.btn_cancel);
		btnConfirm.setOnClickListener(clickListener);
		btnCancel.setOnClickListener(clickListener);
		tvBright = (TextView) findViewById(R.id.tv_bright_val);
		tvDuration = (TextView) findViewById(R.id.tv_duration_val);
		tvBright.setText(getBrightStr(bright));
		tvDuration.setText(getDurationStr(duration));

		sbBright = (SeekBar) findViewById(R.id.sb_bright_val);
		sbDuration = (SeekBar) findViewById(R.id.sb_duration_val);
		sbBright.setOnSeekBarChangeListener(sbListener);
		sbDuration.setOnSeekBarChangeListener(sbListener);
	}

	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int i = v.getId();
			if (i == R.id.btn_confirm) {
				if (mListener != null) {
					mListener.setScript(bright,duration);
				}
				dismiss();
			} else if (i == R.id.btn_cancel) {
				cancel();
			}
		}
	};

	private SeekBar.OnSeekBarChangeListener sbListener = new SeekBar.OnSeekBarChangeListener() {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			if (seekBar.getId() == R.id.sb_bright_val) {
				bright = progress;
				tvBright.setText(getBrightStr(bright));
			} else {
				duration = progress;
				tvDuration.setText(getDurationStr(duration));
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {

		}
	};

	private String getBrightStr(int bright) {
		float brightPercent = (float) bright / (Define.OP_CODE_MIN-1) * 100;
		return String.format("%.2f",brightPercent);
	}

	private String getDurationStr(int duration) {
		float sec = (float) duration / 100;
		String mStr = String.format("%.2f",sec);
		if (duration == 0xFF) {
			mStr = "무한대";
		}
		return mStr;
	}
}
