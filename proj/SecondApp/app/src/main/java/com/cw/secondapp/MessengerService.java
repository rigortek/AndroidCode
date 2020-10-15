package com.cw.secondapp;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.MessageQueue;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import org.w3c.dom.Text;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class MessengerService extends Service {
    public static final String TAG = "jcw";

    StubImpl mStubImpl;

    ParcelFileDescriptor mPfd;
    ICallBack mCallBack;

    MessageQueue.OnFileDescriptorEventListener onFileDescriptorEventListener;

    @Override
    public void onCreate() {
        super.onCreate();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            // only for gingerbread and newer versions
            onFileDescriptorEventListener = new MessageQueue.OnFileDescriptorEventListener() {
                @Override
                public int onFileDescriptorEvents(@NonNull FileDescriptor fd, int events) {
                    Log.d(TAG, "onFileDescriptorEvents: " + fd + ", " + events);
                    ParcelFileDescriptor.AutoCloseInputStream reader = new ParcelFileDescriptor.AutoCloseInputStream(mPfd);
    
                    StringBuilder sb = new StringBuilder();
                    final byte[] buffer = new byte[2048];
                    int size = 0;
                    do {
                        try {
                            size = reader.read(buffer, 0, buffer.length);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (size > 0) {
                            sb.append(new String(buffer, 0, size));
                        }
                        Log.d(TAG, "onFileDescriptorEvents: receive content -> " + sb.toString());
                    } while (size > 0);

                    try {
                        if (null != mCallBack) {
                            String tmp = sb.toString();
                            if (!TextUtils.isEmpty(tmp)) {
                                mCallBack.onReceive(tmp);
                            } else {
                                Log.w(TAG, "onFileDescriptorEvents: receive nothing");
                            }
                        } else {
                            Log.w(TAG, "onFileDescriptorEvents: callback is null");
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
    
                    return TextUtils.isEmpty(sb.toString()) ? EVENT_ERROR : EVENT_INPUT;
                }
            };
        }
    }

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

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void register(ParcelFileDescriptor pfd, ICallBack callback) throws RemoteException {
            Log.d(TAG, "---------- register ---------- pfd: " + pfd + ", callback: " + callback);

            mPfd = pfd;
            mCallBack = callback;

//            IOUtils.setBlocking(fd, false);
            FileDescriptor fd = pfd.getFileDescriptor();
            try {
                Class<?> IoUtils = Class.forName("libcore.io.IoUtils");
                if (null == IoUtils) {
                    Log.e(TAG, "register: IoUtils class is null");
                    return;
                }
                Method setBlocking = IoUtils.getMethod("setBlocking", FileDescriptor.class, boolean.class);
                if (null == IoUtils) {
                    Log.e(TAG, "register: setBlocking method is null");
                    return;
                }

                setBlocking.invoke(IoUtils, fd, false);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            
            if (null != onFileDescriptorEventListener) {
                MessageQueue queue = Looper.getMainLooper().getQueue();
                queue.addOnFileDescriptorEventListener(fd, MessageQueue.OnFileDescriptorEventListener.EVENT_INPUT, onFileDescriptorEventListener);
                Log.d(TAG, "---------- register ---------- fd: " + fd
                        + ", event: " + MessageQueue.OnFileDescriptorEventListener.EVENT_INPUT
                        + ", listener: " + onFileDescriptorEventListener);
            } else {
                Log.e(TAG, "register: ERROR for sdk version : " + android.os.Build.VERSION.SDK_INT + " < " +  android.os.Build.VERSION_CODES.M );
            }
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
