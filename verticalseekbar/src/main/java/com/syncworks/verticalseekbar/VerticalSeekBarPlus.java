package com.syncworks.verticalseekbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by vosami on 2015-03-17.
 */
public class VerticalSeekBarPlus extends RelativeLayout {

    private String title;
    private int max;
    private int progress;

    private SeekBar sbVertical;
    private Button btnPlus, btnMinus;
    private TextView tvMin, tvMax, tvVal, tvTitle;

    public VerticalSeekBarPlus(Context context) {
        this(context,null,0);
    }

    public VerticalSeekBarPlus(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public VerticalSeekBarPlus(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SeekBar_Plus, defStyleAttr, 0);
        title = a.getString(R.styleable.SeekBar_Plus_v_title);
        max = a.getInt(R.styleable.SeekBar_Plus_v_max,100);
        progress = a.getInt(R.styleable.SeekBar_Plus_v_progress,0);
        if (title == null) {
            title = "title";
        }
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.vertical_seek_bar_plus,this);
        sbVertical = (VerticalSeekBar) findViewById(R.id.v_seekbar);
        btnPlus = (Button) findViewById(R.id.v_btn_plus);
        btnMinus = (Button) findViewById(R.id.v_btn_minus);
        tvMax = (TextView) findViewById(R.id.v_max_tv);
        tvMin = (TextView) findViewById(R.id.v_min_tv);
        tvVal = (TextView) findViewById(R.id.v_val_tv);
        tvTitle = (TextView) findViewById(R.id.v_view_title);

        tvTitle.setText(title);
        sbVertical.setMax(max);
        sbVertical.setProgress(progress);
        String afterText = getResources().getString(R.string.after_text);
        float ratio;
        ratio = (float)max/10;
        tvMax.setText(String.format("%.1f%s",ratio,afterText));
        ratio = 0;
        tvMin.setText(String.format("%.1f%s",ratio,afterText));
        ratio = (float)(sbVertical.getProgress())/10;
        tvVal.setText(String.format("%.1f%s",ratio,afterText));
    }
}
