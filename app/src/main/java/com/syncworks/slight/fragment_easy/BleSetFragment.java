package com.syncworks.slight.fragment_easy;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.syncworks.slight.R;
import com.syncworks.slightpref.SLightPref;
import com.syncworks.vosami.blelib.BluetoothDeviceAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link BleSetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BleSetFragment extends Fragment {

    // 스마트라이트 설정
    private SLightPref appPref;

    // 현재 설정된 장치의 이름과 주소 변수
    private String selDevName;
    private String selDevAddr;

    TextView tvCurrentDeviceName, tvCurrentDeviceAddress;
    Button btnBleScan, btnBleStop, btnModName;
    ListView deviceList;

    // 블루투스 장치 리스트
    private List<BluetoothDevice> mDevice = new ArrayList<>();
    // 블루투스 리스트 어댑터
    private BluetoothDeviceAdapter deviceListAdapter;

    private OnEasyFragmentListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BleSetFragment.
     */
    public static BleSetFragment newInstance() {
        BleSetFragment fragment = new BleSetFragment();
        return fragment;
    }

    public BleSetFragment() {
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
        selDevAddr = appPref.getString(SLightPref.DEVICE_ADDR);
        selDevName = appPref.getString(SLightPref.DEVICE_NAME);
        View view = inflater.inflate(R.layout.fragment_ble_set, container, false);
        tvCurrentDeviceName = (TextView) view.findViewById(R.id.tv_current_device_name);
        tvCurrentDeviceAddress = (TextView) view.findViewById(R.id.tv_current_device_address);
        btnBleScan = (Button) view.findViewById(R.id.btn_ble_scan);
        btnBleStop = (Button) view.findViewById(R.id.btn_ble_stop);
        btnModName = (Button) view.findViewById(R.id.btn_dev_mod_name);
        deviceList = (ListView) view.findViewById(R.id.lv_list_device);
        setTvCurrentDevice(selDevName, selDevAddr);

        deviceListAdapter = new BluetoothDeviceAdapter(getActivity().getBaseContext(), R.layout.device_list_item, mDevice);
        deviceList.setAdapter(deviceListAdapter);
        deviceList.setOnItemClickListener(itemClickListener);

        // 버튼 리스너 설정
        btnModName.setOnClickListener(btnClickListener);
        btnBleScan.setOnClickListener(btnClickListener);
        btnBleStop.setOnClickListener(btnClickListener);
        return view;
    }

    private Button.OnClickListener btnClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int id = v.getId();
                if (id == R.id.btn_dev_mod_name) {
                    mListener.onModifyName();
                } else if (id == R.id.btn_ble_scan) {
                    mListener.onScanStart();
                } else if (id == R.id.btn_ble_stop) {
                    mListener.onScanStop();
                }
            }
        }
    };

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String mDevName = ((BluetoothDevice)parent.getAdapter().getItem(position)).getName();
            String mDevAddr = ((BluetoothDevice)parent.getAdapter().getItem(position)).getAddress();
            setTvCurrentDevice(mDevName, mDevAddr);
            if (mListener != null) {
                setTvCurrentDevice(mDevName,mDevAddr);
                mListener.onSetDeviceItem(mDevName, mDevAddr);
            }
        }
    };

    // 현재 설정된 장치의 이름과 주소를 바꿔준다.
    public void setTvCurrentDevice(String name, String addr) {
        tvCurrentDeviceName.setText(name);
        tvCurrentDeviceAddress.setText(addr);
        selDevName = name;
        selDevAddr = addr;
        appPref.putString(SLightPref.DEVICE_NAME,selDevName);
        appPref.putString(SLightPref.DEVICE_ADDR,selDevAddr);
    }

    // 블루투스 검색 리스트의 데이터를 초기화\
    public void clearList() {
        deviceListAdapter.clear();
    }
    // 블루투스 검색 리스트에 검색된 장치 추가
    public void addList(BluetoothDevice device, int rssi) {
        if (device != null) {
            if (mDevice.indexOf(device) == -1) {
                mDevice.add(device);
                deviceListAdapter.addRssi(rssi);
                deviceListAdapter.notifyDataSetChanged();
            }
        }
    }

    // 버튼 출력 설정
    public void displayScanButton(boolean isVisibleScan) {
        // 스캔 버튼이 보이도록 설정
        if (isVisibleScan) {
            btnBleScan.setVisibility(View.VISIBLE);
            btnBleStop.setVisibility(View.GONE);
        }
        // 중지 버튼이 보이도록 설정
        else {
            btnBleScan.setVisibility(View.GONE);
            btnBleStop.setVisibility(View.VISIBLE);
        }
    }

    // 선택된 장치의 Address 정보 획득
    public String getDevAddr() {
        return selDevAddr;
    }
    // 선택된 장치의 Name 정보 획득
    public String getDevName() {
        return selDevName;
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

}
