package com.cw.secondapp;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Looper;
import android.os.MessageQueue;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
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

//    W/System.err( 4151): java.lang.NullPointerException: onCreate fake exception for print callstack
//    W/System.err( 4151):    at com.cw.secondapp.MessengerService.onCreate(MessengerService.java:51)
//    W/System.err( 4151):    at android.app.ActivityThread.handleCreateService(ActivityThread.java:2761)
//    W/System.err( 4151):    at android.app.ActivityThread.access$1800(ActivityThread.java:151)
//    W/System.err( 4151):    at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1386)
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            throw new NullPointerException("onCreate fake exception for print callstack");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            // only for gingerbread and newer versions
            onFileDescriptorEventListener = new MessageQueue.OnFileDescriptorEventListener() {
                @Override
                public int onFileDescriptorEvents(@NonNull FileDescriptor fd, int events) {
                    Log.d(TAG, "onFileDescriptorEvents: " + fd + ", " + events);
                    return save2String();

//                    return save2File();
                }
            };
        }
    }

    private int save2String() {
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

        return TextUtils.isEmpty(sb.toString()) ? MessageQueue.OnFileDescriptorEventListener.EVENT_ERROR : MessageQueue.OnFileDescriptorEventListener.EVENT_INPUT;
    }

    // TODO 问题1：> 1M图片文件，保存到此保持后文件会变大，虽然也是可以打开；
    // TODO 问题2：> 1M图片文件，onFileDescriptorEvents会不断收到回调，但除第一次有内容，后续绋无内容，原因不明？
    private int save2File() {
        ParcelFileDescriptor.AutoCloseInputStream reader = new ParcelFileDescriptor.AutoCloseInputStream(mPfd);

        ByteArrayOutputStream total = new ByteArrayOutputStream();
        final byte[] buffer = new byte[512];
        int size = 0;
        try {
            do {
                try {
                    size = reader.read(buffer, 0, buffer.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (size > 0) {
                    total.write(buffer, 0, size);
                }
            } while (size > 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String path = "";
        byte[] totalByte = total.toByteArray();
        if (totalByte != null && totalByte.length > 0) {
            path = getCacheDir() + "/" + System.currentTimeMillis() + ".jpg";
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            writeToFile(totalByte, path);
        }

        try {
            if (null != mCallBack) {
                if (!TextUtils.isEmpty(path)) {
                    mCallBack.onReceive("save to file: " + path);
                } else {
                    Log.w(TAG, "onFileDescriptorEvents: receive nothing");
                }
            } else {
                Log.w(TAG, "onFileDescriptorEvents: callback is null");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return MessageQueue.OnFileDescriptorEventListener.EVENT_INPUT;
    }

    public void writeToFile(byte[] data, String fileName) {
        try {
            FileOutputStream out = new FileOutputStream(fileName);
            out.write(data);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

//    W/System.err( 4151): java.lang.NullPointerException: onStartCommand fake exception for print callstack
//    W/System.err( 4151):    at com.cw.secondapp.MessengerService.onStartCommand(MessengerService.java:171)
//    W/System.err( 4151):    at android.app.ActivityThread.handleServiceArgs(ActivityThread.java:2894)
//    W/System.err( 4151):    at android.app.ActivityThread.access$2100(ActivityThread.java:151)
//    W/System.err( 4151):    at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1401)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            throw new NullPointerException("onStartCommand fake exception for print callstack");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }

//    W/System.err( 4151): java.lang.NullPointerException: onBind fake exception for print callstack
//    W/System.err( 4151):    at com.cw.secondapp.MessengerService.onBind(MessengerService.java:183)
//    W/System.err( 4151):    at android.app.ActivityThread.handleBindService(ActivityThread.java:2788)
//    W/System.err( 4151):    at android.app.ActivityThread.access$1900(ActivityThread.java:151)
//    W/System.err( 4151):    at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1391)
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        try {
            throw new NullPointerException("onBind fake exception for print callstack");
        } catch (Exception e) {
            e.printStackTrace();
        }

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
