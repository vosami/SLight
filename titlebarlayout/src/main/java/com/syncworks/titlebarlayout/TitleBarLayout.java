package com.syncworks.titlebarlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.syncworks.define.Define;

/**
 * Created by vosami on 2015-03-17.
 */
public class TitleBarLayout extends LinearLayout {
    // 현재 선택된 LED 표시
    private int selectedLed = 0;
    private TextView tvTitle;
    private TopArrow topArrow;

    public TitleBarLayout(Context context) {
        super(context);
        init(context);
    }

    public TitleBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TitleBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        // 레이아웃 인플레이트
        LayoutInflater.from(context).inflate(R.layout.title_bar_layout, this);
        // 세로 방향 정렬
        this.setOrientation(VERTICAL);
        // 초기 선택 LED 는 LED1
        selectedLed = Define.SELECTED_LED1;
        // 타이틀 텍스트
        tvTitle = (TextView) findViewById(R.id.led_title);
        // 상단 화살 표시
        topArrow = (TopArrow) findViewById(R.id.top_arrow);
        // 타이틀 텍스트와 화살표 설정
        setLedNumber(selectedLed);
    }

    /**
     * 타이틀 텍스트와 화살표 설정
     * @param ledNum 선택된 LED
     */
    public void setLedNumber(int ledNum) {
        setTvTitle(ledNum);
        topArrow.setLedNumber(ledNum);
    }

    private void setTvTitle(int ledNum) {
        String titleStr = "";
        if ((ledNum & Define.SELECTED_COLOR_LED1) == Define.SELECTED_COLOR_LED1) {
            if (titleStr.length() != 0) {
                titleStr = titleStr + "," + getResources().getString(R.string.cled1_txt);
            } else {
                titleStr = getResources().getString(R.string.cled1_txt);
            }
        } else {
            if ((ledNum & Define.SELECTED_LED1) != 0) {
                if (titleStr.length() != 0) {
                    titleStr = titleStr + "," + getResources().getString(R.string.led1_txt);
                } else {
                    titleStr = getResources().getString(R.string.led1_txt);
                }
            }
            if ((ledNum & Define.SELECTED_LED2) != 0) {
                if (titleStr.length() != 0) {
                    titleStr = titleStr + "," + getResources().getString(R.string.led2_txt);
                } else {
                    titleStr = getResources().getString(R.string.led2_txt);
                }
            }
            if ((ledNum & Define.SELECTED_LED3) != 0) {
                if (titleStr.length() != 0) {
                    titleStr = titleStr + "," + getResources().getString(R.string.led3_txt);
                } else {
                    titleStr = getResources().getString(R.string.led3_txt);
                }
            }
        }
        if ((ledNum & Define.SELECTED_COLOR_LED2) == Define.SELECTED_COLOR_LED2) {
            if (titleStr.length() != 0) {
                titleStr = titleStr + "," + getResources().getString(R.string.cled2_txt);
            } else {
                titleStr = getResources().getString(R.string.cled2_txt);
            }
        } else {
            if ((ledNum & Define.SELECTED_LED4) != 0) {
                if (titleStr.length() != 0) {
                    titleStr = titleStr + "," + getResources().getString(R.string.led4_txt);
                } else {
                    titleStr = getResources().getString(R.string.led4_txt);
                }
            }
            if ((ledNum & Define.SELECTED_LED5) != 0) {
                if (titleStr.length() != 0) {
                    titleStr = titleStr + "," + getResources().getString(R.string.led5_txt);
                } else {
                    titleStr = getResources().getString(R.string.led5_txt);
                }
            }
            if ((ledNum & Define.SELECTED_LED6) != 0) {
                if (titleStr.length() != 0) {
                    titleStr = titleStr + "," + getResources().getString(R.string.led6_txt);
                } else {
                    titleStr = getResources().getString(R.string.led6_txt);
                }
            }
        }
        if ((ledNum & Define.SELECTED_COLOR_LED3) == Define.SELECTED_COLOR_LED3) {
            if (titleStr.length() != 0) {
                titleStr = titleStr + "," + getResources().getString(R.string.cled3_txt);
            } else {
                titleStr = getResources().getString(R.string.cled3_txt);
            }
        } else {
            if ((ledNum & Define.SELECTED_LED7) != 0) {
                if (titleStr.length() != 0) {
                    titleStr = titleStr + "," + getResources().getString(R.string.led7_txt);
                } else {
                    titleStr = getResources().getString(R.string.led7_txt);
                }
            }
            if ((ledNum & Define.SELECTED_LED8) != 0) {
                if (titleStr.length() != 0) {
                    titleStr = titleStr + "," + getResources().getString(R.string.led8_txt);
                } else {
                    titleStr = getResources().getString(R.string.led8_txt);
                }
            }
            if ((ledNum & Define.SELECTED_LED9) != 0) {
                if (titleStr.length() != 0) {
                    titleStr = titleStr + "," + getResources().getString(R.string.led9_txt);
                } else {
                    titleStr = getResources().getString(R.string.led9_txt);
                }
            }
        }
        tvTitle.setText(titleStr);
    }
}
