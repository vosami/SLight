package com.syncworks.verticalseekbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

/**
 * Created by vosami on 2015-03-17.
 */
public class VerticalSeekBarPlus extends RelativeLayout {
    public VerticalSeekBarPlus(Context context) {
        super(context);
        init(context);
    }

    public VerticalSeekBarPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VerticalSeekBarPlus(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.vertical_seek_bar_plus,this);

    }
}
