package com.cw.broadcastsample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "JCW";

    private static final String ACTION_DYNAMIC_MESSAGE = "android.intent.action.DYNAMIC_MESSAGE";
    private static final String ACTION_STATIC_MESSAGE = "android.intent.action.STATIC_MESSAGE";

    class DynamicReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isMainThread = Looper.myLooper().equals(Looper.getMainLooper());
            Log.d(TAG, "onReceive: isMainThread-> " + isMainThread);

            if (!isMainThread) {
                Looper.myLooper().quit();
            }
        }
    }

    private BroadcastReceiver broadcastReceiver;
    private BroadcastReceiver norMainThreadBroadcastReceiver;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");

        setContentView(R.layout.activity_main);

        broadcastReceiver = new DynamicReceiver();
        norMainThreadBroadcastReceiver = new DynamicReceiver();

        IntentFilter intentFilter = new IntentFilter(ACTION_DYNAMIC_MESSAGE);

        // onReceiver在主线程中回调
        registerReceiver(broadcastReceiver, intentFilter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                handler = new Handler();
                // onReceiver在非主线程中回调
                registerReceiver(norMainThreadBroadcastReceiver, intentFilter, null, handler);
                Looper.loop();
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: sendBroadcast DYNAMIC ST");

        // 发送动态
        Intent intent = new Intent(ACTION_DYNAMIC_MESSAGE);
        sendBroadcast(intent);

        Log.d(TAG, "onResume: sendBroadcast DYNAMIC ED");

        Log.d(TAG, "onResume: sendBroadcast STATIC ST");
        // 发送静态
        Intent staticIntent = new Intent(ACTION_STATIC_MESSAGE);
        // 即使你没有调用顺序发送接口sendOrderedBroadcast，而采用普通sendBroadcast接口，对于静态注册方式的Receiver，它也是按顺序进行回调的
//        sendOrderedBroadcast(staticIntent, null);
        sendBroadcast(staticIntent);

        Log.d(TAG, "onResume: sendBroadcast STATIC ED");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");

        unregisterReceiver(broadcastReceiver);
    }
}