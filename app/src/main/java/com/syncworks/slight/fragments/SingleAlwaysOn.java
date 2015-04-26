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

import com.syncworks.define.Define;
import com.syncworks.slight.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnLedFragmentListener} interface
 * to handle interaction events.
 * Use the {@link SingleAlwaysOn#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SingleAlwaysOn extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";



    private OnLedFragmentListener mListener;

	private boolean isActive = true;

	// Vars
	private int initBright;
    // Widgets
    Button[] btnEasyBright = new Button[6];
    Button btnPlus, btnMinus;
    SeekBar sbBright;
    TextView tvCurBright;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SingleAlwaysOn.
     */
    public static SingleAlwaysOn newInstance(int bright) {
        SingleAlwaysOn fragment = new SingleAlwaysOn();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, bright);
        fragment.setArguments(args);

        return fragment;
    }

    public SingleAlwaysOn() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            initBright = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_single_always_on,container,false);
        btnEasyBright[0] = (Button) v.findViewById(R.id.btn_easy_0_percent);
        btnEasyBright[1] = (Button) v.findViewById(R.id.btn_easy_20_percent);
        btnEasyBright[2] = (Button) v.findViewById(R.id.btn_easy_40_percent);
        btnEasyBright[3] = (Button) v.findViewById(R.id.btn_easy_60_percent);
        btnEasyBright[4] = (Button) v.findViewById(R.id.btn_easy_80_percent);
        btnEasyBright[5] = (Button) v.findViewById(R.id.btn_easy_100_percent);

        btnPlus = (Button) v.findViewById(R.id.btn_plus);
        btnMinus = (Button) v.findViewById(R.id.btn_minus);
        sbBright = (SeekBar) v.findViewById(R.id.sb_bright_detail);
        tvCurBright = (TextView) v.findViewById(R.id.tv_current_bright);

        sbBright.setMax(Define.OP_CODE_MIN-1);

        btnEasyBright[0].setOnClickListener(brightClickListener);
        btnEasyBright[1].setOnClickListener(brightClickListener);
        btnEasyBright[2].setOnClickListener(brightClickListener);
        btnEasyBright[3].setOnClickListener(brightClickListener);
        btnEasyBright[4].setOnClickListener(brightClickListener);
        btnEasyBright[5].setOnClickListener(brightClickListener);

        btnPlus.setOnClickListener(sbBtnClickListener);
        btnMinus.setOnClickListener(sbBtnClickListener);
        sbBright.setOnSeekBarChangeListener(sbListener);


        return v;
    }

	@Override
	public void onStart() {
		super.onStart();
		isActive = true;
		sbBright.setProgress(initBright);
	}

	@Override
	public void onStop() {
		super.onStop();
		isActive = false;
	}

	// 간편 선택 버튼 클릭 리스너
    private View.OnClickListener brightClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.btn_easy_0_percent:
                    sbBright.setProgress(0);
                    doBrightChange(0);
                    break;
                case R.id.btn_easy_20_percent:
                    sbBright.setProgress(38);
                    doBrightChange(38);
                    break;
                case R.id.btn_easy_40_percent:
                    sbBright.setProgress(77);
                    doBrightChange(77);
                    break;
                case R.id.btn_easy_60_percent:
                    sbBright.setProgress(115);
                    doBrightChange(115);
                    break;
                case R.id.btn_easy_80_percent:
                    sbBright.setProgress(153);
                    doBrightChange(153);
                    break;
                case R.id.btn_easy_100_percent:
                    sbBright.setProgress(Define.OP_CODE_MIN-1);
                    doBrightChange(Define.OP_CODE_MIN-1);
                    break;
            }
        }
    };

    // 플러스, 마이너스 버튼 클릭 리스너
    private View.OnClickListener sbBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int progress = sbBright.getProgress();
            switch(v.getId()) {
                case R.id.btn_minus:
                    sbBright.setProgress(progress-1);
                    doBrightChange(sbBright.getProgress());
                    break;
                case R.id.btn_plus:
                    sbBright.setProgress(progress+1);
                    doBrightChange(sbBright.getProgress());
                    break;
            }
        }
    };

    // SeekBar Change Listener
    private SeekBar.OnSeekBarChangeListener sbListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            setTextView(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            doBrightChange(seekBar.getProgress());
        }
    };

    private void setTextView(int progress) {
        float ratioBright = (float)(progress * 100) / (Define.OP_CODE_MIN-1);
        tvCurBright.setText(String.format("%.1f%%", ratioBright));
    }

    private void doBrightChange(int bright) {
        if (mListener != null) {
            mListener.onSingleBrightAction(bright);
        }
    }

    public void setBright(int bright) {
        for (int i=0;i<6;i++) {
            btnEasyBright[i].setSelected(false);
        }
        sbBright.setProgress(bright);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnLedFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnLedFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

	public void setInitBright(int bright) {
		Bundle args = getArguments();
		if (args != null) {
			args.putInt(ARG_PARAM1,bright);
		}
		if (isActive) {
			setBright(bright);
		}
	}

}
