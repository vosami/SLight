package com.syncworks.vosami.blelib;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vosami on 2015-07-22.
 */
public class BleScanParser {
    private final static byte GAP_ADTYPE_FLAGS =                        0x01;
    private final static byte GAP_ADTYPE_16BIT_MORE =                   0x02;
    private final static byte GAP_ADTYPE_16BIT_COMPLETE =               0x03;
    private final static byte GAP_ADTYPE_32BIT_MORE =                   0x04;
    private final static byte GAP_ADTYPE_32BIT_COMPLETE =               0x05;
    private final static byte GAP_ADTYPE_128BIT_MORE =                  0x06;
    private final static byte GAP_ADTYPE_128BIT_COMPLETE =              0x07;
    private final static byte GAP_ADTYPE_LOCAL_NAME_SHORT =             0x08;
    private final static byte GAP_ADTYPE_LOCAL_NAME_COMPLETE =          0x09;
    private final static byte GAP_ADTYPE_POWER_LEVEL =                  0x0A;
    private final static byte GAP_ADTYPE_OOB_CLASS_OF_DEVICE =          0x0D;
    private final static byte GAP_ADTYPE_OOB_SIMPLE_PAIRING_HASHC =     0x0E;
    private final static byte GAP_ADTYPE_OOB_SIMPLE_PAIRING_RANDR =     0x0F;
    private final static byte GAP_ADTYPE_SM_TK =                        0x10;
    private final static byte GAP_ADTYPE_SM_OOB_FLAG =                  0x11;
    private final static byte GAP_ADTYPE_SLAVE_CONN_INTERVAL_RANGE =    0x12;
    private final static byte GAP_ADTYPE_SIGNED_DATA =                  0x13;
    private final static byte GAP_ADTYPE_SERVICES_LIST_16BIT =          0x14;
    private final static byte GAP_ADTYPE_SERVICES_LIST_128BIT =         0x15;
    private final static byte GAP_ADTYPE_SERVICE_DATA =                 0x16;
    private final static byte GAP_ADTYPE_PUBLIC_TARGET_ADDR =           0x17;
    private final static byte GAP_ADTYPE_RANDOM_TARGET_ADDR =           0x18;
    private final static byte GAP_ADTYPE_APPEARANCE =                   0x19;
    private final static byte GAP_ADTYPE_ADV_INTERVAL =                 0x1A;
    private final static byte GAP_ADTYPE_LE_BD_ADDR =                   0x1B;
    private final static byte GAP_ADTYPE_LE_ROLE =                      0x1C;
    private final static byte GAP_ADTYPE_SIMPLE_PAIRING_HASHC_256 =     0x1D;
    private final static byte GAP_ADTYPE_SIMPLE_PAIRING_RANDR_256 =     0x1E;
    private final static byte GAP_ADTYPE_SERVICE_DATA_32BIT =           0x20;
    private final static byte GAP_ADTYPE_SERVICE_DATA_128BIT =          0x21;
    private final static byte GAP_ADTYPE_3D_INFO_DATA =                 0x3D;
    private final static byte GAP_ADTYPE_MANUFACTURER_SPECIFIC =  (byte)0xFF;

    private String name = null;
    private String version = null;
    private byte[] service = null;

    private List<Integer> identifierStartLocation;
    private List<Integer> identifierStartOffset;

    public BleScanParser(byte[] data) {
        identifierStartLocation = new ArrayList<>();
        identifierStartOffset = new ArrayList<>();
        for (int i=0;i<data.length;i++) {
            if (data[i] == 0) {
                break;
            } else {
                identifierStartLocation.add(i);
                identifierStartOffset.add(data[i]-1);
                i = i+data[i];
            }
        }
        int startPos;
        int length;
        for (int i=0;i<identifierStartLocation.size();i++) {
            startPos = identifierStartLocation.get(i)+2;
            length = identifierStartOffset.get(i);

            if (data[startPos - 1] == GAP_ADTYPE_128BIT_COMPLETE) {
                service = new byte[length];
                for (int j=0;j<length;j++) {
                    service[j] = data[startPos + j];
                }
            } else if (data[startPos - 1] == GAP_ADTYPE_LOCAL_NAME_SHORT) {
                try {
                    version = new String(data,startPos,length,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                version = version.replace('\uFFFD',' ');
                version = version.replace('\u0000', ' ');
                version = version.replace(" ","");
            } else if (data[startPos - 1] == GAP_ADTYPE_LOCAL_NAME_COMPLETE) {
                try {
                    name = new String(data, startPos, length,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                name = name.replace('\u0000',' ');
                name = name.replace(" ","");
                //name = name.substring(0,name.length()-2);
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public byte[] getService() {
        return service;
    }

}
