package com.syncworks.scriptdata;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.syncworks.define.Define;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vosami on 2015-04-06.
 */
public class DialogAddScriptData extends Dialog{
	private final static String TAG = DialogAddScriptData.class.getSimpleName();

	private DialogScriptInterface mListener = null;
	List<Integer> opList = null;

    public DialogAddScriptData(Context context) {
        super(context);
    }

	public void setAddScriptListener(DialogScriptInterface dsi) {
		this.mListener = dsi;
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_add_script_data);
        opList = new ArrayList<>();
        opList.add(Define.OP_BRIGHT);
        opList.add(Define.OP_START);
        opList.add(Define.OP_END);
        opList.add(Define.OP_RANDOM_VAL);
        opList.add(Define.OP_RANDOM_DELAY);
        opList.add(Define.OP_NOP);
        opList.add(Define.OP_FOR_START);
        opList.add(Define.OP_FOR_END);
        opList.add(Define.OP_PASS_DATA);
        opList.add(Define.OP_TRANSITION);
        String dialogTitle = getContext().getResources().getString(R.string.dialog_script_operator_title);
        setTitle(dialogTitle);

        ListView lv = (ListView) findViewById(R.id.list_script_operator);
        ScriptOperatorAdapter adapter = new ScriptOperatorAdapter(getContext(),0,opList);
        lv.setAdapter(adapter);
		lv.setOnItemClickListener(listener);
    }

	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Log.d(TAG,"OnItemClickListener"+ position);
			switch (opList.get(position)) {
				case Define.OP_BRIGHT:
					DialogOpBright dialogOpBright = new DialogOpBright(getContext(),0,0);
					dialogOpBright.setListener(dsi);
					dialogOpBright.show();
					break;
				case Define.OP_START:
					DialogOpStart dialogOpStart = new DialogOpStart(getContext(),Define.OP_START,0);
					dialogOpStart.setListener(dsi);
					dialogOpStart.show();
					break;
				case Define.OP_END:
					DialogOpStart dialogOpEnd = new DialogOpStart(getContext(),Define.OP_END,0);
					dialogOpEnd.setListener(dsi);
					dialogOpEnd.show();
					break;
				case Define.OP_RANDOM_VAL:
					break;
				case Define.OP_RANDOM_DELAY:
					break;
				case Define.OP_NOP:
					break;
				case Define.OP_FOR_START:
					break;
				case Define.OP_PASS_DATA:
					break;
				case Define.OP_FOR_END:
					break;
				case Define.OP_TRANSITION:
					break;
			}
		}
	};

	private DialogScriptInterface dsi = new DialogScriptInterface() {

		@Override
		public void setScript(int val, int duration) {
//			Log.d(TAG,"val"+val+", dur"+duration);
			if (mListener != null) {
				mListener.addScript(val, duration);
			}
			dismiss();
		}

		@Override
		public void addScript(int val, int duration) {

		}
	};

}
