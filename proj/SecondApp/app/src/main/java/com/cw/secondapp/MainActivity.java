package com.cw.secondapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "jcw";

    TextView noticeText;
    Button startSecondActivityBt;
    Thread mThread;

    @Override
     public void onClick(View v) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
//        intent.setClassName(getPackageName(), SecondActivity.class.getName());
        intent.setClassName("com.cw.thirdsystemapp", "com.cw.thirdsystemapp.FullscreenActivity");
        // 方式一
        startActivity(intent);
//        // 方式二
//        startActivityForResult(intent, 1);
//        // 方式三
//        getApplicationContext().startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mThread = Thread.currentThread();

        Log.d(TAG, "onCreate: currentThread " + Thread.currentThread());
        noticeText = (TextView) findViewById(R.id.textNotice);

        startSecondActivityBt = (Button) findViewById(R.id.startSecondActivity);
        startSecondActivityBt.setOnClickListener(this);

        // 获取app UID方法一
        int uid;
        try {
            ApplicationInfo info = getPackageManager().getApplicationInfo(
                    getPackageName(), 0);
            uid = info.uid;
        } catch (PackageManager.NameNotFoundException e) {
            uid = -1;
        }

        Log.i(TAG, "pms get UID = " + uid);

        // 获取app UID方法二
        uid = android.os.Process.myUid();
        Log.i(TAG, "android.os.Process.myUid(): " + uid);
    }

    @Override
    protected void onResume() {
        super.onResume();

//        getContentResolver().call(Uri.parse("content://businessprovider.authorities"), "invoke", null, null);
    }

}
