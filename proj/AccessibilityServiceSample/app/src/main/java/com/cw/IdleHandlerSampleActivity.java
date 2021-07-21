package com.cw;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.util.Log;
import android.util.Printer;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cw.app.MainActivity;
import com.cw.app.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class IdleHandlerSampleActivity extends AppCompatActivity {

    MessageQueue.IdleHandler mIdleHandler;

    private final ArrayList<MessageQueue.IdleHandler> mIdleHandlers = new ArrayList<MessageQueue.IdleHandler>();
    private MessageQueue.IdleHandler[] mPendingIdleHandlers;

    private class MyIdleHandler implements MessageQueue.IdleHandler {
        private WeakReference<IdleHandlerSampleActivity> mIdleHandlerSampleActivity;

        public MyIdleHandler(IdleHandlerSampleActivity activity) {
            mIdleHandlerSampleActivity = new WeakReference<>(activity);
        }

        @Override
        public boolean queueIdle() {
//            IdleHandlerSampleActivity activity = mIdleHandlerSampleActivity.get();
//            if (null != activity) {
//                Toast.makeText(activity, "I am idle now...", Toast.LENGTH_SHORT).show();
//            }

            Log.d(MainActivity.TAG, "queueIdle: " + "I am idle now...");

            // 不自动移除
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idle_handler_sample);

        Button button = findViewById(R.id.bt_invoke_ui);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setText(button.getText() + "_1");
            }
        });

        mIdleHandler = new MyIdleHandler(this);
        Looper.myQueue().addIdleHandler(mIdleHandler);

        Looper.myLooper().setMessageLogging(new Printer() {
            @Override
            public void println(String x) {
                Log.d(MainActivity.TAG, "println: " + x);
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Toast.makeText(this, "I am in onKeyDown...", Toast.LENGTH_SHORT).show();

        {
            int pendingIdleHandlerCount = mIdleHandlers.size();

            if (mPendingIdleHandlers == null) {
                mPendingIdleHandlers = new MessageQueue.IdleHandler[Math.max(pendingIdleHandlerCount, 4)];
            }
            mPendingIdleHandlers = mIdleHandlers.toArray(mPendingIdleHandlers);

            // Run the idle handlers.
            // We only ever reach this code block during the first iteration.
            for (int i = 0; i < pendingIdleHandlerCount; i++) {
                final MessageQueue.IdleHandler idler = mPendingIdleHandlers[i];
                mPendingIdleHandlers[i] = null; // release the reference to the handler

                boolean keep = false;
                try {
                    keep = idler.queueIdle();
                } catch (Throwable t) {
//                    Log.wtf(TAG, "IdleHandler threw exception", t);
                }

                if (!keep) {
                    synchronized (this) {
                        mIdleHandlers.remove(idler);
                    }
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
//        Toast.makeText(this, "I am in onStop...", Toast.LENGTH_SHORT).show();

        // match addIdleHandler
        Looper.myQueue().removeIdleHandler(mIdleHandler);
        Looper.myLooper().setMessageLogging(null);
        super.onStop();
    }
}