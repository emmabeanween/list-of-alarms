package com.example.emmagoldberg.alarmclocks;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CustomAlarmAdapter extends BaseAdapter {

    private Context context;
    private List<Alarm> mAlarms;
    private TextView mAlarmTextView;
    private TextView mWeekendTextView;
    private Switch mAlarmSwitch;
    private SimpleDateFormat mTimeFormat;
    private PendingIntent mIntent;


    public CustomAlarmAdapter(Context mContext, List<Alarm> allAlarms){

        context = mContext;
        mAlarms = allAlarms;


    }


    @Override
    public int getCount() {
        return mAlarms.size();
    }

    @Override
    public Object getItem(int position) {
        return mAlarms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void refreshAlarmList(List<Alarm> newAlarms){

        mAlarms = newAlarms;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         View myView = LayoutInflater.from(context).inflate(R.layout.custom_alarm_layout, parent, false);

         final Alarm eachAlarm = mAlarms.get(position);

         mAlarmTextView = (TextView)myView.findViewById(R.id.each_alarm_text_view);
         mWeekendTextView = (TextView)myView.findViewById(R.id.weekend_text_view);

         if (eachAlarm.isOnWeekends() == true){

             mWeekendTextView.setText("Sat/Sun");

         }

         else {

             mWeekendTextView.setText("Mon/Fri");
         }

         // get the time the alarm is set to
        Calendar mAlarmTime = eachAlarm.getmTimeSet();
        Log.i("hour", String.valueOf(mAlarmTime.get(Calendar.HOUR_OF_DAY)));


        // determine whether the hour of day is single or double digit for formatting
        int twelveHour = eachAlarm.getmTimeSet().get(Calendar.HOUR);
        // gets 12-hour time
        if (twelveHour == 10 | twelveHour == 11 | twelveHour == 12){

            mTimeFormat = new SimpleDateFormat(
                    "hh:mm a");


        }
        else {

            mTimeFormat = new SimpleDateFormat("h:mm a");
        }
        mAlarmTextView.setText(mTimeFormat.format(mAlarmTime.getTime()));


         mAlarmSwitch = (Switch)myView.findViewById(R.id.alarm_switch);

         if (eachAlarm.isOn() == true){

             // set the toggle to be checked if the alarm is on

             mAlarmSwitch.setChecked(true);
         }

         else {

             // set the toggle to not be checked if the alarm is off
             mAlarmSwitch.setChecked(false);
         }



        mAlarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked == true){



                    // wire up alarm
                    eachAlarm.setOn(true);
                    // get the current time
                    Calendar currentTime = eachAlarm.getmTimeSet();
                    Calendar mnewAlarmCal = new GetTime().getAlarmTime(eachAlarm.isOnWeekends(), currentTime);
                    eachAlarm.setmTimeSet(mnewAlarmCal);
                    refreshAlarmList(AlarmStorage.getInstance().getmAlarms());


                    // log the hours and minutes until the alarm goes off


                    long finalHours =  ChronoUnit.HOURS.between(Calendar.getInstance().toInstant(),
                            eachAlarm.getmTimeSet().toInstant());

                    Log.i("HOURS", String.valueOf(finalHours));
                    long finalMinutes = (  ChronoUnit.MINUTES.between(Calendar.getInstance().toInstant(),
                            eachAlarm.getmTimeSet().toInstant()) - (finalHours * 60));
                    Log.i("MINUTES", String.format("%02d", finalMinutes));

                    Toast.makeText(context, "Your " +
                                    "alarm is set to ring in " + " " +  finalHours +  "hours and " + " " + finalMinutes
                                    + "minutes"
                            , Toast.LENGTH_SHORT).show();

                    Log.i("user set", "alarm on");

                    // create the intent
                    Intent mAlarmContent = new Intent(context, AlarmReceiver.class);
                    mAlarmContent.putExtra("is_on", true);
                    mAlarmContent.putExtra("alarm_id", eachAlarm.getmAlarmId());
                    mAlarmContent.putExtra("is_weekend", eachAlarm.isOnWeekends());
                    mAlarmContent.putExtra("alarm_sound", eachAlarm.getAlarmSound());
                    Log.i("chosensound", eachAlarm.getAlarmSound());

                    // create a unique pending intent, each with the id of the alarm
                    // this way, we can fire multiple pending intents at once (i.e. multiple alarms)
                    PendingIntent mIntent = PendingIntent.getBroadcast(context,
                            eachAlarm.getmAlarmId(),
                            mAlarmContent, PendingIntent.FLAG_UPDATE_CURRENT);


                    AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

                    // set the alarm

                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                                eachAlarm.getmTimeSet().getTimeInMillis(), AlarmManager.RTC_WAKEUP, mIntent
                        );
                    }



                

                else {

                    // turn off the alarm
                    eachAlarm.setOn(false);
                    refreshAlarmList(AlarmStorage.getInstance().getmAlarms());
                    Log.i("user set", "alarm off");
                    AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                    // cancel the pending intent associated with the alarm
                    Intent mAlarmContent = new Intent(context, AlarmReceiver.class);
                    PendingIntent mIntent = PendingIntent.getBroadcast(context,
                            eachAlarm.getmAlarmId(),
                            mAlarmContent, PendingIntent.FLAG_UPDATE_CURRENT);

                    alarmManager.cancel(mIntent);

                    Toast.makeText(context, "alarm off", Toast.LENGTH_SHORT).show();






                }

            }
        });


       return myView;

    }






}
