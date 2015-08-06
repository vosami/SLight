package com.syncworks.slight.dialog;

/**
 * Created by vosami on 2015-08-06.
 */
public class AlarmDialogData {
    private int alarmNum = 0;
    private int hourOfDay = 0;
    private int minutes = 0;
    private int dateParam = 0;
    private int runTime = 0;
    private int runMode = 0;

    public AlarmDialogData() {
        alarmNum = 0;
        hourOfDay = 0;
        minutes = 0;
        dateParam = 0;
        runTime = 0;
        runMode = 0;
    }

    public AlarmDialogData(int a,int h, int m, int d, int r, int o) {
        this.alarmNum = a;
        this.hourOfDay = h;
        this.minutes = m;
        this.dateParam = d;
        this.runTime = r;
        this.runMode = o;
    }

    public void setAlarmNum(int d) {
        this.alarmNum = d;
    }

    public int getAlarmNum() {
        return this.alarmNum;
    }

    public void setHourOfDay(int d) {
        this.hourOfDay = d;
    }

    public int getHourOfDay() {
        return this.hourOfDay;
    }

    public void setMinutes(int d) {
        this.minutes = d;
    }

    public int getMinutes() {
        return this.minutes;
    }

    public void setDateParam(int d) {
        this.dateParam = d;
    }

    public int getDateParam() {
        return this.dateParam;
    }

    public void setRunTime(int d) {
        this.runTime = d;
    }

    public int getRunTime() {
        return this.runTime;
    }

    public void setRunMode(int d) {
        this.runMode = d;
    }

    public int getRunMode() {
        return  this.runMode;
    }
}
