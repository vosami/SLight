package com.syncworks.slight.fragment_comm;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.syncworks.slight.R;


/**
 * A placeholder fragment containing a simple view.
 */
public class BleSetFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "name";
    private static final String ARG_PARAM2 = "addr";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView tvCurrentDeviceName, tvCurrentDeviceAddress;
    Button btnBleScan;
    ListView deviceList;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BrightFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BleSetFragment newInstance(String param1, String param2) {
        BleSetFragment fragment = new BleSetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public BleSetFragment() {
    }

    // Fragment 의 View 등록
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ble_set, container, false);
        tvCurrentDeviceName = (TextView) view.findViewById(R.id.tv_current_device_name);
        tvCurrentDeviceAddress = (TextView) view.findViewById(R.id.tv_current_device_address);
        btnBleScan = (Button) view.findViewById(R.id.btn_ble_scan);
        deviceList = (ListView) view.findViewById(R.id.list_device);
        return view;
    }

    public void setBtnBleScan(boolean isScan) {
        if (isScan) {
            btnBleScan.setText(getResources().getText(R.string.btn_ble_scan));
        } else {
            btnBleScan.setText(getResources().getText(R.string.btn_ble_stop));
        }
    }

    public void setTvCurrentDevice(String name, String addr) {
        tvCurrentDeviceName.setText(name);
        tvCurrentDeviceAddress.setText(addr);
    }
}
