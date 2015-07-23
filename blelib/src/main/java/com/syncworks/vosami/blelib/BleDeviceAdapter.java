package com.syncworks.vosami.blelib;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


/**
 * Created by vosami on 2015-02-25.
 */
public class BleDeviceAdapter extends ArrayAdapter<BleDeviceData> {
    private List<BleDeviceData> _objects;

	// Adapter 생성자
    public BleDeviceAdapter(Context context, int resource, List<BleDeviceData> objects) {
        super(context, resource, objects);
		this._objects = objects;
    }

    /*public void addRssi(int mRssi) {
        this._dataRssi.add(mRssi);
    }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		// 처음 생성시에는 리스트 아이템을 새로 생성
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.device_list_item, null);
		}
		// TextView 레이아웃에서 ID로 찾기
		TextView tvDevName = (TextView) v.findViewById(R.id.li_dev_name);
		TextView tvDevAddr = (TextView) v.findViewById(R.id.li_dev_addr);
		TextView tvDevRssi = (TextView) v.findViewById(R.id.li_dev_rssi);
		TextView tvDevVersion = (TextView) v.findViewById(R.id.li_dev_version);
		// _objects 데이터에서 현재 위치의 데이터 가져오기
		//String mDevName = _objects.get(position).getName();
		//String mDevAddr = _objects.get(position).getAddress();

		// TextView 에 데이터 기록
		tvDevName.setText(_objects.get(position).getName());
		tvDevAddr.setText(_objects.get(position).getAddr());
		int mDevRssi = _objects.get(position).getRssi();
		tvDevRssi.setText(Integer.toString(mDevRssi));
		tvDevVersion.setText(_objects.get(position).getVersion());
		// 완성된 View 반환
        return v;
    }
}

