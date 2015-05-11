package com.syncworks.endlessadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by vosami on 2015-05-06.
 */
public class HttpListAdapter extends ArrayAdapter<HttpListData> {
    private List<HttpListData> objects;
    private OnHttpListEventListener onHttpListEventListener;

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
        RelativeLayout rlHttpList = (RelativeLayout) retView.findViewById(R.id.rl_http_list);
        final TextView tvCount = (TextView) retView.findViewById(R.id.tv_count);
        ImageView ivType = (ImageView) retView.findViewById(R.id.iv_type);
        TextView tvPatternName = (TextView) retView.findViewById(R.id.tv_pattern_name);
        Button btnAddScript = (Button) retView.findViewById(R.id.iv_add_script);
        TextView tvWriter = (TextView) retView.findViewById(R.id.tv_wr_name);
        TextView tvDate = (TextView) retView.findViewById(R.id.tv_date);

        tvCount.setText(Integer.toString(thisData.getWriteCount()));

        rlHttpList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("test", "httplist" + tvCount.getText());
                String wrCount = tvCount.getText().toString();
                doListClickEvent(Integer.parseInt(wrCount));
            }
        });

        btnAddScript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("test", "addscript"+tvCount.getText());
                String wrCount = tvCount.getText().toString();
                doAddClickEvent(Integer.parseInt(wrCount));
            }
        });

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

    public interface OnHttpListEventListener {
        void onListClickEvent(int wrCount);
        void onAddClickEvent(int wrCount);
    }

    public void setOnHttpListEventListener(OnHttpListEventListener listener) {
        onHttpListEventListener = listener;
    }

    public void doListClickEvent(int wrCount) {
        if (onHttpListEventListener != null) {
            onHttpListEventListener.onListClickEvent(wrCount);
        }
    }

    public void doAddClickEvent(int wrCount) {
        if (onHttpListEventListener != null) {
            onHttpListEventListener.onAddClickEvent(wrCount);
        }
    }
}
