package com.cw.tv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RelativeLayout;

import com.cw.app.R;

public class TVActivity extends AppCompatActivity {
    public static final String TAG = "jcw";

    RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tvactivity);
        mRelativeLayout = new RelativeLayout(this);
        Log.d(TAG, "*********** onCreate: *********** " + mRelativeLayout);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: *********** " + mRelativeLayout);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "*********** onKeyDown: *********** " + keyCode);

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "*********** onStart: *********** " + mRelativeLayout);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "*********** onRestart: *********** " + mRelativeLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "*********** onResume: *********** " + mRelativeLayout);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "*********** onPause: *********** " + mRelativeLayout);
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "*********** onStop: " + mRelativeLayout);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "*********** onDestroy: *********** " + mRelativeLayout);
        super.onDestroy();
    }
}