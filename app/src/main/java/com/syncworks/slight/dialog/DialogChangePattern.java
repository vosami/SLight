package com.syncworks.slight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.syncworks.slight.R;

/**
 * Created by Kim on 2015-06-28.
 * 패턴 변경 확인 다이알로그
 */
public class DialogChangePattern extends Dialog {


    private ChangePatternListener cpListener;
    private TextView tvDescription;
    private Button btnCancel, btnConfirm;
    private boolean ledType;
    private int ledNum;

    public DialogChangePattern(Context context) {
        super(context);
    }
    public DialogChangePattern(Context context, boolean type, int ledNum) {
        super(context);
        this.ledType = type;
        this.ledNum = ledNum;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
        setTitle(getContext().getString(R.string.easy_led_select_tv_title_notify));
        setContentView(R.layout.dialog_change_pattern);
        tvDescription = (TextView) findViewById(R.id.change_patten_description);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);

        btnCancel.setOnClickListener(listener);
        btnConfirm.setOnClickListener(listener);

        tvDescription.setText(getContext().getString(R.string.easy_led_select_tv_cancel_notify));
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_cancel:
                    doCancel();
                    break;
                case R.id.btn_confirm:
                    doConfirm();
                    break;
            }
        }
    };

    public interface ChangePatternListener {
        public void onConfirm(boolean type, int ledNum);
        public void onCancel();
    }

    public void setOnChangePatternListener(ChangePatternListener listener) {
        cpListener = listener;
    }

    private void doConfirm() {
        if (cpListener != null) {
            cpListener.onConfirm(this.ledType, this.ledNum);
        }
    }

    private void doCancel() {
        if (cpListener != null) {
            cpListener.onCancel();
        }
    }


}
