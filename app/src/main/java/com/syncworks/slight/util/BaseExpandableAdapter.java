package com.syncworks.slight.util;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.syncworks.define.Logger;
import com.syncworks.slight.R;

import java.util.ArrayList;

/**
 * Created by vosami on 2015-08-10.
 */
public class BaseExpandableAdapter extends BaseExpandableListAdapter {

    private ArrayList<EffectListData> groupList = null;
    private ArrayList<ArrayList<EffectOptionData>> childList = null;
    private LayoutInflater inflater = null;
    private ViewHolder viewHolder = null;
    private ChildHolder childHolder = null;
    private Context context = null;
    private int lastSelectGroup = 0;
    private BaseExpandableInterface listener = null;

    private Toast mToast = null;

    public BaseExpandableAdapter(Context c, ArrayList<EffectListData> groupList,
                                 ArrayList<EffectOptionData> childList){
        super();
        this.inflater = LayoutInflater.from(c);
        this.groupList = groupList;
        this.childList = new ArrayList<>();
        for (int i=0;i<groupList.size();i++) {
            ArrayList<EffectOptionData> tempOption = new ArrayList<>();
            tempOption.add(childList.get(i));
            this.childList.add(tempOption);
        }
        this.context = c;
    }

    public void setOnExpandableListener(BaseExpandableInterface listener) {
        this.listener = listener;
    }

    private void doStartTime(int curGroup, int startTime) {
        if (listener != null) {
            listener.onStartTime(curGroup, startTime);
        }
    }

    private void doEffectTime(int curGroup, int effectTime) {
        if (listener != null) {
            listener.onEffectTime(curGroup, effectTime);
        }
    }

    private void doRandomTime(int curGroup, int randomTime) {
        if (listener != null) {
            listener.onRandomTime(curGroup, randomTime);
        }
    }
    private void doPatternOption(int curGroup, int patternOption) {
        if (listener != null) {
            listener.onPatternOption(curGroup, patternOption);
        }
    }

    public void setStartTime(int group, int startTime) {
        this.childList.get(group).get(0).timeStartDelay = startTime;
        Logger.d(this,"setStartTime",startTime);
    }
    public void setEffectTime(int group, int effectTime) {
        this.childList.get(group).get(0).timeEffectDelay = effectTime;
    }
    public void setRandomTime(int group, int randomTime) {
        this.childList.get(group).get(0).timeRandomDelay = randomTime;
    }

    // 그룹 포지션을 반환한다.
    @Override
    public String getGroup(int groupPosition) {
        return groupList.get(groupPosition).effectName;
    }

    // 그룹 사이즈를 반환한다.
    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    // 그룹 ID를 반환한다.
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    // 그룹뷰 각각의 ROW
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        View v = convertView;

        if(v == null){
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.list_expandable_row, parent, false);
            viewHolder.tv_groupName = (TextView) v.findViewById(R.id.tv_group);
            viewHolder.iv_image = (ImageView) v.findViewById(R.id.iv_image);
            viewHolder.iv_pattern = (ImageView) v.findViewById(R.id.iv_pattern);
            viewHolder.ll_background = (LinearLayout) v.findViewById(R.id.list_expandable_row_background);
            v.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)v.getTag();
        }
        if (isExpanded) {
            lastSelectGroup = groupPosition;
        }
        // 그룹을 펼칠때와 닫을때 아이콘을 변경해 준다.
        if(lastSelectGroup == groupPosition){
            viewHolder.iv_image.setBackgroundColor(Color.GREEN);
        }else {
            viewHolder.iv_image.setBackgroundColor(Color.WHITE);
        }
        boolean isEven = (groupPosition % 2) == 0;
        if (isEven) {
            viewHolder.ll_background.setBackgroundColor(Color.rgb(0xFA,0xFA,0xFA));
        } else {
            viewHolder.ll_background.setBackgroundColor(Color.rgb(0xFF,0xFF,0xFF));
        }
        int imgId = groupList.get(groupPosition).imgId;
        if (imgId != 0) {
            viewHolder.iv_pattern.setImageDrawable(context.getResources().getDrawable(groupList.get(groupPosition).imgId));
        }

        viewHolder.tv_groupName.setText(getGroup(groupPosition));

        return v;
    }

    // 차일드뷰를 반환한다.
    @Override
    public String getChild(int groupPosition, int childPosition) {
        return null;//childList.get(groupPosition).get(childPosition);
    }

    // 차일드뷰 사이즈를 반환한다.
    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition).size();
    }

    // 차일드뷰 ID를 반환한다.
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    private int curGroupPosition = 0;
    // 차일드뷰 각각의 ROW
    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        View v = convertView;
        curGroupPosition = groupPosition;
        if(v == null){
            childHolder = new ChildHolder();
            v = inflater.inflate(R.layout.list_expandable_child, null);
            childHolder.btnStartTime = (Button) v.findViewById(R.id.btn_start_time);
            childHolder.btnPatternTime = (Button) v.findViewById(R.id.btn_pattern_time);
            childHolder.btnRandomTime = (Button) v.findViewById(R.id.btn_random_time);
            childHolder.btnPatternOption = (Button) v.findViewById(R.id.btn_pattern_option);
            childHolder.llStartTime = (LinearLayout) v.findViewById(R.id.ll_start_time);
            childHolder.llEffectTime = (LinearLayout) v.findViewById(R.id.ll_effect_time);
            childHolder.llRandomTime = (LinearLayout) v.findViewById(R.id.ll_random_time);
            childHolder.llOption = (LinearLayout) v.findViewById(R.id.ll_pattern_option);
            childHolder.pbStartTime = (ProgressBar) v.findViewById(R.id.progressbar_start_time);
            childHolder.stepEffectTime = new View[5];
            childHolder.stepEffectTime[0] = v.findViewById(R.id.effect_step_0);
            childHolder.stepEffectTime[1] = v.findViewById(R.id.effect_step_1);
            childHolder.stepEffectTime[2] = v.findViewById(R.id.effect_step_2);
            childHolder.stepEffectTime[3] = v.findViewById(R.id.effect_step_3);
            childHolder.stepEffectTime[4] = v.findViewById(R.id.effect_step_4);
            childHolder.stepRandomTime = new View[5];
            childHolder.stepRandomTime[0] = v.findViewById(R.id.random_step_0);
            childHolder.stepRandomTime[1] = v.findViewById(R.id.random_step_1);
            childHolder.stepRandomTime[2] = v.findViewById(R.id.random_step_2);
            childHolder.stepRandomTime[3] = v.findViewById(R.id.random_step_3);
            childHolder.stepRandomTime[4] = v.findViewById(R.id.random_step_4);
            childHolder.stepOption = new View[4];
            childHolder.stepOption[0] = v.findViewById(R.id.option_step_0);
            childHolder.stepOption[1] = v.findViewById(R.id.option_step_1);
            childHolder.stepOption[2] = v.findViewById(R.id.option_step_2);
            childHolder.stepOption[3] = v.findViewById(R.id.option_step_3);
            v.setTag(childHolder);
        }else{
            childHolder = (ChildHolder)v.getTag();
        }
        if (childList.get(groupPosition).get(0).isShowStartDelay) {
            childHolder.btnStartTime.setVisibility(View.VISIBLE);
            childHolder.llStartTime.setVisibility(View.VISIBLE);
            childHolder.pbStartTime.setProgress(childList.get(groupPosition).get(0).timeStartDelay);
        } else {
            childHolder.btnStartTime.setVisibility(View.GONE);
            childHolder.llStartTime.setVisibility(View.GONE);
        }
        if (childList.get(groupPosition).get(0).isShowEffectDelay) {
            childHolder.btnPatternTime.setVisibility(View.VISIBLE);
            childHolder.llEffectTime.setVisibility(View.VISIBLE);
            int effectDelay = childList.get(groupPosition).get(0).timeEffectDelay;
            for (int i=0;i < 5;i++) {
                if (effectDelay >= i) {
                    childHolder.stepEffectTime[i].setVisibility(View.VISIBLE);
                } else {
                    childHolder.stepEffectTime[i].setVisibility(View.INVISIBLE);
                }
            }
        } else {
            childHolder.btnPatternTime.setVisibility(View.GONE);
            childHolder.llEffectTime.setVisibility(View.GONE);
        }
        if (childList.get(groupPosition).get(0).isShowRandomDelay) {
            childHolder.btnRandomTime.setVisibility(View.VISIBLE);
            childHolder.llRandomTime.setVisibility(View.VISIBLE);
            int randomDelay = childList.get(groupPosition).get(0).timeRandomDelay;
            for (int i=0;i < 5;i++) {
                if (randomDelay >= i) {
                    childHolder.stepRandomTime[i].setVisibility(View.VISIBLE);
                } else {
                    childHolder.stepRandomTime[i].setVisibility(View.INVISIBLE);
                }
            }
        } else {
            childHolder.btnRandomTime.setVisibility(View.GONE);
            childHolder.llRandomTime.setVisibility(View.GONE);
        }
        if (childList.get(groupPosition).get(0).isShowPatternOption) {
            childHolder.btnPatternOption.setVisibility(View.VISIBLE);
            int patternOption = childList.get(groupPosition).get(0).patternOption;
            String[] optBtnText = context.getResources().getStringArray(R.array.easy_effect_option_array_pattern_option);
            childHolder.btnPatternOption.setText(optBtnText[patternOption]);
            childHolder.llOption.setVisibility(View.VISIBLE);

            for (int i=0;i<4;i++) {
                if (patternOption >= i) {
                    childHolder.stepOption[i].setVisibility(View.VISIBLE);
                } else {
                    childHolder.stepOption[i].setVisibility(View.INVISIBLE);
                }
            }
        } else {
            childHolder.btnPatternOption.setVisibility(View.GONE);
            childHolder.llOption.setVisibility(View.GONE);
        }

        childHolder.btnStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "효과" + curGroupPosition + " 가 선택되었습니다.", Toast.LENGTH_SHORT).show();
                doStartTime(curGroupPosition, childList.get(curGroupPosition).get(0).timeStartDelay);
            }
        });
        childHolder.btnPatternTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childList.get(curGroupPosition).get(0).timeEffectDelay++;
                if (childList.get(curGroupPosition).get(0).timeEffectDelay > 4) {
                    childList.get(curGroupPosition).get(0).timeEffectDelay = 0;
                }
                doEffectTime(curGroupPosition, childList.get(curGroupPosition).get(0).timeEffectDelay);
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(context, "효과시간" + (childList.get(curGroupPosition).get(0).timeEffectDelay+1) + " 이 선택되었습니다.", Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
        childHolder.btnRandomTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childList.get(curGroupPosition).get(0).timeRandomDelay++;
                if (childList.get(curGroupPosition).get(0).timeRandomDelay > 4) {
                    childList.get(curGroupPosition).get(0).timeRandomDelay = 0;
                }
                doRandomTime(curGroupPosition, childList.get(curGroupPosition).get(0).timeRandomDelay);
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(context, "랜덤시간" + (childList.get(curGroupPosition).get(0).timeEffectDelay + 1) + " 이 선택되었습니다.", Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
        childHolder.btnPatternOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                childList.get(curGroupPosition).get(0).patternOption++;
                if (childList.get(curGroupPosition).get(0).patternOption > 3) {
                    childList.get(curGroupPosition).get(0).patternOption = 0;
                }
                doPatternOption(curGroupPosition, childList.get(curGroupPosition).get(0).patternOption);
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(context, "효과옵션" + (childList.get(curGroupPosition).get(0).patternOption + 1) + " 이 선택되었습니다.", Toast.LENGTH_SHORT);
                mToast.show();
            }
        });

        return v;
    }

    @Override
    public boolean hasStableIds() {	return true; }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) { return true; }

    class ViewHolder {
        public ImageView iv_image;
        public ImageView iv_pattern;
        public TextView tv_groupName;
        public LinearLayout ll_background;
    }

    class ChildHolder {
        public Button btnStartTime;
        public Button btnPatternTime;
        public Button btnRandomTime;
        public Button btnPatternOption;
        public LinearLayout llStartTime;
        public LinearLayout llEffectTime;
        public LinearLayout llRandomTime;
        public LinearLayout llOption;
        public ProgressBar pbStartTime;
        public View stepEffectTime[];
        public View stepRandomTime[];
        public View stepOption[];
    }

}
