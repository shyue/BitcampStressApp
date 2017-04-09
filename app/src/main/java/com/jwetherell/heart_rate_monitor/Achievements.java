package com.jwetherell.heart_rate_monitor;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.LinkedList;

public class Achievements extends Activity {
    public static final long TWOWEEKS = 24*60*60*1000*14;
    public static final long ONEDAY = 24*60*60*1000;
    String[][] awardVals = new String[][]{
        {"Reach Exubrance", "Wow, you're super hyped!", "25"}, //0
        {"Get Angry", "Sometimes, you have to let off some steam..", "25"}, //1
        {"Get Calm", "Calm and cool - that's just you!", "25"}, //2
        {"Get Sad", "It's a terrible day for rain..", "25"}, //3
        {"Calmly Exuberant", "Calm but Excited - the most balanced happiness.", "50"}, //4
        {"2 Week Journaler", "These last two weeks, you've got to know the journal quite well :)", "1000"}, //5
        {"Streaking Journaler", "Three days in the journal... can you keep it up?", "200"}, //6
        {"Downed Journaler", "It seems like you've been a bit down lately - hope you feel better :)", "50"}, //7
        {"Peppy Journaler", "Well, someone's in a good mood :P", "150"}, //8
        {"Pounding Heart", "Woah, slow down there heart!", "1"}, //9
        {"Steady Heart Streak", "Strong and stable.", "100"}, //10
        {"Quick Recover", "Fast heartbeat, to slow - you sure know how to control your stress!", "150"}, //11
        {"Solid Sleep", "7-9 hours, as the doctor says!", "50"}, //12
        {"Solid Sleep Streak", "7-9 hours, as the doctor says!", "150"}, //13
        {"Early Sleep", "Don't go past midnight!", "50"}, //14
        {"Early Sleep Streak", "Don't go past midnight!", "150"} //15
    };

    LinkedList<Integer> awards;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
        awards = new LinkedList<Integer>();
        checker();
    }


    private void checker(){
        //Survey
        checkThayer();
        checkJournal();
        checkHeart();
        checkSleep();
        int totPoints = 0;
        for (Integer i: awards){
            totPoints+=Integer.parseInt(awardVals[i][2]);
            Log.i("Debug", i+"");
        }
        int level = totPoints/300;
        ImageView fire = (ImageView) findViewById(R.id.imageView1);
        if (level < 5)
            fire.setImageResource(getResources().getIdentifier("part"+(level+1) , "drawable", getPackageName()));
        else
            fire.setImageResource(getResources().getIdentifier("bitcamp" , "drawable", getPackageName()));

        LinearLayout root = (LinearLayout) findViewById(R.id.LinearLayout1);
        TextView header = new TextView(MainActivity.appstate);
        header.setTextSize(30f);
        header.setTextColor(Color.BLACK);
        header.setText("Achievements");
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llp.setMargins(20, -200, 0, 0); // llp.setMargins(left, top, right, bottom);
        header.setLayoutParams(llp);
        root.addView(header);

        header = new TextView(MainActivity.appstate);
        header.setTextSize(20f);
        header.setTextColor(Color.BLACK);
        header.setText("Level " + (level + 1) + ": " + totPoints);
        llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llp.setMargins(20, 0, 0, 0); // llp.setMargins(left, top, right, bottom);
        header.setLayoutParams(llp);
        root.addView(header);

        for (Integer i: awards){
            TextView a = new TextView(MainActivity.appstate);
            TextView b = new TextView(MainActivity.appstate);
            a.setTextSize(15f);
            b.setTextSize(10f);
            a.setTextColor(Color.BLACK);
            b.setTextColor(Color.BLACK);
            a.setText(awardVals[i][0]);
            b.setText(awardVals[i][1]);
            llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            llp.setMargins(20, 0, 0, 0); // llp.setMargins(left, top, right, bottom);
            a.setLayoutParams(llp);
            b.setLayoutParams(llp);

            root.addView(a);
            root.addView(b);
        }

    }

    private void checkSleep(){
        Date curr = new Date();
        try{
            File file = new File(MainActivity.appstate.getFilesDir()+"/sleep.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line; String sub;

            String[] holder = new String[6];
            Date[] holder2 = new Date[6];
            int count = 0;
            while ((line = bufferedReader.readLine()) != null) {
                Log.i("debug", line);
                //starts at sixth space which is after the date
                String[] lineArr = line.split("\\s+");
                String dateStr = "";
                for (int i = 0; i<6; i++){
                    dateStr+=lineArr[i]+" ";
                }
                Date newer = new Date(
                        dateStr.trim()
                );
                long diff = Math.abs(newer.getTime()-curr.getTime());
                String val = lineArr[6].trim();
                if (diff<=TWOWEEKS){
                    holder[0] = holder[1]; holder[1] = holder[2];
                    holder[2] = holder[3]; holder[3] = holder[4];
                    holder[4] = holder[5]; holder[5] = val;

                    holder2[0] = holder2[1]; holder2[1] = holder2[2];
                    holder2[2] = holder2[3]; holder2[3] = holder2[4];
                    holder2[4] = holder2[5]; holder2[5] = newer;

                    if (val.equals("Awake")){
                        if (newer.getHours()+24-holder2[4].getHours()>=7 && newer.getHours()+24-holder2[4].getHours()<=9){
                            awards.add(12);
                            if (holder2[3].getHours()+24-holder2[2].getHours()>=7 && holder2[3].getHours()+24-holder2[2].getHours()<=9)
                                if (holder2[1].getHours()+24-holder2[0].getHours()>=7 && holder2[1].getHours()+24-holder2[0].getHours()<=9)
                                    awards.add(13);
                        }
                    }
                    if (val.equals("Sleep")){
                        if (holder2[5].getHours()>=21){
                            awards.add(14);
                            if (holder2[3].getHours()>=21 && holder2[1].getHours()>=21)
                                awards.add(15);
                        }
                    }


                }}

        }catch (Exception e){
            e.printStackTrace();
            Log.i("debug", "read fail");};


    }
    private void checkHeart(){
        Date curr = new Date();
        try{
            File file = new File(MainActivity.appstate.getFilesDir()+"/heartbeat.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line; String sub;

            int[] holder = new int[3];
            Date[] holder2 = new Date[3];
            int count = 0;
            while ((line = bufferedReader.readLine()) != null) {
                Log.i("debug", line);
                //starts at sixth space which is after the date
                String[] lineArr = line.split("\\s+");
                String dateStr = "";
                for (int i = 0; i<6; i++){
                    dateStr+=lineArr[i]+" ";
                }
                Date newer = new Date(
                        dateStr.trim()
                );
                holder2[0] = new Date(); holder2[1] = new Date(); holder2[2] = new Date();
                long diff = Math.abs(newer.getTime()-curr.getTime());
                Integer val = Integer.parseInt(lineArr[6].trim());
                if (diff<=TWOWEEKS){
                    holder[0] = holder[1]; holder[1] = holder[2]; holder[2] = val;
                    holder2[0] = holder2[1]; holder2[1] = holder2[2]; holder2[2] = newer;
                    if (val>100) awards.add(9);
                    if (holder[0]<100 && holder[1] <100 && holder[2]<100) awards.add(10);
                    if (holder[2]<100 && holder[1]>100){
                        diff = Math.abs(holder2[2].getTime()-holder2[1].getTime());
                        if (diff<=1000*60*10) awards.add(11);
                    }

                }}

        }catch (Exception e){
            e.printStackTrace();
            Log.i("debug", "read fail");};


    }
    private void checkJournal(){
        Date curr = new Date();
        try{
            File file = new File(MainActivity.appstate.getFilesDir()+"/journal.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line; String sub;

            int[] holder = new int[3];
            int[] holder2 = new int[14];
            int count = 0;
            while ((line = bufferedReader.readLine()) != null) {
                Log.i("debug", line);
                //starts at sixth space which is after the date
                String[] lineArr = line.split("\\s+");
                String dateStr = "";
                for (int i = 0; i<6; i++){
                    dateStr+=lineArr[i]+" ";
                }
                Date newer = new Date(
                        dateStr.trim()
                );
                long diff = Math.abs(newer.getTime()-curr.getTime());
                double val = Double.parseDouble(lineArr[6].trim());
                if (diff<=TWOWEEKS){
                    count ++;
                int extraVal = 0;
                if (val>0) extraVal = 1;
                if (val<0) extraVal = -1;
                holder[0] = holder[1]; holder[1] = holder[2]; holder[2]=extraVal;
                if (holder[0]+holder[1]+holder[2]==3) awards.add(8);
                if (holder[0]+holder[1]+holder[2]==-3) awards.add(7);
                    if (count>=3) awards.add(6);
                    holder2[(int)Math.floor((diff/ONEDAY))]+=1;
            }}
            count = 0;
            for (int i = 0; i<14; i++){
                if (holder2[i]>0) count++;
            }
            if (count ==14) awards.add(5);
        }catch (Exception e){
            e.printStackTrace();
            Log.i("debug", "read fail");};


    }

    private void checkThayer(){
        Date curr = new Date();
        try{
            File file = new File(MainActivity.appstate.getFilesDir()+"/Answers.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line; String sub;
            int intensity; int polarity;

            while ((line = bufferedReader.readLine()) != null) {
                boolean exulberent = false;
                boolean calm = false;
                boolean angry = false;
                boolean sad = false;
                Log.i("debug", line);
                //starts at sixth space which is after the date
                String[] lineArr = line.split("\\s+");
                sub = "";
                for (int i =6; i<lineArr.length;i++){
                    sub+=lineArr[i]+" ";
                }
                String strDate = "";
                for (int i =0; i<6; i++){
                    strDate += lineArr[i]+" ";
                }
                sub = sub.trim(); strDate = strDate.trim();
                System.out.println(sub);
                Date newer = new Date(
                        strDate
                );
                String[] subArr = sub.split("\\s+");
                intensity = Integer.parseInt(subArr[1]);
                polarity = Integer.parseInt(subArr[2]);
                System.out.println(intensity+" "+polarity);
                if (intensity >= 50) {
                    if (polarity >= 50) {
                        exulberent = true;
                    }
                    else { angry = true;}
                } else {
                    if (polarity >= 50) {
                        calm = true;
                    }
                    else { sad = true;}
                }
                long diff = Math.abs(newer.getTime()-curr.getTime());

                if (diff<=TWOWEEKS) {
                    if (exulberent) awards.add(0);
                    if (exulberent && calm) awards.add(4);
                    if (angry) awards.add(1);
                    if (calm) awards.add(2);
                    if (sad) awards.add(3);
                }
            }
        }catch (Exception e){
            Log.i("debug", "read fail");};


    }//end of checkthayer


}
