package com.syncworks.endlessadapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.List;

/**
 * Created by vosami on 2015-05-06.
 * EndlessListView
 */
public class EndlessListView extends ListView implements AbsListView.OnScrollListener {
    private View footer;
    private boolean isLoading;
    private EndlessListener listener;
    private HttpListAdapter adapter;

    public EndlessListView(Context context) {
        super(context);
        this.setOnScrollListener(this);
    }

    public EndlessListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnScrollListener(this);
    }

    public EndlessListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (getAdapter() == null) {
            return;
        }

        if (getAdapter().getCount() == 0) {
            return;
        }

        int l = visibleItemCount + firstVisibleItem;
        if (l >= totalItemCount && !isLoading) {
            // It is time to add new data. We call the listener
            this.addFooterView(footer);
            isLoading = true;
            listener.loadData();
        }
    }

    public void setListener(EndlessListener listener) {
        this.listener = listener;
    }

    public EndlessListener getListener() {
        return listener;
    }

    public void setLoadingView(int resId) {
        LayoutInflater inflater = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footer = (View) inflater.inflate(resId, null);
        this.addFooterView(footer);
    }

    public void setAdapter(HttpListAdapter adapter) {
        super.setAdapter(adapter);
        this.adapter = adapter;
        this.removeFooterView(footer);
    }

    public void addNewData(List<HttpListData> data) {

        this.removeFooterView(footer);

        adapter.addAll(data);
        adapter.notifyDataSetChanged();
        isLoading = false;
    }

    public static interface EndlessListener {
        public void loadData() ;
    }
}
