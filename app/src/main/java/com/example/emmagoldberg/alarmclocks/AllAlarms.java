package com.example.emmagoldberg.alarmclocks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import net.steamcrafted.lineartimepicker.dialog.LinearTimePickerDialog;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

public class AllAlarms extends AppCompatActivity {

    // our home alarm class, that stores the list view of all of the alarms


    private ListView mAlarmListView;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.add_alarm_icon:

                // menu icon for adding a new alarm

                // go to set alarm class
                // pass in that it's a new alarm

                Intent setAlarmIntent = SetAlarm.newIntent(getApplicationContext(), true,
                        0);

                startActivity(setAlarmIntent);




        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final List<Alarm> myAlarms = AlarmStorage.getInstance().getmAlarms();
        Log.i("in on resume", String.valueOf(AlarmStorage.getInstance().getmAlarms().size()));

        mAlarmListView = (ListView)findViewById(R.id.my_alarm_list_view);
        CustomAlarmAdapter customAlarmAdapter = new CustomAlarmAdapter(getApplicationContext(), myAlarms);
        mAlarmListView.setAdapter(customAlarmAdapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_alarms);



        final List<Alarm> myAlarms = AlarmStorage.getInstance().getmAlarms();

        mAlarmListView = (ListView)findViewById(R.id.my_alarm_list_view);
        CustomAlarmAdapter customAlarmAdapter = new CustomAlarmAdapter(getApplicationContext(), myAlarms);
        mAlarmListView.setAdapter(customAlarmAdapter);
        mAlarmListView.setClickable(true);
        mAlarmListView.setEmptyView(findViewById(R.id.empty_list_view));




        mAlarmListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("clicked on list", "true");
                Alarm alarmClickedOn = myAlarms.get(position);
                Intent setIntent = SetAlarm.newIntent(getApplicationContext(), false, alarmClickedOn
                .getmAlarmId());
                startActivity(setIntent);

            }
        });








    }
}
