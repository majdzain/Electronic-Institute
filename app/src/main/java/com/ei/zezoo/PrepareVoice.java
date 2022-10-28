package com.ei.zezoo;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class PrepareVoice extends AsyncTask<Void, Void, String> {
    MediaPlayer mediaPlayer;
    String source;
    ImageView img;
    ProgressBar progress;

    PrepareVoice(MediaPlayer mediaPlayer, ImageView img, ProgressBar progress) {
        this.mediaPlayer = mediaPlayer;
        this.source = source;
        this.img = img;
        this.progress = progress;
    }

    @Override
    protected String doInBackground(Void... params) {
        String s = "";
        try {
            mediaPlayer.setDataSource(source);
            mediaPlayer.prepare();
        } catch (Exception e) {
            s = e.getMessage();
        }
        return s;
    }

    @Override
    protected void onPostExecute(final String token) {
        progress.setVisibility(View.GONE);
        img.setVisibility(View.VISIBLE);
        //    commentPlay.setClickable(true);
        //  commentSeekbar.setEnabled(true);
        //commentSeekbar.setClickable(true);
        //commentSeekbar.setFocusable(true);
    }

}