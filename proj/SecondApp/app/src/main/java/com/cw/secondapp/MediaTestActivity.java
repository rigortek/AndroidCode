package com.cw.secondapp;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class MediaTestActivity extends AppCompatActivity
        implements MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnInfoListener,
        MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnTimedTextListener,
        MediaPlayer.OnVideoSizeChangedListener,
        SurfaceHolder.Callback {
    public static final String TAG = "MediaTestActivity";
    private int mCurPositin = 0;

    MediaPlayer mMediaPlayer;
    private SurfaceHolder vidHolder;
    private SurfaceView vidSurface;
    private ImageView mImageViewCrash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videotest);

        vidSurface = findViewById(R.id.surfView);
        vidHolder = vidSurface.getHolder();
        vidHolder.addCallback(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != mMediaPlayer) {
            Log.d(TAG, "onResume: " + mCurPositin);
        }

//        getContentResolver().call(Uri.parse("content://businessprovider.authorities"), "invoke", null, null);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Toast.makeText(this, "onError: mp" + mp + "what: " + what + "extra: " + extra, Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d(TAG, "onPrepared: " + mp);
        mMediaPlayer.seekTo(mCurPositin);
        mMediaPlayer.start();

        // intent to produce CRASH
//        try {
//            Thread.sleep(500);
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    mImageViewCrash.setColorFilter(Color.RED);
//                }
//            });
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        String url = "http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4";
        if (null == mMediaPlayer) {
            mMediaPlayer = new MediaPlayer();
        } else {
            mMediaPlayer.reset();
        }

        try {
            mMediaPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.setDisplay(vidHolder);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.prepareAsync();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged: ");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed: ");
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Log.d(TAG, "onBufferingUpdate: ");
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(TAG, "onCompletion: ");
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        Log.d(TAG, "onInfo: what:" + what + ", extra: " + extra);
        return false;
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        Log.d(TAG, "onSeekComplete: ");
    }

    @Override
    public void onTimedText(MediaPlayer mp, TimedText text) {
        Log.d(TAG, "onTimedText: " + text.getText());
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        Log.d(TAG, "onVideoSizeChanged: ");
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (null != mMediaPlayer) {
            mCurPositin = mMediaPlayer.getCurrentPosition();
            mMediaPlayer.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mMediaPlayer) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
