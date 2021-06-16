package com.cw.servicesample;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.cw.servicesample.databinding.ActivityFullscreenBinding;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {

    private ActivityFullscreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFullscreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private IBusinessService mIBusinessService;

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {  // 早于onServiceDisconnected
            Log.d(CWConstant.TAG, "binderDied: ");
        }
    };

    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            boolean isMain = Looper.getMainLooper().equals(Looper.myLooper());
            Log.d(CWConstant.TAG, "onServiceConnected: is main looper? " + isMain);

            Log.d(CWConstant.TAG, "onServiceConnected: " + name.getPackageName() + "/" + name.getClassName());
            mIBusinessService = IBusinessService.Stub.asInterface(service);
            try {
//                mIBusinessService.asBinder().linkToDeath(mDeathRecipient, 0);
                mIBusinessService.asBinder().linkToDeath(mDeathRecipient, IBinder.FLAG_ONEWAY);

//                unbindService(this);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            boolean isMain = Looper.getMainLooper().equals(Looper.myLooper());
            Log.d(CWConstant.TAG, "onServiceDisconnected: is main looper? " + isMain);

            Log.d(CWConstant.TAG, "onServiceDisconnected: " + name.getPackageName() + "/" + name.getClassName());
            if (null != mIBusinessService) {
//                mIBusinessService.asBinder().unlinkToDeath(mDeathRecipient, 0);
//                mIBusinessService.asBinder().unlinkToDeath(mDeathRecipient, IBinder.FLAG_ONEWAY);
                mIBusinessService = null;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(CWConstant.TAG, "onKeyDown: " + keyCode);
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_1) {
                if (null == mIBusinessService) {
                    Intent intent = new Intent("cw.intent.action.INVOKE_BUSINESS");
                    intent.setPackage(getPackageName());
                    bindService(intent, mServiceConnection, Service.BIND_AUTO_CREATE);
                } else {
                    // int anInt, long aLong, boolean aBoolean, float aFloat,
                    //            double aDouble, String aString)
                    try {
                        mIBusinessService.basicTypes(1, 1000L, true, 1.0f, 2.0d, "test");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return super.onKeyDown(keyCode, event);
    }
}