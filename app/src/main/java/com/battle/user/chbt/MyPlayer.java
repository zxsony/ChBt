package com.battle.user.chbt;

import android.content.Context;
import android.media.MediaPlayer;

public class MyPlayer {
    protected MediaPlayer _mediaPlayer;
    protected void playFromResource(Context context)
    {


        if (_mediaPlayer != null)
        {
            _mediaPlayer.stop();
            _mediaPlayer.release();
        }
        _mediaPlayer = MediaPlayer.create(context , R.raw.kukaracha);
        _mediaPlayer.start();
    }
}
