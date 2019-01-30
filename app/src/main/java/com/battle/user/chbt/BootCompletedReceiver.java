package com.battle.user.chbt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Timer;

public class BootCompletedReceiver extends BroadcastReceiver {
    Timer myTimer = new Timer();
    MyPlayer mp = new MyPlayer();
    public BootCompletedReceiver() {
    }

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.d("zxapp", "on receive");
            context.startService(new Intent(context, MainService.class));
            //context.startForegroundService(new Intent(context, MainService.class));
        }
    }

}