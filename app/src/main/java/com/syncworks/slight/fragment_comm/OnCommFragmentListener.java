package com.syncworks.slight.fragment_comm;

import android.net.Uri;

/**
 * Created by vosami on 2015-06-11.
 */
public interface OnCommFragmentListener {
    // TODO: Update argument type and name
    public void onFragmentInteraction(Uri uri);

    public void onModifyName();

    public void onScanStart();

    public void onScanStop();

    public void onSetDeviceItem(String devName, String devAddr);
}
