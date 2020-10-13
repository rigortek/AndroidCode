package com.cw.secondapp;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.List;

public class MessengerService extends Service {
    public static final String TAG = "jcw";

    StubImpl mStubImpl;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return (null != mStubImpl) ? mStubImpl : new StubImpl();
    }

    class StubImpl extends IMessengerService.Stub {
        @Override
        public String basicTypes(String aString) throws RemoteException {
            // 此方法无法处理多个应用配置同样的shareUserId
//            String callingApp = getPackageManager().getNameForUid(Binder.getCallingUid());
            String [] pkgList = getPackageNames(getApplicationContext(), Binder.getCallingPid());

            if (null != pkgList) {
                for (String cur :
                        pkgList) {
                    Log.d(TAG, "---------- basicTypes: call from client " + cur);
                }
            }

            return "OK";
        }

        @Override
        public void transferBitMap(Bitmap bitmap) throws RemoteException {
            Log.d(TAG, "---------- transferBitMap ----------, size(byte): " + (null != bitmap ? bitmap.getByteCount() : 0));
        }

        @Override
        public void transferBitMapByBundle(Bundle bundle) throws RemoteException {
            Log.d(TAG, "---------- transferBitMapByBundle ----------");
        }

        @Override
        public void transferRawData(byte[] raw) throws RemoteException {
            Log.d(TAG, "---------- transferRawData ---------- " + (raw != null ? raw.length : 0));
        }
    }

    private String[] getPackageNames(Context context, int pid) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infos = am.getRunningAppProcesses();
        if (infos != null && infos.size() > 0) {
            for(ActivityManager.RunningAppProcessInfo info : infos) {
                if(info.pid == pid) {
                    return info.pkgList;
                }
            }
        }
        return null;
    }
}
