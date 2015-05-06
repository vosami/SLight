package com.syncworks.endlessadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by vosami on 2015-05-06.
 */
public class HttpListAdapter extends ArrayAdapter<HttpListData> {
    private List<HttpListData> objects;

    public HttpListAdapter(Context context, int resource, List<HttpListData> objects) {
        super(context, resource, objects);
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = getLayout(position);

        return convertView;
    }

    private View getLayout(int pos) {
        View retView = null;

        HttpListData thisData = objects.get(pos);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        retView = inflater.inflate(R.layout.rl_http_list,null);
        TextView tvCount = (TextView) retView.findViewById(R.id.tv_count);
        ImageView ivType = (ImageView) retView.findViewById(R.id.iv_type);
        TextView tvPatternName = (TextView) retView.findViewById(R.id.tv_pattern_name);
        ImageView ivAddScript = (ImageView) retView.findViewById(R.id.iv_add_script);
        TextView tvWriter = (TextView) retView.findViewById(R.id.tv_wr_name);
        TextView tvDate = (TextView) retView.findViewById(R.id.tv_date);

        tvCount.setText(Integer.toString(thisData.getWriteCount()));

        switch (thisData.getPatternType()) {
            case 0:
                ivType.setImageDrawable(getContext().
                        getResources().
                        getDrawable(R.drawable.ic_action_brightness_high));
                break;
            case 1:
                ivType.setImageDrawable(getContext().
                        getResources().
                        getDrawable(R.drawable.ic_action_brightness_high));
                break;
        }

        tvPatternName.setText(thisData.getPatternName());

        tvWriter.setText(thisData.getWriter());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        tvDate.setText(dateFormat.format(thisData.getDate()));

        return retView;
    }
}
