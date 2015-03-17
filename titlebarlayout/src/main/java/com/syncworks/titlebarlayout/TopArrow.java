package com.syncworks.titlebarlayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.syncworks.define.Define;

/**
 * Created by vosami on 2015-03-17.
 * LED 타이틀 바 위의 화살 표시
 */
public class TopArrow extends View{

    private Paint paint;
    private int curLed;

    public TopArrow(Context context) {
        super(context);
        init();
    }

    public TopArrow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TopArrow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(getResources().getColor(R.color.Gradient_Blue_Start));
        paint.setStyle(Paint.Style.FILL);


        curLed = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float mWidth = getMeasuredWidth();
        float mHeight = getMeasuredHeight();
        float oneWidth = mWidth / Define.NUMBER_OF_SINGLE_LED;
        float tpX = (float) (oneWidth * 0.5 + oneWidth * curLed);
        float tpY = 0;
        float ltX = (float) (oneWidth * 0.2 + oneWidth * curLed);
        //float ltY = mHeight;
        float rtX = (float) (oneWidth * 0.8 + oneWidth * curLed);
        //float rtY = mHeight;
        Path path = new Path();
        path.moveTo(tpX, tpY);
        path.lineTo(ltX, mHeight);
        path.lineTo(rtX, mHeight);
        path.lineTo(tpX, tpY);
        path.close();
        canvas.drawPath(path, paint);
    }

    // 화살표 위치 설정
    public void setLedNumber(int ledNum) {
        if ((ledNum & Define.SELECTED_COLOR_LED1) == Define.SELECTED_COLOR_LED1) {
            curLed = 1;
        } else if ((ledNum & Define.SELECTED_COLOR_LED2) == Define.SELECTED_COLOR_LED2) {
            curLed = 4;
        } else if ((ledNum & Define.SELECTED_COLOR_LED3) == Define.SELECTED_COLOR_LED3) {
            curLed = 7;
        } else if ((ledNum & Define.SELECTED_LED1) == Define.SELECTED_LED1) {
            curLed = 0;
        } else if ((ledNum & Define.SELECTED_LED2) == Define.SELECTED_LED2) {
            curLed = 1;
        } else if ((ledNum & Define.SELECTED_LED3) == Define.SELECTED_LED3) {
            curLed = 2;
        } else if ((ledNum & Define.SELECTED_LED4) == Define.SELECTED_LED4) {
            curLed = 3;
        } else if ((ledNum & Define.SELECTED_LED5) == Define.SELECTED_LED5) {
            curLed = 4;
        } else if ((ledNum & Define.SELECTED_LED6) == Define.SELECTED_LED6) {
            curLed = 5;
        } else if ((ledNum & Define.SELECTED_LED7) == Define.SELECTED_LED7) {
            curLed = 6;
        } else if ((ledNum & Define.SELECTED_LED8) == Define.SELECTED_LED8) {
            curLed = 7;
        } else if ((ledNum & Define.SELECTED_LED9) == Define.SELECTED_LED9) {
            curLed = 8;
        }
        invalidate();
    }
}
