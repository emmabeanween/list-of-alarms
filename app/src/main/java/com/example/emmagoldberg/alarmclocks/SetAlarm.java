package com.example.emmagoldberg.alarmclocks;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.joda.time.Period;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class SetAlarm extends AppCompatActivity {

    private boolean isNewAlarm;
    private int meachAlarmId;
    private TextView mSetAlarmTextView;
    private TimePicker timePicker;
    private ImageButton mAddNewAlarmButton;
    private RadioGroup mAlarmRadioGroup;
    private RadioButton mMonFriButton;
    private RadioButton mSatSunButton;
    private Button mSetSoundButton;
    private ImageButton mCancelButton;
    private Spinner mSetSoundSpinner;
    private String mSelectedSound;


    public static Intent newIntent(Context packageContext, boolean isNewAlarm, int alarmId){

       Intent i = new Intent(packageContext, SetAlarm.class);
       i.putExtra("is_new", isNewAlarm);
       i.putExtra("alarm_id", alarmId);
       return i;


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.set_alarm_menu, menu);
        // a trash icon to delete the alarm
        // if new alarm, you can't delete it: so hide the trash icon
        if (getIntent().getBooleanExtra("is_new", true)){

            // hide the menu
            for (int i = 0; i < menu.size(); i++){

                menu.getItem(i).setVisible(false);
            }

        }
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.delete_alarm_icon:

                // alert dialog picker
                AlertDialog.Builder builder = new AlertDialog.Builder(SetAlarm.this,
                        R.style.Theme_AppCompat);

                builder.setTitle("Delete alarm")
                        .setMessage("Are you sure you want to delete the alarm?")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // delete alarm
                                AlarmStorage alarmStorage = AlarmStorage.getInstance();
                                Alarm alarmtoDelete = alarmStorage.getAlarm(getIntent().getIntExtra
                                        ("alarm_id", 0));
                                alarmStorage.deleteAlarm(alarmtoDelete);
                                // refresh adapter
                                new CustomAlarmAdapter(getApplicationContext(), alarmStorage.getmAlarms()).refreshAlarmList(
                                        alarmStorage.getmAlarms()
                                );
                                // finish - go back to home list of all alarms
                                finish();


                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();








        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_alarm);


         isNewAlarm = getIntent().getBooleanExtra("is_new", false);
         meachAlarmId = getIntent().getIntExtra("alarm_id", 0);
         mSetAlarmTextView = (TextView)findViewById(R.id.set_alarm_text_view);
         timePicker = (TimePicker)findViewById(R.id.alarm_time_picker);
         mAlarmRadioGroup = (RadioGroup)findViewById(R.id.my_alarm_radio_group);
         mMonFriButton = (RadioButton)findViewById(R.id.non_weekends_radio_button);
         mSatSunButton = (RadioButton)findViewById(R.id.weekends_radio_button);

         if (isNewAlarm == true){

             // set new alarm text
             Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Girlesque.otf");
             mSetAlarmTextView.setTypeface(typeface);
             mSetAlarmTextView.setText("new alarm");
             // set timepicker equal to default time (i.e. 8AM)

             timePicker.setHour(8);
             timePicker.setMinute(0);


             // set default checked to be mon-fri
             mMonFriButton.setChecked(true);


         }

         else {
             Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Girlesque.otf");
             mSetAlarmTextView.setTypeface(typeface);
             // the alarm is not new; set the text view to be 'update alarm'
             mSetAlarmTextView.setText("update alarm");

             Alarm passedinAlarm = AlarmStorage.getInstance().getAlarm(meachAlarmId);
             int currentSetHour = passedinAlarm.getmTimeSet().get(Calendar.HOUR_OF_DAY);
             int currentSetMinute = passedinAlarm.getmTimeSet().get(Calendar.MINUTE);
             Log.i("current hour", String.valueOf(currentSetHour));

             timePicker.setHour(currentSetHour);
             timePicker.setMinute(currentSetMinute);

             if (passedinAlarm.isOnWeekends() == true){

                 mMonFriButton.setChecked(false);
                 mSatSunButton.setChecked(true);
             }

             else {

                 mMonFriButton.setChecked(true);
                 mSatSunButton.setChecked(false);

             }



         }


        mSetSoundSpinner = (Spinner)findViewById(R.id.set_sound_spinner);
         String sounds [] = {"birds", "alarm", "bell", "chatter", "rooster"};
        ArrayAdapter<String> mSoundAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item,
                sounds);
        mSoundAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSetSoundSpinner.setAdapter(mSoundAdapter);
        if (isNewAlarm == false){
            // the user has already selected a sound
            // pre-set spinner with chosen sound from before
            mSetSoundSpinner.setSelection(mSoundAdapter.getPosition(AlarmStorage.getInstance().getAlarm(meachAlarmId)
            .getAlarmSound()));

        }
        else {

            // it is a new alarm

            mSetSoundSpinner.setSelection(mSoundAdapter.getPosition("birds"));

        }

         mAddNewAlarmButton = (ImageButton)findViewById(R.id.finish_set_alarm);
         mAddNewAlarmButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 // if new alarm

                 if (isNewAlarm == true){





                     // create a new alarm

                     Alarm newAlarm = new Alarm();
                     Calendar mAlarmCal = Calendar.getInstance();
                     mAlarmCal.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                     mAlarmCal.set(Calendar.MINUTE, timePicker.getMinute());




                     if (mAlarmRadioGroup.getCheckedRadioButtonId() == R.id.non_weekends_radio_button){

                         newAlarm.setOnWeekends(false);
                     }

                     else {

                         newAlarm.setOnWeekends(true);
                     }

                     newAlarm.setmTimeSet(mAlarmCal);
                     // give the alarm a random id
                     newAlarm.setmAlarmId(100000 + (int)(new Random().nextInt() * 899900));
                     newAlarm.setOn(false);
                     mSelectedSound = mSetSoundSpinner.getSelectedItem().toString();
                     newAlarm.setAlarmSound(mSelectedSound);


                     AlarmStorage.getInstance().addAlarm(newAlarm);



                     finish();



                 }




                 else {

                   // we are updating a current alarm
                   // get the id of the passed in alarm

                     Alarm passedIn = AlarmStorage.getInstance().getAlarm(meachAlarmId);
                     List<Alarm> mAlarms = AlarmStorage.getInstance().getmAlarms();

                     Calendar mNewTimeCal = Calendar.getInstance();
                     mNewTimeCal.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                     mNewTimeCal.set(Calendar.MINUTE, timePicker.getMinute());
                     passedIn.setmTimeSet(mNewTimeCal);
                     // set on
                     passedIn.setOn(false);
                     mSelectedSound = mSetSoundSpinner.getSelectedItem().toString();
                     if (mSelectedSound!=passedIn.getAlarmSound()){
                         // update the alarm sound, if different
                         passedIn.setAlarmSound(mSelectedSound);

                     }

                     if (mAlarmRadioGroup.getCheckedRadioButtonId() == R.id.non_weekends_radio_button
                             ){

                         passedIn.setOnWeekends(false);

                     }

                     else{

                         passedIn.setOnWeekends(true);
                     }

                     new CustomAlarmAdapter(getApplicationContext(), mAlarms).refreshAlarmList(
                            AlarmStorage.getInstance().getmAlarms()
                     );


                     // go back



                     finish();










                 }



             }
         });




    mCancelButton = (ImageButton)findViewById(R.id.cancel_alarm_button);
    mCancelButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            // go back to previous activity - all alarms home view

            finish();



        }
    });





















    }






}


