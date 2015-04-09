package com.syncworks.ledselectlayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.syncworks.define.Define;

/**
 * Created with Android Studio
 * Copyrights (C)SyncWorks All rights reserved by SyncWorks
 * Created by 승현 on 2015-03-16.
 */
public class LedSelectLayout extends LinearLayout {
	private final static String TAG = LedSelectLayout.class.getSimpleName();
    // 이름 설정 중인지 확인
    private boolean isSettingName = false;

    // Activity 와 통신할 수 있는 리스너 설정
    private OnLedSelectListener onLedSelectListener;


	// 체크박스
	private CheckBox[] cbColor;
	private CheckBox[] cbSingle;
    private TextView[] tvColor;
    private TextView[] tvSingle;


	// 버튼
	private Button btnExpand;

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

        // 활성화된 LED 는 Single LED 로 설정
        enabledLedGroup = Define.SINGLE_LED_123_ACTIVATE | Define.SINGLE_LED_456_ACTIVATE | Define.SINGLE_LED_789_ACTIVATE;

		// Smart Light 배경 색깔 적용
		int lecBackgroundColor = getResources().getColor(R.color.LecBackground);
		this.setBackgroundColor(lecBackgroundColor);
		this.setOrientation(VERTICAL);

		// Color LED 커넥터 갯수 설정
		cbColor = new CheckBox[Define.NUMBER_OF_COLOR_LED];
        tvColor = new TextView[Define.NUMBER_OF_COLOR_LED];
		// Single LED 커넥터 갯수 설정
		cbSingle = new CheckBox[Define.NUMBER_OF_SINGLE_LED];
        tvSingle = new TextView[Define.NUMBER_OF_SINGLE_LED];

		// Color 커넥터 컨트롤 획득
		cbColor[0] = (CheckBox) findViewById(R.id.id_color_led_1);
		cbColor[1] = (CheckBox) findViewById(R.id.id_color_led_2);
		cbColor[2] = (CheckBox) findViewById(R.id.id_color_led_3);
        // Color 커넥터 이름 컨트롤 획득
        tvColor[0] = (TextView) findViewById(R.id.tv_color_led_1);
        tvColor[1] = (TextView) findViewById(R.id.tv_color_led_2);
        tvColor[2] = (TextView) findViewById(R.id.tv_color_led_3);

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
        // Single 커넥터 이름 컨트롤 획득
        tvSingle[0] = (TextView) findViewById(R.id.tv_single_led_1);
        tvSingle[1] = (TextView) findViewById(R.id.tv_single_led_2);
        tvSingle[2] = (TextView) findViewById(R.id.tv_single_led_3);
        tvSingle[3] = (TextView) findViewById(R.id.tv_single_led_4);
        tvSingle[4] = (TextView) findViewById(R.id.tv_single_led_5);
        tvSingle[5] = (TextView) findViewById(R.id.tv_single_led_6);
        tvSingle[6] = (TextView) findViewById(R.id.tv_single_led_7);
        tvSingle[7] = (TextView) findViewById(R.id.tv_single_led_8);
        tvSingle[8] = (TextView) findViewById(R.id.tv_single_led_9);

		// 메뉴 확장 버튼
		btnExpand = (Button) findViewById(R.id.btn_expand_menu);
		btnExpand.setOnClickListener(clickListener);

		// 최초에 활성화 상태
        setBtnSelected(R.id.id_single_led_1);

        // 버튼 클릭시 행동 설정
        setLedClickListener();
	}

    // LED 버튼의 클릭시 행동 설정
    private void setLedClickListener() {
        if (isSettingName) {
            for (int i=0;i<Define.NUMBER_OF_COLOR_LED;i++) {
                cbColor[i].setOnClickListener(setNameClickListener);
                cbColor[i].setOnLongClickListener(nullClickListener);
            }
            for (int i=0;i<Define.NUMBER_OF_SINGLE_LED;i++) {
                cbSingle[i].setOnClickListener(setNameClickListener);
                cbSingle[i].setOnLongClickListener(nullClickListener);
            }
        } else {
            for (int i=0;i<Define.NUMBER_OF_COLOR_LED;i++) {
                cbColor[i].setOnClickListener(colorClickListener);
                cbColor[i].setOnLongClickListener(colorLongClickListener);
            }
            for (int i=0;i<Define.NUMBER_OF_SINGLE_LED;i++) {
                cbSingle[i].setOnClickListener(singleClickListener);
                cbSingle[i].setOnLongClickListener(singleLongClickListener);
            }
        }
    }

    private OnClickListener setNameClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            AlertDialog dialog = createSetNameDialog(v.getId());
            dialog.show();
        }
    };

    private OnLongClickListener nullClickListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            return true;
        }
    };

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
        int checkedLed = Define.SELECTED_LED1|Define.SELECTED_LED2|Define.SELECTED_LED3|
                Define.SELECTED_LED4|Define.SELECTED_LED5|Define.SELECTED_LED6|
                Define.SELECTED_LED7|Define.SELECTED_LED8|Define.SELECTED_LED9;
        doLedCheck(Define.SINGLE_LED, checkedLed);
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
        int checkedLed = Define.SELECTED_COLOR_LED1|Define.SELECTED_COLOR_LED2|Define.SELECTED_COLOR_LED3;
        doLedCheck(Define.COLOR_LED, checkedLed);
	}


	// 버튼 클릭 리스너
	private OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int id = v.getId();
			// 확장 메뉴 출력
			if (id == R.id.btn_expand_menu) {
                if (isSettingName) {
                    isSettingName = false;
                    setLedClickListener();
                } else {
                    PopupMenu popup = new PopupMenu(getContext(), btnExpand);
                    popup.getMenuInflater().inflate(R.menu.popup_led_select, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int id = item.getItemId();
                            if (id == R.id.menu_init_name) {

                            } else if (id == R.id.menu_set_name) {
                                isSettingName = true;
                                setLedClickListener();
                            } else if (id == R.id.menu_select_all_led) {
                                selectAllLed();
                            } else if (id == R.id.menu_select_all_color) {
                                selectAllColor();
                            }
                            return false;
                        }
                    });
                    popup.show();
                }
            }
		}
	};

    private int vId;
    private EditText etSetName;

    private AlertDialog createSetNameDialog(int id) {
        vId = id;
        AlertDialog dialogBox = new AlertDialog.Builder(getContext())
                .setTitle("이름을 설정하세요.")
                .setMessage("LED")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nameStr = etSetName.getText().toString();
                        if (vId == R.id.id_color_led_1) {
                            tvColor[0].setText(nameStr);
                        } else if (vId == R.id.id_color_led_2) {
                            tvColor[1].setText(nameStr);
                        } else if (vId == R.id.id_color_led_3) {
                            tvColor[2].setText(nameStr);
                        } else if (vId == R.id.id_single_led_1) {
                            tvSingle[0].setText(nameStr);
                        } else if (vId == R.id.id_single_led_2) {
                            tvSingle[1].setText(nameStr);
                        } else if (vId == R.id.id_single_led_3) {
                            tvSingle[2].setText(nameStr);
                        } else if (vId == R.id.id_single_led_4) {
                            tvSingle[3].setText(nameStr);
                        } else if (vId == R.id.id_single_led_5) {
                            tvSingle[4].setText(nameStr);
                        } else if (vId == R.id.id_single_led_6) {
                            tvSingle[5].setText(nameStr);
                        } else if (vId == R.id.id_single_led_7) {
                            tvSingle[6].setText(nameStr);
                        } else if (vId == R.id.id_single_led_8) {
                            tvSingle[7].setText(nameStr);
                        } else if (vId == R.id.id_single_led_9) {
                            tvSingle[8].setText(nameStr);
                        }

                    }
                })
                .setNeutralButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        Context context = getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_set_name,null);
        etSetName = (EditText) layout.findViewById(R.id.et_set_name);
        dialogBox.setView(layout);
        return dialogBox;
    }

}
