package com.cw.updateuifromchildthread.asyncmessage;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.cw.updateuifromchildthread.Constant;

import java.util.concurrent.TimeUnit;

/**
 * Create by robin On 21-3-5
 */
public class IntentServiceDemo extends IntentService {

    // MUST implement zero argument constructor
    public IntentServiceDemo() {
        super("IntentServiceDemo");
    }
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public IntentServiceDemo(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (null != intent) {
            if ("com.cw.jcw.intent.action.TEST_ACTION".equals(intent.getAction())) {
                Log.d(Constant.TAG, "onHandleIntent: " + intent.getStringExtra(Constant.KEY_PARAM));
            }
        }
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
