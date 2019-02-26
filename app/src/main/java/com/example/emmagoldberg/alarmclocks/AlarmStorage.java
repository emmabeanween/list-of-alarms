package com.example.emmagoldberg.alarmclocks;

import android.util.Log;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class AlarmStorage {


    // store alarms in a singleton so that we don't recreate the list of alarms
    private static AlarmStorage alarmStorage;
    private List<Alarm> alarmList;



    public static AlarmStorage getInstance(){
        if (alarmStorage == null){
            // if this is the first instance of the alarm being created
            alarmStorage = new AlarmStorage();
        }

        return alarmStorage;
    }


    public AlarmStorage(){

        alarmList = new ArrayList<>();
        // create one alarm prematurely so that the list of alarms is not empty
        Alarm preAlarm = new Alarm();
        preAlarm.setOn(false);
        preAlarm.setOnWeekends(false);
        Calendar preCal = Calendar.getInstance();
        // set the alarm to 8 AM
        preCal.set(Calendar.HOUR_OF_DAY, 8);
        preCal.set(Calendar.MINUTE, 0);
        // see what day it is today
        int randId = 100000 + (int)(new Random().nextInt() * 899900);
        preAlarm.setmAlarmId(randId);
        preAlarm.setmTimeSet(preCal);
        preAlarm.setAlarmSound("birds");
        alarmList.add(preAlarm);

    }



    public void addAlarm(Alarm mAlarm){

        // a method to add an alarm


        alarmList.add(mAlarm);



    }

    public void deleteAlarm(Alarm mAlarm){

        // a method to delete an alarm

        alarmList.remove(mAlarm);

    }

    public List<Alarm> getmAlarms() {

        // a method to retrieve the current list of alarms

        return alarmList;

    }

    public Alarm getAlarm(int mId){

        // a method to get a specific alarm based off of its id

        for (Alarm eachAlarm:alarmList
                ) {

            if (eachAlarm.getmAlarmId() == mId){

                return eachAlarm;
            }

        }
        return null;
    }






}