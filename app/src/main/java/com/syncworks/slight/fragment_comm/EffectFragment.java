package com.syncworks.slight.fragment_comm;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.syncworks.define.Define;
import com.syncworks.slight.R;


public class EffectFragment extends Fragment {


    private LinearLayout llRGBArray, llSingleArray;
    private TextView tvRgb[] = new TextView[3];
    private TextView tvSingle[] = new TextView[9];

    private RadioButton rbEffect[] = new RadioButton[5];
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

        rbEffect[0].setOnClickListener(onClickListener);
        rbEffect[1].setOnClickListener(onClickListener);
        rbEffect[2].setOnClickListener(onClickListener);
        rbEffect[3].setOnClickListener(onClickListener);
        rbEffect[4].setOnClickListener(onClickListener);

        swEffect[1].setOnCheckedChangeListener(onCheckedChangeListener);
        swEffect[2].setOnCheckedChangeListener(onCheckedChangeListener);
        swEffect[3].setOnCheckedChangeListener(onCheckedChangeListener);

        if (isRGB) {
            displayRgb(paramRGB);
        } else {
            displaySingle(paramLED);
        }
    }

    private void displayRgb(int ledBit) {
        llSingleArray.setVisibility(View.GONE);
        llRGBArray.setVisibility(View.VISIBLE);
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
        llRGBArray.setVisibility(View.GONE);
        for (int i=0;i< Define.NUMBER_OF_SINGLE_LED;i++) {
            if (((ledBit >> i) & 0x01) == 1) {
                tvSingle[i].setVisibility(View.VISIBLE);
            } else {
                tvSingle[i].setVisibility(View.INVISIBLE);
            }
        }
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
                    break;
                case R.id.radio_effect_2:
                    mListener.onEffect(1,boolToInt(swEffect[1].isChecked()));
                    break;
                case R.id.radio_effect_3:
                    mListener.onEffect(2,boolToInt(swEffect[2].isChecked()));
                    break;
                case R.id.radio_effect_4:
                    mListener.onEffect(3,boolToInt(swEffect[3].isChecked()));
                    break;
                case R.id.radio_effect_5:
                    mListener.onEffect(4,0);
                    break;
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int param = 0;
            switch (buttonView.getId()) {
                case R.id.sw_effect_2:
                    if (rbEffect[1].isChecked()) {
                        mListener.onEffect(1,boolToInt(isChecked));
                    }
                    break;
                case R.id.sw_effect_3:
                    if (rbEffect[2].isChecked()) {
                        mListener.onEffect(2,boolToInt(isChecked));
                    }
                    break;
                case R.id.sw_effect_4:
                    if (rbEffect[3].isChecked()) {
                        mListener.onEffect(3,boolToInt(isChecked));
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

}
