package com.syncworks.slight.fragment_easy;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.syncworks.slight.R;
import com.syncworks.slightpref.SLightPref;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link InstallFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InstallFragment extends Fragment {

    SLightPref appPref = null;

    // 이미지 리소스
    private final static int IMAGE_SCM1 = R.drawable.desc_scm100_1_t;
    private final static int IMAGE_SCM2 = R.drawable.desc_scm100_2_t;
    private final static int IMAGE_SCM3 = R.drawable.desc_scm100_3_t;
    private final static int IMAGE_SPM1 = R.drawable.desc_spm100_1_t;
    private final static int IMAGE_SPM2 = R.drawable.desc_spm100_2_t;
    private final static int IMAGE_SPM3 = R.drawable.desc_spm100_3_t;
    private final static int IMAGE_SPM4 = R.drawable.desc_spm100_4_t;

    private ToggleButton tbScm, tbSpm;
    private ImageButton ibScm1, ibScm2, ibScm3;
    private ImageButton ibSpm1, ibSpm2, ibSpm3, ibSpm4;
    private CheckBox cbNotShow;
    private LinearLayout llScm, llSpm;
    private Button btnYoutubeScm, btnYoutubeSpm;

    private OnEasyFragmentListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InstallFragment.
     */
    public static InstallFragment newInstance() {
        InstallFragment fragment = new InstallFragment();
        return fragment;
    }

    public InstallFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        appPref = new SLightPref(getActivity());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_install,container,false);
        tbScm = (ToggleButton) view.findViewById(R.id.btn_scm100);
        tbSpm = (ToggleButton) view.findViewById(R.id.btn_spm100);
        ibScm1 = (ImageButton) view.findViewById(R.id.desc_btn_scm_1);
        ibScm2 = (ImageButton) view.findViewById(R.id.desc_btn_scm_2);
        ibScm3 = (ImageButton) view.findViewById(R.id.desc_btn_scm_3);
        ibSpm1 = (ImageButton) view.findViewById(R.id.desc_btn_spm_1);
        ibSpm2 = (ImageButton) view.findViewById(R.id.desc_btn_spm_2);
        ibSpm3 = (ImageButton) view.findViewById(R.id.desc_btn_spm_3);
        ibSpm4 = (ImageButton) view.findViewById(R.id.desc_btn_spm_4);
        cbNotShow = (CheckBox) view.findViewById(R.id.chk_not_display);
        llScm = (LinearLayout) view.findViewById(R.id.ll_easy_scm);
        llSpm = (LinearLayout) view.findViewById(R.id.ll_easy_spm);
        btnYoutubeScm = (Button) view.findViewById(R.id.youtube_btn_scm);
        btnYoutubeSpm = (Button) view.findViewById(R.id.youtube_btn_spm);

        // 리스너 설정
        tbScm.setOnClickListener(tbListener);
        tbSpm.setOnClickListener(tbListener);

        cbNotShow.setOnClickListener(onClickListener);
        ibScm1.setOnClickListener(onClickListener);
        ibScm2.setOnClickListener(onClickListener);
        ibScm3.setOnClickListener(onClickListener);
        ibSpm1.setOnClickListener(onClickListener);
        ibSpm2.setOnClickListener(onClickListener);
        ibSpm3.setOnClickListener(onClickListener);
        ibSpm4.setOnClickListener(onClickListener);
        btnYoutubeScm.setOnClickListener(onClickListener);
        btnYoutubeSpm.setOnClickListener(onClickListener);

        // 초기화
        cbNotShow.setChecked(appPref.getBoolean(SLightPref.FRAG_INSTALL_NOT_SHOW));
        return view;
    }

    // 상단 커넥터 타입 도움말과 에나멜선 타입 도움말 선택 버튼 리스너
    private View.OnClickListener tbListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_scm100) {
                llScm.setVisibility(View.VISIBLE);
                llSpm.setVisibility(View.GONE);
                tbScm.setEnabled(false);
                tbScm.setChecked(true);
                tbSpm.setChecked(false);
                tbSpm.setEnabled(true);
            } else if (v.getId() == R.id.btn_spm100) {
                llScm.setVisibility(View.GONE);
                llSpm.setVisibility(View.VISIBLE);
                tbSpm.setEnabled(false);
                tbSpm.setChecked(true);
                tbScm.setChecked(false);
                tbScm.setEnabled(true);
            }
        }
    };

    // 다양한 버튼 리스너
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.chk_not_display:
                    appPref.putBoolean(SLightPref.FRAG_INSTALL_NOT_SHOW,cbNotShow.isChecked());
                    break;
                case R.id.desc_btn_scm_1:
                    showImage(IMAGE_SCM1);
                    break;
                case R.id.desc_btn_scm_2:
                    showImage(IMAGE_SCM2);
                    break;
                case R.id.desc_btn_scm_3:
                    showImage(IMAGE_SCM3);
                    break;
                case R.id.desc_btn_spm_1:
                    showImage(IMAGE_SPM1);
                    break;
                case R.id.desc_btn_spm_2:
                    showImage(IMAGE_SPM2);
                    break;
                case R.id.desc_btn_spm_3:
                    showImage(IMAGE_SPM3);
                    break;
                case R.id.desc_btn_spm_4:
                    showImage(IMAGE_SPM4);
                    break;
                case R.id.youtube_btn_scm:
                    watchYoutubeVideo("A9TjfEEja7Y");
                    break;
                case R.id.youtube_btn_spm:
                    watchYoutubeVideo("kSaBZsgi5Ww");
                    break;
            }
        }
    };


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


    // 유튜브 비디오 플레이
    public void watchYoutubeVideo(String id){
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            startActivity(intent);
        }catch (ActivityNotFoundException ex){
            Intent intent=new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v="+id));
            startActivity(intent);
        }
    }
    // 설명 이미지를 크게 보여준다.
    public void showImage(int drawableID) {

        final Dialog builder = new Dialog(getActivity());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });
        LinearLayout llDialog = new LinearLayout(getActivity());
        llDialog.setOrientation(LinearLayout.VERTICAL);
        ImageView imageView = new ImageView(getActivity());
        imageView.setImageDrawable(getResources().getDrawable(drawableID));
        Button btnClose = new Button(getActivity());
        btnClose.setText(getString(R.string.str_close));
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        llDialog.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        llDialog.addView(imageView);
        llDialog.addView(btnClose);

        builder.addContentView(llDialog, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        builder.show();
    }

}
