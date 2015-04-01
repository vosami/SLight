package com.syncworks.slight.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.ToggleButton;

import com.syncworks.colorpickerview.ColorPickerView;
import com.syncworks.define.Define;
import com.syncworks.slight.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.syncworks.slight.fragments.OnLedFragmentListener} interface
 * to handle interaction events.
 * Use the {@link ColorAlwaysOn#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ColorAlwaysOn extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    private int initRed;
    private int initGreen;
    private int initBlue;


    private OnLedFragmentListener mListener;

    //Widgets
    private ToggleButton tbSelect;
    private Button[] btnEasyColor = new Button[10];
    private TableLayout easyTable;
    private ColorPickerView colorPickerView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ColorAlwaysOn.
     */
    public static ColorAlwaysOn newInstance(int red, int green, int blue) {
        ColorAlwaysOn fragment = new ColorAlwaysOn();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, red);
        args.putInt(ARG_PARAM2, green);
        args.putInt(ARG_PARAM3, blue);
        fragment.setArguments(args);
        return fragment;
    }

    public ColorAlwaysOn() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            initRed = getArguments().getInt(ARG_PARAM1);
            initGreen = getArguments().getInt(ARG_PARAM2);
            initBlue = getArguments().getInt(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_color_always_on,container,false);
        tbSelect = (ToggleButton) v.findViewById(R.id.toggleButton);
        btnEasyColor[0] =(Button) v.findViewById(R.id.easy_color_0);
        btnEasyColor[1] =(Button) v.findViewById(R.id.easy_color_1);
        btnEasyColor[2] =(Button) v.findViewById(R.id.easy_color_2);
        btnEasyColor[3] =(Button) v.findViewById(R.id.easy_color_3);
        btnEasyColor[4] =(Button) v.findViewById(R.id.easy_color_4);
        btnEasyColor[5] =(Button) v.findViewById(R.id.easy_color_5);
        btnEasyColor[6] =(Button) v.findViewById(R.id.easy_color_6);
        btnEasyColor[7] =(Button) v.findViewById(R.id.easy_color_7);
        btnEasyColor[8] =(Button) v.findViewById(R.id.easy_color_8);
        btnEasyColor[9] =(Button) v.findViewById(R.id.easy_color_9);
        easyTable = (TableLayout) v.findViewById(R.id.easy_table);
        colorPickerView = (ColorPickerView) v.findViewById(R.id.color_picker);
        colorPickerView.setColor(Color.rgb(initRed, initGreen, initBlue));

        tbSelect.setOnCheckedChangeListener(tbListener);
        colorPickerView.setOnTouchListener(touchListener);
        btnEasyColor[0].setOnClickListener(clickListener);
        btnEasyColor[1].setOnClickListener(clickListener);
        btnEasyColor[2].setOnClickListener(clickListener);
        btnEasyColor[3].setOnClickListener(clickListener);
        btnEasyColor[4].setOnClickListener(clickListener);
        btnEasyColor[5].setOnClickListener(clickListener);
        btnEasyColor[6].setOnClickListener(clickListener);
        btnEasyColor[7].setOnClickListener(clickListener);
        btnEasyColor[8].setOnClickListener(clickListener);
        btnEasyColor[9].setOnClickListener(clickListener);
        return v;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int newColor;
            switch(v.getId()) {
                case R.id.easy_color_0:
                    newColor = getResources().getColor(R.color.easy_color_0_red);
                    doColorChange((newColor >> 16) & 0xFF, (newColor >> 8) & 0xFF, (newColor) & 0xFF);
                    break;
                case R.id.easy_color_1:
                    newColor = getResources().getColor(R.color.easy_color_1_orange);
                    doColorChange((newColor >> 16) & 0xFF, (newColor >> 8) & 0xFF, (newColor) & 0xFF);
                    break;
                case R.id.easy_color_2:
                    newColor = getResources().getColor(R.color.easy_color_2_yellow);
                    doColorChange((newColor >> 16) & 0xFF, (newColor >> 8) & 0xFF, (newColor) & 0xFF);
                    break;
                case R.id.easy_color_3:
                    newColor = getResources().getColor(R.color.easy_color_3_green);
                    doColorChange((newColor >> 16) & 0xFF, (newColor >> 8) & 0xFF, (newColor) & 0xFF);
                    break;
                case R.id.easy_color_4:
                    newColor = getResources().getColor(R.color.easy_color_4_blue);
                    doColorChange((newColor >> 16) & 0xFF, (newColor >> 8) & 0xFF, (newColor) & 0xFF);
                    break;
                case R.id.easy_color_5:
                    newColor = getResources().getColor(R.color.easy_color_5_navy);
                    doColorChange((newColor >> 16) & 0xFF, (newColor >> 8) & 0xFF, (newColor) & 0xFF);
                    break;
                case R.id.easy_color_6:
                    newColor = getResources().getColor(R.color.easy_color_6_purple);
                    doColorChange((newColor >> 16) & 0xFF, (newColor >> 8) & 0xFF, (newColor) & 0xFF);
                    break;
                case R.id.easy_color_7:
                    newColor = getResources().getColor(R.color.easy_color_7_black);
                    doColorChange((newColor >> 16) & 0xFF, (newColor >> 8) & 0xFF, (newColor) & 0xFF);
                    break;
                case R.id.easy_color_8:
                    newColor = getResources().getColor(R.color.easy_color_8_white);
                    doColorChange((newColor >> 16) & 0xFF, (newColor >> 8) & 0xFF, (newColor) & 0xFF);
                    break;
                case R.id.easy_color_9:
                    newColor = getResources().getColor(R.color.easy_color_9_pink);
                    doColorChange((newColor >> 16) & 0xFF, (newColor >> 8) & 0xFF, (newColor) & 0xFF);
                    break;
            }
        }
    };

    // Color Picker Touch Listener
    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    int newColor = colorPickerView.getColor();
                    doColorChange((newColor >> 16) & 0xFF, (newColor >> 8) & 0xFF, (newColor) & 0xFF);
                    break;
            }
            return false;
        }
    };

    // 토글 버튼 Listener
    private CompoundButton.OnCheckedChangeListener tbListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                easyTable.setVisibility(View.GONE);
                colorPickerView.setVisibility(View.VISIBLE);

            } else {
                easyTable.setVisibility(View.VISIBLE);
                colorPickerView.setVisibility(View.GONE);
            }
        }
    };

    // 색 변화를 Activity 에 전달
    private void doColorChange(int red, int green, int blue) {
        if (mListener != null) {
            int tRed = red * (Define.OP_CODE_MIN-1) / 255;
            int tGreen = green * (Define.OP_CODE_MIN-1) /255;
            int tBlue = blue * (Define.OP_CODE_MIN-1) / 255;
            mListener.onColorChangeAction(tRed,tGreen,tBlue);
        }
        colorPickerView.setColor(Color.rgb(red, green, blue));
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

    // Fragment 의 색상 설정
    public void setFragment(int red, int green, int blue) {
        colorPickerView.setColor(Color.rgb(red,green,blue));
    }


}
