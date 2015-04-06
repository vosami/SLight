package com.syncworks.scriptdata;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ListView;

import com.syncworks.define.Define;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vosami on 2015-04-06.
 */
public class DialogAddScriptData extends Dialog{

    public DialogAddScriptData(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_add_script_data);
        List<Integer> opList = new ArrayList<>();
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
    }

}
