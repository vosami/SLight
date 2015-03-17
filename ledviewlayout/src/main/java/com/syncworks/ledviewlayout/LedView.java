package com.syncworks.ledviewlayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.syncworks.define.Define;

/**
 * Created by vosami on 2015-03-17.
 * LED 점멸 효과를 보여주는 View
 */
public class LedView extends View {

    Paint paint;
    private int oldRed;
    private int oldGreen;
    private int oldBlue;

    public LedView(Context context) {
        super(context);
        init();
    }

    public LedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.rgb(0, 0, 0));
        paint.setStyle(Paint.Style.FILL);

        oldRed = 0;
        oldGreen = 0;
        oldBlue = 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // width 진짜 크기 구하기
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = 0;
        switch(widthMode) {
            case MeasureSpec.UNSPECIFIED:    // mode 가 셋팅되지 않은 크기가 넘어올때
                widthSize = widthMeasureSpec;
                break;
            case MeasureSpec.AT_MOST:        // wrap_content (뷰 내부의 크기에 따라 크기가 달라짐)
                widthSize = 100;
                break;
            case MeasureSpec.EXACTLY:        // fill_parent, match_parent (외부에서 이미 크기가 지정되었음)
                widthSize = MeasureSpec.getSize(widthMeasureSpec);
                break;
        }

        // height 진짜 크기 구하기
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = 0;
        switch(heightMode) {
            case MeasureSpec.UNSPECIFIED:    // mode 가 셋팅되지 않은 크기가 넘어올때
                heightSize = heightMeasureSpec;
                break;
            case MeasureSpec.AT_MOST:        // wrap_content (뷰 내부의 크기에 따라 크기가 달라짐)
                heightSize = widthSize/Define.NUMBER_OF_SINGLE_LED;
                break;
            case MeasureSpec.EXACTLY:        // fill_parent, match_parent (외부에서 이미 크기가 지정되었음)
                heightSize = MeasureSpec.getSize(heightMeasureSpec);
                break;
        }

        setMeasuredDimension(widthSize, heightSize);
    }

    // LED 그리기 메소드
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float mWidth = getMeasuredWidth();
        float mHeight = getMeasuredHeight();
        float mRadius;
        if (mWidth >= mHeight) {
            mRadius = (float) (mHeight * 0.3);
        } else {
            mRadius = (float) (mWidth * 0.3);
        }


        canvas.drawCircle((float)(mWidth*0.5),
                (float)(mHeight*0.5),mRadius,paint);
    }

    // LED 색깔을 지정하는 함수
    public void setLedColor(int mRed, int mGreen, int mBlue) {
        if (oldRed !=mRed || oldGreen != mGreen || oldBlue != mBlue) {
            paint.setColor(Color.rgb(mRed, mGreen, mBlue));
            oldRed = mRed;
            oldGreen = mGreen;
            oldBlue = mBlue;
            invalidate();
        }
    }
}
