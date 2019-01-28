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
            Toast toast = Toast.makeText(context.getApplicationContext(),
                    context.getResources().getString(R.string.your_message), Toast.LENGTH_LONG);
            toast.show();
            Log.d("myapp", context.getResources().getString(R.string.your_message));

            mp.playFromResource(context, R.raw.kukaracha);
            // ваш код здесь
            beepTask(context);
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
                        mp.playFromResource(ctx, R.raw.beep);
                    }
                });
            }

            ;
        }, 1000L, 6L * 1000);
    }


}