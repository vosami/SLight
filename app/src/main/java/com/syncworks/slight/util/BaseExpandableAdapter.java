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
import android.widget.TextView;
import android.widget.Toast;

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
        viewHolder.iv_pattern.setImageDrawable(context.getResources().getDrawable(groupList.get(groupPosition).imgId));
        /*switch (groupPosition) {
            case 0:
                viewHolder.iv_pattern.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pattern_always));
                break;
            case 1:
                viewHolder.iv_pattern.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pattern_pulse));
                break;
            case 2:
                viewHolder.iv_pattern.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pattern_flash));
                break;
            case 3:
                viewHolder.iv_pattern.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pattern_up_down));
                break;
            case 4:
                viewHolder.iv_pattern.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pattern_torch));
                break;
            case 5:
                viewHolder.iv_pattern.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pattern_always));
                break;
            case 6:
                viewHolder.iv_pattern.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pattern_always));
                break;
        }*/

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
            v.setTag(childHolder);
        }else{
            childHolder = (ChildHolder)v.getTag();
        }
        if (childList.get(groupPosition).get(0).isShowStartDelay) {
            childHolder.btnStartTime.setVisibility(View.VISIBLE);
            int timeStartDelay = childList.get(groupPosition).get(0).timeStartDelay;
        } else {
            childHolder.btnStartTime.setVisibility(View.INVISIBLE);
        }
        if (childList.get(groupPosition).get(0).isShowEffectDelay) {
            childHolder.btnPatternTime.setVisibility(View.VISIBLE);
        } else {
            childHolder.btnPatternTime.setVisibility(View.INVISIBLE);
        }
        if (childList.get(groupPosition).get(0).isShowRandomDelay) {
            childHolder.btnRandomTime.setVisibility(View.VISIBLE);
        } else {
            childHolder.btnRandomTime.setVisibility(View.INVISIBLE);
        }
        childHolder.btnStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Button StartTime" + curGroupPosition + " is clicked !", Toast.LENGTH_SHORT).show();
            }
        });
        childHolder.btnPatternTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Button PatternTime" + curGroupPosition + " is clicked !", Toast.LENGTH_SHORT).show();
            }
        });
        childHolder.btnRandomTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Button RandomTime " + curGroupPosition + " is clicked !", Toast.LENGTH_SHORT).show();
            }
        });
        //childHolder.btnStartTime.setText(getChild(groupPosition, childPosition));

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
    }

}
