package com.example.emmagoldberg.alarmclocks;

import java.util.Calendar;
import java.util.UUID;

public class Alarm {

    private int mAlarmId;
    // the alarm id
    private boolean isOn;
    // whether the alarm is on or off
    private Calendar mTimeSet;
    // the time we are setting the alarm
    private boolean onWeekends;
    // whether the alarm is set for Sat-Sun
    private String alarmSound;
    // what alarm sound the user chooses


    public int getmAlarmId() {
        return mAlarmId;
    }

    public void setmAlarmId(int mAlarmId) {
        this.mAlarmId = mAlarmId;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public Calendar getmTimeSet() {
        return mTimeSet;
    }

    public void setmTimeSet(Calendar mTimeSet) {
        this.mTimeSet = mTimeSet;
    }

    public boolean isOnWeekends() {
        return onWeekends;
    }

    public void setOnWeekends(boolean onWeekends) {
        this.onWeekends = onWeekends;
    }

    public String getAlarmSound() {
        return alarmSound;
    }

    public void setAlarmSound(String alarmSound) {
        this.alarmSound = alarmSound;
    }
}
