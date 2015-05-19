package com.syncworks.scriptdata;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.syncworks.define.Define;

import java.util.List;

/**
 * Created with Android Studio
 * Copyrights (C)SyncWorks All rights reserved by SyncWorks
 * Created by 승현 on 2015-04-05.
 */
public class ScriptAdapter extends ArrayAdapter<ScriptData> {
    private final static String TAG = ScriptAdapter.class.getSimpleName();

	private List<ScriptData> objects;

    private TextView tvCount;
    private TextView tvFirstVal;
    private TextView tvSecondVal;


	public ScriptAdapter(Context context, int resource, List<ScriptData> objects) {
		super(context, resource, objects);
		this.objects = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        convertView = getLayout(position);
		/*if (convertView == null) {
            convertView = getLayout(position);
            Log.d(TAG,"null-Position:"+position);
		}
        else {
            Log.d(TAG,"Position:"+position);
            convertView = getLayout(position);
        }*/
		return convertView;
	}

    private View getLayout(int pos) {
        View retView = null;
        int val = objects.get(pos).getVal();
        int duration = objects.get(pos).getDuration();
        LayoutInflater inflater = LayoutInflater.from(getContext());

        String mStr;
        float mDuration;

        if (val < Define.OP_CODE_MIN) {
            retView = inflater.inflate(R.layout.rl_default,null);
            // 스크립트 카운트 적용
            tvCount = (TextView) retView.findViewById(R.id.script_count);
            tvCount.setText(Integer.toString(pos));
            // 밝기 텍스트 적용
            tvFirstVal = (TextView) retView.findViewById(R.id.first_value);
            float brightPercent = (float)val / (Define.OP_CODE_MIN-1) *100;
            mStr = String.format("%.1f",brightPercent);
            tvFirstVal.setText(mStr);
            // 밝기 값 적용
            View vBright = retView.findViewById(R.id.view_bright);
            int rgbColor = (int)(255 * brightPercent / 100);
            vBright.setBackgroundColor(Color.rgb(rgbColor,rgbColor,rgbColor));
            // 지연시간 텍스트 적용
            tvSecondVal = (TextView) retView.findViewById(R.id.second_value);
            mDuration = (float)(duration+1) / 100;
            mStr = String.format("%.2f", mDuration);
            if (duration == 0xFF) {
                mStr = "∞";
            }
            tvSecondVal.setText(mStr);
        }
        else {
            switch (val) {
                case Define.OP_START:
                    retView = inflater.inflate(R.layout.rl_start,null);
                    // 스크립트 카운트 적용
                    tvCount = (TextView) retView.findViewById(R.id.script_count);
                    tvCount.setText(Integer.toString(pos));
                    // 지연 시간 텍스트 적용
                    tvFirstVal = (TextView) retView.findViewById(R.id.first_value);
                    mDuration = (float)(duration*8+1) / 100;
                    mStr = String.format("%.2f", mDuration);
                    if (duration == 0xFF) {
                        mStr = "∞";
                    }
                    tvFirstVal.setText(mStr);
                    break;
                case Define.OP_END:
                    retView = inflater.inflate(R.layout.rl_end,null);
                    // 스크립트 카운트 적용
                    tvCount = (TextView) retView.findViewById(R.id.script_count);
                    tvCount.setText(Integer.toString(pos));
                    // 지연 시간 텍스트 적용
                    tvFirstVal = (TextView) retView.findViewById(R.id.first_value);
                    mDuration = (float)(duration*8+1) / 100;
                    mStr = String.format("%.2f", mDuration);
                    if (duration == 0xFF) {
                        mStr = "∞";
                    }
                    tvFirstVal.setText(mStr);
                    break;
				case Define.OP_RANDOM_VAL:
					retView = inflater.inflate(R.layout.rl_random_val,null);
					// 스크립트 카운트 적용
					tvCount = (TextView) retView.findViewById(R.id.script_count);
					tvCount.setText(Integer.toString(pos));
					// 기준 밝기 텍스트 적용
					tvFirstVal = (TextView) retView.findViewById(R.id.first_value);
					float refBright = (float) (duration>>3) * 6 / (Define.OP_CODE_MIN-1) *100;
					mStr = String.format("%.1f",refBright);
					tvFirstVal.setText(mStr);
					// 랜덤 범위 텍스트 적용
					tvSecondVal = (TextView) retView.findViewById(R.id.second_value);
					break;
				case Define.OP_RANDOM_DELAY:
					break;
				case Define.OP_NOP:
					break;
				case Define.OP_FOR_START:
					break;
				case Define.OP_PASS_DATA:
					break;
				case Define.OP_FOR_END:
					break;
//				case Define.OP_TRANSITION:
//					break;
                default:
                    retView = inflater.inflate(R.layout.rl_default,null);
                    break;
            }
        }
        return retView;
    }

}
