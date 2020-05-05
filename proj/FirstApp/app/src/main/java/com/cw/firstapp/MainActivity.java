package com.cw.firstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.cw.secondapp.IMessengerService;


/*
指定广播接收者权限,如果接收者无权限提示错误
W/BroadcastQueue(  927): Permission Denial: receiving Intent { act=com.cw.firstapp.action.TEST flg=0x10 } to
com.cw.secondapp/.MyBroadcastReceiver requires com.cw.firstapp.permission.TEST due to sender com.cw.firstapp (uid 10084)

广播发送者被接收者限制权限，如果发送者无权限提示错误
W/BroadcastQueue(  927): Permission Denial: broadcasting Intent { act=com.cw.secondapp.action.TEST flg=0x10 } from
com.cw.firstapp (pid=11561, uid=10084) requires com.cw.permission.TEST due to receiver com.cw.secondapp/.MyBroadcastReceiver

 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = "jcw";
    public static final String BROADCAST_ACTION_TEST = "com.cw.firstapp.action.TEST";
    public static final String BROADCAST_RECEIVE_RPERMISSION_TEST = "com.cw.firstapp.permission.TEST";

    Button mBtSendBroadcast;
    Button mBtAccessContentProvider;

    IMessengerService mIMessengerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mBtSendBroadcast = (Button)findViewById(R.id.bt_sendbroadcast);
        mBtSendBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onKeyDown: " + "sendBroadcast");
                // 指定广播接收者权限
//        sendBroadcast(new Intent(BROADCAST_ACTION_TEST), BROADCAST_RECEIVE_RPERMISSION_TEST);

                // 广播发送者被接收者限制权限
                sendBroadcast(new Intent("com.cw.secondapp.action.TEST"));
            }
        });

        mBtAccessContentProvider = (Button)findViewById(R.id.bt_access_cp);
        mBtAccessContentProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContentResolver().call(Uri.parse("content://provider.authorities"), "onClick", null, null);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (null != mIMessengerService) {
            return;
        }

        Intent service = new Intent();
        service.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        service.setClassName("com.cw.secondapp", "com.cw.secondapp.MessengerService");
        startService(service);
        bindService(service, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mIMessengerService = IMessengerService.Stub.asInterface(service);
                Log.d(TAG, "---------- onServiceConnected: ----------");

                try {
                    mIMessengerService.basicTypes("hi, me is first app");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mIMessengerService = null;
                Log.d(TAG, "---------- onServiceDisconnected: ----------");
            }
        }, Service.BIND_AUTO_CREATE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return super.onKeyDown(keyCode, event);
    }
}
