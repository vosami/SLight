package com.syncworks.slight.fragment_easy;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.syncworks.define.Define;
import com.syncworks.leddata.LedSelect;
import com.syncworks.slight.R;
import com.syncworks.slight.widget.CustomVerticalSeekBar;


public class BrightFragment extends Fragment {

    LedSelect ledSelect = null;

    Button btnLed[] = new Button[Define.NUMBER_OF_SINGLE_LED];
    Button btnRgb[] = new Button[Define.NUMBER_OF_COLOR_LED];
    CustomVerticalSeekBar seekLed[] = new CustomVerticalSeekBar[Define.NUMBER_OF_SINGLE_LED];
    CustomVerticalSeekBar seekRgb[] = new CustomVerticalSeekBar[Define.NUMBER_OF_SINGLE_LED];
    RelativeLayout rlLed[] = new RelativeLayout[Define.NUMBER_OF_SINGLE_LED];
    RelativeLayout rlRgb[] = new RelativeLayout[Define.NUMBER_OF_COLOR_LED];
    TextView tvLed[] = new TextView[Define.NUMBER_OF_SINGLE_LED];
    TextView tvRgb[] = new TextView[Define.NUMBER_OF_SINGLE_LED];
    Button btnColorSelect[] = new Button[Define.NUMBER_OF_COLOR_LED];

    private OnEasyFragmentListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BrightFragment.
     */
    public static BrightFragment newInstance() {
        BrightFragment fragment = new BrightFragment();
        return fragment;
    }

    public BrightFragment() {
        // Required empty public constructor
    }

    public void setLedSelect(LedSelect ls) {
        ledSelect = ls;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bright,container,false);
        btnLed[0] = (Button) view.findViewById(R.id.led_1);
        btnLed[1] = (Button) view.findViewById(R.id.led_2);
        btnLed[2] = (Button) view.findViewById(R.id.led_3);
        btnLed[3] = (Button) view.findViewById(R.id.led_4);
        btnLed[4] = (Button) view.findViewById(R.id.led_5);
        btnLed[5] = (Button) view.findViewById(R.id.led_6);
        btnLed[6] = (Button) view.findViewById(R.id.led_7);
        btnLed[7] = (Button) view.findViewById(R.id.led_8);
        btnLed[8] = (Button) view.findViewById(R.id.led_9);
        btnRgb[0] = (Button) view.findViewById(R.id.rgb_1);
        btnRgb[1] = (Button) view.findViewById(R.id.rgb_2);
        btnRgb[2] = (Button) view.findViewById(R.id.rgb_3);
        seekLed[0] = (CustomVerticalSeekBar) view.findViewById(R.id.led_1_seekbar);
        seekLed[1] = (CustomVerticalSeekBar) view.findViewById(R.id.led_2_seekbar);
        seekLed[2] = (CustomVerticalSeekBar) view.findViewById(R.id.led_3_seekbar);
        seekLed[3] = (CustomVerticalSeekBar) view.findViewById(R.id.led_4_seekbar);
        seekLed[4] = (CustomVerticalSeekBar) view.findViewById(R.id.led_5_seekbar);
        seekLed[5] = (CustomVerticalSeekBar) view.findViewById(R.id.led_6_seekbar);
        seekLed[6] = (CustomVerticalSeekBar) view.findViewById(R.id.led_7_seekbar);
        seekLed[7] = (CustomVerticalSeekBar) view.findViewById(R.id.led_8_seekbar);
        seekLed[8] = (CustomVerticalSeekBar) view.findViewById(R.id.led_9_seekbar);
        seekRgb[0] = (CustomVerticalSeekBar) view.findViewById(R.id.rgb1_red_seekbar);
        seekRgb[1] = (CustomVerticalSeekBar) view.findViewById(R.id.rgb1_green_seekbar);
        seekRgb[2] = (CustomVerticalSeekBar) view.findViewById(R.id.rgb1_blue_seekbar);
        seekRgb[3] = (CustomVerticalSeekBar) view.findViewById(R.id.rgb2_red_seekbar);
        seekRgb[4] = (CustomVerticalSeekBar) view.findViewById(R.id.rgb2_green_seekbar);
        seekRgb[5] = (CustomVerticalSeekBar) view.findViewById(R.id.rgb2_blue_seekbar);
        seekRgb[6] = (CustomVerticalSeekBar) view.findViewById(R.id.rgb3_red_seekbar);
        seekRgb[7] = (CustomVerticalSeekBar) view.findViewById(R.id.rgb3_green_seekbar);
        seekRgb[8] = (CustomVerticalSeekBar) view.findViewById(R.id.rgb3_blue_seekbar);
        rlLed[0] = (RelativeLayout) view.findViewById(R.id.rl_led_1);
        rlLed[1] = (RelativeLayout) view.findViewById(R.id.rl_led_2);
        rlLed[2] = (RelativeLayout) view.findViewById(R.id.rl_led_3);
        rlLed[3] = (RelativeLayout) view.findViewById(R.id.rl_led_4);
        rlLed[4] = (RelativeLayout) view.findViewById(R.id.rl_led_5);
        rlLed[5] = (RelativeLayout) view.findViewById(R.id.rl_led_6);
        rlLed[6] = (RelativeLayout) view.findViewById(R.id.rl_led_7);
        rlLed[7] = (RelativeLayout) view.findViewById(R.id.rl_led_8);
        rlLed[8] = (RelativeLayout) view.findViewById(R.id.rl_led_9);
        rlRgb[0] = (RelativeLayout) view.findViewById(R.id.rl_rgb_1);
        rlRgb[1] = (RelativeLayout) view.findViewById(R.id.rl_rgb_2);
        rlRgb[2] = (RelativeLayout) view.findViewById(R.id.rl_rgb_3);
        tvLed[0] = (TextView) view.findViewById(R.id.led_1_percent);
        tvLed[1] = (TextView) view.findViewById(R.id.led_2_percent);
        tvLed[2] = (TextView) view.findViewById(R.id.led_3_percent);
        tvLed[3] = (TextView) view.findViewById(R.id.led_4_percent);
        tvLed[4] = (TextView) view.findViewById(R.id.led_5_percent);
        tvLed[5] = (TextView) view.findViewById(R.id.led_6_percent);
        tvLed[6] = (TextView) view.findViewById(R.id.led_7_percent);
        tvLed[7] = (TextView) view.findViewById(R.id.led_8_percent);
        tvLed[8] = (TextView) view.findViewById(R.id.led_9_percent);
        tvRgb[0] = (TextView) view.findViewById(R.id.rgb1_red_percent);
        tvRgb[1] = (TextView) view.findViewById(R.id.rgb1_green_percent);
        tvRgb[2] = (TextView) view.findViewById(R.id.rgb1_blue_percent);
        tvRgb[3] = (TextView) view.findViewById(R.id.rgb2_red_percent);
        tvRgb[4] = (TextView) view.findViewById(R.id.rgb2_green_percent);
        tvRgb[5] = (TextView) view.findViewById(R.id.rgb2_blue_percent);
        tvRgb[6] = (TextView) view.findViewById(R.id.rgb3_red_percent);
        tvRgb[7] = (TextView) view.findViewById(R.id.rgb3_green_percent);
        tvRgb[8] = (TextView) view.findViewById(R.id.rgb3_blue_percent);
        btnColorSelect[0] = (Button) view.findViewById(R.id.btn_rgb_select_1);
        btnColorSelect[1] = (Button) view.findViewById(R.id.btn_rgb_select_2);
        btnColorSelect[2] = (Button) view.findViewById(R.id.btn_rgb_select_3);

        seekLed[0].setOnSeekBarChangeListener(seekBarChangeListener);
        seekLed[1].setOnSeekBarChangeListener(seekBarChangeListener);
        seekLed[2].setOnSeekBarChangeListener(seekBarChangeListener);
        seekLed[3].setOnSeekBarChangeListener(seekBarChangeListener);
        seekLed[4].setOnSeekBarChangeListener(seekBarChangeListener);
        seekLed[5].setOnSeekBarChangeListener(seekBarChangeListener);
        seekLed[6].setOnSeekBarChangeListener(seekBarChangeListener);
        seekLed[7].setOnSeekBarChangeListener(seekBarChangeListener);
        seekLed[8].setOnSeekBarChangeListener(seekBarChangeListener);
        seekRgb[0].setOnSeekBarChangeListener(seekBarChangeListener);
        seekRgb[1].setOnSeekBarChangeListener(seekBarChangeListener);
        seekRgb[2].setOnSeekBarChangeListener(seekBarChangeListener);
        seekRgb[3].setOnSeekBarChangeListener(seekBarChangeListener);
        seekRgb[4].setOnSeekBarChangeListener(seekBarChangeListener);
        seekRgb[5].setOnSeekBarChangeListener(seekBarChangeListener);
        seekRgb[6].setOnSeekBarChangeListener(seekBarChangeListener);
        seekRgb[7].setOnSeekBarChangeListener(seekBarChangeListener);
        seekRgb[8].setOnSeekBarChangeListener(seekBarChangeListener);
        seekLed[0].setOnVerticalSeekBarListener(seekBarTouchListener);
        seekLed[1].setOnVerticalSeekBarListener(seekBarTouchListener);
        seekLed[2].setOnVerticalSeekBarListener(seekBarTouchListener);
        seekLed[3].setOnVerticalSeekBarListener(seekBarTouchListener);
        seekLed[4].setOnVerticalSeekBarListener(seekBarTouchListener);
        seekLed[5].setOnVerticalSeekBarListener(seekBarTouchListener);
        seekLed[6].setOnVerticalSeekBarListener(seekBarTouchListener);
        seekLed[7].setOnVerticalSeekBarListener(seekBarTouchListener);
        seekLed[8].setOnVerticalSeekBarListener(seekBarTouchListener);
        seekRgb[0].setOnVerticalSeekBarListener(seekBarTouchListener);
        seekRgb[1].setOnVerticalSeekBarListener(seekBarTouchListener);
        seekRgb[2].setOnVerticalSeekBarListener(seekBarTouchListener);
        seekRgb[3].setOnVerticalSeekBarListener(seekBarTouchListener);
        seekRgb[4].setOnVerticalSeekBarListener(seekBarTouchListener);
        seekRgb[5].setOnVerticalSeekBarListener(seekBarTouchListener);
        seekRgb[6].setOnVerticalSeekBarListener(seekBarTouchListener);
        seekRgb[7].setOnVerticalSeekBarListener(seekBarTouchListener);
        seekRgb[8].setOnVerticalSeekBarListener(seekBarTouchListener);

        btnLed[0].setOnClickListener(onClickListener);
        btnLed[1].setOnClickListener(onClickListener);
        btnLed[2].setOnClickListener(onClickListener);
        btnLed[3].setOnClickListener(onClickListener);
        btnLed[4].setOnClickListener(onClickListener);
        btnLed[5].setOnClickListener(onClickListener);
        btnLed[6].setOnClickListener(onClickListener);
        btnLed[7].setOnClickListener(onClickListener);
        btnLed[8].setOnClickListener(onClickListener);
        btnRgb[0].setOnClickListener(onClickListener);
        btnRgb[1].setOnClickListener(onClickListener);
        btnRgb[2].setOnClickListener(onClickListener);
        btnColorSelect[0].setOnClickListener(onClickListener);
        btnColorSelect[1].setOnClickListener(onClickListener);
        btnColorSelect[2].setOnClickListener(onClickListener);



        return view;
    }

    @Override
    public void onResume() {
        displaySet();
        super.onResume();
    }

    private void setRgbPercent(int num, int progress) {
        int convertProgress = Math.round((float)progress * (float)100 / (float)191);
        String strPercent = Integer.toString(convertProgress) + "%";
        tvRgb[num].setText(strPercent);
    }

    private void setSinglePercent(int num, int progress) {
        int convertProgress = Math.round((float)progress * (float)100 / (float)191);
        String strPercent = Integer.toString(convertProgress) + "%";
        tvLed[num].setText(strPercent);
    }

    private CustomVerticalSeekBar.OnVerticalSeekBarListener seekBarTouchListener = new CustomVerticalSeekBar.OnVerticalSeekBarListener() {
        @Override
        public void onEvent(int id, int progress) {
            switch (id) {
                case R.id.led_1_seekbar:
                    onTracking(0,progress);
                    break;
                case R.id.led_2_seekbar:
                    onTracking(1,progress);
                    break;
                case R.id.led_3_seekbar:
                    onTracking(2,progress);
                    break;
                case R.id.led_4_seekbar:
                    onTracking(3,progress);
                    break;
                case R.id.led_5_seekbar:
                    onTracking(4,progress);
                    break;
                case R.id.led_6_seekbar:
                    onTracking(5,progress);
                    break;
                case R.id.led_7_seekbar:
                    onTracking(6,progress);
                    break;
                case R.id.led_8_seekbar:
                    onTracking(7,progress);
                    break;
                case R.id.led_9_seekbar:
                    onTracking(8,progress);
                    break;
                case R.id.rgb1_red_seekbar:
                    onTracking(0,progress);
                    break;
                case R.id.rgb1_green_seekbar:
                    onTracking(1,progress);
                    break;
                case R.id.rgb1_blue_seekbar:
                    onTracking(2,progress);
                    break;
                case R.id.rgb2_red_seekbar:
                    onTracking(3,progress);
                    break;
                case R.id.rgb2_green_seekbar:
                    onTracking(4,progress);
                    break;
                case R.id.rgb2_blue_seekbar:
                    onTracking(5,progress);
                    break;
                case R.id.rgb3_red_seekbar:
                    onTracking(6,progress);
                    break;
                case R.id.rgb3_green_seekbar:
                    onTracking(7,progress);
                    break;
                case R.id.rgb3_blue_seekbar:
                    onTracking(8,progress);
                    break;
            }
        }
    };

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            switch (seekBar.getId()) {
                case R.id.led_1_seekbar:
                    setSinglePercent(0,progress);
                    break;
                case R.id.led_2_seekbar:
                    setSinglePercent(1,progress);
                    break;
                case R.id.led_3_seekbar:
                    setSinglePercent(2,progress);
                    break;
                case R.id.led_4_seekbar:
                    setSinglePercent(3,progress);
                    break;
                case R.id.led_5_seekbar:
                    setSinglePercent(4,progress);
                    break;
                case R.id.led_6_seekbar:
                    setSinglePercent(5,progress);
                    break;
                case R.id.led_7_seekbar:
                    setSinglePercent(6,progress);
                    break;
                case R.id.led_8_seekbar:
                    setSinglePercent(7,progress);
                    break;
                case R.id.led_9_seekbar:
                    setSinglePercent(8,progress);
                    break;
                case R.id.rgb1_red_seekbar:
                    setRgbPercent(0,progress);
                    break;
                case R.id.rgb1_green_seekbar:
                    setRgbPercent(1,progress);
                    break;
                case R.id.rgb1_blue_seekbar:
                    setRgbPercent(2,progress);
                    break;
                case R.id.rgb2_red_seekbar:
                    setRgbPercent(3,progress);
                    break;
                case R.id.rgb2_green_seekbar:
                    setRgbPercent(4,progress);
                    break;
                case R.id.rgb2_blue_seekbar:
                    setRgbPercent(5,progress);
                    break;
                case R.id.rgb3_red_seekbar:
                    setRgbPercent(6,progress);
                    break;
                case R.id.rgb3_green_seekbar:
                    setRgbPercent(7,progress);
                    break;
                case R.id.rgb3_blue_seekbar:
                    setRgbPercent(8,progress);
                    break;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            switch (seekBar.getId()) {
                case R.id.led_1_seekbar:
                    onTracking(0,seekBar.getProgress());
                    break;
                case R.id.led_2_seekbar:
                    onTracking(1,seekBar.getProgress());
                    break;
                case R.id.led_3_seekbar:
                    onTracking(2,seekBar.getProgress());
                    break;
                case R.id.led_4_seekbar:
                    onTracking(3,seekBar.getProgress());
                    break;
                case R.id.led_5_seekbar:
                    onTracking(4,seekBar.getProgress());
                    break;
                case R.id.led_6_seekbar:
                    onTracking(5,seekBar.getProgress());
                    break;
                case R.id.led_7_seekbar:
                    onTracking(6,seekBar.getProgress());
                    break;
                case R.id.led_8_seekbar:
                    onTracking(7,seekBar.getProgress());
                    break;
                case R.id.led_9_seekbar:
                    onTracking(8,seekBar.getProgress());
                    break;
                case R.id.rgb1_red_seekbar:
                    onTracking(0,seekBar.getProgress());
                    break;
                case R.id.rgb1_green_seekbar:
                    onTracking(1,seekBar.getProgress());
                    break;
                case R.id.rgb1_blue_seekbar:
                    onTracking(2,seekBar.getProgress());
                    break;
                case R.id.rgb2_red_seekbar:
                    onTracking(3,seekBar.getProgress());
                    break;
                case R.id.rgb2_green_seekbar:
                    onTracking(4,seekBar.getProgress());
                    break;
                case R.id.rgb2_blue_seekbar:
                    onTracking(5,seekBar.getProgress());
                    break;
                case R.id.rgb3_red_seekbar:
                    onTracking(6,seekBar.getProgress());
                    break;
                case R.id.rgb3_green_seekbar:
                    onTracking(7,seekBar.getProgress());
                    break;
                case R.id.rgb3_blue_seekbar:
                    onTracking(8,seekBar.getProgress());
                    break;
            }
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.led_1:
                    seekLed[0].setProgress(seekLed[0].getMax());
                    onTracking(0, seekLed[0].getProgress());
                    break;
                case R.id.led_2:
                    seekLed[1].setProgress(seekLed[1].getMax());
                    onTracking(1, seekLed[1].getProgress());
                    break;
                case R.id.led_3:
                    seekLed[2].setProgress(seekLed[2].getMax());
                    onTracking(2, seekLed[2].getProgress());
                    break;
                case R.id.led_4:
                    seekLed[3].setProgress(seekLed[3].getMax());
                    onTracking(3, seekLed[3].getProgress());
                    break;
                case R.id.led_5:
                    seekLed[4].setProgress(seekLed[4].getMax());
                    onTracking(4, seekLed[4].getProgress());
                    break;
                case R.id.led_6:
                    seekLed[5].setProgress(seekLed[5].getMax());
                    onTracking(5, seekLed[5].getProgress());
                    break;
                case R.id.led_7:
                    seekLed[6].setProgress(seekLed[6].getMax());
                    onTracking(6, seekLed[6].getProgress());
                    break;
                case R.id.led_8:
                    seekLed[7].setProgress(seekLed[7].getMax());
                    onTracking(7, seekLed[7].getProgress());
                    break;
                case R.id.led_9:
                    seekLed[8].setProgress(seekLed[8].getMax());
                    onTracking(8, seekLed[8].getProgress());
                    break;
                case R.id.btn_rgb_select_1:
                    seekRgb[0].setProgress(seekRgb[0].getMax());
                    seekRgb[1].setProgress(seekRgb[1].getMax());
                    seekRgb[2].setProgress(seekRgb[2].getMax());
                    onTracking(0, seekRgb[0].getProgress());
                    onTracking(1, seekRgb[1].getProgress());
                    onTracking(2, seekRgb[2].getProgress());
                    break;
                case R.id.btn_rgb_select_2:
                    seekRgb[3].setProgress(seekRgb[3].getMax());
                    seekRgb[4].setProgress(seekRgb[4].getMax());
                    seekRgb[5].setProgress(seekRgb[5].getMax());
                    onTracking(3, seekRgb[3].getProgress());
                    onTracking(4, seekRgb[4].getProgress());
                    onTracking(5, seekRgb[5].getProgress());
                    break;
                case R.id.btn_rgb_select_3:
                    seekRgb[6].setProgress(seekRgb[6].getMax());
                    seekRgb[7].setProgress(seekRgb[7].getMax());
                    seekRgb[8].setProgress(seekRgb[8].getMax());
                    onTracking(6, seekRgb[6].getProgress());
                    onTracking(7, seekRgb[7].getProgress());
                    onTracking(8, seekRgb[8].getProgress());
                    break;
            }
        }
    };

    private void showRgb(int i) {
        btnLed[i*3].setVisibility(View.GONE);
        btnLed[i*3 +1].setVisibility(View.GONE);
        btnLed[i*3 +2].setVisibility(View.GONE);
        btnRgb[i].setVisibility(View.VISIBLE);

        rlLed[i*3].setVisibility(View.GONE);
        rlLed[i*3 +1].setVisibility(View.GONE);
        rlLed[i*3 +2].setVisibility(View.GONE);
        rlRgb[i].setVisibility(View.VISIBLE);
    }

    private void showLed(int i) {
        int cLedNum = (i*3) / 3;
        btnRgb[i/3].setVisibility(View.GONE);
        rlRgb[i/3].setVisibility(View.GONE);
        for (int k=0;k<3;k++) {
            if (i == cLedNum + k) {
                rlLed[cLedNum].setVisibility(View.VISIBLE);
                btnLed[cLedNum].setVisibility(View.VISIBLE);
            } else if (btnLed[cLedNum].getVisibility() == View.GONE){
                rlLed[cLedNum].setVisibility(View.INVISIBLE);
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

    private void onTracking(int ledNum, int bright) {
        if (mListener != null) {
            mListener.onBrightLed(ledNum, bright);
        }
    }

}
