package com.cw.servicesample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class BusinessService extends Service {
    public BusinessService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBusinessServiceStub;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // return START_STICKY_COMPATIBILITY or START_STICKY by default
        return super.onStartCommand(intent, flags, startId);
    }

    IBusinessService.Stub mBusinessServiceStub = new IBusinessService.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {
            long startTime = System.currentTimeMillis();
            Log.d("jcw", "basicTypes: sleep start");

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                Log.d("jcw", "basicTypes: sleep end, time: " + (System.currentTimeMillis() - startTime));
            }

            // 人为制造exception，必须另起线程，否则会被catch住
            // W/Binder  ( 9169): Caught a RuntimeException from the binder stub implementation.
            new Thread(new Runnable() {
                @Override
                public void run() {
                    throw new UnsupportedOperationException("Fake not yet implemented");
                }
            }).start();
        }
    };
}