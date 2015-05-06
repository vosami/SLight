package com.syncworks.slight.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.syncworks.slight.R;

/**
 * Created by vosami on 2015-05-06.
 */
public class FragmentColorScriptList extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View android = inflater.inflate(R.layout.fragment_script_data2, container, false);
        ((TextView) android.findViewById(R.id.tv_string)).setText("Tab2");
        return android;
    }
}
