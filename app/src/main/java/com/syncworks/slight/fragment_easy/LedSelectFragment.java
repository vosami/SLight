package com.syncworks.slight.fragment_easy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.syncworks.define.Define;
import com.syncworks.leddata.LedOptions;
import com.syncworks.leddata.LedSelect;
import com.syncworks.slight.R;
import com.syncworks.slight.dialog.DialogChangePattern;
import com.syncworks.slight.widget.LedBtn;
import com.syncworks.slightpref.SLightPref;

import static com.syncworks.define.Define.NUMBER_OF_COLOR_LED;
import static com.syncworks.define.Define.NUMBER_OF_SINGLE_LED;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link } interface
 * to handle interaction events.
 * Use the {@link LedSelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LedSelectFragment extends Fragment {

    // 스마트라이트 설정
    private SLightPref appPref;

    // LED 선택 변수
    private LedSelect ledSelect = null;
    // LED 옵션 변수
    private LedOptions ledOptions[] = null;
    private int rgbPattern[] = new int[NUMBER_OF_COLOR_LED];
    private int ledPattern[] = new int[NUMBER_OF_SINGLE_LED];

    private LedBtn btnRGB[] = new LedBtn[NUMBER_OF_COLOR_LED];
    private LedBtn btnSingle[] = new LedBtn[NUMBER_OF_SINGLE_LED];
    private ImageView ivRGB[] = new ImageView[NUMBER_OF_COLOR_LED];
    private ImageView ivSingle[] = new ImageView[NUMBER_OF_SINGLE_LED];

    private OnEasyFragmentListener mListener = null;

    private DialogChangePattern changePatternDialog = null;

    // 새로운 Fragment Instance 생성
    public static LedSelectFragment newInstance() {
        return new LedSelectFragment();
    }

    public LedSelectFragment() {
        // 효과 패턴 초기화
        for (int i=0;i<NUMBER_OF_COLOR_LED;i++) {
            rgbPattern[i] = 0;
        }
        for (int i=0;i<NUMBER_OF_SINGLE_LED;i++) {
            ledPattern[i] = 0;
        }
    }

    public void setPattern(boolean isRgb,int ledNum, int pattern) {
        if (isRgb) {
            rgbPattern[ledNum] = pattern;
        } else {
            ledPattern[ledNum] = pattern;
        }
    }

    public void setLedSelect(LedSelect ls) {
        this.ledSelect = ls;
    }

    public void setLedOptions(LedOptions[] lo) {
        this.ledOptions = lo;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_led_select,container,false);
        appPref = new SLightPref(getActivity());
        btnRGB[0] = (LedBtn) view.findViewById(R.id.rgb_1);
        btnRGB[1] = (LedBtn) view.findViewById(R.id.rgb_2);
        btnRGB[2] = (LedBtn) view.findViewById(R.id.rgb_3);

        btnSingle[0] = (LedBtn) view.findViewById(R.id.led_1);
        btnSingle[1] = (LedBtn) view.findViewById(R.id.led_2);
        btnSingle[2] = (LedBtn) view.findViewById(R.id.led_3);
        btnSingle[3] = (LedBtn) view.findViewById(R.id.led_4);
        btnSingle[4] = (LedBtn) view.findViewById(R.id.led_5);
        btnSingle[5] = (LedBtn) view.findViewById(R.id.led_6);
        btnSingle[6] = (LedBtn) view.findViewById(R.id.led_7);
        btnSingle[7] = (LedBtn) view.findViewById(R.id.led_8);
        btnSingle[8] = (LedBtn) view.findViewById(R.id.led_9);
        ivRGB[0] = (ImageView) view.findViewById(R.id.rgb_1_pattern);
        ivRGB[1] = (ImageView) view.findViewById(R.id.rgb_2_pattern);
        ivRGB[2] = (ImageView) view.findViewById(R.id.rgb_3_pattern);
        ivSingle[0] = (ImageView) view.findViewById(R.id.led_1_pattern);
        ivSingle[1] = (ImageView) view.findViewById(R.id.led_2_pattern);
        ivSingle[2] = (ImageView) view.findViewById(R.id.led_3_pattern);
        ivSingle[3] = (ImageView) view.findViewById(R.id.led_4_pattern);
        ivSingle[4] = (ImageView) view.findViewById(R.id.led_5_pattern);
        ivSingle[5] = (ImageView) view.findViewById(R.id.led_6_pattern);
        ivSingle[6] = (ImageView) view.findViewById(R.id.led_7_pattern);
        ivSingle[7] = (ImageView) view.findViewById(R.id.led_8_pattern);
        ivSingle[8] = (ImageView) view.findViewById(R.id.led_9_pattern);

        btnRGB[0].setOnLedBtnListener(ledBtnListener);
        btnRGB[1].setOnLedBtnListener(ledBtnListener);
        btnRGB[2].setOnLedBtnListener(ledBtnListener);
        btnSingle[0].setOnLedBtnListener(ledBtnListener);
        btnSingle[1].setOnLedBtnListener(ledBtnListener);
        btnSingle[2].setOnLedBtnListener(ledBtnListener);
        btnSingle[3].setOnLedBtnListener(ledBtnListener);
        btnSingle[4].setOnLedBtnListener(ledBtnListener);
        btnSingle[5].setOnLedBtnListener(ledBtnListener);
        btnSingle[6].setOnLedBtnListener(ledBtnListener);
        btnSingle[7].setOnLedBtnListener(ledBtnListener);
        btnSingle[8].setOnLedBtnListener(ledBtnListener);
        if (!appPref.getBoolean(SLightPref.EASY_ACTIVITY[1])) {
            appPref.putBoolean(SLightPref.EASY_ACTIVITY[1],true);
            showOverLay();
        }
        return view;
    }

    private final static int PATTERN[] = {
            R.drawable.ic_pattern_always,
            R.drawable.ic_pattern_pulse,
            R.drawable.ic_pattern_flash,
            R.drawable.ic_pattern_up_down,
            R.drawable.ic_pattern_torch
    };

    private void displayBtn() {
        for (int i=0;i< Define.NUMBER_OF_COLOR_LED;i++) {
            if (ledSelect.getRgb(i) == LedSelect.SelectType.SELECTED) {
                btnRGB[i].setBtnBright(false);
                btnRGB[i].setBtnChecked(true);
                btnRGB[i].setBtnEnabled(true);
                ivRGB[i].setVisibility(View.INVISIBLE);
            } else if (ledSelect.getRgb(i) == LedSelect.SelectType.DISABLED) {
                btnRGB[i].setBtnBright(false);
                btnRGB[i].setBtnChecked(false);
                btnRGB[i].setBtnEnabled(false);
                ivRGB[i].setVisibility(View.INVISIBLE);
            } else if (ledSelect.getRgb(i) == LedSelect.SelectType.COMPLETED) {
                // 버튼 완료 표시
                btnRGB[i].setBtnBright(true);
                btnRGB[i].setBtnChecked(false);
                btnRGB[i].setBtnEnabled(true);
                // RGB 색깔 옵션 설정
                if (ledOptions != null) {
                    btnRGB[i].setBright(ledOptions[i * 3].getRatioBright()*191/100,
                            ledOptions[i * 3 + 1].getRatioBright()*191/100,
                            ledOptions[i * 3 + 2].getRatioBright()*191/100);
                }
                ivRGB[i].setBackground(getResources().getDrawable(PATTERN[rgbPattern[i]]));
                ivRGB[i].setVisibility(View.VISIBLE);
            } else {
                btnRGB[i].setBtnBright(false);
                btnRGB[i].setBtnChecked(false);
                btnRGB[i].setBtnEnabled(true);
                ivRGB[i].setVisibility(View.INVISIBLE);
            }
        }

        for (int i=0;i<Define.NUMBER_OF_SINGLE_LED;i++) {
            if (ledSelect.getLed(i) == LedSelect.SelectType.SELECTED) {
                btnSingle[i].setBtnBright(false);
                btnSingle[i].setBtnChecked(true);
                btnSingle[i].setBtnEnabled(true);
                ivSingle[i].setVisibility(View.INVISIBLE);
            } else if (ledSelect.getLed(i) == LedSelect.SelectType.DISABLED) {
                btnSingle[i].setBtnBright(false);
                btnSingle[i].setBtnChecked(false);
                btnSingle[i].setBtnEnabled(false);
                ivSingle[i].setVisibility(View.INVISIBLE);
            } else if (ledSelect.getLed(i) == LedSelect.SelectType.COMPLETED) {
                btnSingle[i].setBtnBright(true);
                btnSingle[i].setBtnChecked(false);
                btnSingle[i].setBtnEnabled(true);
                if (ledOptions != null) {
                    btnSingle[i].setBright(ledOptions[i].getRatioBright()*191/100);
                }
                ivSingle[i].setBackground(getResources().getDrawable(PATTERN[ledPattern[i]]));
                ivSingle[i].setVisibility(View.VISIBLE);
            } else {
                btnSingle[i].setBtnBright(false);
                btnSingle[i].setBtnChecked(false);
                btnSingle[i].setBtnEnabled(true);
                ivSingle[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (ledSelect != null && ledOptions != null) {
            displayBtn();
        }
    }

    private LedBtn.OnLedBtnListener ledBtnListener = new LedBtn.OnLedBtnListener() {
        @Override
        public void onClick(View buttonView) {
            switch (buttonView.getId()) {
                case R.id.rgb_1:
                    clickRgb(1);
                    break;
                case R.id.rgb_2:
                    clickRgb(1);
                    break;
                case R.id.rgb_3:
                    clickRgb(2);
                    break;
                case R.id.led_1:
                    clickSingle(0);
                    break;
                case R.id.led_2:
                    clickSingle(1);
                    break;
                case R.id.led_3:
                    clickSingle(2);
                    break;
                case R.id.led_4:
                    clickSingle(3);
                    break;
                case R.id.led_5:
                    clickSingle(4);
                    break;
                case R.id.led_6:
                    clickSingle(5);
                    break;
                case R.id.led_7:
                    clickSingle(6);
                    break;
                case R.id.led_8:
                    clickSingle(7);
                    break;
                case R.id.led_9:
                    clickSingle(8);
                    break;
            }
        }

        @Override
        public void onNotify(View buttonView) {
            switch (buttonView.getId()) {
                case R.id.rgb_1:
                    notifyRGB(0);
                    break;
                case R.id.rgb_2:
                    notifyRGB(1);
                    break;
                case R.id.rgb_3:
                    notifyRGB(2);
                    break;
                case R.id.led_1:
                    notifySingle(0);
                    break;
                case R.id.led_2:
                    notifySingle(1);
                    break;
                case R.id.led_3:
                    notifySingle(2);
                    break;
                case R.id.led_4:
                    notifySingle(3);
                    break;
                case R.id.led_5:
                    notifySingle(4);
                    break;
                case R.id.led_6:
                    notifySingle(5);
                    break;
                case R.id.led_7:
                    notifySingle(6);
                    break;
                case R.id.led_8:
                    notifySingle(7);
                    break;
                case R.id.led_9:
                    notifySingle(8);
                    break;
            }
        }
    };

    private void notifyRGB(int ledNum) {
        if (btnRGB[ledNum].getBtnBright()) {
            // TODO 대화창 표시
            showNotifyDialog(true,ledNum);
            return;
        }
        // 현재 선택한 RGB 버튼을 체크할 때
        for (int i=0;i<3;i++) {
            if (btnSingle[ledNum*3 + i].getBtnBright()) {
                // TODO 대화창 표시
                showNotifyDialog(false,ledNum);
                return;
            }
        }
        for (int i=0;i<3;i++) {
            // 단색 LED 버튼 체크 해제, Disabled
            btnSingle[ledNum*3 + i].setBtnEnabled(false);
            ledSelect.setLed(ledNum*3 + i, LedSelect.SelectType.DISABLED);
        }
        // 선택된 RGB 버튼 체크 설정, Enabled
        ledSelect.setRgb(ledNum, LedSelect.SelectType.SELECTED);
        btnRGB[ledNum].setBtnEnabled(true);
        btnRGB[ledNum].setBtnChecked(true);
        mListener.onSelectRGB(ledNum, btnRGB[ledNum].getBtnChecked());
    }
    private void notifySingle(int ledNum) {
        if (btnSingle[ledNum].getBtnBright()) {
            // TODO 대화창 표시
            return;
        }
        if (btnRGB[ledNum/3].getBtnBright()) {
            // TODO 대화창 표시
            return;
        }
        // RGB 버튼 체크 해제, Disabled
        btnRGB[ledNum/3].setBtnEnabled(false);
        ledSelect.setRgb(ledNum/3, LedSelect.SelectType.DISABLED);
        for(int i=0;i<3;i++) {
            int cLedNum = (ledNum/3)*3 + i;
            ledSelect.setLed(cLedNum, LedSelect.SelectType.DEFAULT);
            btnSingle[cLedNum].setBtnEnabled(true);
            // 다른 LED 는 초기화
            if (cLedNum != ledNum) {
                mListener.onSelectLed(cLedNum,btnSingle[cLedNum].getBtnChecked());
            }
        }
        ledSelect.setLed(ledNum, LedSelect.SelectType.SELECTED);
        btnSingle[ledNum].setBtnChecked(true);
        mListener.onSelectLed(ledNum, btnSingle[ledNum].getBtnChecked());
    }

    private void clickRgb(int ledNum) {
        ledSelect.setRgb(ledNum, btnRGB[ledNum].getBtnChecked());
        mListener.onSelectRGB(ledNum, btnRGB[ledNum].getBtnChecked());
    }

    private void clickSingle(int ledNum) {
        ledSelect.setLed(ledNum, btnSingle[ledNum].getBtnChecked());
        mListener.onSelectLed(ledNum, btnSingle[ledNum].getBtnChecked());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnEasyFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void showOverLay() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_help_led_select);
        dialog.setCanceledOnTouchOutside(true);
        //for dismissing anywhere you touch
        View masterView = dialog.findViewById(R.id.overlay_help);
        masterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private AlertDialog notifyDialog = null;
    private void showNotifyDialog(boolean isRgb, int ledNum) {
        notifyDialog = createNotifyDialog(isRgb, ledNum);
        notifyDialog.show();
    }

    private AlertDialog createNotifyDialog(boolean isRgb, int ledNum) {
        AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
        String ledType;
        if (isRgb) {
            ledType = getString(R.string.led_type_rgb);
        } else {
            ledType = getString(R.string.led_type_rgb);
        }
        String title = ledType  + Integer.toString(ledNum) +" "+  getString(R.string.str_select);
        ab.setTitle(title);
        TextView tvCancelNotify = new TextView(getActivity());
        tvCancelNotify.setText(getString(R.string.easy_led_select_tv_cancel_notify));
        ab.setView(tvCancelNotify);
        ab.setCancelable(true);
        ab.setPositiveButton(getString(R.string.str_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                notifyDialog.dismiss();
            }
        });
        ab.setNegativeButton(getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                notifyDialog.dismiss();
            }
        });
        return ab.create();
    }
}
