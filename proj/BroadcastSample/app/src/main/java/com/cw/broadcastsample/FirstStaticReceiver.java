package com.cw.broadcastsample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class FirstStaticReceiver extends BroadcastReceiver {
    public static final String TAG = "JCW";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.d(TAG, "onReceive: FirstStaticReceiver, current time: " + SystemClock.elapsedRealtime());

        try {
            throw new NullPointerException("onReceive fake exception for print callstack");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}