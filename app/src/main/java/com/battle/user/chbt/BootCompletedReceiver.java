package com.battle.user.chbt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class BootCompletedReceiver extends BroadcastReceiver {
    Timer myTimer = new Timer();
    MyPlayer mp = new MyPlayer();
    public BootCompletedReceiver() {
    }

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            //Toast.makeText(context, "onReceive", Toast.LENGTH_SHORT).show();
            Log.d("zxapp", "onReceive");
            //mp.playFromResource(context, R.raw.kukaracha);
            // ваш код здесь
            context.startService(new Intent(context, MainService.class));
            //context.startForegroundService(new Intent(context, MainService.class));
            //beepTask(context);
        }
    }
    public void beepTask(Context context) {

        final Handler uiHandler = new Handler();

        final Context ctx = context;
        myTimer.schedule(new TimerTask() { // Определяем задачу
            @Override
            public void run() {

                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mp.playFromResource(ctx, R.raw.beep);
                        Toast.makeText(ctx, "Beep", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            ;
        }, 1000L, 6L * 1000);
    }


}