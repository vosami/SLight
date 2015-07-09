package com.syncworks.slight.fragment_easy;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.syncworks.define.Define;
import com.syncworks.slight.R;
import com.syncworks.slight.widget.CustomVerticalSeekBar;


public class BrightFragment extends Fragment {

    Button btnLed[] = new Button[Define.NUMBER_OF_SINGLE_LED];
    Button btnRgb[] = new Button[Define.NUMBER_OF_COLOR_LED];
    CustomVerticalSeekBar seekLed[] = new CustomVerticalSeekBar[Define.NUMBER_OF_SINGLE_LED];
    CustomVerticalSeekBar seekRgb[] = new CustomVerticalSeekBar[Define.NUMBER_OF_SINGLE_LED];
    RelativeLayout rlLed[] = new RelativeLayout[Define.NUMBER_OF_SINGLE_LED];
    RelativeLayout rlRgb[] = new RelativeLayout[Define.NUMBER_OF_COLOR_LED];
    TextView tvLed[] = new TextView[Define.NUMBER_OF_SINGLE_LED];
    TextView tvRgb[] = new TextView[Define.NUMBER_OF_SINGLE_LED];
    Button btnColorSelect[] = new Button[Define.NUMBER_OF_COLOR_LED];

    private OnEasyFragmentListener mListener;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bright,container,false);
        btnLed[0] = (Button) view.findViewById(R.id.led_1);
        btnLed[1] = (Button) view.findViewById(R.id.led_2);
        btnLed[2] = (Button) view.findViewById(R.id.led_3);
        btnLed[3] = (Button) view.findViewById(R.id.led_4);
        btnLed[4] = (Button) view.findViewById(R.id.led_5);
        btnLed[5] = (Button) view.findViewById(R.id.led_6);
        btnLed[6] = (Button) view.findViewById(R.id.led_7);
        btnLed[7] = (Button) view.findViewById(R.id.led_8);
        btnLed[8] = (Button) view.findViewById(R.id.led_9);
        btnRgb[0] = (Button) view.findViewById(R.id.rgb_1);
        btnRgb[1] = (Button) view.findViewById(R.id.rgb_2);
        btnRgb[2] = (Button) view.findViewById(R.id.rgb_3);
        seekLed[0] = (CustomVerticalSeekBar) view.findViewById(R.id.led_1_seekbar);
        seekLed[1] = (CustomVerticalSeekBar) view.findViewById(R.id.led_2_seekbar);
        seekLed[2] = (CustomVerticalSeekBar) view.findViewById(R.id.led_3_seekbar);
        seekLed[3] = (CustomVerticalSeekBar) view.findViewById(R.id.led_4_seekbar);
        seekLed[4] = (CustomVerticalSeekBar) view.findViewById(R.id.led_5_seekbar);
        seekLed[5] = (CustomVerticalSeekBar) view.findViewById(R.id.led_6_seekbar);
        seekLed[6] = (CustomVerticalSeekBar) view.findViewById(R.id.led_7_seekbar);
        seekLed[7] = (CustomVerticalSeekBar) view.findViewById(R.id.led_8_seekbar);
        seekLed[8] = (CustomVerticalSeekBar) view.findViewById(R.id.led_9_seekbar);
        seekRgb[0] = (CustomVerticalSeekBar) view.findViewById(R.id.rgb1_red_seekbar);
        seekRgb[1] = (CustomVerticalSeekBar) view.findViewById(R.id.rgb1_green_seekbar);
        seekRgb[2] = (CustomVerticalSeekBar) view.findViewById(R.id.rgb1_blue_seekbar);
        seekRgb[3] = (CustomVerticalSeekBar) view.findViewById(R.id.rgb2_red_seekbar);
        seekRgb[4] = (CustomVerticalSeekBar) view.findViewById(R.id.rgb2_green_seekbar);
        seekRgb[5] = (CustomVerticalSeekBar) view.findViewById(R.id.rgb2_blue_seekbar);
        seekRgb[6] = (CustomVerticalSeekBar) view.findViewById(R.id.rgb3_red_seekbar);
        seekRgb[7] = (CustomVerticalSeekBar) view.findViewById(R.id.rgb3_green_seekbar);
        seekRgb[8] = (CustomVerticalSeekBar) view.findViewById(R.id.rgb3_blue_seekbar);
        rlLed[0] = (RelativeLayout) view.findViewById(R.id.rl_led_1);
        rlLed[1] = (RelativeLayout) view.findViewById(R.id.rl_led_2);
        rlLed[2] = (RelativeLayout) view.findViewById(R.id.rl_led_3);
        rlLed[3] = (RelativeLayout) view.findViewById(R.id.rl_led_4);
        rlLed[4] = (RelativeLayout) view.findViewById(R.id.rl_led_5);
        rlLed[5] = (RelativeLayout) view.findViewById(R.id.rl_led_6);
        rlLed[6] = (RelativeLayout) view.findViewById(R.id.rl_led_7);
        rlLed[7] = (RelativeLayout) view.findViewById(R.id.rl_led_8);
        rlLed[8] = (RelativeLayout) view.findViewById(R.id.rl_led_9);
        rlRgb[0] = (RelativeLayout) view.findViewById(R.id.rl_rgb_1);
        rlRgb[1] = (RelativeLayout) view.findViewById(R.id.rl_rgb_2);
        rlRgb[2] = (RelativeLayout) view.findViewById(R.id.rl_rgb_3);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onBrightLed(1,1);
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


}
