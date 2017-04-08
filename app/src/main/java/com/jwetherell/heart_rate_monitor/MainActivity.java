package com.jwetherell.heart_rate_monitor;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.util.Log;
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
    }

    public void getHeartbeat(View view) {
        Log.i("Debug", "Recieved");
        Intent intent = new Intent(MainActivity.this, HeartRateMonitor.class);
        MainActivity.this.startActivity(intent);
    }

}
