package com.syncworks.slight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.syncworks.slight.R;


/**
 * Created by Kim on 2015-06-15.
 * Color Select Dialog
 */
public class DialogColorSelect extends Dialog {

    private OnColorSelectListener onColorSelectListener = null;

    private Button btnColor[] = new Button[8];

    public DialogColorSelect(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
        setTitle("칼라선택");
        setContentView(R.layout.dialog_color_select);
        btnColor[0] = (Button) findViewById(R.id.btn_color_white);
        btnColor[1] = (Button) findViewById(R.id.btn_color_red);
        btnColor[2] = (Button) findViewById(R.id.btn_color_orange);
        btnColor[3] = (Button) findViewById(R.id.btn_color_yellow);
        btnColor[4] = (Button) findViewById(R.id.btn_color_green);
        btnColor[5] = (Button) findViewById(R.id.btn_color_blue);
        btnColor[6] = (Button) findViewById(R.id.btn_color_purple);
        btnColor[7] = (Button) findViewById(R.id.btn_color_pink);

        btnColor[0].setOnClickListener(onClickListener);
        btnColor[1].setOnClickListener(onClickListener);
        btnColor[2].setOnClickListener(onClickListener);
        btnColor[3].setOnClickListener(onClickListener);
        btnColor[4].setOnClickListener(onClickListener);
        btnColor[5].setOnClickListener(onClickListener);
        btnColor[6].setOnClickListener(onClickListener);
        btnColor[7].setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_color_white:
                    doColorSelect(R.color.White);
                    break;
                case R.id.btn_color_red:
                    doColorSelect(R.color.easy_color_0_red);
                    break;
                case R.id.btn_color_orange:
                    doColorSelect(R.color.easy_color_1_orange);
                    break;
                case R.id.btn_color_yellow:
                    doColorSelect(R.color.easy_color_2_yellow);
                    break;
                case R.id.btn_color_green:
                    doColorSelect(R.color.easy_color_3_green);
                    break;
                case R.id.btn_color_blue:
                    doColorSelect(R.color.easy_color_4_blue);
                    break;
                case R.id.btn_color_purple:
                    doColorSelect(R.color.easy_color_6_purple);
                    break;
                case R.id.btn_color_pink:
                    doColorSelect(R.color.easy_color_9_pink);
                    break;
            }

        }
    };

    public interface OnColorSelectListener {
        void onColorSelect(int color);
    }

    private void doColorSelect(int color) {
        if (onColorSelectListener != null) {
            onColorSelectListener.onColorSelect(color);
        }
    }

    public void setOnColorSelectListener (OnColorSelectListener listener) {
        onColorSelectListener = listener;
    }
}
