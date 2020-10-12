package com.cw.thirdsystemapp;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import org.xml.sax.InputSource;

//    W/System.err( 4639): java.lang.NullPointerException: FullscreenActivity call fake exception for print callstack
//    W/System.err( 4639): 	at com.cw.thirdapp.FullscreenActivity.onPause(FullscreenActivity.java:221)
//    W/System.err( 4639): 	at android.app.Activity.performPause(Activity.java:6101)
//    W/System.err( 4639): 	at android.app.Instrumentation.callActivityOnPause(Instrumentation.java:1310)
//    W/System.err( 4639): 	at android.app.ActivityThread.performPauseActivity(ActivityThread.java:3246)
//    W/System.err( 4639): 	at android.app.ActivityThread.performPauseActivity(ActivityThread.java:3219)
//    W/System.err( 4639): 	at android.app.ActivityThread.handlePauseActivity(ActivityThread.java:3194)
//    W/System.err( 4639): 	at android.app.ActivityThread.access$1000(ActivityThread.java:151)
//    W/System.err( 4639): 	at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1314)
//    W/System.err( 4639): 	at android.os.Handler.dispatchMessage(Handler.java:102)
//    W/System.err( 4639): 	at android.os.Looper.loop(Looper.java:135)
//    W/System.err( 4639): 	at android.app.ActivityThread.main(ActivityThread.java:5254)
//    W/System.err( 4639): 	at java.lang.reflect.Method.invoke(Native Method)
//    W/System.err( 4639): 	at java.lang.reflect.Method.invoke(Method.java:372)
//    W/System.err( 4639): 	at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:903)
//    W/System.err( 4639): 	at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:698)
//    I/ActivityManager( 2632): Killing 3731:com.android.development/u0a28 (adj 15): empty for 1801s
//    W/libprocessgroup( 2632): failed to open /acct/uid_10028/pid_3731/cgroup.procs: No such file or directory
//    D/jcw     ( 4639): onCreate: NextActivity
//    W/System.err( 4639): java.lang.NullPointerException: NextActivity call fake exception for print callstack
//    W/System.err( 4639): 	at com.cw.thirdapp.NextActivity.onCreate(NextActivity.java:107)
//    W/System.err( 4639): 	at android.app.Activity.performCreate(Activity.java:5990)
//    W/System.err( 4639): 	at android.app.Instrumentation.callActivityOnCreate(Instrumentation.java:1106)
//    W/System.err( 4639): 	at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2278)
//    W/System.err( 4639): 	at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:2387)
//    W/System.err( 4639): 	at android.app.ActivityThread.access$800(ActivityThread.java:151)
//    W/System.err( 4639): 	at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1303)
//    W/System.err( 4639): 	at android.os.Handler.dispatchMessage(Handler.java:102)
//    W/System.err( 4639): 	at android.os.Looper.loop(Looper.java:135)
//    W/System.err( 4639): 	at android.app.ActivityThread.main(ActivityThread.java:5254)
//    W/System.err( 4639): 	at java.lang.reflect.Method.invoke(Native Method)
//    W/System.err( 4639): 	at java.lang.reflect.Method.invoke(Method.java:372)
//    W/System.err( 4639): 	at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:903)
//    W/System.err( 4639): 	at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:698)


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    public static final String TAG = "jcw";

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

        setContentView(R.layout.activity_fullscreen);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);

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

        findViewById(R.id.startNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setClass(getApplicationContext(), NextActivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.injectKeyEvent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendKeyEvent(KeyEvent.KEYCODE_0);
            }
        });

        Intent intent = getIntent();
        Bitmap bitmap = intent != null ? (Bitmap)intent.getParcelableExtra("bitmap") : null;
        if (null != bitmap) {
            findViewById(R.id.pass_image).setBackground(new BitmapDrawable(getResources(), bitmap));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            throw new NullPointerException("FullscreenActivity call fake exception for print callstack");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        try {
            throw new NullPointerException("FullscreenActivity call fake exception for print callstack");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.onKeyUp(keyCode, event);
    }

    private void sendKeyEvent(int keyCode) {
        InputHelper inputHelper = new InputHelper();
        long now = SystemClock.uptimeMillis();

        inputHelper.injectKeyEvent(new KeyEvent(now, now, KeyEvent.ACTION_DOWN, keyCode, 0, 0,
                KeyCharacterMap.VIRTUAL_KEYBOARD, 0, 0, InputDevice.SOURCE_KEYBOARD));

        inputHelper.injectKeyEvent(new KeyEvent(now, now, KeyEvent.ACTION_UP, keyCode, 0, 0,
                KeyCharacterMap.VIRTUAL_KEYBOARD, 0, 0, InputDevice.SOURCE_KEYBOARD));
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
    protected void onPause() {
        Log.d(TAG, "onPause: FullscreenActivity");
        // verify onPause call stack
//        try {
//            throw new NullPointerException("FullscreenActivity call fake exception for print callstack");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        super.onPause();

//        try {
//            Thread.sleep(4000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: FullscreenActivity");
        // verify onStop call stack
//        try {
//            throw new NullPointerException("FullscreenActivity call fake exception for print callstack");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        super.onStop();
    }
}