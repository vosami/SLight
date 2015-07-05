package com.syncworks.slight.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


import com.syncworks.slight.R;

import org.w3c.dom.Text;

/**
 * Created by Kim on 2015-06-28.
 * 패턴 변경 확인 다이알로그
 */
public class DialogChangePattern extends Dialog {

    private TextView tvDescription;
    private Button btnCancel, btnConfirm;
    private String strDescription;

    public DialogChangePattern(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
        setTitle("효과 변경 확인");
        setContentView(R.layout.dialog_change_pattern);
        tvDescription = (TextView) findViewById(R.id.change_patten_description);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);

        btnCancel.setOnClickListener(listener);
        btnConfirm.setOnClickListener(listener);

        tvDescription.setText(strDescription);
    }



    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_cancel:

                    break;
                case R.id.btn_confirm:
                    break;
            }
        }
    };


}
