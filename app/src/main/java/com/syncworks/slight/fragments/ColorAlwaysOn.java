package com.syncworks.slight.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.ToggleButton;

import com.syncworks.colorpickerview.ColorPickerView;
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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ColorAlwaysOn.
     */
    // TODO: Rename and change types and number of parameters
    public static ColorAlwaysOn newInstance(String param1, String param2) {
        ColorAlwaysOn fragment = new ColorAlwaysOn();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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

        tbSelect.setOnCheckedChangeListener(tbListener);
        return v;
    }

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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onColorChangeAction(100,0,0);
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
