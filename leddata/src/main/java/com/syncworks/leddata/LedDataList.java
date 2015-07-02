package com.syncworks.leddata;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static com.syncworks.define.Define.*;

/**
 * Created by vosami on 2015-07-02.
 * LED 데이타 리스트
 */
public class LedDataList implements List<LedData>, Serializable {
    // LED 번호
    private int ledNumber = 0;
    // 스크립트 데이터
    private List<LedData> ledDatas;

    private int percentBright = 100;
    private int percentDuration = 100;


    // 기본 생성자 - LED 번호가 0으로 설정
    public LedDataList() {
        ledNumber = 0;
        ledDatas = new ArrayList<>();
        init();
    }
    // LED 번호 생성자 - LED 번호를 설정
    public LedDataList(int ledNum) {
        this.ledNumber = ledNum;
        ledDatas = new ArrayList<>();
        init();
    }
    // LED 번호와 데이터 리스트 생성자
    public LedDataList(int ledNum, List<LedData> dataList) {
        ledNumber = ledNum;
        ledDatas = dataList;
        init();
    }
    // 기본 변수 초기화
    private void init() {
        percentBright = 100;
        percentDuration = 100;
    }

    @Override
    public void add(int i, LedData ledData) {
        if (ledDatas.size() <= DATA_ARRAY_MAX) {
            this.ledDatas.add(i,ledData);
        }
    }

    @Override
    public boolean add(LedData ledData) {
        if (ledDatas.size() <= DATA_ARRAY_MAX) {
            this.ledDatas.add(ledData);
            return true;
        }
        return false;
    }

    @Override
    public boolean addAll(int i, Collection<? extends LedData> collection) {
        return ledDatas.addAll(i,collection);
    }

    @Override
    public boolean addAll(Collection<? extends LedData> collection) {
        return ledDatas.addAll(collection);
    }

    @Override
    public void clear() {
        ledDatas.clear();
        init();
    }

    @Override
    public boolean contains(Object o) {
        int val = (int) o;
        int compareVal;
        if (val > OP_CODE_MIN) {
            for (int i = 0; i < ledDatas.size(); i++) {
                compareVal = ledDatas.get(i).getVal();
                if (compareVal == val) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return ledDatas.containsAll(collection);
    }

    @Override
    public LedData get(int i) {
        return ledDatas.get(i);
    }

    @Override
    public int indexOf(Object o) {
        int val = (int) o;
        int compareVal;
        if (val>OP_CODE_MIN) {
            for (int i=0;i < ledDatas.size();i++) {
                compareVal = ledDatas.get(i).getVal();
                if (compareVal == val) {
                    return i;
                }
            }
        }
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return ledDatas.isEmpty();
    }

    @NonNull
    @Override
    public Iterator<LedData> iterator() {
        return ledDatas.iterator();
    }

    @Override
    public int lastIndexOf(Object o) {
        return ledDatas.lastIndexOf(o);
    }

    @NonNull
    @Override
    public ListIterator<LedData> listIterator() {
        return ledDatas.listIterator();
    }

    @NonNull
    @Override
    public ListIterator<LedData> listIterator(int i) {
        return ledDatas.listIterator(i);
    }

    @Override
    public LedData remove(int i) {
        return ledDatas.remove(i);
    }

    @Override
    public boolean remove(Object o) {
        return ledDatas.remove(o);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return ledDatas.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return ledDatas.retainAll(collection);
    }

    @Override
    public LedData set(int i, LedData ledData) {
        return ledDatas.set(i, ledData);
    }

    @Override
    public int size() {
        return ledDatas.size();
    }

    @NonNull
    @Override
    public List<LedData> subList(int i, int i1) {
        return ledDatas.subList(i, i1);
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return ledDatas.toArray();
    }

    @NonNull
    @Override
    public <T> T[] toArray(T[] ts) {
        return ledDatas.toArray(ts);
    }

    public byte[] toByteArray() {
        int size = ledDatas.size();
        int byteLength = size*2;
        LedData subData;
        byte[] byteArray = new byte[byteLength];
        for (int i=0;i<size;i++) {
            subData = ledDatas.get(i);
            byteArray[2*i] = (byte)subData.getVal();
            byteArray[2*i +1] = (byte) subData.getDuration();
        }
        return byteArray;
    }

    public void setBrightPercent(int percent) {
        if (percent<0 || percent > 100) {
            return;
        }
        int size = ledDatas.size();
        percentBright = percent;
        for (int i=0;i<size;i++) {
            ledDatas.get(i).modBrightPercent(percent);
        }
    }

    public void setDurationPercent(int percent) {
        if (percent<0 || percent > 300) {
            return;
        }
        int size = ledDatas.size();
        percentDuration = percent;
        for (int i=0;i<size;i++) {
            ledDatas.get(i).modDurationPercent(percent);
        }
    }

    public void setLedNumber(int ledNum) {
        ledNumber = ledNum;
    }
}
