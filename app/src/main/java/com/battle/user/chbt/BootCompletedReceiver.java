package com.battle.user.chbt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BootCompletedReceiver extends BroadcastReceiver {
    public BootCompletedReceiver() {
    }

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Toast toast = Toast.makeText(context.getApplicationContext(),
                    context.getResources().getString(R.string.your_message), Toast.LENGTH_LONG);
            toast.show();
            Log.d("myapp", context.getResources().getString(R.string.your_message));
            MyPlayer mp = new MyPlayer();
            mp.playFromResource(context);
            // ваш код здесь
        }
    }

}