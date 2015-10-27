package com.example.bjarne.setitup;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private Wake[] wake;
    private ArrayAdapter<Wake> listAdapter;
    TimePicker wakePicker;
    TimePicker sleepPicker;
    Button doneButton;
    Spinner choices;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    int[] notifId = new int[5];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize IDs for PendingIntent, which is for each Notification
        for (int i = 0; i < 5; i++) {
            notifId[i] = i + 5;
        }

        final ListView mainListView = (ListView) findViewById(R.id.mainListView);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        choices = (Spinner)findViewById(R.id.choices);

        // Does certain actions when ListView item is clicked
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View item,
                                    final int position, long id) {
                // Sets the Wake time, 1st list item in ListView
                if (position == 0) {
                    final Dialog dialog = new Dialog(MainActivity.this);
                    dialog.setTitle("Wake Up Time");
                    dialog.setContentView(R.layout.time_dialog);
                    dialog.show();

                    wakePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
                    doneButton = (Button) dialog.findViewById(R.id.done_button);
                    final TextView time = (TextView) findViewById(R.id.subRowTextView);

                    doneButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int wakeHour;
                            int wakeMinute;
                            String wakeTime;

                            wakeHour = wakePicker.getCurrentHour().intValue();
                            wakeMinute = wakePicker.getCurrentMinute().intValue();

                            wakeTime = String.valueOf(wakeHour) + ":" + String.valueOf(wakeMinute)
                                    + " AM";


                            time.setText(wakeTime);
                            dialog.cancel();
                        }
                    });
                }
                // Sets the Sleep time, 2nd list item in ListView
                if (position == 1) {
                    final Dialog dialog = new Dialog(MainActivity.this);
                    dialog.setTitle("Sleep Time");
                    dialog.setContentView(R.layout.time_dialog);
                    dialog.show();

                    sleepPicker = (TimePicker) dialog.findViewById(R.id.timePicker);
                    doneButton = (Button) dialog.findViewById(R.id.done_button);
                    final TextView time = (TextView) item.findViewById(R.id.subRowTextView);

                    doneButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int sleepHour;
                            int sleepMinute;
                            String sleepTime;

                            sleepHour = sleepPicker.getCurrentHour().intValue();
                            sleepMinute = sleepPicker.getCurrentMinute().intValue();

                            sleepTime = String.valueOf(sleepHour) + ":" + String.valueOf(sleepMinute)
                                    + " AM";


                            time.setText(sleepTime);
                            dialog.cancel();
                        }
                    });
                }

                // Sets the notifications, or cancels all if already initialized
                if (position == 2) {
                    TextView on_off = (TextView)item.findViewById(R.id.subRowTextView);

                    if (wakePicker == null || sleepPicker == null) {
                        Toast.makeText(getApplicationContext(),
                                "Must set both Wake and Sleep Time",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Alarm Set!", Toast.LENGTH_SHORT).show();
                        setAlarm(item, wakePicker, sleepPicker, choices);
                        on_off.setText("ON!");
                    }
                }

                // Cancels all notifications
                if (position == 3) {
                    TextView reset = (TextView)item.findViewById(R.id.subRowTextView);

                    if (alarmManager != null) {
                        Intent intent = new Intent(MainActivity.this, AlertReceiver.class);
                        PendingIntent pIntent;

                        for (int i = 0; i < 5; i++) {
                            pIntent = PendingIntent.getBroadcast(MainActivity.this, notifId[i], intent, 0);
                            alarmManager.cancel(pIntent);
                        }
                        Toast.makeText(getApplicationContext(), "Alarm Cancelled!", Toast.LENGTH_SHORT).show();
                        reset.setText("ON!");
                    }
                }
            }
        });

        // Create and populate Wake Times
        wake = new Wake[]{
                new Wake("Set Wake Time", "7:00 AM"),
                new Wake("Set Sleep Time", "11:00 PM"),
                new Wake("Click ON", ":-("),
                new Wake("Reset",":-(")
        };

        ArrayList<Wake> wakeList = new ArrayList<Wake>();
        wakeList.addAll(Arrays.asList(wake));

        // This is where I'll set custom array adapter as the ListView's adapter
        listAdapter = new WakeArrayAdapter(this, wakeList);
        mainListView.setAdapter(listAdapter);
    }
    
    public void setAlarm(View view, TimePicker wakePicker, TimePicker sleepPicker, Spinner choice) {

        double val;
        long interval;

        String number = String.valueOf(choice.getSelectedItem());
        int num = Integer.parseInt(number);

        Intent alertIntent = new Intent(this, AlertReceiver.class);
        int wakeHour = wakePicker.getCurrentHour().intValue();
        int sleepHour = sleepPicker.getCurrentHour().intValue();

        if (wakeHour == sleepHour) {
            wakeHour = wakePicker.getCurrentMinute().intValue();
            sleepHour = sleepPicker.getCurrentMinute().intValue();

            interval = (long)(sleepHour - wakeHour) / (num + 1);
        } else if (sleepHour < wakeHour) {
            sleepHour += 24;
            val = (double)(sleepHour - wakeHour) / (num + 1);
            interval = (long)(val * 60);
        } else {
            val = (double)(sleepHour - wakeHour) / (num + 1);
            interval = (long)(val * 60);
        }

        Calendar calendar = Calendar.getInstance();
        Calendar calSet = (Calendar)calendar.clone();

        calSet.set(Calendar.HOUR_OF_DAY, wakePicker.getCurrentHour());
        calSet.set(Calendar.MINUTE, wakePicker.getCurrentMinute());
        calSet.set(Calendar.SECOND, 0);

        if (calSet.compareTo(calendar) <= 0) {
            calSet.add(Calendar.DATE, 1);
        }

        for (int i = 0; i < num; i++) {

            pendingIntent = PendingIntent.getBroadcast(MainActivity.this, notifId[i], alertIntent, 0);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                    calSet.getTimeInMillis() + (i + 1) * interval * 60 * 1000,
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public Object onRetainCustomNonConfigurationInstance() {
        return wake;
    }
}