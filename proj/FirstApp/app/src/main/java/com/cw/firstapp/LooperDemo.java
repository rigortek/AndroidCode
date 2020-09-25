package com.cw.firstapp;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LooperDemo {
    public static final String TAG = "jcw";

    public static final int WHAT_MSG_ID_1 = 1000;
    public static final int WHAT_MSG_ID_2 = 1001;

    private Activity mActivity;
    Handler mHandler;

    private static class EventHandler extends Handler {
        private final Activity mActivity;

        public EventHandler() {
            super();
            mActivity = null;
        }

        public EventHandler(Activity mActivity) {
            super();
            this.mActivity = mActivity;
        }

        public EventHandler(Activity activity, @Nullable Callback callback) {
            super(callback);
            mActivity = activity;
        }

        public EventHandler(Activity activity, @NonNull Looper looper) {
            super(looper);
            mActivity = activity;
        }

        public EventHandler(Activity activity, @NonNull Looper looper, @Nullable Callback callback) {
            super(looper, callback);
            mActivity = activity;
        }
        // handleMessage call stack
//        09-24 15:05:08.110 D/jcw     ( 6412): handleMessage: what: 1000
//        09-24 15:05:08.110 W/System.err( 6412): java.lang.NullPointerException: fake NullPointerException for print callstack
//        09-24 15:05:08.110 W/System.err( 6412):         at com.cw.firstapp.LooperDemo$EventHandler.handleMessage(LooperDemo.java:48)
//        09-24 15:05:08.110 W/System.err( 6412):         at android.os.Handler.dispatchMessage(Handler.java:102)
//        09-24 15:05:08.110 W/System.err( 6412):         at android.os.Looper.loop(Looper.java:135)
//        09-24 15:05:08.110 W/System.err( 6412):         at android.app.ActivityThread.main(ActivityThread.java:5267)
//        09-24 15:05:08.110 W/System.err( 6412):         at java.lang.reflect.Method.invoke(Native Method)
//        09-24 15:05:08.110 W/System.err( 6412):         at java.lang.reflect.Method.invoke(Method.java:372)
//        09-24 15:05:08.110 W/System.err( 6412):         at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:903)
//        09-24 15:05:08.111 W/System.err( 6412):         at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:698)
        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.d(TAG, "handleMessage: what: " + msg.what);
            switch (msg.what) {
                case WHAT_MSG_ID_1:
                    try {
                        throw new NullPointerException("fake NullPointerException for print callstack");
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    break;

                case WHAT_MSG_ID_2:
                    break;

                default:
                    break;
            }

            super.handleMessage(msg);
        }
    }

    public LooperDemo(Activity activity) {
        mActivity = activity;
        mHandler = new EventHandler(activity);
    }

//    非UI子线程中创建Handler时，必须调用，否则会抛出异常
//    java.lang.RuntimeException: Can't create handler inside thread that has not called Looper.prepare()和Looper.loop()
//    当然还有更偷懒的方法，继承platform/android/frameworks/base/core/java/android/os/HandlerThread.java，这两人个调用，它已经帮你写好了

    static class LooperThread extends Thread {
        public Handler mHandler;

         public void run() {
            Looper.prepare();

             mHandler = new EventHandler();

             Looper.loop();
        }

        public boolean sendMessageDelayed() {
            Message message = Message.obtain();
            message.what = WHAT_MSG_ID_1;
            boolean result = mHandler.sendMessageDelayed(message, 1000L);
            Log.d(TAG, "LooperThread: sendMessageAtTime/sendMessageDelayed:" + result);
            return result;
        }
    }

}
