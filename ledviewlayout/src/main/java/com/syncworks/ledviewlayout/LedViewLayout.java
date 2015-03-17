package com.syncworks.ledviewlayout;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.syncworks.define.Define;

/**
 * Created by vosami on 2015-03-17.
 * LED 점멸 패턴을 보여주는 레이아웃
 */
public class LedViewLayout extends LinearLayout{
    // 각 Led 의 밝기 값 저장 변수
    private int[] ledColorData = new int[9];
    // 화면에 출력될 View 등록
    private LedView[] ledViews = new LedView[9];
    // 활성화된 LED 확인(Color/Single)
    private boolean activateBool[] = new boolean[3];

    public LedViewLayout(Context context) {
        super(context);
        init(context);
    }

    public LedViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LedViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        // LED 초기 밝기 0으로 설정
        ledColorData[0] = 0;
        ledColorData[1] = 0;
        ledColorData[2] = 0;
        ledColorData[3] = 0;
        ledColorData[4] = 0;
        ledColorData[5] = 0;
        ledColorData[6] = 0;
        ledColorData[7] = 0;
        ledColorData[8] = 0;
        // 단색 LED 로 설정
        activateBool[0] = Define.SINGLE_LED;
        activateBool[1] = Define.SINGLE_LED;
        activateBool[2] = Define.SINGLE_LED;

        LayoutInflater.from(context).inflate(R.layout.led_layout, this);
        this.setOrientation(HORIZONTAL);
        this.setBackgroundColor(Color.rgb(0,0,0));
        /*LinearLayout.LayoutParams paramControl = (LayoutParams) this.getLayoutParams();
        paramControl.setMarginStart(10);
        paramControl.setMarginEnd(10);*/

        ledViews[0] = (LedView) findViewById(R.id.led_blink1);
        ledViews[1] = (LedView) findViewById(R.id.led_blink2);
        ledViews[2] = (LedView) findViewById(R.id.led_blink3);
        ledViews[3] = (LedView) findViewById(R.id.led_blink4);
        ledViews[4] = (LedView) findViewById(R.id.led_blink5);
        ledViews[5] = (LedView) findViewById(R.id.led_blink6);
        ledViews[6] = (LedView) findViewById(R.id.led_blink7);
        ledViews[7] = (LedView) findViewById(R.id.led_blink8);
        ledViews[8] = (LedView) findViewById(R.id.led_blink9);
        reDraw();
    }

    // 단색 LED 컬러 LED 활성화 함수
    public void setActivateBool(int idx, boolean bool) {
        activateBool[idx] = bool;
    }

    // LED 밝기 설정 함수
    public void setLedColorData(int idx, int color) {
        ledColorData[idx] = color;
    }

    public void reDraw() {
        // LED 1,2,3 이 단색 LED 로 선택되었다면
        if (activateBool[0]) {
            ledViews[0].setLedColor(ledColorData[0],ledColorData[0],ledColorData[0]);
            ledViews[1].setLedColor(ledColorData[1],ledColorData[1],ledColorData[1]);
            ledViews[2].setLedColor(ledColorData[2],ledColorData[2],ledColorData[2]);
        }
        // LED1,2,3이 Color LED 로 선택되었다면
        else {
            ledViews[0].setLedColor(0,0,0);// 검정 표시
            ledViews[1].setLedColor(ledColorData[0],ledColorData[1],ledColorData[2]);
            ledViews[2].setLedColor(0,0,0);// 검정 표시
        }
        // LED4,5,6이 Single LED 로 선택되었다면
        if (activateBool[1]) {
            ledViews[3].setLedColor(ledColorData[3],ledColorData[3],ledColorData[3]);
            ledViews[4].setLedColor(ledColorData[4],ledColorData[4],ledColorData[4]);
            ledViews[5].setLedColor(ledColorData[5],ledColorData[5],ledColorData[5]);
        }
        // LED4,5,6이 Color LED 로 선택되었다면
        else {
            ledViews[3].setLedColor(0,0,0);// 검정 표시
            ledViews[4].setLedColor(ledColorData[3],ledColorData[4],ledColorData[5]);
            ledViews[5].setLedColor(0,0,0);// 검정 표시
        }
        // LED7,8,9이 Single LED 로 선택되었다면
        if (activateBool[2]) {
            ledViews[6].setLedColor(ledColorData[6],ledColorData[6],ledColorData[6]);
            ledViews[7].setLedColor(ledColorData[7],ledColorData[7],ledColorData[7]);
            ledViews[8].setLedColor(ledColorData[8],ledColorData[8],ledColorData[8]);
        }
        // LED7,8,9이 Color LED 로 선택되었다면
        else {
            ledViews[6].setLedColor(0,0,0);// 검정 표시
            ledViews[7].setLedColor(ledColorData[6],ledColorData[7],ledColorData[8]);
            ledViews[8].setLedColor(0,0,0);// 검정 표시
        }
    }
}
