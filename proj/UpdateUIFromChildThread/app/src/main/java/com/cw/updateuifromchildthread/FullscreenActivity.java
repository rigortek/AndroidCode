package com.cw.updateuifromchildthread;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {

    public static final String TAG = "jcw";

    private TextView notice_changeable;
    private TextView child_thread_access_ui;
    private TextView main_thread_access_ui;

    private Button child_thread_switch_to_main_thread;
    private Button finishBt;
    TextView subThreadCreateTextView;

    Thread mThread;

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.child_thread_access_ui:
                    childThreadAccessView();
                    break;
                case R.id.main_thread_access_ui:
                    mainThreadAccessView();
                    break;
                case R.id.child_thread_switch_to_main_thread:
                    childThreadSwitch2MainThread(methodIndex);
                    break;
                case R.id.finishBt:
                    finish();
                    break;

                default:
                    break;
            }
        }
    }

    View.OnClickListener onClickListener = new MyOnClickListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mThread = Thread.currentThread();

        setContentView(R.layout.activity_fullscreen);

        notice_changeable = findViewById(R.id.notice_changeable);

        child_thread_access_ui = findViewById(R.id.child_thread_access_ui);
        child_thread_access_ui.setOnClickListener(onClickListener);

        main_thread_access_ui = findViewById(R.id.main_thread_access_ui);
        main_thread_access_ui.setOnClickListener(onClickListener);

        child_thread_switch_to_main_thread = findViewById(R.id.child_thread_switch_to_main_thread);
        child_thread_switch_to_main_thread.setOnClickListener(onClickListener);

        finishBt = findViewById(R.id.finishBt);
        finishBt.setOnClickListener(onClickListener);
//        finishBt.setBackground(getResources().getDrawable(R.drawable.imageview_selector));


        childThreadAccessView();
        noMainThreadCreateView();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }


    private void childThreadAccessView() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (null != notice_changeable) {
                    // sleep will leads to exception:
                    // android.view.ViewRootImpl$CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
//                    try {
//                        Thread.sleep(1000);
//                    } catch (Exception e) {
//                        Log.e(TAG, "run: error: " + e.getMessage());
//                    }
                    Log.d(TAG, "run: currentThread " + Thread.currentThread());
                    Log.d(TAG, "run: is same thread: " + (mThread == Thread.currentThread()));
                    notice_changeable.setText("Hello, set text one by child thread.");
                }
            }
        }).start();
    }


    private void noMainThreadCreateView() {
        if (null != subThreadCreateTextView) {
            return;
        }
        subThreadCreateTextView = new TextView(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (null != notice_changeable) {
                    Looper.prepare();
                    // subThreadCreateTextView create by no main thread, the main thread has no permission to touch it
                    addWindow(subThreadCreateTextView);
                    Log.d(TAG, "run, currentThread " + Thread.currentThread());
                    subThreadCreateTextView.setText("Hello, set text two by child thread.");

                    Looper.loop();
                }
            }
        }).start();
    }

    private void mainThreadAccessView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // subThreadCreateTextView create by no main thread, the main thread has no permission to touch it
                subThreadCreateTextView.setText("Hello, set text three by main thread.");
            }
        }, 1000L);
    }

    private void addWindow(TextView view) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                0, 0,
                PixelFormat.TRANSPARENT
        );

        layoutParams.flags= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }

        layoutParams.gravity = Gravity.TOP | Gravity.RIGHT;
        layoutParams.x = 150;
        layoutParams.y = 50;

        WindowManager windowManager = getWindowManager();
        windowManager.addView(view, layoutParams);
    }

    private int methodIndex = 0;

    private static final int WHAT_MESSAGE_ID = 1000;

    private class MyHandler extends android.os.Handler {
        public MyHandler(@NonNull Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {

            switch (msg.what) {
                case WHAT_MESSAGE_ID:
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private Handler myHandler = new MyHandler(Looper.getMainLooper());

    private void childThreadSwitch2MainThread(final int method) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                switch (method) {
                    case 0:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                subThreadCreateTextView.setText("Hello, set text by runOnUiThread.");
                            }
                        });
                        break;
                    case 1:
                        subThreadCreateTextView.post(new Runnable() {
                            @Override
                            public void run() {
                                subThreadCreateTextView.setText("Hello, set text by post.");
                            }
                        });
                        break;
                    case 2:
                        myHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                subThreadCreateTextView.setText("Hello, set text by Handler.post.");
                            }
                        });
                        // 或者
                        Message message = Message.obtain();
                        message.what = WHAT_MESSAGE_ID;
                        message.obj = "Hello, set text by Handler.sendMessage.";
                        myHandler.sendMessage(message);
                        break;
                    case 3:
                        break;

                    default:
                        break;
                }

                if (method < 4) {
                    methodIndex = method + 1;
                } else {
                    methodIndex = 0;
                }
            }
        }).start();
    }
}