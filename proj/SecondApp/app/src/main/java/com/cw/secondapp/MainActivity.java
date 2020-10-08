package com.cw.secondapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "jcw";

    Button startSecondActivityBt;

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setClassName(getPackageName(), SecondActivity.class.getName());
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

        startSecondActivityBt = findViewById(R.id.startSecondActivity);
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
