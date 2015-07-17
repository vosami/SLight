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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.syncworks.slight.R;
import com.syncworks.slight.util.LecHeaderParam;
import com.syncworks.slightpref.SLightPref;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SaveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SaveFragment extends Fragment {

    // 스마트라이트 설정
    private SLightPref appPref;

    private OnEasyFragmentListener mListener;

    private TextView tvSleepMinute, tvRandomMinute;
    private SeekBar sbSleepSeek, sbRandomSeek;
    private CheckBox cbSleepLed, cbRandom;
    private Button btnSleep, btnWakeUp, btnExecA, btnExecB, btnExecC, btnSave;
    private Button btnSaveA, btnSaveB, btnSaveC;
    private Spinner spPatten;
    private RelativeLayout rlRandom;

    private int dataPosition = 0;

    private LecHeaderParam lecHeader;

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

    public void setLecHeader(LecHeaderParam lecHeader) {
        this.lecHeader = lecHeader;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        appPref = new SLightPref(getActivity());


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
        btnSaveA = (Button) view.findViewById(R.id.btn_save_a);
        btnSaveB = (Button) view.findViewById(R.id.btn_save_b);
        btnSaveC = (Button) view.findViewById(R.id.btn_save_c);
        btnSave = (Button) view.findViewById(R.id.btn_save);
        spPatten = (Spinner) view.findViewById(R.id.sp_pattern_select);
        rlRandom = (RelativeLayout) view.findViewById(R.id.rl_random);

        String[] spinnerItem = getResources().getStringArray(R.array.spinner_save_pattern);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item,spinnerItem);
        spPatten.setAdapter(adapter);
        spPatten.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dataPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnSleep.setOnClickListener(onClickListener);
        btnWakeUp.setOnClickListener(onClickListener);
        btnExecA.setOnClickListener(onClickListener);
        btnExecB.setOnClickListener(onClickListener);
        btnExecC.setOnClickListener(onClickListener);
        btnSaveA.setOnClickListener(onClickListener);
        btnSaveB.setOnClickListener(onClickListener);
        btnSaveC.setOnClickListener(onClickListener);
        btnSave.setOnClickListener(onClickListener);
        cbSleepLed.setOnClickListener(onClickListener);
        cbRandom.setOnClickListener(onClickListener);

        sbSleepSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress >= seekBar.getMax()) {
                    tvSleepMinute.setText(getString(R.string.str_infinite));
                }else {
                    tvSleepMinute.setText(Integer.toString((progress + 1)*10) + getString(R.string.str_minute));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar.getProgress() >= seekBar.getMax()) {
                    mListener.onSleepTime(0xFFFF);
                } else {
                    mListener.onSleepTime((seekBar.getProgress()+1)*10);
                }
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
                if (seekBar.getProgress() == 0) {
                    showRandomSeek(false);
                }
                mListener.onRandomPlay(sbRandomSeek.getProgress());
            }
        });
        if (!appPref.getBoolean(SLightPref.EASY_ACTIVITY[0])) {
            appPref.putBoolean(SLightPref.EASY_ACTIVITY[0],true);
            showOverLay();
        }
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
                    mListener.onSleep(false);
                    break;
                case R.id.btn_wakeup:
                    mListener.onSleep(true);
                    break;
                case R.id.cb_random_check:
                    showRandomSeek(cbRandom.isChecked());
                    mListener.onRandomPlay(sbRandomSeek.getProgress());
                    break;
                case R.id.btn_exec_a:
                    mListener.onFetchData(0);
                    break;
                case R.id.btn_exec_b:
                    mListener.onFetchData(1);
                    break;
                case R.id.btn_exec_c:
                    mListener.onFetchData(2);
                    break;
                case R.id.btn_save_a:
                    mListener.onSaveData(0);
                    break;
                case R.id.btn_save_b:
                    mListener.onSaveData(1);
                    break;
                case R.id.btn_save_c:
                    mListener.onSaveData(2);
                    break;
                case R.id.btn_save:
                    mListener.onSaveData(dataPosition);
                    break;
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        displayViews();
    }

    private void displayViews() {
        int offTime = lecHeader.getOffTime();
        offTime = Math.round(((float)offTime)/10);
        sbSleepSeek.setProgress(offTime-1);
        boolean isSleepLedBlink = lecHeader.isSleepLedBlink();
        cbSleepLed.setChecked(isSleepLedBlink);
        int randomPlayTime = lecHeader.getRandomDataTime();
        if (randomPlayTime == 0) {
            cbRandom.setChecked(false);
            showRandomSeek(false);
        } else {
            cbRandom.setChecked(true);
            showRandomSeek(true);
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

    // 랜덤 Seekbar 디스플레이 설정
    public void showRandomSeek(boolean isDisplay) {
        if (isDisplay) {
            if (rlRandom != null) {
                cbRandom.setChecked(true);
                sbRandomSeek.setProgress(10);
                rlRandom.setVisibility(View.VISIBLE);
            }
        } else {
            if (rlRandom != null) {
                cbRandom.setChecked(false);
                rlRandom.setVisibility(View.GONE);
                sbRandomSeek.setProgress(0);
            }
        }
    }

    public void showOverLay() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_help_save);
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
