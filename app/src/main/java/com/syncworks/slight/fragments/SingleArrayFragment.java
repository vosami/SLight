package com.syncworks.slight.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SingleArrayFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        return inflater.inflate(R.layout.fragment_single_array, container, false);
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
