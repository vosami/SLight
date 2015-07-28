package com.syncworks.slight.fragment_easy;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.syncworks.define.Logger;
import com.syncworks.slight.R;
import com.syncworks.slightpref.SLightPref;
import com.syncworks.vosami.blelib.BleDeviceAdapter;
import com.syncworks.vosami.blelib.BleDeviceData;
import com.syncworks.vosami.blelib.BleScanParser;
import com.syncworks.vosami.blelib.LecGattAttributes;

import java.util.ArrayList;
import java.util.Arrays;
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
    private String selDevVersion;
    private boolean isNotScan;

    TextView tvCurrentDeviceName, tvCurrentDeviceAddress, tvCurrentDeviceVersion;
    Button btnBleScan, btnBleStop, btnModName,btnTestConnect;
    ProgressBar pbScan;
    ListView deviceList;
    CheckBox cbNotScan;

    // 블루투스 장치 리스트
    private List<BleDeviceData> mDevice = new ArrayList<>();
    // 블루투스 리스트 어댑터
    private BleDeviceAdapter deviceListAdapter;

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
        selDevVersion = appPref.getString(SLightPref.DEVICE_VERSION);
        isNotScan = appPref.getBoolean(SLightPref.EASY_BLE_NOT_SCAN);
        View view = inflater.inflate(R.layout.fragment_ble_set, container, false);
        cbNotScan = (CheckBox) view.findViewById(R.id.cb_not_scan);
        cbNotScan.setChecked(isNotScan);
        tvCurrentDeviceName = (TextView) view.findViewById(R.id.tv_current_device_name);
        tvCurrentDeviceAddress = (TextView) view.findViewById(R.id.tv_current_device_address);
        tvCurrentDeviceVersion = (TextView) view.findViewById(R.id.tv_current_device_version);
        btnBleScan = (Button) view.findViewById(R.id.btn_ble_scan);
        btnBleStop = (Button) view.findViewById(R.id.btn_ble_stop);
        btnTestConnect = (Button) view.findViewById(R.id.btn_dev_test_connect);
        pbScan = (ProgressBar) view.findViewById(R.id.progress_scan);
        btnModName = (Button) view.findViewById(R.id.btn_dev_mod_name);
        deviceList = (ListView) view.findViewById(R.id.lv_list_device);
        setTvCurrentDevice(selDevName, selDevAddr,selDevVersion);

        deviceListAdapter = new BleDeviceAdapter(getActivity().getBaseContext(), R.layout.device_list_item, mDevice);
        deviceList.setAdapter(deviceListAdapter);
        deviceList.setOnItemClickListener(itemClickListener);

        // 버튼 리스너 설정
        btnModName.setOnClickListener(btnClickListener);
        btnBleScan.setOnClickListener(btnClickListener);
        btnBleStop.setOnClickListener(btnClickListener);
        btnTestConnect.setOnClickListener(btnClickListener);
        if (!appPref.getBoolean(SLightPref.EASY_ACTIVITY[0])) {
            appPref.putBoolean(SLightPref.EASY_ACTIVITY[0],true);
            showOverLay();
        }
        cbNotScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appPref.putBoolean(SLightPref.EASY_BLE_NOT_SCAN,cbNotScan.isChecked());
                if (cbNotScan.isChecked()) {
                    mListener.onScanStop();
                }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Logger.d(this,"onStart");
        if (mListener != null) {
            // 설정된 장치가 없거나 장치 검색 체크박스가 해제되어 있다면 검색 시작
            String devName = appPref.getString(SLightPref.DEVICE_NAME);
            if (!appPref.getBoolean(SLightPref.EASY_BLE_NOT_SCAN) || ((devName.length() == 4)&&(devName.contains(getString(R.string.str_none))))) {
                mListener.onFrag1Start();
                displayScanButton(false);
            } else {
                displayScanButton(true);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.d(this, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.d(this, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Logger.d(this, "onStop");
        if (mListener != null) {
            mListener.onFrag1End();
        }
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
                } else if (id == R.id.btn_dev_test_connect) {
                    mListener.onTestConnect();
                }
            }
        }
    };

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String mDevName = ((BleDeviceData)parent.getAdapter().getItem(position)).getName();
            String mDevAddr = ((BleDeviceData)parent.getAdapter().getItem(position)).getAddr();
            String mDevVersion = ((BleDeviceData)parent.getAdapter().getItem(position)).getVersion();
            if (mListener != null) {
                setTvCurrentDevice(mDevName,mDevAddr,mDevVersion);
                mListener.onSetDeviceItem(mDevName, mDevAddr);
            }
        }
    };

    // 현재 설정된 장치의 이름과 주소를 바꿔준다.
    public void setTvCurrentDevice(String name, String addr, String version) {
        tvCurrentDeviceName.setText(name);
        tvCurrentDeviceAddress.setText(addr);
        tvCurrentDeviceVersion.setText(version);
        selDevName = name;
        selDevAddr = addr;
        selDevVersion = version;
        appPref.putString(SLightPref.DEVICE_NAME,selDevName);
        appPref.putString(SLightPref.DEVICE_ADDR,selDevAddr);
        appPref.putString(SLightPref.DEVICE_VERSION,selDevVersion);
    }

    public void setTvCurrentDeviceName(String name) {
        String addr = appPref.getString(SLightPref.DEVICE_ADDR);
        tvCurrentDeviceName.setText(name);
        tvCurrentDeviceAddress.setText(addr);
        selDevName = name;
        selDevAddr = addr;
        appPref.putString(SLightPref.DEVICE_NAME,selDevName);
    }

    // 블루투스 검색 리스트의 데이터를 초기화\
    public void clearList() {
        deviceListAdapter.clear();
    }
    // 블루투스 검색 리스트에 검색된 장치 추가
    public void addList(byte[] scanResult, String addr, int rssi) {
        BleScanParser bsp = new BleScanParser(scanResult);
        byte[] serviceUUID = bsp.getService();
        if (!Arrays.equals(serviceUUID,LecGattAttributes.TXRX_SERV_UUID)) {
            return;
        }
        int containDevice = 0;
        for (int i=0;i<mDevice.size();i++) {
            if (mDevice.get(i).getAddr().contains(addr)) {
                containDevice++;
                mDevice.get(i).setRssi(rssi);
                deviceListAdapter.notifyDataSetChanged();
            }
        }
        if (containDevice == 0) {
            BleDeviceData bdd = new BleDeviceData(bsp.getName(),addr,rssi,bsp.getVersion());
            mDevice.add(bdd);
            deviceListAdapter.notifyDataSetChanged();
        }

        //mDevice.add(device);
        //deviceListAdapter.notifyDataSetChanged();
        /*if (device != null) {
            if (mDevice.indexOf(device) == -1) {
                deviceListAdapter.addRssi(rssi);
                mDevice.add(scanResult);
                //mDevice.add(device);
                deviceListAdapter.notifyDataSetChanged();
            }
        }*/
    }

    // 버튼 출력 설정
    public void displayScanButton(boolean isVisibleScan) {
        // 스캔 버튼이 보이도록 설정
        if (isVisibleScan) {
            btnBleScan.setVisibility(View.VISIBLE);
            btnBleStop.setVisibility(View.GONE);
            pbScan.setVisibility(View.GONE);
        }
        // 중지 버튼이 보이도록 설정
        else {
            btnBleScan.setVisibility(View.GONE);
            btnBleStop.setVisibility(View.VISIBLE);
            pbScan.setVisibility(View.VISIBLE);
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

    public void showOverLay() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_help_ble_set);
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

}
