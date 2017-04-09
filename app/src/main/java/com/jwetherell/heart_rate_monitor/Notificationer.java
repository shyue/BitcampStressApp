package com.jwetherell.heart_rate_monitor;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Date;

public class Notificationer extends Activity {

    private static final int time = 10000;
    public static String sleepStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        String lastWord = "";
        try{
            File file = new File(MainActivity.appstate.getFilesDir()+"/sleep.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            fileReader.close();
            lastWord = line.substring(line.lastIndexOf(" ")+1);
        }catch (Exception e){Log.i("debug", "read fail");};

        if (lastWord.equals("Sleep")){
            sleepStatus = "Sleep";
            ImageView fire = (ImageView) findViewById(R.id.imageView1);
            fire.setImageResource(getResources().getIdentifier("part2" , "drawable", getPackageName()));
            Button sleeper = (Button) findViewById(R.id.button3);
            sleeper.setText("Turn on notifications?");
        }else {
            sleepStatus = "Awake";
            ImageView fire = (ImageView) findViewById(R.id.imageView1);
            fire.setImageResource(getResources().getIdentifier("bitcamp" , "drawable", getPackageName()));
            Button sleeper = (Button) findViewById(R.id.button3);
            sleeper.setText("Turn off notifications?");
            Intent notifyIntent = new Intent(this, MyReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast
                    (this, 1, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) MainActivity.appstate.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                    time, pendingIntent);
        }
        Log.i("Debug", sleepStatus);


    }

    public void getSleep(View view) {
        if (sleepStatus.equals("Awake")){
            try {
                FileOutputStream fOut = MainActivity.appstate.openFileOutput("sleep.txt", Context.MODE_APPEND);
                fOut.write(((new Date()).toString() + " " + "Sleep" + "\n").getBytes());
                fOut.close();
            }catch (Exception e){Log.i("debug", "fail");};
            sleepStatus = "Sleep";
        }else{
            try {
                FileOutputStream fOut = MainActivity.appstate.openFileOutput("sleep.txt", Context.MODE_APPEND);
                fOut.write(((new Date()).toString() + " " + "Awake" + "\n").getBytes());
                fOut.close();
            }catch (Exception e){Log.i("debug", "fail");};
            sleepStatus = "Awake";
        }
        String lastWord = sleepStatus;
        if (lastWord.equals("Sleep")){
            sleepStatus = "Sleep";
            ImageView fire = (ImageView) findViewById(R.id.imageView1);
            fire.setImageResource(getResources().getIdentifier("part2" , "drawable", getPackageName()));
            Button sleeper = (Button) findViewById(R.id.button3);
            sleeper.setText("Turn on notifications?");
        }else {
            sleepStatus = "Awake";
            ImageView fire = (ImageView) findViewById(R.id.imageView1);
            fire.setImageResource(getResources().getIdentifier("bitcamp" , "drawable", getPackageName()));
            Button sleeper = (Button) findViewById(R.id.button3);
            sleeper.setText("Turn off notifications?");
            Intent notifyIntent = new Intent(this, MyReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast
                    (this, 1, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) MainActivity.appstate.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                    time, pendingIntent);
        }
        Log.i("Debug", sleepStatus);
    }
}
