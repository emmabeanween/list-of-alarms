package com.example.emmagoldberg.alarmclocks;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {


    
    @Override
    public void onReceive(Context context, Intent intent) {

       // the receiver

        boolean isRunning = AlarmWakeup.currentlyRunning;
        Log.i("is running", String.valueOf(isRunning));
        boolean isOn = intent.getBooleanExtra("is_on", true);
        boolean isWeekends = intent.getBooleanExtra("is_weekend", false);
        String chosen_sound = intent.getStringExtra("alarm_sound");

        // the alarm is set on, and there is no alarm currently running
        if ( isOn == true && isRunning == false ){


               Intent alarmIntent = new Intent(context, AlarmWakeup.class);
               alarmIntent.putExtra("alarm_id", intent.getIntExtra("alarm_id", 0));
               alarmIntent.putExtra("alarm_sound", chosen_sound);
               context.startActivity(alarmIntent);


        }

        else {

          // we have another alarm currently running - turn off the one in the receiver
          Alarm mPassedIn = AlarmStorage.getInstance().getAlarm(intent.getIntExtra("alarm_id", 0));
          mPassedIn.setOn(false);
          new CustomAlarmAdapter(context, AlarmStorage.getInstance().getmAlarms()).refreshAlarmList(
                  AlarmStorage.getInstance().getmAlarms()
          );

        }


    }




}

