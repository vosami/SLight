package com.syncworks.scriptdata;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by vosami on 2015-03-23.
 * 스피너에 출력되는 어댑터
 */
public class ScriptDataListSpinnerAdapter extends ArrayAdapter<String>{
    Context context = null;
    String[] items = null;

    // 생성자
    public ScriptDataListSpinnerAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        this.context = context;
        this.items = objects;
    }

    /**
     * 스피너 클릭시 보여지는 View 정의
     */
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(items[position]);
        tv.setTextColor(Color.RED);
        tv.setTextSize(20);
        return convertView;
    }

    /**
     * 기본 스피너 View 정의
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(
                    android.R.layout.simple_spinner_item, parent, false);
        }

        TextView tv = (TextView) convertView
                .findViewById(android.R.id.text1);
        tv.setText(items[position]);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(15);
        return convertView;
    }
}
