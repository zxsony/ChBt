package com.battle.user.chbt;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TerminalActivity extends AppCompatActivity {
    private TextView dataread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        dataread = findViewById(R.id.tv_1);
        dataread.setText("");
    }

    @Override
    protected void onPause(){
        super.onPause();
        dataread.append("Pause\n");
    }

    @Override
    public void onResume() {
        super.onResume();
        Timer myTimer = new Timer(); // Создаем таймер
        final Handler uiHandler = new Handler();
        final TextView txtResult = (TextView)findViewById(R.id.tv_1);
        myTimer.schedule(new TimerTask() { // Определяем задачу
            @Override
            public void run() {
                final float result = getActualData();
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Date currentTime = Calendar.getInstance().getTime();
                        txtResult.append( currentTime.getHours() + ":" + currentTime.getMinutes() + " " + result + " mV\n");
                    }
                });
            };
        }, 1000L, 600L * 1000);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_terminal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public float getActualData() {

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, ifilter);

        //are we charging / charged?
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;

        if (isCharging == true) {
            //dataread.setText("CHARGING");
        } else {
            //dataread.setText("NOT CHARGING");
        }
        int voltage = batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
        //int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        //int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        //dataread.append("\n" + voltage + " V " + level +"%");
        batteryStatus = null;
        return voltage;
    }
}