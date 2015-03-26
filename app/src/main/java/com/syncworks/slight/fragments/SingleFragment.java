package com.syncworks.slight.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.syncworks.slight.R;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnLedFragmentListener} interface
 * to handle interaction events.
 * Use the {@link SingleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SingleFragment extends Fragment {
    private static final String TAG = SingleFragment.class.getSimpleName();
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "EffectRatio";
    private static final String ARG_PARAM2 = "StartDelay";
    private static final String ARG_PARAM3 = "EndDelay";

    private int effectRatio;
    private int startDelay;
    private int endDelay;

    private OnLedFragmentListener mListener;

    // Widgets
    Button btnEffectMinus, btnEffectPlus, btnStartMinus, btnStartPlus, btnEndMinus, btnEndPlus;
    SeekBar sbEffect, sbStart, sbEnd;
    TextView tvEffectVal, tvStartVal, tvEndVal;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param effectRatio Parameter 1.
     * @param startDelay Parameter 2.
     * @param endDelay   Parameter 3.
     * @return A new instance of fragment SingleFragment.
     */
    /**
     * 새로운 Fragment 생성시 주어진 Parameter 를 이용해 생성
     * @param effectRatio 지연 시간 비율
     * @param startDelay  시작 시간
     * @param endDelay    종료 시간
     * @return
     */
    public static SingleFragment newInstance(int effectRatio, int startDelay, int endDelay) {
        SingleFragment fragment = new SingleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, effectRatio);
        args.putInt(ARG_PARAM2, startDelay);
        args.putInt(ARG_PARAM3, endDelay);
        fragment.setArguments(args);
        return fragment;
    }

    public SingleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            effectRatio = getArguments().getInt(ARG_PARAM1);
            startDelay = getArguments().getInt(ARG_PARAM2);
            endDelay = getArguments().getInt(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_single, container, false);
        sbEffect = (SeekBar)v.findViewById(R.id.seekbar_effect_ratio);
        sbStart = (SeekBar) v.findViewById(R.id.seekbar_start_delay);
        sbEnd = (SeekBar) v.findViewById(R.id.seekbar_end_delay);

        btnEffectMinus = (Button) v.findViewById(R.id.btn_effect_minus);
        btnEffectPlus = (Button) v.findViewById(R.id.btn_effect_plus);
        btnStartMinus = (Button) v.findViewById(R.id.btn_start_minus);
        btnStartPlus = (Button) v.findViewById(R.id.btn_start_plus);
        btnEndMinus = (Button) v.findViewById(R.id.btn_end_minus);
        btnEndPlus = (Button) v.findViewById(R.id.btn_end_plus);

        tvEffectVal = (TextView) v.findViewById(R.id.val_effect_ratio);
        tvStartVal = (TextView) v.findViewById(R.id.val_start_delay);
        tvEndVal = (TextView) v.findViewById(R.id.val_end_delay);

        btnEffectMinus.setOnClickListener(onBtnClick);
        btnEffectPlus.setOnClickListener(onBtnClick);
        btnStartMinus.setOnClickListener(onBtnClick);
        btnStartPlus.setOnClickListener(onBtnClick);
        btnEndMinus.setOnClickListener(onBtnClick);
        btnEndPlus.setOnClickListener(onBtnClick);

        sbEffect.setOnSeekBarChangeListener(sbListener);
        sbStart.setOnSeekBarChangeListener(sbListener);
        sbEnd.setOnSeekBarChangeListener(sbListener);

        sbEffect.setProgress(effectRatio);
        sbStart.setProgress(startDelay);
        sbEnd.setProgress(endDelay);
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnLedFragmentListener) activity;
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

    // SeekBar 체인지 리스너
    private SeekBar.OnSeekBarChangeListener sbListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            Log.d(TAG,"onProgressChanged" + progress+ "User" + fromUser);
            switch(seekBar.getId()){
                case R.id.seekbar_effect_ratio:
                    setEffectText(progress);
                    break;
                case R.id.seekbar_start_delay:
                    setStartText(progress);
                    break;
                case R.id.seekbar_end_delay:
                    setEndText(progress);
                    break;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            switch (seekBar.getId()) {
                case R.id.seekbar_effect_ratio:
                    float ratio = (float) (seekBar.getProgress() * 0.1);
                    mListener.onEffectRatioAction(ratio);
                    break;
                case R.id.seekbar_start_delay:
                    mListener.onStartDelayAction(seekBar.getProgress());
                    break;
                case R.id.seekbar_end_delay:
                    mListener.onEndDelayAction(seekBar.getProgress());
                    break;
            }
        }
    };

    // 버튼 클릭 리스너
    private View.OnClickListener onBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            float ratio;
            switch (v.getId()){
                case R.id.btn_effect_minus:
                    sbEffect.setProgress(sbEffect.getProgress()-1);
                    ratio = (float) (sbEffect.getProgress() * 0.1);
                    mListener.onEffectRatioAction(ratio);
                    break;
                case R.id.btn_effect_plus:
                    sbEffect.setProgress(sbEffect.getProgress()+1);
                    ratio = (float) (sbEffect.getProgress() * 0.1);
                    mListener.onEffectRatioAction(ratio);
                    break;
                case R.id.btn_start_minus:
                    sbStart.setProgress(sbStart.getProgress()-1);
                    mListener.onStartDelayAction(sbStart.getProgress());
                    break;
                case R.id.btn_start_plus:
                    sbStart.setProgress(sbStart.getProgress()+1);
                    mListener.onStartDelayAction(sbStart.getProgress());
                    break;
                case R.id.btn_end_minus:
                    sbEnd.setProgress(sbEnd.getProgress()-1);
                    mListener.onEndDelayAction(sbEnd.getProgress());
                    break;
                case R.id.btn_end_plus:
                    sbEnd.setProgress(sbEnd.getProgress()+1);
                    mListener.onEndDelayAction(sbEnd.getProgress());
                    break;
            }
        }
    };

    private void setEffectText(int progress) {
        float ratio = (float) (progress * 0.1);
        DecimalFormat df = new DecimalFormat("#.#");
        tvEffectVal.setText(df.format(ratio));
    }

    private void setStartText(int progress) {
        float sec = (float) ((progress * 8 + 1) * 0.01);
        DecimalFormat df = new DecimalFormat("#.##");
        tvStartVal.setText(df.format(sec));
    }
    private void setEndText(int progress) {
        float sec = (float) ((progress * 8 + 1) * 0.01);
        DecimalFormat df = new DecimalFormat("#.##");
        tvEndVal.setText(df.format(sec));
    }



}
