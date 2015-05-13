package com.syncworks.slight.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by vosami on 2015-05-13.
 */
public class CustomToast {
    public static void middleBottom(Context c, String txt) {
        Toast m = Toast.makeText(c, txt, Toast.LENGTH_SHORT);
        m.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 300);
        m.show();
    }
    public static void middleTop(Context c, String txt) {
        Toast m = Toast.makeText(c, txt, Toast.LENGTH_SHORT);
        m.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 200);
        m.show();
    }
}
