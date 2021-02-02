package com.cw.firstapp;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class NextActivity extends AppCompatActivity {
    public static final String TAG = "jcw";
    LooperDemo looperDemo;
    LooperDemo.LooperThread looperThread;

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private ImageView mImageView;
    Animation mRotateAnimation;

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (AUTO_HIDE) {
                        delayedHide(AUTO_HIDE_DELAY_MILLIS);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    view.performClick();
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: NextActivity");

        setContentView(R.layout.activity_next);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        mImageView = findViewById(R.id.loading_image);

        mRotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_loading);
        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);

        try {
            throw new NullPointerException("call fake exception for print callstack");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume: NextActivity");

        try {
            throw new NullPointerException("call fake exception for print callstack");
        } catch (Exception e) {
            e.printStackTrace();
        }

//        mImageView.setVisibility(View.VISIBLE);
//        mImageView.startAnimation(mRotateAnimation);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: NextActivity");
//        mImageView.clearAnimation();
//        mImageView.setVisibility(View.GONE);
    }



    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: MainActivity");

        setResult(RESULT_FIRST_USER + RESULT_FIRST_USER, new Intent("fake_action_for_test"));
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: NextActivity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: NextActivity");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: NextActivity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy: NextActivity");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (looperDemo == null) {
            looperDemo = new LooperDemo(this);
        }

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_0) {
                looperDemo.mHandler.postAtTime(new Runnable() {
                    // Runnable call stack
//                    D/jcw     ( 5339): run: postAtTime to self Runnable
//                    W/System.err( 5339): java.lang.NullPointerException: fake NullPointerException for print callstack
//                    W/System.err( 5339):    at com.cw.firstapp.NextActivity$6.run(NextActivity.java:215)
//                    W/System.err( 5339):    at android.os.Handler.handleCallback(Handler.java:739)
//                    W/System.err( 5339):    at android.os.Handler.dispatchMessage(Handler.java:95)
//                    W/System.err( 5339):    at android.os.Looper.loop(Looper.java:135)
//                    W/System.err( 5339):    at android.app.ActivityThread.main(ActivityThread.java:5267)
//                    W/System.err( 5339):    at java.lang.reflect.Method.invoke(Native Method)
//                    W/System.err( 5339):    at java.lang.reflect.Method.invoke(Method.java:372)
//                    W/System.err( 5339):    at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:903)
//                    W/System.err( 5339):    at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:698)
                    @Override
                    public void run() {
                        Log.d(TAG, "run: postAtTime to self Runnable");
                        try {
                            throw new NullPointerException("fake NullPointerException for print callstack");
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                }, SystemClock.currentThreadTimeMillis() + 5000L);
            } else if (keyCode == KeyEvent.KEYCODE_1) {
               if (null == looperThread) {
                   looperThread = new LooperDemo.LooperThread();
                   looperThread.start();
               } else {
                   looperThread.sendMessageDelayed();
               }
            } else if (keyCode == KeyEvent.KEYCODE_BACK) {
                Log.d(TAG, "onKeyDown: KEYCODE_BACK");
//                setResult(RESULT_FIRST_USER + RESULT_FIRST_USER, new Intent("fake_action_for_test"));

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getIpbyHostName();
                    }
                }).start();
            } else {
                Message message = new Message();
                message.what = LooperDemo.WHAT_MSG_ID_1;
                boolean result = looperDemo.mHandler.sendMessageAtTime(message, SystemClock.uptimeMillis() + 5000L);
//                boolean result = looperDemo.mHandler.sendMessageDelayed(message, 5000L);
                Log.d(TAG, "onKeyDown: sendMessageAtTime/sendMessageDelayed:" + result);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
    * look up ip by host name
    *
     */
    private static void getIpbyHostName() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            displayStuff("local host", inetAddress);
            Log.d(TAG, "getIpbyHostName: --------------------------");

            InetAddress inetAddress2 = InetAddress.getByName("www.baidu.com");
            displayStuff("www.baidu.com", inetAddress2);

            Log.d(TAG, "getIpbyHostName: --------------------------");
            InetAddress[] inetAddressArray = InetAddress.getAllByName("www.google.com");
            for (int i = 0; i < inetAddressArray.length; i++) {
                displayStuff("www.google.com #" + (i + 1), inetAddressArray[i]);
            }
        } catch (Exception e) {
            Log.d(TAG, "getIpbyHostName: " + e.getMessage());
        }
    }

    public static void displayStuff(String whichHost, InetAddress inetAddress) {
        Log.d(TAG, "displayStuff: --------------------------");
        Log.d(TAG, "displayStuff: Which Host:" + whichHost);
        Log.d(TAG, "displayStuff: Canonical Host Name:" + inetAddress.getCanonicalHostName());
        Log.d(TAG, "displayStuff: Host Name:" + inetAddress.getHostName());
        Log.d(TAG, "displayStuff: Host Address:" + inetAddress.getHostAddress());
    }
}