package com.syncworks.slight.fragment_easy;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.ToggleButton;

import com.syncworks.define.Define;
import com.syncworks.define.Logger;
import com.syncworks.leddata.LedSelect;
import com.syncworks.slight.R;
import com.syncworks.slightpref.SLightPref;


public class EffectFragment extends Fragment {

    SLightPref appPref;

    private LedSelect ledSelect;
    private OnEasyFragmentListener mListener;

    private Button btnRgb[] = new Button[Define.NUMBER_OF_COLOR_LED];
    private Button btnLed[] = new Button[Define.NUMBER_OF_SINGLE_LED];

    private RadioButton rbPattern[] = new RadioButton[5];
    private Button btnSetStartTime;
    private ToggleButton tbDelay[] = new ToggleButton[3];
    private ToggleButton tbRandom[] = new ToggleButton[2];

    private boolean isDelayLong[] = new boolean[5];
    private boolean isRandom[] = new boolean[5];
    private int alwaysStartTime;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EffectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EffectFragment newInstance() {
        return new EffectFragment();
    }

    public EffectFragment() {
        // Required empty public constructor
    }

    public void setLedSelect(LedSelect ls) {
        this.ledSelect = ls;
    }

    public void init() {
        isDelayLong[0] = false;
        isDelayLong[1] = false;
        isDelayLong[2] = false;
        isDelayLong[3] = false;
        isDelayLong[4] = false;
        isRandom[0] = false;
        isRandom[1] = false;
        isRandom[2] = false;
        isRandom[3] = false;
        isRandom[4] = false;
        alwaysStartTime = 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        Logger.d(this,"EffectFragment create");
    }

    @Override
    public void onStart() {
        super.onStart();
        displaySet();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        appPref = new SLightPref(getActivity());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_effect,container,false);

        btnRgb[0] = (Button) view.findViewById(R.id.rgb_1);
        btnRgb[1] = (Button) view.findViewById(R.id.rgb_2);
        btnRgb[2] = (Button) view.findViewById(R.id.rgb_3);
        btnLed[0] = (Button) view.findViewById(R.id.led_1);
        btnLed[1] = (Button) view.findViewById(R.id.led_2);
        btnLed[2] = (Button) view.findViewById(R.id.led_3);
        btnLed[3] = (Button) view.findViewById(R.id.led_4);
        btnLed[4] = (Button) view.findViewById(R.id.led_5);
        btnLed[5] = (Button) view.findViewById(R.id.led_6);
        btnLed[6] = (Button) view.findViewById(R.id.led_7);
        btnLed[7] = (Button) view.findViewById(R.id.led_8);
        btnLed[8] = (Button) view.findViewById(R.id.led_9);

        rbPattern[0] = (RadioButton) view.findViewById(R.id.rb_pattern_1);
        rbPattern[1] = (RadioButton) view.findViewById(R.id.rb_pattern_2);
        rbPattern[2] = (RadioButton) view.findViewById(R.id.rb_pattern_3);
        rbPattern[3] = (RadioButton) view.findViewById(R.id.rb_pattern_4);
        rbPattern[4] = (RadioButton) view.findViewById(R.id.rb_pattern_5);
        btnSetStartTime = (Button) view.findViewById(R.id.btn_set_start_time);
        tbDelay[0] = (ToggleButton) view.findViewById(R.id.tb_long_2);
        tbDelay[1] = (ToggleButton) view.findViewById(R.id.tb_long_3);
        tbDelay[2] = (ToggleButton) view.findViewById(R.id.tb_long_4);
        tbRandom[0] = (ToggleButton) view.findViewById(R.id.tb_random_2);
        tbRandom[1] = (ToggleButton) view.findViewById(R.id.tb_random_3);

        rbPattern[0].setOnClickListener(onClickListener);
        rbPattern[1].setOnClickListener(onClickListener);
        rbPattern[2].setOnClickListener(onClickListener);
        rbPattern[3].setOnClickListener(onClickListener);
        rbPattern[4].setOnClickListener(onClickListener);
        btnSetStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 시작 시간 대화창 표시
            }
        });
        tbDelay[0].setOnClickListener(onOptionListener);
        tbDelay[1].setOnClickListener(onOptionListener);
        tbDelay[2].setOnClickListener(onOptionListener);
        tbRandom[0].setOnClickListener(onOptionListener);
        tbRandom[1].setOnClickListener(onOptionListener);

        if (!appPref.getBoolean(SLightPref.EASY_ACTIVITY[3])) {
            appPref.putBoolean(SLightPref.EASY_ACTIVITY[3],true);
            showOverLay();
        }

        // Inflate the layout for this fragment
        return view;
    }
    // 패턴 선택 리스너
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rb_pattern_1:
                    rbClick(0);
                    break;
                case R.id.rb_pattern_2:
                    rbClick(1);
                    break;
                case R.id.rb_pattern_3:
                    rbClick(2);
                    break;
                case R.id.rb_pattern_4:
                    rbClick(3);
                    break;
                case R.id.rb_pattern_5:
                    rbClick(4);
                    break;
            }
        }
    };

    // 옵션 리스너
    private ToggleButton.OnClickListener onOptionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tb_long_2:
                    isDelayLong[1] = tbDelay[0].isChecked();
                    if (rbPattern[1].isChecked()) {
                        doPattern(1,tbDelay[0].isChecked(),tbRandom[0].isChecked(),0);
                    }
                    break;
                case R.id.tb_long_3:
                    isDelayLong[2] = tbDelay[1].isChecked();
                    if (rbPattern[2].isChecked()) {
                        doPattern(2,tbDelay[1].isChecked(),tbRandom[1].isChecked(),0);
                    }
                    break;
                case R.id.tb_long_4:
                    isDelayLong[3] = tbDelay[2].isChecked();
                    if (rbPattern[3].isChecked()) {
                        doPattern(3,tbDelay[2].isChecked(),false,0);
                    }
                    break;
                case R.id.tb_random_2:
                    isRandom[1] = tbRandom[0].isChecked();
                    if (rbPattern[1].isChecked()) {
                        doPattern(1,tbDelay[0].isChecked(),tbRandom[0].isChecked(),0);
                    }
                    break;
                case R.id.tb_random_3:
                    isRandom[2] = tbRandom[1].isChecked();
                    if (rbPattern[2].isChecked()) {
                        doPattern(2,tbDelay[1].isChecked(),tbRandom[1].isChecked(),0);
                    }
                    break;
            }
        }
    };

    // 라디오 버튼 클릭
    private void rbClick(int pattern) {
        for (int i=0;i<5;i++) {
            if (i == pattern) {
                rbPattern[i].setChecked(true);
                doPattern(i);
            } else {
                rbPattern[i].setChecked(false);
            }
        }
    }

    private void showRgb(int i) {
        btnLed[i*3].setVisibility(View.GONE);
        btnLed[i*3 +1].setVisibility(View.GONE);
        btnLed[i*3 +2].setVisibility(View.GONE);
        btnRgb[i].setVisibility(View.VISIBLE);
    }

    private void showLed(int i) {
        int cLedNum = (i*3) / 3;
        btnRgb[i/3].setVisibility(View.GONE);
        for (int k=0;k<3;k++) {
            if (i == cLedNum + k) {
                btnLed[cLedNum].setVisibility(View.VISIBLE);
            } else if (btnLed[cLedNum].getVisibility() == View.GONE){
                btnLed[cLedNum].setVisibility(View.INVISIBLE);
            }
        }
    }

    private void displaySet() {
        if (ledSelect != null) {
            for (int i=0;i<Define.NUMBER_OF_COLOR_LED;i++) {
                if (ledSelect.getRgb(i) == LedSelect.SelectType.SELECTED) {
                    showRgb(i);
                }
            }

            for (int i=0;i<Define.NUMBER_OF_SINGLE_LED;i++) {
                if (ledSelect.getLed(i) == LedSelect.SelectType.SELECTED) {
                    showLed(i);
                }
            }
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnEasyFragmentListener) activity;
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

    // EasyActivity 에 패턴 전달
    private void doPattern(int effect) {
        if (mListener != null) {
            mListener.onEffect(effect, isDelayLong[effect], isRandom[effect], 0);
        }
    }

    private void doPattern(int effect, boolean isDelay, boolean isRandom, int startTime) {
        if (mListener != null) {
            mListener.onEffect(effect,isDelay, isRandom, startTime);
        }
    }

    public void showOverLay() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_help_ble_set);
        dialog.setCanceledOnTouchOutside(true);
        //for dismissing anywhere you touch
        View masterView = dialog.findViewById(R.id.overlay_help);
        masterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
