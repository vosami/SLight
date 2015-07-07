package com.syncworks.slight.fragment_easy;

import android.app.Activity;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.syncworks.slight.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link InstallFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InstallFragment extends Fragment {


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

        tbScm.setOnClickListener(tbListener);
        tbSpm.setOnClickListener(tbListener);


        ibScm1.setOnClickListener(onClickListener);
        ibScm2.setOnClickListener(onClickListener);
        ibScm3.setOnClickListener(onClickListener);
        ibSpm1.setOnClickListener(onClickListener);
        ibSpm2.setOnClickListener(onClickListener);
        ibSpm3.setOnClickListener(onClickListener);
        ibSpm4.setOnClickListener(onClickListener);
        btnYoutubeScm.setOnClickListener(onClickListener);
        btnYoutubeSpm.setOnClickListener(onClickListener);
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
                case R.id.desc_btn_scm_1:
                    break;
                case R.id.desc_btn_scm_2:
                    break;
                case R.id.desc_btn_scm_3:
                    break;
                case R.id.desc_btn_spm_1:
                    break;
                case R.id.desc_btn_spm_2:
                    break;
                case R.id.desc_btn_spm_3:
                    break;
                case R.id.desc_btn_spm_4:
                    break;
                case R.id.youtube_btn_scm:
                    watchYoutubeVideo("WqSx4s1G2N8");
                    break;
                case R.id.youtube_btn_spm:
                    watchYoutubeVideo("WqSx4s1G2N8");
                    break;
            }
        }
    };

    private CheckBox.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

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

}
