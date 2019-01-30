package com.battle.user.chbt;

import android.content.Context;
import android.media.MediaPlayer;

public class MyPlayer {
    protected MediaPlayer _mediaPlayer;
    protected void playFromResource(Context context, int resId)
    {
        if (_mediaPlayer != null)
        {
            _mediaPlayer.stop();
            _mediaPlayer.release();
        }
        _mediaPlayer = MediaPlayer.create(context , resId);
        _mediaPlayer.start();
        _mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            };
        });
    }

}
