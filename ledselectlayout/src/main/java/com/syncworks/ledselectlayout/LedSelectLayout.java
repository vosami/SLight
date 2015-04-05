package com.syncworks.ledselectlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.syncworks.define.Define;

/**
 * Created with Android Studio
 * Copyrights (C)SyncWorks All rights reserved by SyncWorks
 * Created by 승현 on 2015-03-16.
 */
public class LedSelectLayout extends LinearLayout {
	private final static String TAG = LedSelectLayout.class.getSimpleName();
    // Activity 와 통신할 수 있는 리스너 설정
    private OnLedSelectListener onLedSelectListener;


	// 체크박스
	private CheckBox[] cbColor;
	private CheckBox[] cbSingle;

    // 현재 활성화된 LED 그룹
    private int enabledLedGroup;

	/**
	 * 소스상에서 생성할 때 사용됨
	 * @param context 어플리케이션 정보 데이터
	 */
	public LedSelectLayout(Context context) {
		super(context);
		init(context);
	}

	/**
	 * XML 을 통해 생성할 때 사용됨
	 * @param context 어플리케이션 정보 데이터
	 * @param attrs attribute 확인
	 */
	public LedSelectLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 * XML을 통해 생성하면서 Style 도 함께 적용되어 있을 때 사용됨
	 * @param context 어플리케이션 정보 데이터
	 * @param attrs attribute 확인
	 * @param defStyleAttr 스타일 인자 (0이면 No Style)
	 */
	public LedSelectLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		// 레이아웃 Inflate
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.led_select_layout,this);
        // 활성화된 LED 는 Single LED로 설정
        enabledLedGroup = Define.SINGLE_LED_123_ACTIVATE | Define.SINGLE_LED_456_ACTIVATE | Define.SINGLE_LED_789_ACTIVATE;

		// Smart Light 배경 색깔 적용
		int lecBackgroundColor = getResources().getColor(R.color.LecBackground);
		this.setBackgroundColor(lecBackgroundColor);
		this.setOrientation(VERTICAL);
		// Color LED 커넥터 갯수 설정
		cbColor = new CheckBox[Define.NUMBER_OF_COLOR_LED];
		// Single LED 커넥터 갯수 설정
		cbSingle = new CheckBox[Define.NUMBER_OF_SINGLE_LED];

		// Color 커넥터 컨트롤 획득
		cbColor[0] = (CheckBox) findViewById(R.id.id_color_led_1);
		cbColor[1] = (CheckBox) findViewById(R.id.id_color_led_2);
		cbColor[2] = (CheckBox) findViewById(R.id.id_color_led_3);

		// 클릭시 행동 설정
		cbColor[0].setOnClickListener(colorClickListener);
		cbColor[1].setOnClickListener(colorClickListener);
		cbColor[2].setOnClickListener(colorClickListener);

		// 롱클릭시 행동 설정
		cbColor[0].setOnLongClickListener(colorLongClickListener);
		cbColor[1].setOnLongClickListener(colorLongClickListener);
		cbColor[2].setOnLongClickListener(colorLongClickListener);

		// Single 커넥터 컨트롤 획득
		cbSingle[0] = (CheckBox) findViewById(R.id.id_single_led_1);
		cbSingle[1] = (CheckBox) findViewById(R.id.id_single_led_2);
		cbSingle[2] = (CheckBox) findViewById(R.id.id_single_led_3);
		cbSingle[3] = (CheckBox) findViewById(R.id.id_single_led_4);
		cbSingle[4] = (CheckBox) findViewById(R.id.id_single_led_5);
		cbSingle[5] = (CheckBox) findViewById(R.id.id_single_led_6);
		cbSingle[6] = (CheckBox) findViewById(R.id.id_single_led_7);
		cbSingle[7] = (CheckBox) findViewById(R.id.id_single_led_8);
		cbSingle[8] = (CheckBox) findViewById(R.id.id_single_led_9);

		// 클릭시 행동 설정
		cbSingle[0].setOnClickListener(singleClickListener);
		cbSingle[1].setOnClickListener(singleClickListener);
		cbSingle[2].setOnClickListener(singleClickListener);
		cbSingle[3].setOnClickListener(singleClickListener);
		cbSingle[4].setOnClickListener(singleClickListener);
		cbSingle[5].setOnClickListener(singleClickListener);
		cbSingle[6].setOnClickListener(singleClickListener);
		cbSingle[7].setOnClickListener(singleClickListener);
		cbSingle[8].setOnClickListener(singleClickListener);

		// 롱클릭시 행동 설정
		cbSingle[0].setOnLongClickListener(singleLongClickListener);
		cbSingle[1].setOnLongClickListener(singleLongClickListener);
		cbSingle[2].setOnLongClickListener(singleLongClickListener);
		cbSingle[3].setOnLongClickListener(singleLongClickListener);
		cbSingle[4].setOnLongClickListener(singleLongClickListener);
		cbSingle[5].setOnLongClickListener(singleLongClickListener);
		cbSingle[6].setOnLongClickListener(singleLongClickListener);
		cbSingle[7].setOnLongClickListener(singleLongClickListener);
		cbSingle[8].setOnLongClickListener(singleLongClickListener);

        // 최초에 활성화 상태
        setBtnSelected(R.id.id_single_led_1);
	}

	// Color 커넥터 클릭시 행동
	private OnClickListener colorClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			((CheckBox)v).setChecked(false);
			int i = v.getId();
			setBtnSelected(i);
			if (i == R.id.id_color_led_1) {
				setBtnActivated(Define.SINGLE_LED_123_ACTIVATE, Define.COLOR_LED);
                doLedSelect(Define.COLOR_LED, Define.SELECTED_COLOR_LED1);
			} else if (i == R.id.id_color_led_2) {
				setBtnActivated(Define.SINGLE_LED_456_ACTIVATE, Define.COLOR_LED);
                doLedSelect(Define.COLOR_LED, Define.SELECTED_COLOR_LED2);
			} else if (i == R.id.id_color_led_3) {
				setBtnActivated(Define.SINGLE_LED_789_ACTIVATE, Define.COLOR_LED);
                doLedSelect(Define.COLOR_LED, Define.SELECTED_COLOR_LED3);
			}
		}
	};

	// Color 커넥터 롱클릭시 행동
	private OnLongClickListener colorLongClickListener = new OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
            setBtnActivated(Define.SINGLE_LED_123_ACTIVATE, Define.COLOR_LED);
			setBtnActivated(Define.SINGLE_LED_456_ACTIVATE, Define.COLOR_LED);
			setBtnActivated(Define.SINGLE_LED_789_ACTIVATE, Define.COLOR_LED);
			// Single LED 는 모두 체크 해제 선택 해제
			for (int i=0;i<Define.NUMBER_OF_SINGLE_LED;i++) {
				cbSingle[i].setChecked(false);
				cbSingle[i].setSelected(false);
			}
			// 현재 커넥터가 체크되어 있다면 체크 해제
			if (((CheckBox)v).isChecked()) {
				((CheckBox)v).setChecked(false);
			}
			// 현재 커넥터가 체크되어 있지 않다면 체크 설정
			else {
				((CheckBox)v).setChecked(true);
			}

            int checkedLed = 0;
			int getCheckNumber = 0;
			// Color LED 체크 갯수 확인
			for (int i=0;i<Define.NUMBER_OF_COLOR_LED;i++) {
				cbColor[i].setSelected(false);
				if (cbColor[i].isChecked()) {
					getCheckNumber++;
                    switch (i) {
                        case 0:
                            checkedLed = checkedLed | Define.SELECTED_COLOR_LED1;
                            break;
                        case 1:
                            checkedLed = checkedLed | Define.SELECTED_COLOR_LED2;
                            break;
                        case 2:
                            checkedLed = checkedLed | Define.SELECTED_COLOR_LED3;
                            break;
                    }
				}
			}
			// 체크되어 있는 것이 없다면 선택 설정
			if (getCheckNumber == 0) {
				v.setSelected(true);
                int id = v.getId();
                int selectedLed = 0;
                if (id == R.id.id_color_led_1) {
                    selectedLed = Define.SELECTED_COLOR_LED1;
                } else if (id == R.id.id_color_led_2) {
                    selectedLed = Define.SELECTED_COLOR_LED2;
                } else if (id == R.id.id_color_led_3) {
                    selectedLed = Define.SELECTED_COLOR_LED3;
                }
                doLedSelect(Define.COLOR_LED, selectedLed);
			} else {
                doLedCheck(Define.COLOR_LED, checkedLed);
            }


			return true;
		}
	};

    // Single 커넥터 클릭시 행동
	private OnClickListener singleClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			((CheckBox)v).setChecked(false);
			int i = v.getId();
			setBtnSelected(i);
			if (i==R.id.id_single_led_1) {
				setBtnActivated(Define.SINGLE_LED_123_ACTIVATE, Define.SINGLE_LED);
                doLedSelect(Define.SINGLE_LED, Define.SELECTED_LED1);
			} else if (i == R.id.id_single_led_2) {
				setBtnActivated(Define.SINGLE_LED_123_ACTIVATE, Define.SINGLE_LED);
                doLedSelect(Define.SINGLE_LED, Define.SELECTED_LED2);
			} else if (i == R.id.id_single_led_3) {
				setBtnActivated(Define.SINGLE_LED_123_ACTIVATE, Define.SINGLE_LED);
                doLedSelect(Define.SINGLE_LED, Define.SELECTED_LED3);
			} else if (i == R.id.id_single_led_4) {
				setBtnActivated(Define.SINGLE_LED_456_ACTIVATE, Define.SINGLE_LED);
                doLedSelect(Define.SINGLE_LED, Define.SELECTED_LED4);
			} else if (i == R.id.id_single_led_5) {
				setBtnActivated(Define.SINGLE_LED_456_ACTIVATE, Define.SINGLE_LED);
                doLedSelect(Define.SINGLE_LED, Define.SELECTED_LED5);
			} else if (i == R.id.id_single_led_6) {
				setBtnActivated(Define.SINGLE_LED_456_ACTIVATE, Define.SINGLE_LED);
                doLedSelect(Define.SINGLE_LED, Define.SELECTED_LED6);
			} else if (i == R.id.id_single_led_7) {
				setBtnActivated(Define.SINGLE_LED_789_ACTIVATE, Define.SINGLE_LED);
                doLedSelect(Define.SINGLE_LED, Define.SELECTED_LED7);
			} else if (i == R.id.id_single_led_8) {
				setBtnActivated(Define.SINGLE_LED_789_ACTIVATE, Define.SINGLE_LED);
                doLedSelect(Define.SINGLE_LED, Define.SELECTED_LED8);
			} else if (i == R.id.id_single_led_9) {
				setBtnActivated(Define.SINGLE_LED_789_ACTIVATE, Define.SINGLE_LED);
                doLedSelect(Define.SINGLE_LED, Define.SELECTED_LED9);
			}
		}
	};
    // Single 커넥터 롱 클릭시 행동
	private OnLongClickListener singleLongClickListener = new OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			setBtnActivated(Define.SINGLE_LED_123_ACTIVATE, Define.SINGLE_LED);
			setBtnActivated(Define.SINGLE_LED_456_ACTIVATE, Define.SINGLE_LED);
			setBtnActivated(Define.SINGLE_LED_789_ACTIVATE, Define.SINGLE_LED);
			// Color LED 는 모두 체크 해제 선택 해제
			for (int i=0;i<Define.NUMBER_OF_COLOR_LED;i++) {
				cbColor[i].setChecked(false);
				cbColor[i].setSelected(false);
			}
			// 현재 커넥터가 체크되어 있다면 체크 해제
			if (((CheckBox)v).isChecked()) {
				((CheckBox)v).setChecked(false);
			}
			// 현재 커넥터가 체크되어 있지 않다면 체크 설정
			else {
				((CheckBox)v).setChecked(true);
			}
            int checkedLed = 0;
			int getCheckNumber = 0;
			// Single LED 체크 갯수 확인
			for (int i=0;i<Define.NUMBER_OF_SINGLE_LED;i++) {
				cbSingle[i].setSelected(false);
				if (cbSingle[i].isChecked()) {
					getCheckNumber++;
                    switch (i) {
                        case 0:
                            checkedLed = checkedLed | Define.SELECTED_LED1;
                            break;
                        case 1:
                            checkedLed = checkedLed | Define.SELECTED_LED2;
                            break;
                        case 2:
                            checkedLed = checkedLed | Define.SELECTED_LED3;
                            break;
                        case 3:
                            checkedLed = checkedLed | Define.SELECTED_LED4;
                            break;
                        case 4:
                            checkedLed = checkedLed | Define.SELECTED_LED5;
                            break;
                        case 5:
                            checkedLed = checkedLed | Define.SELECTED_LED6;
                            break;
                        case 6:
                            checkedLed = checkedLed | Define.SELECTED_LED7;
                            break;
                        case 7:
                            checkedLed = checkedLed | Define.SELECTED_LED8;
                            break;
                        case 8:
                            checkedLed = checkedLed | Define.SELECTED_LED9;
                            break;
                    }
				}
			}
			// 체크되어 있는 것이 없다면 선택 설정
			if (getCheckNumber == 0) {
				v.setSelected(true);
                int id = v.getId();
                int selectedLed = 0;
                if (id == R.id.id_single_led_1) {
                    selectedLed = Define.SELECTED_LED1;
                } else if (id == R.id.id_single_led_2) {
                    selectedLed = Define.SELECTED_LED2;
                } else if (id == R.id.id_single_led_3) {
                    selectedLed = Define.SELECTED_LED3;
                } else if (id == R.id.id_single_led_4) {
                    selectedLed = Define.SELECTED_LED4;
                } else if (id == R.id.id_single_led_5) {
                    selectedLed = Define.SELECTED_LED5;
                } else if (id == R.id.id_single_led_6) {
                    selectedLed = Define.SELECTED_LED6;
                } else if (id == R.id.id_single_led_7) {
                    selectedLed = Define.SELECTED_LED7;
                } else if (id == R.id.id_single_led_8) {
                    selectedLed = Define.SELECTED_LED8;
                } else if (id == R.id.id_single_led_9) {
                    selectedLed = Define.SELECTED_LED9;
                }
                doLedSelect(Define.SINGLE_LED, selectedLed);
			}
            else {
                doLedCheck(Define.SINGLE_LED, checkedLed);
            }
			return true;
		}
	};

	private void setBtnSelected(int id) {
		Log.d(TAG, "selected");
		for (int i=0;i<Define.NUMBER_OF_COLOR_LED;i++) {
			cbColor[i].setChecked(false);
			if (cbColor[i].getId() == id) {
				cbColor[i].setSelected(true);
			} else {
				cbColor[i].setSelected(false);
			}
		}
		for (int i=0;i<Define.NUMBER_OF_SINGLE_LED;i++) {
			cbSingle[i].setChecked(false);
			if (cbSingle[i].getId() == id) {
				cbSingle[i].setSelected(true);
			} else {
				cbSingle[i].setSelected(false);
			}
		}
	}

	private void setBtnActivated(int ledGroup, boolean isSingle) {
		switch (ledGroup) {
			case Define.SINGLE_LED_123_ACTIVATE:
				if (isSingle) {
					cbSingle[0].setBackgroundResource(R.drawable.selector_connector);
					cbSingle[1].setBackgroundResource(R.drawable.selector_connector);
					cbSingle[2].setBackgroundResource(R.drawable.selector_connector);
					cbColor[0].setBackgroundResource(R.drawable.selector_connector_disable);
                    enabledLedGroup = enabledLedGroup | Define.SINGLE_LED_123_ACTIVATE;
				} else {
					cbSingle[0].setBackgroundResource(R.drawable.selector_connector_disable);
					cbSingle[1].setBackgroundResource(R.drawable.selector_connector_disable);
					cbSingle[2].setBackgroundResource(R.drawable.selector_connector_disable);
					cbColor[0].setBackgroundResource(R.drawable.selector_connector);
                    // 첫 비트를 0으로 만듬 (Color LED1 Enable 상태)
                    enabledLedGroup = enabledLedGroup & 0xFE;
				}
				break;
			case Define.SINGLE_LED_456_ACTIVATE:
				if (isSingle) {
					cbSingle[3].setBackgroundResource(R.drawable.selector_connector);
					cbSingle[4].setBackgroundResource(R.drawable.selector_connector);
					cbSingle[5].setBackgroundResource(R.drawable.selector_connector);
					cbColor[1].setBackgroundResource(R.drawable.selector_connector_disable);
                    enabledLedGroup = enabledLedGroup | Define.SINGLE_LED_456_ACTIVATE;
				} else {
					cbSingle[3].setBackgroundResource(R.drawable.selector_connector_disable);
					cbSingle[4].setBackgroundResource(R.drawable.selector_connector_disable);
					cbSingle[5].setBackgroundResource(R.drawable.selector_connector_disable);
					cbColor[1].setBackgroundResource(R.drawable.selector_connector);
                    // 두번째 비트를 0으로 만듬 (Color LED1 Enable 상태)
                    enabledLedGroup = enabledLedGroup & 0xFD;
				}
				break;
			case Define.SINGLE_LED_789_ACTIVATE:
				if (isSingle) {
					cbSingle[6].setBackgroundResource(R.drawable.selector_connector);
					cbSingle[7].setBackgroundResource(R.drawable.selector_connector);
					cbSingle[8].setBackgroundResource(R.drawable.selector_connector);
					cbColor[2].setBackgroundResource(R.drawable.selector_connector_disable);
                    enabledLedGroup = enabledLedGroup | Define.SINGLE_LED_789_ACTIVATE;
				} else {
					cbSingle[6].setBackgroundResource(R.drawable.selector_connector_disable);
					cbSingle[7].setBackgroundResource(R.drawable.selector_connector_disable);
					cbSingle[8].setBackgroundResource(R.drawable.selector_connector_disable);
					cbColor[2].setBackgroundResource(R.drawable.selector_connector);
                    // 세번째 비트를 0으로 만듬 (Color LED1 Enable 상태)
                    enabledLedGroup = enabledLedGroup & 0xFB;
				}
				break;
		}
	}

    // Activity 와 통신할 수 있는 인터페이스 설정
    public interface OnLedSelectListener {
        void onLedSelect(boolean isSingleLed, int enabledLedGroup, int selectedLed);
        void onLedCheck(boolean isSingleLed, int enabledLedGroup, int checkedLed);
    }

    public void setOnLedSelectListener(OnLedSelectListener listener) {
        onLedSelectListener = listener;
    }

    public void doLedSelect(boolean isSingleLed, int selectedLed) {
        if (onLedSelectListener != null) {
            onLedSelectListener.onLedSelect(isSingleLed, enabledLedGroup, selectedLed);
        }
    }

    public void doLedCheck(boolean isSingleLed, int checkedLed) {
        if (onLedSelectListener != null) {
            onLedSelectListener.onLedCheck(isSingleLed, enabledLedGroup, checkedLed);
        }
    }
	// Single LED 모두 선택 설정
	public void selectAllLed() {
		setBtnActivated(Define.SINGLE_LED_123_ACTIVATE, Define.SINGLE_LED);
		setBtnActivated(Define.SINGLE_LED_456_ACTIVATE, Define.SINGLE_LED);
		setBtnActivated(Define.SINGLE_LED_789_ACTIVATE, Define.SINGLE_LED);
		// Color LED 는 모두 체크 해제 선택 해제
		for (int i=0;i<Define.NUMBER_OF_COLOR_LED;i++) {
			cbColor[i].setChecked(false);
			cbColor[i].setSelected(false);
		}
		for (int i=0;i<Define.NUMBER_OF_SINGLE_LED;i++) {
			cbSingle[i].setChecked(true);
		}
	}
	// Color Led 모두 선택 설정
	public void selectAllColor() {
		setBtnActivated(Define.SINGLE_LED_123_ACTIVATE, Define.COLOR_LED);
		setBtnActivated(Define.SINGLE_LED_456_ACTIVATE, Define.COLOR_LED);
		setBtnActivated(Define.SINGLE_LED_789_ACTIVATE, Define.COLOR_LED);
		// Single LED 는 모두 체크 해제 선택 해제
		for (int i=0;i<Define.NUMBER_OF_SINGLE_LED;i++) {
			cbSingle[i].setChecked(false);
			cbSingle[i].setSelected(false);
		}
		for (int i=0;i<Define.NUMBER_OF_COLOR_LED;i++) {
			cbColor[i].setChecked(true);
		}
	}
}
