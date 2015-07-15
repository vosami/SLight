package com.syncworks.slight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.syncworks.colorpickerview.ColorPickerView;
import com.syncworks.slight.R;


/**
 * Created by Kim on 2015-06-15.
 * Color Select Dialog
 */
public class DialogColorSelect extends Dialog {

    private OnColorSelectListener onColorSelectListener = null;

    private Button btnColor[] = new Button[8];
    private Button btnConfirm;
    private ColorPickerView colorPickerView;
    private int selLedNum = 0;
    private int color = 0;

    public DialogColorSelect(Context context) {
        super(context);
    }

    public DialogColorSelect(Context context, int ledNum) {
        super(context);
        selLedNum = ledNum;
    }

    public void setSelLedNum(int ledNum) {
        selLedNum = ledNum;
    }

    public int getSelLedNum() {
        return selLedNum;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
        setTitle(getContext().getString(R.string.easy_led_select_btn_color_select));
        setContentView(R.layout.dialog_color_select);
        btnColor[0] = (Button) findViewById(R.id.btn_color_white);
        btnColor[1] = (Button) findViewById(R.id.btn_color_red);
        btnColor[2] = (Button) findViewById(R.id.btn_color_orange);
        btnColor[3] = (Button) findViewById(R.id.btn_color_yellow);
        btnColor[4] = (Button) findViewById(R.id.btn_color_green);
        btnColor[5] = (Button) findViewById(R.id.btn_color_blue);
        btnColor[6] = (Button) findViewById(R.id.btn_color_purple);
        btnColor[7] = (Button) findViewById(R.id.btn_color_pink);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        colorPickerView = (ColorPickerView) findViewById(R.id.color_picker);
        colorPickerView.setColor(color);

        btnColor[0].setOnClickListener(onClickListener);
        btnColor[1].setOnClickListener(onClickListener);
        btnColor[2].setOnClickListener(onClickListener);
        btnColor[3].setOnClickListener(onClickListener);
        btnColor[4].setOnClickListener(onClickListener);
        btnColor[5].setOnClickListener(onClickListener);
        btnColor[6].setOnClickListener(onClickListener);
        btnColor[7].setOnClickListener(onClickListener);
        btnConfirm.setOnClickListener(onClickListener);
        colorPickerView.setOnColorChangedListener(new ColorPickerView.OnColorChangedListener() {
            @Override
            public void onColorChanged(int newColor) {
                doColorSelect(newColor);
            }
        });
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_color_white:
                    colorPickerView.setColor(getContext().getResources().getColor(R.color.White));
                    doColorSelect(R.color.White);
                    break;
                case R.id.btn_color_red:
                    colorPickerView.setColor(getContext().getResources().getColor(R.color.easy_color_0_red));
                    doColorSelect(R.color.easy_color_0_red);
                    break;
                case R.id.btn_color_orange:
                    colorPickerView.setColor(getContext().getResources().getColor(R.color.easy_color_1_orange));
                    doColorSelect(R.color.easy_color_1_orange);
                    break;
                case R.id.btn_color_yellow:
                    colorPickerView.setColor(getContext().getResources().getColor(R.color.easy_color_2_yellow));
                    doColorSelect(R.color.easy_color_2_yellow);
                    break;
                case R.id.btn_color_green:
                    colorPickerView.setColor(getContext().getResources().getColor(R.color.easy_color_3_green));
                    doColorSelect(R.color.easy_color_3_green);
                    break;
                case R.id.btn_color_blue:
                    colorPickerView.setColor(getContext().getResources().getColor(R.color.easy_color_4_blue));
                    doColorSelect(R.color.easy_color_4_blue);
                    break;
                case R.id.btn_color_purple:
                    colorPickerView.setColor(getContext().getResources().getColor(R.color.easy_color_6_purple));
                    doColorSelect(R.color.easy_color_6_purple);
                    break;
                case R.id.btn_color_pink:
                    colorPickerView.setColor(getContext().getResources().getColor(R.color.easy_color_9_pink));
                    doColorSelect(R.color.easy_color_9_pink);
                    break;
                case R.id.btn_confirm:
                    doConfirm();
                    break;
            }
        }
    };

    public interface OnColorSelectListener {
        void onColorSelect(int color);
        void onConfirm();
    }

    private void doColorSelect(int color) {
        if (onColorSelectListener != null) {
            onColorSelectListener.onColorSelect(color);
        }
    }
    private void doConfirm() {
        if (onColorSelectListener != null) {
            onColorSelectListener.onConfirm();
        }
    }

    public void setOnColorSelectListener (OnColorSelectListener listener) {
        onColorSelectListener = listener;
    }

    public void setColor(int color) {
        if (colorPickerView != null) {
            colorPickerView.setColor(color);
        } else {
            this.color = color;
        }
    }
}
