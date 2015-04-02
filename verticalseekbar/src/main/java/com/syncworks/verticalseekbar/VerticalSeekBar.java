package com.syncworks.verticalseekbar;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

/**
 * Created by vosami on 2015-03-17.
 */
public class VerticalSeekBar extends SeekBar {

    OnVSeekBarTouchListener onVSeekBarTouchListener;

    public VerticalSeekBar(Context context) {
        super(context);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
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

    private OnSeekBarChangeListener onChangeListener;

    @Override
    public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
        //super.setOnSeekBarChangeListener(l);
        this.onChangeListener = l;
    }

    private int lastProgress = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onChangeListener.onStartTrackingTouch(this);
                setPressed(true);
                setSelected(true);
                break;
            case MotionEvent.ACTION_MOVE:
                super.onTouchEvent(event);
                int progress = getMax() - (int) (getMax() * event.getY() / getHeight());

                // Ensure progress stays within boundaries
                if(progress < 0) {progress = 0;}
                if(progress > getMax()) {progress = getMax();}
                setProgress(progress);  // Draw progress
                if(progress != lastProgress) {
                    // Only enact listener if the progress has actually changed
                    lastProgress = progress;
                    onChangeListener.onProgressChanged(this, progress, true);
                }

                onSizeChanged(getWidth(), getHeight() , 0, 0);
                setPressed(true);
                setSelected(true);
                break;
            case MotionEvent.ACTION_UP:
                onChangeListener.onStopTrackingTouch(this);
                setPressed(false);
                setSelected(false);
                /*int i=0;
                i=getMax() - (int) (getMax() * event.getY() / getHeight());
                setProgress(i);
                onSizeChanged(getWidth(), getHeight(), 0, 0);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    doVSeekBarEvent();	// 상위 View 로 값을 보냄
                }*/
                break;
            case MotionEvent.ACTION_CANCEL:
                super.onTouchEvent(event);
                setPressed(false);
                setSelected(false);
                break;
        }
        return true;
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
        onSizeChanged(getWidth(), getHeight() , 0, 0);
        if(progress != lastProgress && onChangeListener != null) {
            // Only enact listener if the progress has actually changed
            lastProgress = progress;
            onChangeListener.onProgressChanged(this, progress, true);
        }
    }

    public synchronized void setMaximum(int maximum) {
        setMax(maximum);
    }

    public synchronized int getMaximum() {
        return getMax();
    }

    public interface OnVSeekBarTouchListener {
        void onVSeekBarEvent();
    }

    public void setOnVSeekBarTouchListener(OnVSeekBarTouchListener listener) {
        onVSeekBarTouchListener = listener;
    }
    public void setOnVTouchListener(OnVSeekBarTouchListener listener) {
        onVSeekBarTouchListener = listener;
    }

    private void doVSeekBarEvent() {
        if (onVSeekBarTouchListener != null) {
            onVSeekBarTouchListener.onVSeekBarEvent();
        }
    }
}
