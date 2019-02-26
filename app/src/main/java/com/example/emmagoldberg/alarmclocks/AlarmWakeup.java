package com.example.emmagoldberg.alarmclocks;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.time.temporal.ChronoUnit;
import java.util.Calendar;

public class AlarmWakeup extends AppCompatActivity {

    // where the alarm goes off after going through the receiver

    private int alarm_id;
    private Alarm mCurrentAlarm;
    private Button mTurnOffAlarmButton;
    private MediaPlayer mPlayer;
    private String chosen_sound;
    private int sound;
    static boolean currentlyRunning = false;


    public void onStart() {
        super.onStart();
        currentlyRunning = true; //Store status of Activity somewhere like in shared //preference
    }
    public void onStop() {
        super.onStop();
        currentlyRunning = false;//Store status of Activity somewhere like in shared //preference
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_notify);


        alarm_id = getIntent().getIntExtra("alarm_id", 0);
        chosen_sound = getIntent().getStringExtra("alarm_sound");
        Log.i("this_sound", chosen_sound);

        // switch statement to fetch the sound the user selected for the alarm

        switch(chosen_sound){

            case "birds":
            sound = R.raw.birds;
            break;

            case "alarm":
            sound = R.raw.alarm;
            break;

            case "bell":
            sound = R.raw.bell;
            break;

            case "chatter":
            sound = R.raw.chatter;
            break;

            case "rooster":
            sound = R.raw.rooster;
            break;




        }

        // start alarm

        Log.i("in_wakeup", "true");
        mPlayer = MediaPlayer.create(getApplicationContext(), sound);
        mPlayer.setLooping(true);
        mPlayer.start();


        mTurnOffAlarmButton = (Button)findViewById(R.id.turn_off_alarm_button);
        mTurnOffAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // stop the sound
            mPlayer.stop();


            mCurrentAlarm = AlarmStorage.getInstance().getAlarm(alarm_id);
            // turn off the alarm
            mCurrentAlarm.setOn(false);






                // go back to home alarm screen

                Intent restart = new Intent(getApplicationContext(), AllAlarms.class);
                startActivity(restart);





            }
        });









    }
}
