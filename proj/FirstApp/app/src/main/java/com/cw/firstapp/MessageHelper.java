package com.cw.firstapp;

import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.text.TextUtils;
import android.util.Log;
import android.util.Printer;
import android.view.Choreographer;

import java.lang.reflect.Field;

public class MessageHelper {
    private static final String TAG = "QUEUE";

    private static Field messagesField = null;
    private static Field nextField = null;

    public static void printFrame() {
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame ( long frameTimeNanos){
                Choreographer.getInstance().postFrameCallback(this);
                Log.d(TAG, ">>>>> doFrame");
                printMessages();
                Log.d(TAG, "<<<<< doFrame");
            }
        });
    }

    private static void printMessages() {
        try {
            if (null == messagesField) {
                messagesField = MessageQueue.class.getDeclaredField("mMessages");
                messagesField.setAccessible(true);
            }

            if (null == nextField) {
                nextField = Message.class.getDeclaredField("next");
                nextField.setAccessible(true);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        MessageQueue queue = Looper.myQueue();

        try {
            Message msg = (Message) messagesField.get(queue);
            StringBuilder sb = new StringBuilder();
            while (msg != null) {
                sb.append(msg.toString()).append("\n");
                msg = (Message) nextField.get(msg);
            }
            String total = sb.toString();
            if (!TextUtils.isEmpty(total)) {
                Log.i(TAG, total);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void setMessageLogging() {
        Looper.getMainLooper().setMessageLogging(new Printer() {
            @Override
            public void println(String x) {
                if (x.startsWith(">>>>>")) {
                    Log.d(TAG, x);
                }
            }
        });
    }
}
