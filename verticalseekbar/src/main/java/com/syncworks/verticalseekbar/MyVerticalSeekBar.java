package com.syncworks.verticalseekbar;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.SeekBar;

/**
 * Created by vosami on 2015-06-12.
 */
public class MyVerticalSeekBar extends SeekBar {

    private OnVerticalSeekBarListener onVerticalSeekBarListener;

    public MyVerticalSeekBar(Context context) {
        super(context);
    }

    public MyVerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyVerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    protected void onDraw(Canvas c) {
        c.rotate(-90);
        c.translate(-getHeight(),0);

        super.onDraw(c);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        int i=0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                i=getMax() - (int) (getMax() * event.getY() / getHeight());
                setProgress(i);
                Log.i("Progress", getProgress() + "");
                onSizeChanged(getWidth(), getHeight(), 0, 0);
                break;
            case MotionEvent.ACTION_UP:
                i=getMax() - (int) (getMax() * event.getY() / getHeight());
                setProgress(i);
                Log.i("Progress", getProgress() + "");
                onSizeChanged(getWidth(), getHeight(), 0, 0);
                doChange(getId(),getProgress());
                break;

            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    public interface OnVerticalSeekBarListener {
        void onEvent(int id, int progress);
    }

    private void doChange(int id, int progress) {
        if (onVerticalSeekBarListener != null) {
            onVerticalSeekBarListener.onEvent(id, progress);
        }
    }

    public void setOnVerticalSeekBarListener(OnVerticalSeekBarListener listener) {
        onVerticalSeekBarListener = listener;
    }
}
