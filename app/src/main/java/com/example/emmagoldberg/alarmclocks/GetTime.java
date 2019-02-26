package com.example.emmagoldberg.alarmclocks;

import android.util.Log;

import java.time.temporal.ChronoUnit;
import java.util.Calendar;

public class GetTime  {


    public Calendar getAlarmTime(boolean onWeekends, Calendar mAlarmCal){

       // logic chain to fetch the correct time based on when the alarm is set and weekend functionality

        Calendar calendar = Calendar.getInstance();
        int dayofWeek = calendar.get(Calendar.DAY_OF_WEEK);

        int alarmdayofWeek = mAlarmCal.get(Calendar.DAY_OF_WEEK);


        // if the alarm is set to the weekends, and it is not the weekend
        if (onWeekends == true && isWeekends() == false ){

            // if it's monday, we want it to ring saturday - add five days
            // it's tuesday, we want the alarm to ring starting saturday - add four days
            // and so on
            // in addition, we want to check that we don't add twice
            // so, we also check that the alarm isn't already set on saturday
            if (dayofWeek == Calendar.MONDAY && alarmdayofWeek!=Calendar.SATURDAY){
                mAlarmCal.add(Calendar.DATE, 5);
                return mAlarmCal;

            }

            else if (dayofWeek == Calendar.TUESDAY && alarmdayofWeek!=Calendar.SATURDAY){

                mAlarmCal.add(Calendar.DATE, 4);
                return mAlarmCal;


            }

            else if (dayofWeek == Calendar.WEDNESDAY && alarmdayofWeek!=Calendar.SATURDAY){

                mAlarmCal.add(Calendar.DATE, 3);
                return mAlarmCal;

            }

            else if (dayofWeek == Calendar.THURSDAY && alarmdayofWeek!= Calendar.SATURDAY){

                mAlarmCal.add(Calendar.DATE, 2);
                return mAlarmCal;

            }

            else if (dayofWeek == Calendar.FRIDAY && alarmdayofWeek!=Calendar.SATURDAY) {

                mAlarmCal.add(Calendar.DATE, 1);
                return mAlarmCal;

            }
            else {

                return mAlarmCal;
            }



        }

        // if it's set to the weekends, and it is the weekend
        if (onWeekends == true && isWeekends() == true){



            long hours = ChronoUnit.HOURS.between(Calendar.getInstance().toInstant(), mAlarmCal.toInstant());
            long minutes = ChronoUnit.MINUTES.between(Calendar.getInstance().toInstant(), mAlarmCal.toInstant());

            // it's saturday, and past the time (i.e. set for 9AM Saturday, but it's 9:10 AM - minutes < 0)
            // we also check that the alarm has not already been set
            if ( hours < 0 | minutes==0 | minutes < 0 && dayofWeek!=Calendar.SUNDAY && alarmdayofWeek!=Calendar
            .SUNDAY){
                // add a day to the alarm
                mAlarmCal.add(Calendar.DATE, 1);
                return mAlarmCal;

            }

            // if it's sunday, and pass the time - set for next weekend (starting Saturday)
            // and we haven't already set the alarm, so we don't add twelve days

            else if ( hours < 0 | minutes==0 | minutes < 0 && dayofWeek == Calendar.SUNDAY && alarmdayofWeek
            !=Calendar.SATURDAY){
                // add six days to the alarm
                Log.i("should be logged", "TRUE");
                mAlarmCal.add(Calendar.DATE, 6);
                return mAlarmCal;

            }

            else {

                // a regular weekend alarm - i.e. it's 3:00 PM on Saturday and we want our alarm to ring at 4:00 PM
                // it is not past the time
                return  mAlarmCal;
            }




        }

        if (onWeekends == false && isWeekends() == true){

            // if it's set for Mon-Fri, but it's the weekend
            // if it's saturday, add two days to the alarm
            // if it's sunday, add a day to the alarm

            if (dayofWeek == Calendar.SATURDAY && alarmdayofWeek!=Calendar.MONDAY){

                // the alarm has not already been updated

                mAlarmCal.add(Calendar.DATE, 2);
                return  mAlarmCal;
            }

            else if (dayofWeek == Calendar.SUNDAY && alarmdayofWeek!=Calendar.MONDAY) {

                // it's sunday and the alarm has not already been updated

                mAlarmCal.add(Calendar.DATE, 1);
                return mAlarmCal;

            }

            else {

                return mAlarmCal;
            }



        }

        if (onWeekends == false && isWeekends() == false){

            // if it's not set for the weekend, and it's not the weekend
            // if it's past the time, add a day


            // i.e. it's 3:10 on monday, the alarm is set for 2:10 on monday
            // we want to add a day so that it rings on 3:10 on tuesday - an alarm can't ring in the past
            // but we don't want the alarm to ring 3:10 on wednesday (add again), we only want to add once

            long hours = ChronoUnit.HOURS.between(Calendar.getInstance().toInstant(), mAlarmCal.toInstant());
            long minutes = ChronoUnit.MINUTES.between(Calendar.getInstance().toInstant(), mAlarmCal.toInstant());
            if (  hours < 0 | minutes==0 | minutes < 0 && dayofWeek == Calendar.MONDAY &&
                    alarmdayofWeek!=Calendar.TUESDAY

            ){
                mAlarmCal.add(Calendar.DATE, 1);


                return mAlarmCal;

            }

            if (  hours < 0 | minutes==0 | minutes < 0 && dayofWeek == Calendar.TUESDAY &&
                    alarmdayofWeek!=Calendar.WEDNESDAY

            ){
                mAlarmCal.add(Calendar.DATE, 1);


                return mAlarmCal;

            }



            if (  hours < 0 | minutes==0 | minutes < 0 && dayofWeek == Calendar.WEDNESDAY &&
                    alarmdayofWeek!=Calendar.THURSDAY

            ){
                mAlarmCal.add(Calendar.DATE, 1);


                return mAlarmCal;

            }

            if (  hours < 0 | minutes==0 | minutes < 0 && dayofWeek == Calendar.THURSDAY &&
                    alarmdayofWeek!=Calendar.FRIDAY

            ){
                mAlarmCal.add(Calendar.DATE, 1);


                return mAlarmCal;

            }



            // it's friday, set for mon-fri, alarm set for next day
            else if ( hours < 0 | minutes==0 | minutes < 0 && dayofWeek==Calendar.FRIDAY
            && alarmdayofWeek!=Calendar.MONDAY) {
                // add three days so it rings on monday
                mAlarmCal.add(Calendar.DATE, 3);
                return mAlarmCal;
            }
            else {

                return mAlarmCal;
            }



        }



        return null;

    }


    public boolean isWeekends(){
        // a function to determine whether it's the weekend or not
        Calendar calendar = Calendar.getInstance();
        int dayofWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayofWeek == Calendar.SATURDAY | dayofWeek == Calendar.SUNDAY){

            return true;
        }
        else {

            return false;
        }



    }






}
