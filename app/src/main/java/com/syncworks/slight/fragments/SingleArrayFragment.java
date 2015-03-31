package com.syncworks.slight.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.syncworks.slight.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnLedFragmentListener} interface
 * to handle interaction events.
 * Use the {@link SingleArrayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SingleArrayFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "EffectRatio";
    private static final String ARG_PARAM2 = "GapDelay";
    private static final String ARG_PARAM3 = "EndDelay";

    private int effectRatio;
    private int gapDelay;
    private int endDelay;

    private OnLedFragmentListener mListener;

    private Button btnEffectMinus, btnEffectPlus, btnGapMinus, btnGapPlus, btnEndMinus, btnEndPlus;
    private SeekBar sbEffect, sbGap, sbEnd;
    private TextView tvEffect, tvGap, tvEnd;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SingleArrayFragment.
     */
    public static SingleArrayFragment newInstance(int effectRatio, int gapDelay, int endDelay) {
        SingleArrayFragment fragment = new SingleArrayFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, effectRatio);
        args.putInt(ARG_PARAM2, gapDelay);
        args.putInt(ARG_PARAM3, endDelay);
        fragment.setArguments(args);
        return fragment;
    }

    public SingleArrayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            effectRatio = getArguments().getInt(ARG_PARAM1);
            gapDelay = getArguments().getInt(ARG_PARAM2);
            endDelay = getArguments().getInt(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_single_array, container, false);
        sbEffect = (SeekBar) v.findViewById(R.id.seekbar_effect_ratio);
        sbGap = (SeekBar) v.findViewById(R.id.seekbar_gap_delay);
        sbEnd = (SeekBar) v.findViewById(R.id.seekbar_end_delay);
        btnEffectMinus = (Button) v.findViewById(R.id.btn_effect_minus);
        btnEffectPlus = (Button) v.findViewById(R.id.btn_effect_plus);
        btnGapMinus = (Button) v.findViewById(R.id.btn_gap_minus);
        btnGapPlus = (Button) v.findViewById(R.id.btn_gap_plus);
        btnEndMinus = (Button) v.findViewById(R.id.btn_end_minus);
        btnEndPlus = (Button) v.findViewById(R.id.btn_end_plus);
        tvEffect = (TextView) v.findViewById(R.id.val_effect_ratio);
        tvGap = (TextView) v.findViewById(R.id.val_gap_delay);
        tvEnd = (TextView) v.findViewById(R.id.val_end_delay);

        sbEffect.setMax(100);
        sbGap.setMax(25);
        sbEnd.setMax(25);

        sbEffect.setOnSeekBarChangeListener(sbEffectListener);
        sbGap.setOnSeekBarChangeListener(sbGapListener);
        sbEnd.setOnSeekBarChangeListener(sbEndListener);

        btnEffectMinus.setOnClickListener(clickListener);
        btnEffectPlus.setOnClickListener(clickListener);
        btnGapMinus.setOnClickListener(clickListener);
        btnGapPlus.setOnClickListener(clickListener);
        btnEndMinus.setOnClickListener(clickListener);
        btnEndPlus.setOnClickListener(clickListener);

        sbEffect.setProgress(effectRatio);
        sbGap.setProgress(gapDelay);
        sbEnd.setProgress(endDelay);
        return v;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_effect_minus:
                    sbEffect.setProgress(sbEffect.getProgress()-1);
                    doEffectChange(sbEffect.getProgress());
                    break;
                case R.id.btn_effect_plus:
                    sbEffect.setProgress(sbEffect.getProgress()+1);
                    doEffectChange(sbEffect.getProgress());
                    break;
                case R.id.btn_gap_minus:
                    sbGap.setProgress(sbGap.getProgress()-1);
                    doGapChange(sbGap.getProgress());
                    break;
                case R.id.btn_gap_plus:
                    sbGap.setProgress(sbGap.getProgress()+1);
                    doGapChange(sbGap.getProgress());
                    break;
                case R.id.btn_end_minus:
                    sbEnd.setProgress(sbEnd.getProgress()-1);
                    doEndChange(sbEnd.getProgress());
                    break;
                case R.id.btn_end_plus:
                    sbEnd.setProgress(sbEnd.getProgress()+1);
                    doEndChange(sbEnd.getProgress());
                    break;
            }
        }
    };

    // Effect SeekBar Change Listener
    private SeekBar.OnSeekBarChangeListener sbEffectListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            setEffectText(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            doEffectChange(seekBar.getProgress());
        }
    };

    // Gap SeekBar Change Listener
    private SeekBar.OnSeekBarChangeListener sbGapListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            setGapText(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            doGapChange(seekBar.getProgress());
        }
    };

    // End SeekBar Change Listener
    private SeekBar.OnSeekBarChangeListener sbEndListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            setEndText(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            doEndChange(seekBar.getProgress());
        }
    };

    private void setEffectText(int progress) {
        float ratio = (float) progress / 10;
        tvEffect.setText(String.format("%.1f",ratio));
    }

    private void setGapText(int progress) {
        float sec = (float) progress * 8 / 100;
        tvGap.setText(String.format("%.2f",sec));
    }

    private void setEndText(int progress) {
        float sec = (float) progress * 8 / 100;
        tvEnd.setText(String.format("%.2f",sec));
    }

    private void doEffectChange(int progress) {
        if (mListener != null) {
            float ratio = (float) progress / 10;
            mListener.onEffectRatioAction(ratio);
        }
    }

    private void doGapChange(int progress) {
        if (mListener != null) {
            mListener.onArrayGapDelayAction(progress);
        }
    }

    private void doEndChange(int progress) {
        if (mListener != null) {
            mListener.onArrayEndDelayAction(progress);
        }
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

}
