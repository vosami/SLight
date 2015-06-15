package com.syncworks.slight.fragment_comm;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.syncworks.define.Define;
import com.syncworks.slight.R;
import com.syncworks.verticalseekbar.MyVerticalSeekBar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link BrightFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrightFragment extends Fragment {
    private final static String TAG = BrightFragment.class.getSimpleName();

    LinearLayout llRgbTitle, llSingleTitle, llRgbSeek, llSingleSeek, llRgbPercent, llSinglePercent;
    TextView tvRgbPercent[], tvSinglePercent[];
    MyVerticalSeekBar sbRgb[], sbSingle[];
    TextView tvRgbTitle[], tvSingleTitle[];

    private boolean isRGB = false;
    private int paramRGB = 0;
    private int paramLED = 0;

    private int[] brightSingle = {50,50,50,50,50,50,50,50,50};
    private int[] brightRgb = {50,50,50};

    private OnCommFragmentListener mListener;

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

    public void setRGB(boolean isRGB) {
        this.isRGB = isRGB;
    }

    public void setParamRGB(int paramRGB) {
        this.paramRGB = paramRGB;
    }

    public void setParamLED(int paramLed) {
        this.paramLED = paramLed;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bright, container, false);
        findViews(view);
        Log.d(TAG, "onCreateView");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
    }



    @Override
    public void onStart() {
        super.onStart();
        initUI();
        Log.d(TAG,"onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
    }

    public void findViews(View v) {
        llRgbTitle = (LinearLayout) v.findViewById(R.id.ll_frag_rgb_array);
        llSingleTitle = (LinearLayout) v.findViewById(R.id.ll_frag_bright_array);
        llRgbPercent = (LinearLayout) v.findViewById(R.id.ll_frag_rgb_margin);
        llSinglePercent = (LinearLayout) v.findViewById(R.id.ll_frag_bright_margin);
        llRgbSeek = (LinearLayout) v.findViewById(R.id.ll_frag_rgb_seek);
        llSingleSeek = (LinearLayout) v.findViewById(R.id.ll_frag_bright_seek);

        tvRgbTitle = new TextView[Define.NUMBER_OF_COLOR_LED];
        tvSingleTitle = new TextView[Define.NUMBER_OF_SINGLE_LED];
        tvRgbPercent = new TextView[Define.NUMBER_OF_COLOR_LED];
        tvSinglePercent = new TextView[Define.NUMBER_OF_SINGLE_LED];
        sbRgb = new MyVerticalSeekBar[Define.NUMBER_OF_COLOR_LED];
        sbSingle = new MyVerticalSeekBar[Define.NUMBER_OF_SINGLE_LED];

        tvRgbTitle[0] = (TextView) v.findViewById(R.id.frag_rgb_1);
        tvRgbTitle[1] = (TextView) v.findViewById(R.id.frag_rgb_2);
        tvRgbTitle[2] = (TextView) v.findViewById(R.id.frag_rgb_3);

        tvSingleTitle[0] = (TextView) v.findViewById(R.id.frag_bright_1);
        tvSingleTitle[1] = (TextView) v.findViewById(R.id.frag_bright_2);
        tvSingleTitle[2] = (TextView) v.findViewById(R.id.frag_bright_3);
        tvSingleTitle[3] = (TextView) v.findViewById(R.id.frag_bright_4);
        tvSingleTitle[4] = (TextView) v.findViewById(R.id.frag_bright_5);
        tvSingleTitle[5] = (TextView) v.findViewById(R.id.frag_bright_6);
        tvSingleTitle[6] = (TextView) v.findViewById(R.id.frag_bright_7);
        tvSingleTitle[7] = (TextView) v.findViewById(R.id.frag_bright_8);
        tvSingleTitle[8] = (TextView) v.findViewById(R.id.frag_bright_9);

        tvRgbPercent[0] = (TextView) v.findViewById(R.id.tv_rgb_percent_1);
        tvRgbPercent[1] = (TextView) v.findViewById(R.id.tv_rgb_percent_2);
        tvRgbPercent[2] = (TextView) v.findViewById(R.id.tv_rgb_percent_3);

        tvSinglePercent[0] = (TextView) v.findViewById(R.id.tv_bright_percent_1);
        tvSinglePercent[1] = (TextView) v.findViewById(R.id.tv_bright_percent_2);
        tvSinglePercent[2] = (TextView) v.findViewById(R.id.tv_bright_percent_3);
        tvSinglePercent[3] = (TextView) v.findViewById(R.id.tv_bright_percent_4);
        tvSinglePercent[4] = (TextView) v.findViewById(R.id.tv_bright_percent_5);
        tvSinglePercent[5] = (TextView) v.findViewById(R.id.tv_bright_percent_6);
        tvSinglePercent[6] = (TextView) v.findViewById(R.id.tv_bright_percent_7);
        tvSinglePercent[7] = (TextView) v.findViewById(R.id.tv_bright_percent_8);
        tvSinglePercent[8] = (TextView) v.findViewById(R.id.tv_bright_percent_9);

        sbRgb[0] = (MyVerticalSeekBar) v.findViewById(R.id.rgb_seekbar_1);
        sbRgb[1] = (MyVerticalSeekBar) v.findViewById(R.id.rgb_seekbar_2);
        sbRgb[2] = (MyVerticalSeekBar) v.findViewById(R.id.rgb_seekbar_3);

        sbSingle[0] = (MyVerticalSeekBar) v.findViewById(R.id.bright_seekbar_1);
        sbSingle[1] = (MyVerticalSeekBar) v.findViewById(R.id.bright_seekbar_2);
        sbSingle[2] = (MyVerticalSeekBar) v.findViewById(R.id.bright_seekbar_3);
        sbSingle[3] = (MyVerticalSeekBar) v.findViewById(R.id.bright_seekbar_4);
        sbSingle[4] = (MyVerticalSeekBar) v.findViewById(R.id.bright_seekbar_5);
        sbSingle[5] = (MyVerticalSeekBar) v.findViewById(R.id.bright_seekbar_6);
        sbSingle[6] = (MyVerticalSeekBar) v.findViewById(R.id.bright_seekbar_7);
        sbSingle[7] = (MyVerticalSeekBar) v.findViewById(R.id.bright_seekbar_8);
        sbSingle[8] = (MyVerticalSeekBar) v.findViewById(R.id.bright_seekbar_9);


        sbRgb[0].setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbRgb[1].setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbRgb[2].setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbSingle[0].setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbSingle[1].setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbSingle[2].setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbSingle[3].setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbSingle[4].setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbSingle[5].setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbSingle[6].setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbSingle[7].setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbSingle[8].setOnSeekBarChangeListener(onSeekBarChangeListener);


        sbRgb[0].setOnVerticalSeekBarListener(verticalSeekBarListener);
        sbRgb[1].setOnVerticalSeekBarListener(verticalSeekBarListener);
        sbRgb[2].setOnVerticalSeekBarListener(verticalSeekBarListener);
        sbSingle[0].setOnVerticalSeekBarListener(verticalSeekBarListener);
        sbSingle[1].setOnVerticalSeekBarListener(verticalSeekBarListener);
        sbSingle[2].setOnVerticalSeekBarListener(verticalSeekBarListener);
        sbSingle[3].setOnVerticalSeekBarListener(verticalSeekBarListener);
        sbSingle[4].setOnVerticalSeekBarListener(verticalSeekBarListener);
        sbSingle[5].setOnVerticalSeekBarListener(verticalSeekBarListener);
        sbSingle[6].setOnVerticalSeekBarListener(verticalSeekBarListener);
        sbSingle[7].setOnVerticalSeekBarListener(verticalSeekBarListener);
        sbSingle[8].setOnVerticalSeekBarListener(verticalSeekBarListener);
        if (isRGB) {
            displayRgb(paramRGB);
        } else {
            displaySingle(paramLED);
        }

    }

    public void initUI() {
        for (int i=0;i<Define.NUMBER_OF_COLOR_LED;i++) {
            sbRgb[i].setProgress(brightRgb[i]);
        }
        for (int i=0;i<Define.NUMBER_OF_SINGLE_LED;i++) {
            sbSingle[i].setProgress(brightSingle[i]);
        }
    }

    public void initSingleProgress(int led) {
        Log.d(TAG,"initSingleProgress"+ led);
        brightSingle[led] = 50;
    }

    public void initRGBProgress(int led) {
        brightRgb[led] = 50;
    }

    private void setRgbPercent(int num, int progress) {
        String strPercent = Integer.toString(progress) + "%";
        tvRgbPercent[num].setText(strPercent);
    }

    private void setSinglePercent(int num, int progress) {
        String strPercent = Integer.toString(progress) + "%";
        tvSinglePercent[num].setText(strPercent);
    }

    private void showRgb(int num, int visibility) {
        sbRgb[num].setVisibility(visibility);
        tvRgbTitle[num].setVisibility(visibility);
        tvRgbPercent[num].setVisibility(visibility);
    }

    private void showSingle(int num, int visibility) {
        sbSingle[num].setVisibility(visibility);
        tvSingleTitle[num].setVisibility(visibility);
        tvSinglePercent[num].setVisibility(visibility);
    }

    public void displaySingle(int ledNum) {
        llRgbPercent.setVisibility(View.INVISIBLE);
        llRgbTitle.setVisibility(View.INVISIBLE);
        llRgbSeek.setVisibility(View.INVISIBLE);
        llSinglePercent.setVisibility(View.VISIBLE);
        llSingleTitle.setVisibility(View.VISIBLE);
        llSingleSeek.setVisibility(View.VISIBLE);
        for (int i= 0;i<Define.NUMBER_OF_SINGLE_LED;i++) {
            if (((ledNum >> i) & 0x01) == 1) {
                showSingle(i, View.VISIBLE);
            } else {
                showSingle(i, View.INVISIBLE);
            }
        }
    }

    public void displayRgb(int ledNum) {
        llRgbPercent.setVisibility(View.VISIBLE);
        llRgbTitle.setVisibility(View.VISIBLE);
        llRgbSeek.setVisibility(View.VISIBLE);
        llSinglePercent.setVisibility(View.INVISIBLE);
        llSingleTitle.setVisibility(View.INVISIBLE);
        llSingleSeek.setVisibility(View.INVISIBLE);
        for (int i= 0;i<Define.NUMBER_OF_COLOR_LED;i++) {
            if (((ledNum >> i) & 0x01) == 1) {
                showRgb(i, View.VISIBLE);
            } else {
                showRgb(i, View.INVISIBLE);
            }
        }
    }

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            Log.d(TAG,"Changed"+ progress);
            switch (seekBar.getId()) {
                case R.id.rgb_seekbar_1:
                    setRgbPercent(0,progress);
                    break;
                case R.id.rgb_seekbar_2:
                    setRgbPercent(1,progress);
                    break;
                case R.id.rgb_seekbar_3:
                    setRgbPercent(2,progress);
                    break;
                case R.id.bright_seekbar_1:
                    setSinglePercent(0, progress);
                    break;
                case R.id.bright_seekbar_2:
                    setSinglePercent(1,progress);
                    break;
                case R.id.bright_seekbar_3:
                    setSinglePercent(2,progress);
                    break;
                case R.id.bright_seekbar_4:
                    setSinglePercent(3,progress);
                    break;
                case R.id.bright_seekbar_5:
                    setSinglePercent(4,progress);
                    break;
                case R.id.bright_seekbar_6:
                    setSinglePercent(5,progress);
                    break;
                case R.id.bright_seekbar_7:
                    setSinglePercent(6,progress);
                    break;
                case R.id.bright_seekbar_8:
                    setSinglePercent(7,progress);
                    break;
                case R.id.bright_seekbar_9:
                    setSinglePercent(8,progress);
                    break;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    private MyVerticalSeekBar.OnVerticalSeekBarListener verticalSeekBarListener = new MyVerticalSeekBar.OnVerticalSeekBarListener() {
        @Override
        public void onEvent(int id, int progress) {
            Log.d("Event","k" + progress);
            switch (id) {
                case R.id.rgb_seekbar_1:
                    mListener.onBrightRGB(0,progress);
                    brightRgb[0] = progress;
                    break;
                case R.id.rgb_seekbar_2:
                    mListener.onBrightRGB(1,progress);
                    brightRgb[1] = progress;
                    break;
                case R.id.rgb_seekbar_3:
                    mListener.onBrightRGB(2,progress);
                    brightRgb[2] = progress;
                    break;
                case R.id.bright_seekbar_1:
                    mListener.onBrightLed(0, progress);
                    brightSingle[0] = progress;
                    break;
                case R.id.bright_seekbar_2:
                    mListener.onBrightLed(1,progress);
                    brightSingle[1] = progress;
                    break;
                case R.id.bright_seekbar_3:
                    mListener.onBrightLed(2,progress);
                    brightSingle[2] = progress;
                    break;
                case R.id.bright_seekbar_4:
                    mListener.onBrightLed(3,progress);
                    brightSingle[3] = progress;
                    break;
                case R.id.bright_seekbar_5:
                    mListener.onBrightLed(4,progress);
                    brightSingle[4] = progress;
                    break;
                case R.id.bright_seekbar_6:
                    mListener.onBrightLed(5,progress);
                    brightSingle[5] = progress;
                    break;
                case R.id.bright_seekbar_7:
                    mListener.onBrightLed(6,progress);
                    brightSingle[6] = progress;
                    break;
                case R.id.bright_seekbar_8:
                    mListener.onBrightLed(7,progress);
                    brightSingle[7] = progress;
                    break;
                case R.id.bright_seekbar_9:
                    mListener.onBrightLed(8,progress);
                    brightSingle[8] = progress;
                    break;
            }

        }
    };


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


}
