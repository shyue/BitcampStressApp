package com.jwetherell.heart_rate_monitor;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.util.Log;
import android.content.Context;
public class MainActivity extends Activity {

    public static Context appstate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        appstate = getApplicationContext();
    }

    public void getHeartbeat(View view) {
        Log.i("Debug", "Recieved");
        Intent intent = new Intent(MainActivity.this, HeartRateMonitor.class);
        //intent.putExtra("Context",this.getApplicationContext());
        MainActivity.this.startActivity(intent);
    }

    public void getJournal(View view) {
        Intent intent = new Intent(MainActivity.this, Journal.class);
        MainActivity.this.startActivity(intent);
    }


}
