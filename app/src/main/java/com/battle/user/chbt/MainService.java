package com.battle.user.chbt;

import android.app.Service;
import android.content.Context;
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
                //Toast.makeText( getApplicationContext(), "kukaracha 1", Toast.LENGTH_SHORT).show();
                playSnd( R.raw.kukaracha);
                someTask(new Intent(getApplicationContext(), MainService.class), getApplicationContext());
                Thread.sleep(5000000);
                playSnd( R.raw.kukaracha);

                //Toast.makeText( getApplicationContext(), "kukaracha 2", Toast.LENGTH_SHORT).show();
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
        Log.d("zxapp", "onCreate");
        //Toast.makeText(this, "create service", Toast.LENGTH_SHORT).show();
        //mp.playFromResource(this, R.raw.beep);
        //super.onCreate();
        HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
        //startForeground(1,new Notification());
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("zxapp", "onStartCommand");
        //mp.playFromResource(this, R.raw.beep);
        //someTask(intent, this);
        Toast.makeText(this, "start command service", Toast.LENGTH_SHORT).show();
        //return super.onStartCommand(intent, flags, startId);
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

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
                playSnd( R.raw.beep);
                count = count + 1 ;
                //Toast.makeText(getApplicationContext(), "count - " + String.valueOf(count), Toast.LENGTH_SHORT).show();
                Log.d("zxapp", "count - " + String.valueOf(count));
//                uiHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Date currentTime = Calendar.getInstance().getTime();
//                        count = count + 1 ;
//                        playSnd( R.raw.beep);
//                        //SystemClock.sleep(2000);
//                        Toast.makeText(ctxt, "count - " + String.valueOf(count), Toast.LENGTH_SHORT).show();
////                        Toast toast = Toast.makeText(ctxt.getApplicationContext(),
////                            "1-" + String.valueOf(count), Toast.LENGTH_LONG);
////                        toast.show();
////                        SystemClock.sleep(4000);
////                        toast = Toast.makeText(ctxt.getApplicationContext(),
////                                "2-" + String.valueOf(count), Toast.LENGTH_LONG);
////                        toast.show();
//                    }
//                });
            }
        }, 1000L, 600L * 1000);
    }

    protected void playSnd(int resId)
    {
        MyPlayer mp = new MyPlayer();
        mp.playFromResource(this, resId);

    }
}
