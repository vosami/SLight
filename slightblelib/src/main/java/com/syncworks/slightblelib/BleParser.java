package com.syncworks.slightblelib;

import java.util.regex.Pattern;

/**
 * Created by vosami on 2015-07-24.
 * 스캔 데이터 읽어오기
 */
public class BleParser {
    private static final Object TAG = BleParser.class;
    private static final Pattern I_PATTERN = Pattern.compile("i\\:(\\d+)\\-(\\d+)(l?)");
    private static final Pattern M_PATTERN = Pattern.compile("m\\:(\\d+)-(\\d+)\\=([0-9A-Fa-f]+)");
    private static final Pattern S_PATTERN = Pattern.compile("s\\:(\\d+)-(\\d+)\\=([0-9A-Fa-f]+)");
    private static final Pattern D_PATTERN = Pattern.compile("d\\:(\\d+)\\-(\\d+)([bl]?)");
    private static final Pattern P_PATTERN = Pattern.compile("p\\:(\\d+)\\-(\\d+)");
    private static final char[] HEX_ARRAY = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
}
