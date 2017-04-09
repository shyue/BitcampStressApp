package com.jwetherell.heart_rate_monitor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;


public class Survey extends Activity {



    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        TextView q1 = (TextView) findViewById(R.id.text1);
        TextView q2 = (TextView) findViewById(R.id.text2);
        TextView q3 = (TextView) findViewById(R.id.text3);
        TextView q4 = (TextView) findViewById(R.id.text4);
        TextView q5 = (TextView) findViewById(R.id.text5);
        TextView q6 = (TextView) findViewById(R.id.text6);
        TextView q7 = (TextView) findViewById(R.id.text7);
        TextView q8 = (TextView) findViewById(R.id.text8);
        TextView q9 = (TextView) findViewById(R.id.text9);
        TextView q10 = (TextView) findViewById(R.id.text10);
        TextView q11 = (TextView) findViewById(R.id.text11);
        TextView q12 = (TextView) findViewById(R.id.text12);
        try{
        //BufferedReader ber = new BufferedReader(new FileReader("Questions.txt"));
        //String[] questions = new String[12];
        //for (int i = 0; i < 12; i++) {
        //    questions[i] = ber.readLine();
        //}
        String[] questions = {
                "In the last month, how often have you been upset because of something that happened unexpectedly?",
                "In the last month, how often have you felt that you were unable to control the important things in your life?",
                "In the last month, how often have you felt nervous and “stressed”?",
            "In the last month, how often have you felt confident about your ability to handle your personal problems?",
                    "In the last month, how often have you felt that things were going your way?",
            "In the last month, how often have you found that you could not cope with all the things that you had to do?",
            "In the last month, how often have you been able to control irritations in your life?",
                    "In the last month, how often have you felt that you were on top of things?",
                    "In the last month, how often have you been angered because of things that were outside of your control?",
            "In the last month, how often have you felt difficulties were piling up so high that you could not overcome them?",
            "On a scale from 1 to 100, how intense are your emotions?",
            "On a scale from 1 to 100, how positive are your emotions?"
        };
        q1.setText(questions[0]);
        q2.setText(questions[1]);
        q3.setText(questions[2]);
        q4.setText(questions[3]);
        q5.setText(questions[4]);
        q6.setText(questions[5]);
        q7.setText(questions[6]);
        q8.setText(questions[7]);
        q9.setText(questions[8]);
        q10.setText(questions[9]);
        q11.setText(questions[10]);
        q12.setText(questions[11]);
        }catch(Exception e){Log.i("Debug", "q error");};
    }


    public void results(View view){
        SeekBar s1 = (SeekBar) findViewById(R.id.seek1);
        SeekBar s2 = (SeekBar) findViewById(R.id.seek2);
        SeekBar s3 = (SeekBar) findViewById(R.id.seek3);
        SeekBar s4 = (SeekBar) findViewById(R.id.seek4);
        SeekBar s5 = (SeekBar) findViewById(R.id.seek5);
        SeekBar s6 = (SeekBar) findViewById(R.id.seek6);
        SeekBar s7 = (SeekBar) findViewById(R.id.seek7);
        SeekBar s8 = (SeekBar) findViewById(R.id.seek8);
        SeekBar s9 = (SeekBar) findViewById(R.id.seek9);
        SeekBar s10 = (SeekBar) findViewById(R.id.seek10);
        SeekBar s11 = (SeekBar) findViewById(R.id.seek11);
        SeekBar s12 = (SeekBar) findViewById(R.id.seek12);

        int total = s1.getProgress();
        total += s2.getProgress();
        total += s3.getProgress();
        total += (4 - s4.getProgress());
        total += (4 - s5.getProgress());
        total += s6.getProgress();
        total += (4 - s7.getProgress());
        total += (4 - s8.getProgress());
        total += s9.getProgress();
        total += s10.getProgress();

        int intensity = s11.getProgress();
        int polarity = s12.getProgress();
        String mood = "";
        if (intensity >= 50) {
            if (polarity >= 50) {
                System.out.println("You are Exubarant");
                mood+="Exubarant ";
            }
            else {
                System.out.println("You are Angry");
                mood+="Angry ";
            }
        }
        else {
            if (polarity >= 50) {
                System.out.println("You are Calm");
                mood+="Calm ";
            }
            else {
                System.out.println("You are Sad");
                mood+="Sad ";
            }
        }



        try {
            FileOutputStream fOut = MainActivity.appstate.openFileOutput("Answers.txt", Context.MODE_APPEND);
            fOut.write(((new Date()).toString() + " " + total + "\t" + intensity + "\t" + polarity + "\t" + mood + "\n").getBytes());
            fOut.close();
        }catch (Exception e){
            Log.i("debug", "fail");};

        try{
            File file = new File(MainActivity.appstate.getFilesDir()+"/Answers.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Log.i("debug", line);
            }
            fileReader.close();
        }catch (Exception e){Log.i("debug", "read fail");};


        Intent intent = new Intent(Survey.this, MainActivity.class);
        Survey.this.startActivity(intent);
    }







}
