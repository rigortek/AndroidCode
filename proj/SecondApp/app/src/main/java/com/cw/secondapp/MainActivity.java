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

    TextView subThreadCreateTextView;

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (null != noticeText) {
                    // sleep will leads to exception:
                    // android.view.ViewRootImpl$CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
//                    try {
//                        Thread.sleep(1000);
//                    } catch (Exception e) {
//                        Log.e(TAG, "run: error: " + e.getMessage());
//                    }
                    Log.d(TAG, "run: currentThread " + Thread.currentThread());
                    Log.d(TAG, "run: is same thread: " + (mThread == Thread.currentThread()));
                    noticeText.setText("Hello, I am new set text.");
                }
            }
        }).start();

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

//        noMainThreadCreateView();
//
//        mainThreadAccessView();

    }

    private void noMainThreadCreateView() {
        subThreadCreateTextView = new TextView(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (null != noticeText) {
                    Looper.prepare();
                    // subThreadCreateTextView create by no main thread, the main thread has no permission to touch it
                    addWindow(subThreadCreateTextView);
                    Log.d(TAG, "run, currentThread " + Thread.currentThread());
                    subThreadCreateTextView.setText("Hello, I am new set text 2.");

                    Looper.loop();
                }
            }
        }).start();
    }

    private void mainThreadAccessView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // subThreadCreateTextView create by no main thread, the main thread has no permission to touch it
//                subThreadCreateTextView.setText("Hello, I am new set text 3.");
            }
        }, 1000L);
    }

    private void addWindow(TextView view) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                0, 0,
                PixelFormat.TRANSPARENT
        );

        layoutParams.flags= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }

        layoutParams.gravity = Gravity.TOP | Gravity.RIGHT;
        layoutParams.x = 150;
        layoutParams.y = 250;

        WindowManager windowManager = getWindowManager();
        windowManager.addView(view, layoutParams);
    }

}
