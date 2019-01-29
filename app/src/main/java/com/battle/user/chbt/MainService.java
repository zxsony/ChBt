package com.battle.user.chbt;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainService extends Service {
    final String LOG_TAG = "myLogs";
    MyPlayer mp = new MyPlayer();
    Timer myTimer = new Timer();

    static int  count = 0;
    public void onCreate() {
        Log.d("zxapp", "onCreate");
        //Toast.makeText(this, "create service", Toast.LENGTH_SHORT).show();
        //mp.playFromResource(this, R.raw.beep);
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("zxapp", "onStartCommand");
        mp.playFromResource(this, R.raw.beep);
        someTask(intent, this);
        Toast.makeText(this, "start command service", Toast.LENGTH_SHORT).show();
        //return super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }

    public void onDestroy() {
        Log.d("zxapp", "onDestroy");
        mp.playFromResource(this, R.raw.beep);
        Toast.makeText(this, "service destroy", Toast.LENGTH_SHORT).show();
        myTimer.cancel();
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        Log.d("zxapp", "onBind");
        return null;
    }

    void someTask(Intent intent, Context context) {

        final Handler uiHandler = new Handler();
        final Intent itnt = intent;
        final Context ctxt = context;
        //SystemClock.sleep(2000);
        myTimer.schedule(new TimerTask() { // Определяем задачу
            @Override
            public void run() {

                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Date currentTime = Calendar.getInstance().getTime();
                        count = count + 1 ;
                        playSnd( R.raw.beep);
                        //SystemClock.sleep(2000);
                        Toast.makeText(ctxt, "count - " + String.valueOf(count), Toast.LENGTH_SHORT).show();
//                        Toast toast = Toast.makeText(ctxt.getApplicationContext(),
//                            "1-" + String.valueOf(count), Toast.LENGTH_LONG);
//                        toast.show();
//                        SystemClock.sleep(4000);
//                        toast = Toast.makeText(ctxt.getApplicationContext(),
//                                "2-" + String.valueOf(count), Toast.LENGTH_LONG);
//                        toast.show();
                    }
                });
            }
        }, 1000L, 60L * 1000);
    }

    protected void playSnd(int resId)
    {
        MyPlayer mp = new MyPlayer();
        mp.playFromResource(this, resId);

    }
}
