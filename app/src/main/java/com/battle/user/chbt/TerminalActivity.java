package com.battle.user.chbt;

import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TerminalActivity extends AppCompatActivity {
    private TextView dataread;
    private Button btnStart;
    private Button btnStop;
    private boolean chgEn;
    //private boolean prEn;
    private static boolean tmEn = false;
    //protected MediaPlayer _mediaPlayer;
    private float prevVoltage;
    private Timer myTimer = new Timer();
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
                Snackbar.make(view, "ЧНЯ", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        dataread = findViewById(R.id.tv_1);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);

        dataread.setText("");
        //prEn = true;
        prevVoltage = getActualData();
        if (!tmEn){timerStart();tmEn = true;}

    }

    @Override
    protected void onPause(){
        super.onPause();
        dataread.append("Pause\n");
    }

    protected void onResume() {
        super.onResume();
        //if (prEn) {
        //    prEn = false;



        //}
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
    @Override
    public void onBackPressed(){
        myTimer.cancel();
        tmEn = false;
        super.onBackPressed();
    }

    public float getActualData() {

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, ifilter);

        //are we charging / charged?
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;

        if (isCharging == true) {
            chgEn = true;
            //dataread.setText("CHARGING");
        } else {
            chgEn = false;
            //dataread.setText("NOT CHARGING");
        }
        int voltage = batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
        //int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        //int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        //dataread.append("\n" + voltage + " V " + level +"%");
        batteryStatus = null;
        return voltage;
    }
    protected void playSnd(int resId)
    {
        MyPlayer mp = new MyPlayer();
        mp.playFromResource(this, resId);
    }

    protected void timerStart() {
        final Handler uiHandler = new Handler();
        final TextView txtResult = (TextView) findViewById(R.id.tv_1);
        myTimer.schedule(new TimerTask() { // Определяем задачу
            @Override
            public void run() {
                final float result = getActualData();
                final float datadiff = result - prevVoltage;
                if (result != prevVoltage){
                    prevVoltage = result;
                }
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Date currentTime = Calendar.getInstance().getTime();
                        txtResult.append(currentTime.getHours() + ":" + currentTime.getMinutes() + " " + result/1000 + " V (d: " + (int)datadiff + " mV)\n");
                        if (chgEn) {
                            try {
                                Uri notify = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notify);
                                r.play();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                Thread.sleep(2000);
                            } catch (Exception e) {

                            }
                            if (result > 4250) {
                                playSnd(R.raw.pp);
                            } else if (result > 4200) {
                                playSnd(R.raw.sg);
                            } else if (result > 4150) {
                                playSnd(R.raw.ob);
                            } else {
                                playSnd(R.raw.nsmt);
                            }
                        }
                    }
                });
            }

            ;
        }, 1000L, 600L * 1000);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnStart:
                // кнопка ОК
                Log.d("zxapp", "btnStart");
                startService(new Intent(this, MainService.class));
                break;
            case R.id.btnStop:
                // кнопка Cancel
                Log.d("zxapp", "btnStop");
                stopService(new Intent(this, MainService.class));
                break;
        }
    }
}
