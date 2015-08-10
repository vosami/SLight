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
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.ToggleButton;

import com.syncworks.define.Define;
import com.syncworks.define.Logger;
import com.syncworks.leddata.LedSelect;
import com.syncworks.slight.R;
import com.syncworks.slight.dialog.DialogStartTime;
import com.syncworks.slight.util.BaseExpandableAdapter;
import com.syncworks.slightpref.SLightPref;

import java.util.ArrayList;


public class EffectFragment extends Fragment {

    SLightPref appPref;

    private DialogStartTime dialogStartTime = null;

    private LedSelect ledSelect;
    private OnEasyFragmentListener mListener;

    private int effectNum = 0;

    private Button btnRgb[] = new Button[Define.NUMBER_OF_COLOR_LED];
    private Button btnLed[] = new Button[Define.NUMBER_OF_SINGLE_LED];

    private RadioButton rbPattern[] = new RadioButton[6];
    private Button btnSetStartTime;
    private ToggleButton tbDelay[] = new ToggleButton[3];
    private ToggleButton tbRandom[] = new ToggleButton[2];

    private int patternTime[] = new int[7];
    private int randomTime[] = new int[7];
    private int startTime[] = new int[7];


    private ExpandableListView mListView;
    private ArrayList<String> mGroupList = null;
    private ArrayList<ArrayList<String>> mChildList = null;
    private ArrayList<String> mChildListContent = null;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EffectFragment.
     */
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
        patternTime[0] = 0;
        patternTime[1] = 0;
        patternTime[2] = 0;
        patternTime[3] = 0;
        patternTime[4] = 0;
        patternTime[5] = 0;
        patternTime[6] = 0;
        randomTime[0] = 0;
        randomTime[1] = 0;
        randomTime[2] = 0;
        randomTime[3] = 0;
        randomTime[4] = 0;
        randomTime[5] = 0;
        randomTime[6] = 0;
        startTime[0] = 0;
        startTime[1] = 0;
        startTime[2] = 0;
        startTime[3] = 0;
        startTime[4] = 0;
        startTime[5] = 0;
        startTime[6] = 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        Logger.d(this, "EffectFragment create");
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
/*
        rbPattern[0] = (RadioButton) view.findViewById(R.id.rb_pattern_1);
        rbPattern[1] = (RadioButton) view.findViewById(R.id.rb_pattern_2);
        rbPattern[2] = (RadioButton) view.findViewById(R.id.rb_pattern_3);
        rbPattern[3] = (RadioButton) view.findViewById(R.id.rb_pattern_4);
        rbPattern[4] = (RadioButton) view.findViewById(R.id.rb_pattern_5);
        rbPattern[5] = (RadioButton) view.findViewById(R.id.rb_pattern_6);
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
        rbPattern[5].setOnClickListener(onClickListener);
        btnSetStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogStartTime = new DialogStartTime(getActivity(),alwaysStartTime);
                dialogStartTime.setOnStartTimeListener(startTimeListener);
                dialogStartTime.show();
            }
        });
        tbDelay[0].setOnClickListener(onOptionListener);
        tbDelay[1].setOnClickListener(onOptionListener);
        tbDelay[2].setOnClickListener(onOptionListener);
        tbRandom[0].setOnClickListener(onOptionListener);
        tbRandom[1].setOnClickListener(onOptionListener);
*/
        if (!appPref.getBoolean(SLightPref.EASY_ACTIVITY[3])) {
            appPref.putBoolean(SLightPref.EASY_ACTIVITY[3],true);
            showOverLay();
        }

        mListView = (ExpandableListView) view.findViewById(R.id.elv_effect);
        mGroupList = new ArrayList<>();
        mChildList = new ArrayList<>();
        mChildListContent = new ArrayList<>();

        mGroupList.add(getString(R.string.easy_effect_pattern_always));
        mGroupList.add(getString(R.string.easy_effect_pattern_pulse));
        mGroupList.add(getString(R.string.easy_effect_pattern_flash));
        mGroupList.add(getString(R.string.easy_effect_pattern_updown));
        mGroupList.add(getString(R.string.easy_effect_pattern_torch));
        mGroupList.add(getString(R.string.easy_effect_rgb_rainbow));
        mGroupList.add(getString(R.string.easy_effect_rgb_sin));

        mChildListContent.add("1");

        mChildList.add(mChildListContent);
        mChildList.add(mChildListContent);
        mChildList.add(mChildListContent);
        mChildList.add(mChildListContent);
        mChildList.add(mChildListContent);
        mChildList.add(mChildListContent);
        mChildList.add(mChildListContent);

        mListView.setAdapter(new BaseExpandableAdapter(getActivity(), mGroupList, mChildList));

        mListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < mListView.getChildCount(); i++) {
                    if (i != groupPosition) {
                        mListView.collapseGroup(i);
                    }
                }
            }
        });

        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        displaySet();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    // 패턴 선택 리스너
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                /*
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
                case R.id.rb_pattern_6:
                    rbClick(5);
                    break;
                    */
            }
        }
    };

    private DialogStartTime.OnStartTimeListener startTimeListener = new DialogStartTime.OnStartTimeListener() {
        @Override
        public void onStartTime(int effectNum, int time) {
            startTime[effectNum] = time;
            doPattern(effectNum,patternTime[effectNum],randomTime[effectNum],startTime[effectNum]);
        }

        @Override
        public void onConfirm() {
            if (dialogStartTime.isShowing()) {
                dialogStartTime.dismiss();
            }
        }
    };

    // 옵션 리스너
    private ToggleButton.OnClickListener onOptionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                /*
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
                    */
            }
        }
    };

    // 라디오 버튼 클릭
    private void rbClick(int pattern) {
        effectNum = pattern;
        for (int i=0;i<6;i++) {
            if (i == pattern) {
//                rbPattern[i].setChecked(true);
                doPattern(i);
            } else {
//                rbPattern[i].setChecked(false);
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
    public void initEffectNum() {
        effectNum = 0;
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
            // 리스트뷰 Expand 초기화
            mListView.expandGroup(effectNum);
            // 라디오 버튼 초기화
            //rbClick(effectNum);
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
            mListener.onEffect(effect, patternTime[effect], randomTime[effect], 0);
        }
    }

    private void doPattern(int effect, int patternTime, int randomTime, int startTime) {
        if (mListener != null) {
            mListener.onEffect(effect,patternTime, randomTime, startTime);
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
