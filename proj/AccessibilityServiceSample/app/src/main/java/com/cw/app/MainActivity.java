package com.cw.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RelativeLayout;

import com.cw.GloabKeyLinstenService;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "jcw";
    RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.istvactivity);
        mRelativeLayout = new RelativeLayout(this);
        Log.d(TAG, "---------- onCreate: ---------- " + mRelativeLayout);

        Intent intent = new Intent();
        intent.setClass(this, GloabKeyLinstenService.class);
        startService(intent);

        Log.d(TAG, "---------- onCreate: ---------- " + mRelativeLayout);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "---------- onNewIntent: ---------- " + mRelativeLayout);

        super.onNewIntent(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "---------- onKeyDown: ---------- " + keyCode);

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "---------- onStart: ----------" + mRelativeLayout);
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "---------- onRestart: ---------- " + mRelativeLayout);
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "---------- onResume: ---------- " + mRelativeLayout);
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "---------- onPause: ----------" + mRelativeLayout);
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "---------- onStop: ---------- " + mRelativeLayout);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "---------- onDestroy: ---------- " + mRelativeLayout);
        super.onDestroy();
    }
}