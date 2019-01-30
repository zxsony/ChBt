package com.battle.user.chbt;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class MainService extends Service {
    final String LOG_TAG = "myLogs";
    MyPlayer mp = new MyPlayer();
    Timer myTimer = new Timer();

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;


    static int  count = 0;

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            try {
                playSnd( R.raw.kukaracha);
                Thread.sleep(2000);
                scheduleTask();
                Thread.sleep(60000000);
                playSnd( R.raw.hlstop);
            } catch (InterruptedException e) {
                // Restore interrupt status.
                Thread.currentThread().interrupt();
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }
    }

    public void onCreate() {
        Log.d("zxapp", "on create");
        //super.onCreate();
        HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("zxapp", "on start command");
        Toast.makeText(this, "on start command service", Toast.LENGTH_SHORT).show();
        //return super.onStartCommand(intent, flags, startId);
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        return START_STICKY;
    }

    public void onDestroy() {
        Log.d("zxapp", "on destroy");
        mp.playFromResource(this, R.raw.beep);
        Toast.makeText(this, "service destroy", Toast.LENGTH_SHORT).show();
        myTimer.cancel();
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        Log.d("zxapp", "onBind");
        return null;
    }

    protected void playSnd(int resId)
    {
        MyPlayer mp = new MyPlayer();
        mp.playFromResource(this, resId);
    }

    void scheduleTask() {
        Log.d("zxapp", "schedule start count - " + String.valueOf(count));
        myTimer.schedule(new TimerTask() { // Определяем задачу
            @Override
            public void run() {
                playSnd( R.raw.beep);
                count = count + 1 ;
                postTask();
            }
        }, 1000L, 600L * 1000);
    }

    void postTask(){
        final  Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(new Runnable() {

            @Override
            public void run() {
                Log.d("zxapp", "current count - " + String.valueOf(count));
                Toast.makeText(getApplicationContext(), "current count - " + String.valueOf(count), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
