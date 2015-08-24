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
import android.widget.ToggleButton;

import com.syncworks.define.Define;
import com.syncworks.define.Logger;
import com.syncworks.leddata.LedSelect;
import com.syncworks.slight.R;
import com.syncworks.slight.dialog.DialogStartTime;
import com.syncworks.slight.util.BaseExpandableAdapter;
import com.syncworks.slight.util.BaseExpandableInterface;
import com.syncworks.slight.util.EffectListData;
import com.syncworks.slight.util.EffectOptionData;
import com.syncworks.slightpref.SLightPref;

import java.util.ArrayList;


public class EffectFragment extends Fragment {

    SLightPref appPref;

    private DialogStartTime dialogStartTime = null;

    private LedSelect ledSelect;
    //private LedOptions ledOption[];
    private OnEasyFragmentListener mListener;

    private static int staticEffectNum = 0;
    private static int staticStartTime = 0;
    private static int staticEffectTime = 2;
    private static int staticRandomTime = 0;


    private Button btnRgb[] = new Button[Define.NUMBER_OF_COLOR_LED];
    private Button btnLed[] = new Button[Define.NUMBER_OF_SINGLE_LED];

    /*private RadioButton rbPattern[] = new RadioButton[6];
    private Button btnSetStartTime;
    private ToggleButton tbDelay[] = new ToggleButton[3];
    private ToggleButton tbRandom[] = new ToggleButton[2];*/

    /*private int patternTime[] = new int[7];
    private int randomTime[] = new int[7];
    private int startTime[] = new int[7];*/


    private ExpandableListView mListView;
    private ArrayList<EffectListData> mGroupList = null;
    private ArrayList<EffectOptionData> mChildList = null;
    private BaseExpandableAdapter expandableAdapter = null;

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

    /*public void setLedOption(LedOptions option[]) {
        this.ledOption = option;
    }*/


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d(this, "EffectFragment create");
        staticEffectNum = 0;
        staticStartTime = 0;
        staticEffectTime = 2;
        staticRandomTime = 0;
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
        EffectListData listData[];
        EffectOptionData optionData[];

        int listCount;
        if (getLedSelected() == LED_NOT_SELECTED) {
            listData = new EffectListData[1];
            optionData = new EffectOptionData[1];
            listData[0] = new EffectListData();
            listData[0].init();
            listData[0].effectName = getString(R.string.easy_effect_not_selected);
            optionData[0] = new EffectOptionData();
            mGroupList.add(listData[0]);
            mChildList.add(optionData[0]);
        } else if (getLedSelected() == LED_RGB_ONLY_SELECTED) {
            listCount = 8;
            listData = new EffectListData[listCount];
            for (int i=0;i<listCount;i++) {
                listData[i] = new EffectListData();
                listData[i].init();
            }
            listData[0].effectName = getString(R.string.easy_effect_pattern_always);
            listData[0].imgId = R.drawable.ic_pattern_always;
            mGroupList.add(listData[0]);
            listData[1].effectName = getString(R.string.easy_effect_pattern_pulse);
            listData[1].imgId = R.drawable.ic_pattern_pulse;
            mGroupList.add(listData[1]);
            listData[2].effectName = getString(R.string.easy_effect_pattern_flash);
            listData[2].imgId = R.drawable.ic_pattern_flash;
            mGroupList.add(listData[2]);
            listData[3].effectName = getString(R.string.easy_effect_pattern_updown);
            listData[3].imgId = R.drawable.ic_pattern_up_down;
            mGroupList.add(listData[3]);
            listData[4].effectName = getString(R.string.easy_effect_pattern_torch);
            listData[4].imgId = R.drawable.ic_pattern_torch;
            mGroupList.add(listData[4]);
            listData[5].effectName = getString(R.string.easy_effect_pattern_sin);
            listData[5].imgId = R.drawable.ic_pattern_torch;
            mGroupList.add(listData[5]);
            listData[6].effectName = getString(R.string.easy_effect_pattern_laser);
            listData[6].imgId = R.drawable.ic_pattern_torch;
            mGroupList.add(listData[6]);
            listData[7].effectName = getString(R.string.easy_effect_rgb_rainbow);
            listData[7].imgId = R.drawable.ic_pattern_rainbow;
            mGroupList.add(listData[7]);
            optionData = new EffectOptionData[listCount];
            for (int i=0;i<listCount;i++) {
                optionData[i] = new EffectOptionData();
                optionData[i].isShowStartDelay = true;
                if (i == 0) {
                    optionData[i].isShowEffectDelay = false;
                    optionData[i].isShowRandomDelay = false;
                } else {
                    optionData[i].isShowEffectDelay = true;
                    optionData[i].isShowRandomDelay = true;
                }
                if (staticEffectNum == i) {
                    optionData[i].timeStartDelay = staticStartTime;
                    optionData[i].timeEffectDelay = staticEffectTime;
                    optionData[i].timeRandomDelay = staticRandomTime;
                } else {
                    optionData[i].timeStartDelay = 0;
                    optionData[i].timeEffectDelay = 2;
                    optionData[i].timeRandomDelay = 0;
                }
            }

            mChildList.add(optionData[0]);
            mChildList.add(optionData[1]);
            mChildList.add(optionData[2]);
            mChildList.add(optionData[3]);
            mChildList.add(optionData[4]);
            mChildList.add(optionData[5]);
            mChildList.add(optionData[6]);
            mChildList.add(optionData[7]);
        } else {
            listCount = 7;
            listData = new EffectListData[listCount];
            for (int i=0;i<listCount;i++) {
                listData[i] = new EffectListData();
                listData[i].init();
            }
            listData[0].effectName = getString(R.string.easy_effect_pattern_always);
            listData[0].imgId = R.drawable.ic_pattern_always;
            mGroupList.add(listData[0]);
            listData[1].effectName = getString(R.string.easy_effect_pattern_pulse);
            listData[1].imgId = R.drawable.ic_pattern_pulse;
            mGroupList.add(listData[1]);
            listData[2].effectName = getString(R.string.easy_effect_pattern_flash);
            listData[2].imgId = R.drawable.ic_pattern_flash;
            mGroupList.add(listData[2]);
            listData[3].effectName = getString(R.string.easy_effect_pattern_updown);
            listData[3].imgId = R.drawable.ic_pattern_up_down;
            mGroupList.add(listData[3]);
            listData[4].effectName = getString(R.string.easy_effect_pattern_torch);
            listData[4].imgId = R.drawable.ic_pattern_torch;
            mGroupList.add(listData[4]);
            listData[5].effectName = getString(R.string.easy_effect_pattern_sin);
            listData[5].imgId = R.drawable.ic_pattern_torch;
            mGroupList.add(listData[5]);
            listData[6].effectName = getString(R.string.easy_effect_pattern_laser);
            listData[6].imgId = R.drawable.ic_pattern_torch;
            mGroupList.add(listData[6]);
            optionData = new EffectOptionData[listCount];
            for (int i=0;i<listCount;i++) {
                optionData[i] = new EffectOptionData();
                optionData[i].isShowStartDelay = true;
                if (i == 0) {
                    optionData[i].isShowEffectDelay = false;
                    optionData[i].isShowRandomDelay = false;
                } else {
                    optionData[i].isShowEffectDelay = true;
                    optionData[i].isShowRandomDelay = true;
                }
                if (staticEffectNum == i) {
                    optionData[i].timeStartDelay = staticStartTime;
                    optionData[i].timeEffectDelay = staticEffectTime;
                    optionData[i].timeRandomDelay = staticRandomTime;
                } else {
                    optionData[i].timeStartDelay = 0;
                    optionData[i].timeEffectDelay = 2;
                    optionData[i].timeRandomDelay = 0;
                }
            }

            mChildList.add(optionData[0]);
            mChildList.add(optionData[1]);
            mChildList.add(optionData[2]);
            mChildList.add(optionData[3]);
            mChildList.add(optionData[4]);
            mChildList.add(optionData[5]);
            mChildList.add(optionData[6]);
        }

        expandableAdapter = new BaseExpandableAdapter(getActivity(),mGroupList,mChildList);
        mListView.setAdapter(expandableAdapter);

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
                staticEffectNum = groupPosition;
                staticStartTime = 0;
                staticEffectTime = 2;
                staticRandomTime = 0;
                expandableAdapter.setStartTime(staticEffectNum, staticStartTime);
                expandableAdapter.setEffectTime(staticEffectNum, staticEffectTime);
                expandableAdapter.setRandomTime(staticEffectNum, staticRandomTime);
                expandableAdapter.notifyDataSetChanged();
                doPattern(staticEffectNum,staticEffectTime,staticRandomTime,staticStartTime);
                return false;
            }
        });

        expandableAdapter.setOnExpandableListener(new BaseExpandableInterface() {
            @Override
            public void onStartTime(int curGroup, int startTime) {
                dialogStartTime = new DialogStartTime(getActivity(),staticStartTime);
                dialogStartTime.setOnStartTimeListener(startTimeListener);
                dialogStartTime.show();
            }

            @Override
            public void onEffectTime(int curGroup, int effectTime) {
                //mChildList.get(curGroup).timeEffectDelay = effectTime;
                expandableAdapter.notifyDataSetChanged();
                staticEffectTime = effectTime;
                doPattern(staticEffectNum,staticEffectTime,staticRandomTime,staticStartTime);
            }

            @Override
            public void onRandomTime(int curGroup, int randomTime) {
                expandableAdapter.notifyDataSetChanged();
                staticRandomTime = randomTime;
                doPattern(staticEffectNum,staticEffectTime,staticRandomTime,staticStartTime);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private final static int LED_NOT_SELECTED = 0;
    private final static int LED_SELECTED = 1;
    private final static int LED_SINGLE_ONLY_SELECTED = 2;
    private final static int LED_RGB_ONLY_SELECTED = 3;

    private int getLedSelected() {
        int ledCount = 0;
        int rgbCount = 0;
        for (int i=0;i<Define.NUMBER_OF_COLOR_LED;i++) {
            if (ledSelect.getRgb(i) == LedSelect.SelectType.SELECTED) {
                rgbCount++;
            }
        }
        for (int i=0;i<Define.NUMBER_OF_SINGLE_LED;i++) {
            if (ledSelect.getLed(i) == LedSelect.SelectType.SELECTED) {
                ledCount++;
            }
        }
        if (ledCount == 0 && rgbCount == 0) {
            return LED_NOT_SELECTED;
        } else if (ledCount != 0 && rgbCount == 0) {
            return LED_SINGLE_ONLY_SELECTED;
        } else if (ledCount == 0) {
            return LED_RGB_ONLY_SELECTED;
        } else {
            return LED_SELECTED;
        }
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
            staticStartTime = time;
            expandableAdapter.setStartTime(staticEffectNum,time);
            expandableAdapter.notifyDataSetChanged();
            doPattern(staticEffectNum,staticEffectTime,staticRandomTime,staticStartTime);
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
        staticEffectNum = 0;
        staticStartTime = 0;
        staticEffectTime = 2;
        staticRandomTime = 0;
    }

    public int getEffectNum() {
        return staticEffectNum;
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
            if (staticEffectNum > mListView.getChildCount()) {
                staticEffectNum = 0;
                staticStartTime = 0;
                staticEffectTime = 2;
                staticRandomTime = 0;
            }
            for (int i = 0; i < mListView.getChildCount(); i++) {
                if (i == staticEffectNum) {
                    mListView.expandGroup(i);
                } else {
                    mListView.collapseGroup(i);
                }
            }
            //mListView.expandGroup(staticEffectNum);

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
            mListener.onEffect(effect, staticEffectTime, staticRandomTime, staticStartTime);
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
        dialog.setContentView(R.layout.dialog_help_effect);
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
