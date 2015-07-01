package com.syncworks.leddata;

import static com.syncworks.define.Define.*;

/**
 * Created by vosami on 2015-07-01.
 */
public class LedData {
    private int _val;
    private int _duration;

    // �⺻ ������
    public LedData() {
        _val = 0;
        _duration = 0;
    }

    // �⺻ ��� ���� ������
    public LedData(int val, int duration) {
        // val ���� 0~191 �� ��� �⺻ ��� ����
        if (val < OP_CODE_MIN && val >= 0) {
            _val = val;
            _duration = duration;
        }
        // val ���� instruct ������ �� ��ɾ� ����
        else if (val > OP_CODE_MIN && val <= 0xFF) {
            setInstruct(val,duration);
        } else {
            _val = 0;
            _duration = 0;
        }
    }
    // ��ɾ� ���� ������
    public LedData(int instruct, Object... args) {
        if (instruct > OP_CODE_MIN && instruct <= 0xFF) {
            setInstruct(instruct,args);
        }
    }

    public void setInstruct(int instruct, Object... args) {
        switch (instruct) {
            case OP_START:
                break;
            case OP_END:
                break;
        }
    }
}
