package com.syncworks.verticalseekbar;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

/**
 * Created by vosami on 2015-03-17.
 */
public class VerticalSeekBar2 extends SeekBar {

    OnVSeekBarTouchListener onVSeekBarTouchListener;

    public VerticalSeekBar2(Context context) {
        super(context);
    }

    public VerticalSeekBar2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalSeekBar2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.rotate(-90);
        canvas.translate(-getHeight(),0);
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                int i=0;
                i=getMax() - (int) (getMax() * event.getY() / getHeight());
                setProgress(i);
                onSizeChanged(getWidth(), getHeight(), 0, 0);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    doVSeekBarEvent();	// 상위 View 로 값을 보냄
                }
                break;
        }
        return true;
    }

    public interface OnVSeekBarTouchListener {
        void onVSeekBarEvent();
    }

    public void setOnVSeekBarTouchListener(OnVSeekBarTouchListener listener) {
        onVSeekBarTouchListener = listener;
    }

    private void doVSeekBarEvent() {
        if (onVSeekBarTouchListener != null) {
            onVSeekBarTouchListener.onVSeekBarEvent();
        }
    }
}
