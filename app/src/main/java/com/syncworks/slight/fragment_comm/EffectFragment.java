package com.syncworks.slight.fragment_comm;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.syncworks.define.Define;
import com.syncworks.slight.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class EffectFragment extends Fragment {

    private boolean isInit = false;
    private LinearLayout llRgbEffect, llSingleEffect;
    private TextView tvRgbEffect, tvSingleEffect;

    private LinearLayout llRGBArray, llSingleArray;
    private TextView tvRgb[] = new TextView[3];
    private TextView tvSingle[] = new TextView[9];

    private RadioButton rbEffect[] = new RadioButton[5];
    private RadioButton rbRgbEffect[] = new RadioButton[2];
    private Button btnSelectColor;
    private Switch swRgbEffect[] = new Switch[1];
    private TextView tvRGBEffect[] = new TextView[1];
    private Switch swEffect[] = new Switch[5];
    private TextView tvEffect[] = new TextView[5];

    private OnCommFragmentListener mListener;

    // 상단 LED 선택 뷰 파라미터
    private boolean isRGB = false;
    private int paramRGB = 0;
    private int paramLED = 0;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EffectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EffectFragment newInstance() {
        EffectFragment fragment = new EffectFragment();
        return fragment;
    }

    public EffectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_effect, container, false);
        findViews(view);
        // Inflate the layout for this fragment
        return view;
    }

    private void findViews(View v) {
        llRgbEffect = (LinearLayout) v.findViewById(R.id.ll_rgb_select_effect);
        llSingleEffect = (LinearLayout) v.findViewById(R.id.ll_select_effect);
        tvRgbEffect = (TextView) v.findViewById(R.id.tv_title_rgb_select_effect);
        tvSingleEffect = (TextView) v.findViewById(R.id.tv_title_select_effect);

        llRGBArray = (LinearLayout) v.findViewById(R.id.ll_effect_rgb_array);
        llSingleArray = (LinearLayout) v.findViewById(R.id.ll_effect_bright_array);
        tvRgb[0] = (TextView) v.findViewById(R.id.effect_rgb_1);
        tvRgb[1] = (TextView) v.findViewById(R.id.effect_rgb_2);
        tvRgb[2] = (TextView) v.findViewById(R.id.effect_rgb_3);

        tvSingle[0] = (TextView) v.findViewById(R.id.effect_bright_1);
        tvSingle[1] = (TextView) v.findViewById(R.id.effect_bright_2);
        tvSingle[2] = (TextView) v.findViewById(R.id.effect_bright_3);
        tvSingle[3] = (TextView) v.findViewById(R.id.effect_bright_4);
        tvSingle[4] = (TextView) v.findViewById(R.id.effect_bright_5);
        tvSingle[5] = (TextView) v.findViewById(R.id.effect_bright_6);
        tvSingle[6] = (TextView) v.findViewById(R.id.effect_bright_7);
        tvSingle[7] = (TextView) v.findViewById(R.id.effect_bright_8);
        tvSingle[8] = (TextView) v.findViewById(R.id.effect_bright_9);

        rbEffect[0] = (RadioButton) v.findViewById(R.id.radio_effect_1);
        rbEffect[1] = (RadioButton) v.findViewById(R.id.radio_effect_2);
        rbEffect[2] = (RadioButton) v.findViewById(R.id.radio_effect_3);
        rbEffect[3] = (RadioButton) v.findViewById(R.id.radio_effect_4);
        rbEffect[4] = (RadioButton) v.findViewById(R.id.radio_effect_5);

        rbRgbEffect[0] = (RadioButton) v.findViewById(R.id.radio_rgb_effect_1);
        rbRgbEffect[1] = (RadioButton) v.findViewById(R.id.radio_rgb_effect_2);

        swEffect[0] = null;
        swEffect[1] = (Switch) v.findViewById(R.id.sw_effect_2);
        swEffect[2] = (Switch) v.findViewById(R.id.sw_effect_3);
        swEffect[3] = (Switch) v.findViewById(R.id.sw_effect_4);
        swEffect[4] = null;

        tvEffect[0] = null;
        tvEffect[1] = (TextView) v.findViewById(R.id.tv_effect_2);
        tvEffect[2] = (TextView) v.findViewById(R.id.tv_effect_3);
        tvEffect[3] = (TextView) v.findViewById(R.id.tv_effect_4);
        tvEffect[4] = null;

        btnSelectColor = (Button) v.findViewById(R.id.btn_select_color);
        swRgbEffect[0] = (Switch) v.findViewById(R.id.sw_rgb_effect_2);
        tvRGBEffect[0] = (TextView) v.findViewById(R.id.tv_rgb_effect_2);

        rbEffect[0].setOnClickListener(onClickListener);
        rbEffect[1].setOnClickListener(onClickListener);
        rbEffect[2].setOnClickListener(onClickListener);
        rbEffect[3].setOnClickListener(onClickListener);
        rbEffect[4].setOnClickListener(onClickListener);

        rbRgbEffect[0].setOnClickListener(onClickListener);
        rbRgbEffect[1].setOnClickListener(onClickListener);
        btnSelectColor.setOnClickListener(onClickListener);

        swEffect[1].setOnCheckedChangeListener(onCheckedChangeListener);
        swEffect[2].setOnCheckedChangeListener(onCheckedChangeListener);
        swEffect[3].setOnCheckedChangeListener(onCheckedChangeListener);

        swRgbEffect[0].setOnCheckedChangeListener(onCheckedChangeListener);



        if (isRGB) {
            displayRgb(paramRGB);
        } else {
            displaySingle(paramLED);
        }
    }

    private void initUI() {
        swEffect[1].setChecked(false);
        swEffect[2].setChecked(false);
        swEffect[3].setChecked(false);
        tvEffect[1].setText(getResources().getString(R.string.tv_select_2_1));
        tvEffect[2].setText(getResources().getString(R.string.tv_select_3_1));
        tvEffect[3].setText(getResources().getString(R.string.tv_select_3_1));
        rbEffect[0].setChecked(true);
        rbEffect[1].setChecked(false);
        rbEffect[2].setChecked(false);
        rbEffect[3].setChecked(false);
        rbEffect[4].setChecked(false);

        swRgbEffect[0].setChecked(false);
        tvRGBEffect[0].setText(getString(R.string.tv_select_3_1));
        rbRgbEffect[0].setChecked(true);
        rbRgbEffect[1].setChecked(false);
    }

    private void displayRgb(int ledBit) {
        llSingleArray.setVisibility(View.GONE);
        llSingleEffect.setVisibility(View.GONE);
        tvSingleEffect.setVisibility(View.GONE);
        llRGBArray.setVisibility(View.VISIBLE);
        llRgbEffect.setVisibility(View.VISIBLE);
        tvRgbEffect.setVisibility(View.VISIBLE);

        for (int i=0;i< Define.NUMBER_OF_COLOR_LED;i++) {
            if (((ledBit >> i) & 0x01) == 1) {
                tvRgb[i].setVisibility(View.VISIBLE);
            } else {
                tvRgb[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    private void displaySingle(int ledBit) {
        llSingleArray.setVisibility(View.VISIBLE);
        llSingleEffect.setVisibility(View.VISIBLE);
        tvSingleEffect.setVisibility(View.VISIBLE);
        llRGBArray.setVisibility(View.GONE);
        llRgbEffect.setVisibility(View.GONE);
        tvRgbEffect.setVisibility(View.GONE);

        for (int i=0;i< Define.NUMBER_OF_SINGLE_LED;i++) {
            if (((ledBit >> i) & 0x01) == 1) {
                tvSingle[i].setVisibility(View.VISIBLE);
            } else {
                tvSingle[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    public void setInit() {
        isInit = true;
    }

    public void setRGB(boolean isRGB) {
        this.isRGB = isRGB;
    }

    public void setParamRGB(int paramRGB) {
        this.paramRGB = paramRGB;
    }

    public void setParamLED(int paramLed) {
        this.paramLED = paramLed;
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.radio_effect_1:
                    mListener.onEffect(0,0);
                    setRadioButton(0);
                    break;
                case R.id.radio_effect_2:
                    mListener.onEffect(1,boolToInt(swEffect[1].isChecked()));
                    setRadioButton(1);
                    break;
                case R.id.radio_effect_3:
                    mListener.onEffect(2,boolToInt(swEffect[2].isChecked()));
                    setRadioButton(2);
                    break;
                case R.id.radio_effect_4:
                    mListener.onEffect(3,boolToInt(swEffect[3].isChecked()));
                    setRadioButton(3);
                    break;
                case R.id.radio_effect_5:
                    mListener.onEffect(4,0);
                    setRadioButton(4);
                    break;
                case R.id.radio_rgb_effect_1:
                    mListener.onEffect(0,0);
                    setRgbRadioButton(0);
                    break;
                case R.id.radio_rgb_effect_2:
                    mListener.onEffect(1,boolToInt(swRgbEffect[0].isChecked()));
                    setRgbRadioButton(1);
                    break;
                case R.id.btn_select_color:
                    if (rbRgbEffect[0].isChecked()) {
                        mListener.onColorDialog();
                    } else {
                        mListener.onNotDialog();
                    }
                    break;
            }
        }
    };

    private void setRadioButton(int radioNum) {
        for (int i=0;i<5;i++) {
            if (i != radioNum) {
                rbEffect[i].setChecked(false);
            }
        }
    }

    private void setRgbRadioButton(int radioNum) {
        for (int i=0;i<2;i++) {
            if (i != radioNum) {
                rbRgbEffect[i].setChecked(false);
            }
        }
    }

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Log.d("onCheckedChanged","isChecked"+isChecked);
            switch (buttonView.getId()) {
                case R.id.sw_effect_2:
                    if (rbEffect[1].isChecked()) {
                        if (swEffect[1].isChecked()) {
                            mListener.onEffect(1, boolToInt(isChecked));
                            tvEffect[1].setText(getResources().getString(R.string.tv_select_2_2));
                        } else {
                            tvEffect[1].setText(getResources().getString(R.string.tv_select_2_1));
                            mListener.onEffect(1, boolToInt(isChecked));
                        }
                    }
                    break;
                case R.id.sw_effect_3:
                    if (rbEffect[2].isChecked()) {
                        if (swEffect[2].isChecked()) {
                            mListener.onEffect(2, boolToInt(isChecked));
                            tvEffect[2].setText(getResources().getString(R.string.tv_select_3_2));
                        } else {
                            tvEffect[2].setText(getResources().getString(R.string.tv_select_3_1));
                            mListener.onEffect(2, boolToInt(isChecked));
                        }
                    }
                    break;
                case R.id.sw_effect_4:
                    if (rbEffect[3].isChecked()) {
                        if (swEffect[3].isChecked()) {
                            mListener.onEffect(3, boolToInt(isChecked));
                            tvEffect[3].setText(getResources().getString(R.string.tv_select_3_2));
                        } else {
                            tvEffect[3].setText(getResources().getString(R.string.tv_select_3_1));
                            mListener.onEffect(3, boolToInt(isChecked));
                        }
                    }
                    break;
                case R.id.sw_rgb_effect_2:
                    if (rbRgbEffect[1].isChecked()) {
                        if (swRgbEffect[0].isChecked()) {
                            mListener.onEffect(1,boolToInt(isChecked));
                            tvRGBEffect[0].setText(getString(R.string.tv_select_3_2));
                        } else {
                            mListener.onEffect(1,boolToInt(isChecked));
                            tvRGBEffect[0].setText(getString(R.string.tv_select_3_1));
                        }
                    }
                    break;
            }
        }
    };

    private int boolToInt(boolean isChecked) {
        if (isChecked) {
            return 1;
        }
        return 0;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isInit) {
            isInit = false;
            initUI();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnCommFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    // 항상 켜기
    public byte[] getEffectByte1(int bright) {
        byte[] retBytes = new byte[6];
        retBytes[0] = (byte) Define.OP_START;
        retBytes[1] = 0;
        retBytes[2] = (byte) (191 * bright * 0.01);
        retBytes[3] = 0;
        retBytes[4] = (byte) Define.OP_END;
        retBytes[5] = 0;
        return retBytes;
    }
    // 한번 깜박이기
    public byte[] getEffectByte2(int bright, int param) {
        byte[] retBytes = new byte[8];
        retBytes[0] = (byte) Define.OP_START;
        retBytes[1] = 0;
        retBytes[2] = 0;
        retBytes[3] = 50;
        retBytes[4] = 0;
        retBytes[5] = 48;
        retBytes[6] = (byte) Define.OP_END;
        retBytes[7] = 0;
        if (param == 0) {
            retBytes[2] = (byte) (191 * bright * 0.01);
            retBytes[4] = 0;
        } else {
            retBytes[2] = 0;
            retBytes[4] = (byte) (191 * bright * 0.01);
        }
        return retBytes;
    }
    // 짧게 반짝이기
    public byte[] getEffectByte3(int bright, int param) {
        byte[] retBytes = new byte[8];
        retBytes[0] = (byte) Define.OP_START;
        retBytes[1] = 0;
        retBytes[2] = 0;
        if (param == 0) {
            retBytes[3] = (byte)200;
        } else {
            retBytes[3] = (byte)100;
        }
        retBytes[4] = (byte) (191*bright*0.01);
        retBytes[5] = 0;
        retBytes[6] = (byte) Define.OP_END;
        retBytes[7] = 0;
        return retBytes;
    }
    // 밝아졌다가 어두워지기
    public byte[] getEffectByte4(int bright, int param) {
        byte[] retBytes = new byte[34];
        retBytes[0] = (byte) Define.OP_START;
        retBytes[1] = 0;
        retBytes[2] = (byte) (20*bright*0.01);
        retBytes[3] = (byte) (10 - param * 5);
        retBytes[4] = (byte) (35*bright*0.01);
        retBytes[5] = (byte) (10 - param * 5);
        retBytes[6] = (byte) (50*bright*0.01);
        retBytes[7] = (byte) (10 - param * 5);
        retBytes[8] = (byte) (75*bright*0.01);
        retBytes[9] = (byte) (10 - param * 5);
        retBytes[10] = (byte) (100*bright*0.01);
        retBytes[11] = (byte) (10 - param * 5);
        retBytes[12] = (byte) (130*bright*0.01);
        retBytes[13] = (byte) (10 - param * 5);
        retBytes[14] = (byte) (160*bright*0.01);
        retBytes[15] = (byte) (10 - param * 5);
        retBytes[16] = (byte) (191*bright*0.01);
        retBytes[17] = (byte) (10 - param * 5);
        retBytes[18] = (byte) (160*bright*0.01);
        retBytes[19] = (byte) (10 - param * 5);
        retBytes[20] = (byte) (130*bright*0.01);
        retBytes[21] = (byte) (10 - param * 5);
        retBytes[22] = (byte) (100*bright*0.01);
        retBytes[23] = (byte) (10 - param * 5);
        retBytes[24] = (byte) (75*bright*0.01);
        retBytes[25] = (byte) (10 - param * 5);
        retBytes[26] = (byte) (50*bright*0.01);
        retBytes[27] = (byte) (10 - param * 5);
        retBytes[28] = (byte) (35*bright*0.01);
        retBytes[29] = (byte) (10 - param * 5);
        retBytes[30] = (byte) (20*bright*0.01);
        retBytes[31] = (byte) (10 - param * 5);
        retBytes[32] = 0;
        retBytes[33] = (byte) (10 - param * 5);
        retBytes[32] = (byte) Define.OP_END;
        retBytes[33] = 0;

        return retBytes;
    }

    public byte[] getEffectByte5(int bright) {
        byte[] retBytes = new byte[8];
        retBytes[0] = (byte) Define.OP_START;
        retBytes[1] = 0;
        retBytes[2] = (byte) Define.OP_RANDOM_VAL;
        int ref = bright / 3;
        if (ref > 31) {
            ref = 31;
        }
        ref = (ref << 3) & 0xF8;
        retBytes[3] = (byte) (ref | 0x06);
        retBytes[4] = (byte) Define.OP_NOP;
        retBytes[5] = 2;
        retBytes[6] = (byte) Define.OP_END;
        retBytes[7] = 0;

        return retBytes;
    }

    public List<byte[]> getRgbEffect1(int color, int bright) {
        List<byte[]> data = new ArrayList<>();
        byte red = (byte) (((color>>16) & 0xFF) * bright * 0.01);
        byte green = (byte) (((color>>8) & 0xFF) * bright * 0.01);
        byte blue = (byte) (((color) & 0xFF) * bright * 0.01);
        byte[] redData = {(byte)Define.OP_START,0,red,0,(byte)Define.OP_END,0};
        byte[] greenData = {(byte)Define.OP_START,0,green,0,(byte)Define.OP_END,0};
        byte[] blueData = {(byte)Define.OP_START,0,blue,0,(byte)Define.OP_END,0};
        data.add(redData);
        data.add(greenData);
        data.add(blueData);
        return data;
    }

    public List<byte[]> getRgbEffect2(int bright, int param) {
        List<byte[]> data = new ArrayList<>();
        byte[] redData = {(byte)Define.OP_START,0,(byte)(191*bright*0.01),(byte)(100-50*param),0,(byte)(100-50*param),0,(byte)(98-50*param),(byte)Define.OP_END,0};
        byte[] greenData = {(byte)Define.OP_START,0,0,(byte)(100-50*param),(byte)(191*bright*0.01),(byte)(100-50*param),0,(byte)(98-50*param),(byte)Define.OP_END,0};
        byte[] blueData = {(byte)Define.OP_START,0,0,(byte)(100-50*param),0,(byte)(100-50*param),(byte)(191*bright*0.01),(byte)(98-50*param),(byte)Define.OP_END,0};
        data.add(redData);
        data.add(greenData);
        data.add(blueData);
        return data;
    }

}
