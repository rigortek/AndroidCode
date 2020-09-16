package com.cw.childrenabc;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

/**
 * Created by Robin on 2020/09
 */

public class VoiceUtil {
    private Context context;
    private MediaPlayer mediaPlayer;
    private SoundPool soundPool;

    /**
     * 开关，默认关闭，没声音
     * 0关闭，1media，2sp，3all
     */
    public static int enableMode = 0;

    /**
     * 构造方法
     *
     * @param context
     */
    public VoiceUtil(Context context) {
        this.context = context;
        soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
    }

    /**
     * 用media播放
     *
     * @param res
     */
    public void playByMedia(int res) {
        if (enableMode == 1 || enableMode == 3) {
            stopByMedia();
            mediaPlayer = MediaPlayer.create(context, res);
            mediaPlayer.start();
        }
    }

    /**
     * 用SoundPool播放
     *
     * @param res
     */
    public void playBySoundPool(int res) {
        if (enableMode == 2) {
            int resId = soundPool.load(context, res, 1);
            soundPool.stop(resId);
            soundPool.play(resId, 1, 1, 0, 0, 1);
        }
    }

    public void stopByMedia() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }
}
