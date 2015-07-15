package com.syncworks.slight.fragment_easy;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.syncworks.slight.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SaveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SaveFragment extends Fragment {

    private OnEasyFragmentListener mListener;

    private TextView tvSleepMinute, tvRandomMinute;
    private SeekBar sbSleepSeek, sbRandomSeek;
    private CheckBox cbSleepLed, cbRandom;
    private Button btnSleep, btnWakeUp, btnExecA, btnExecB, btnExecC, btnSave;
    private Spinner spPatten;
    private RelativeLayout rlRandom;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SaveFragment.
     */
    public static SaveFragment newInstance() {

        return new SaveFragment();
    }

    public SaveFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_save,container,false);
        tvSleepMinute = (TextView) view.findViewById(R.id.tv_sleep_seek);
        tvRandomMinute = (TextView) view.findViewById(R.id.tv_random_seek);
        sbSleepSeek = (SeekBar) view.findViewById(R.id.sb_sleep_seek);
        sbRandomSeek = (SeekBar) view.findViewById(R.id.sb_random_seek);
        cbSleepLed = (CheckBox) view.findViewById(R.id.cb_sleep_led);
        cbRandom = (CheckBox) view.findViewById(R.id.cb_random_check);
        btnSleep = (Button) view.findViewById(R.id.btn_sleep);
        btnWakeUp = (Button) view.findViewById(R.id.btn_wakeup);
        btnExecA = (Button) view.findViewById(R.id.btn_exec_a);
        btnExecB = (Button) view.findViewById(R.id.btn_exec_b);
        btnExecC = (Button) view.findViewById(R.id.btn_exec_c);
        btnSave = (Button) view.findViewById(R.id.btn_save);
        spPatten = (Spinner) view.findViewById(R.id.sp_pattern_select);
        rlRandom = (RelativeLayout) view.findViewById(R.id.rl_random);

        String[] spinnerItem = getResources().getStringArray(R.array.spinner_save_pattern);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item,spinnerItem);
        spPatten.setAdapter(adapter);
        btnSleep.setOnClickListener(onClickListener);
        btnWakeUp.setOnClickListener(onClickListener);
        btnExecA.setOnClickListener(onClickListener);
        btnExecB.setOnClickListener(onClickListener);
        btnExecC.setOnClickListener(onClickListener);
        btnSave.setOnClickListener(onClickListener);
        sbSleepSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSleepMinute.setText(Integer.toString(progress) + getString(R.string.str_minute));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbRandomSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvRandomMinute.setText(Integer.toString(progress) + getString(R.string.str_minute));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return view;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cb_sleep_led:
                    mListener.onSleepLedCheck(cbSleepLed.isChecked());
                    break;
                case R.id.btn_sleep:
                    mListener.onSleep(true);
                    break;
                case R.id.btn_wakeup:
                    mListener.onSleep(false);
                    break;
                case R.id.cb_random_check:
                    showRandomSeek(cbRandom.isChecked());
                    mListener.onRandomPlay(sbRandomSeek.getProgress());
                    break;
                case R.id.btn_exec_a:
                    break;
                case R.id.btn_exec_b:
                    break;
                case R.id.btn_exec_c:
                    break;
                case R.id.btn_save:
                    break;
            }
        }
    };

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

    // 랜덤 Seekbar 디스플레이 설정
    public void showRandomSeek(boolean isDisplay) {
        if (isDisplay) {
            if (rlRandom != null) {
                sbRandomSeek.setProgress(10);
                rlRandom.setVisibility(View.VISIBLE);
            }
        } else {
            if (rlRandom != null) {
                rlRandom.setVisibility(View.GONE);
                sbRandomSeek.setProgress(0);
            }
        }
    }

}
