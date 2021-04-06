package com.cw.updateuifromchildthread;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.StatFs;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.cw.updateuifromchildthread.asyncmessage.HandlerThreadTest;
import com.cw.updateuifromchildthread.asyncmessage.IntentServiceDemo;
import com.cw.updateuifromchildthread.asyncmessage.UIHandler;
import com.cw.updateuifromchildthread.asyncmessage.WorkHandler;
import com.cw.updateuifromchildthread.io.NIOClient;
import com.cw.updateuifromchildthread.io.NIOServer;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {

    private TextView notice_changeable;
    private TextView child_thread_access_ui;
    private TextView main_thread_access_ui;

    private Button child_thread_switch_to_main_thread;
    private Button finishBt;
    private Button testHandlerThread;
    private Button testIntentService;
    private Button testOkHttp;
    private Button testVolley;
    private Button testNIO;
    private Button testAnr;
    TextView subThreadCreateTextView;

    Thread mThread;
    HandlerThread handlerThread;
    Handler uiHandler;
    Handler workHandler;

    public static final String TASK_CMD_ANR = "anr";
    private int okhttpMode = 0;  // 0: sync, 1: async
    ExecutorService mExecutorService = Executors.newSingleThreadExecutor();
    OkHttpDemo okHttpDemo;
    VolleyDemo volleyDemo;

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

                case R.id.testHandlerThread: {
                    if (null == uiHandler) {
                        uiHandler = new UIHandler(getApplication(), Looper.getMainLooper());
                    }
                    if (null == handlerThread) {
                        handlerThread = new HandlerThreadTest("test_sip", Process.THREAD_PRIORITY_FOREGROUND);
                        handlerThread.start();
                        Log.d(Constant.TAG, "onClick: isMainLooper : " + handlerThread.getLooper().equals(Looper.getMainLooper()));
                        workHandler = new WorkHandler(handlerThread.getLooper(), uiHandler);
                    }

                    // 可以通过基于HandlerThread中子线程Looper创建的子线程Handler发送消息，从而实现让其持续执行一个个任务
                    Message msg = Message.obtain();
                    msg.what = UIHandler.MSG_WORKTHREAD_START;
                    msg.obj = "invoke handlerthread start work";
                    Toast.makeText(getApplication(), (String) msg.obj, Toast.LENGTH_SHORT).show();
                    workHandler.sendMessage(msg);
                }
                break;

                case R.id.testIntentService: {
//                    Intent intent = new Intent(FullscreenActivity.this, IntentServiceDemo.class);
                    Intent intent = new Intent();
                    intent.setAction("com.cw.jcw.intent.action.TEST_ACTION");
//                    intent.setPackage(getPackageName());
                    intent.setClass(FullscreenActivity.this, IntentServiceDemo.class);
                    for (int i = 0; i < 3; i++) {
                        intent.putExtra(Constant.KEY_PARAM, "task id: " + i);
                        startService(intent);
                        try {
                            TimeUnit.MILLISECONDS.sleep(50);
                        } catch (Exception e) {
                            Log.e(Constant.TAG, "onClick: sleep " + e.getMessage());
                        }
                    }
                    break;
                }

                case R.id.testOkHttp: {
                    if (null == okHttpDemo) {
                        okHttpDemo = new OkHttpDemo();
                    }
                    if (0 == okhttpMode) {
                        mExecutorService.submit(new Runnable() {
                            @Override
                            public void run() {
                                okHttpDemo.testSyncOkhttp("https://www.baidu.com/");
                            }
                        });
                        ++okhttpMode;
                    } else {
                        okHttpDemo.testAsyncOkHttp("https://www.baidu.com/");
                        --okhttpMode;
                    }
                }
                break;

                case R.id.testVolley: {
                    if (null == volleyDemo) {
                        volleyDemo = new VolleyDemo();
                    }

                    volleyDemo.testStringVolley(FullscreenActivity.this);
                    volleyDemo.testJsonVolley(FullscreenActivity.this);
                    break;
                }

                case R.id.testNIO: {
                    if (nioClient == null && nioServer == null) {
                        nioServer = new NIOServer(8888);
                        nioClient = new NIOClient("127.0.0.1", 8888);

                        new Thread(nioServer).start();
                        new Thread(nioClient).start();
                    } else {
                        nioClient.sendMsg("");
                        nioClient.sendMsg("---TEST---");
                        nioClient.sendMsg("QUIT");
                        nioClient = null;
                        nioServer = null;
                    }
                    break;
                }
                case R.id.testAnr: {
                    ChildThreadSwitch2MainThreakTask task = new ChildThreadSwitch2MainThreakTask();
                    synchronized (task) {
                        task.execute(TASK_CMD_ANR);
                        // Wait for this worker thread’s notification
                        try {
                            task.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    }

    NIOServer nioServer;
    NIOClient nioClient;

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

        testHandlerThread = findViewById(R.id.testHandlerThread);
        testHandlerThread.setOnClickListener(onClickListener);

        testIntentService = findViewById(R.id.testIntentService);
        testIntentService.setOnClickListener(onClickListener);

        testOkHttp = findViewById(R.id.testOkHttp);
        testOkHttp.setOnClickListener(onClickListener);

        testVolley = findViewById(R.id.testVolley);
        testVolley.setOnClickListener(onClickListener);

        testNIO = findViewById(R.id.testNIO);
        testNIO.setOnClickListener(onClickListener);

        testAnr = findViewById(R.id.testAnr);
        testAnr.setOnClickListener(onClickListener);
        
        childThreadAccessView();
        noMainThreadCreateView();

        registerReceiver();

        long freeBlockSize = getAvailSpace(Environment.getDataDirectory().getPath()) / (2 << 19);  // (1024 * 1024)
        Log.d(Constant.TAG, "onCreate: " + freeBlockSize);
    }

    /**
     * 根据路劲获取某个目录的可用空间
     *
     * @param path 文件的路径
     * @return result 返回该目录的可用空间大小
     */
    private long getAvailSpace(String path) {
        StatFs statFs = new StatFs(path);

//        int JELLY_BEAN_MR2
//        July 2013: Android 4.3, the revenge of the beans.
//
//        Constant Value: 18 (0x00000012)
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            long size = statFs.getBlockSizeLong();// 获取分区的大小
            long blocks = statFs.getAvailableBlocksLong();// 获取可用分区的个数
            return blocks * size;
        } else {
            long size = statFs.getBlockSize();// 获取分区的大小
            long blocks = statFs.getAvailableBlocks();// 获取可用分区的个数
            return blocks * size;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        myHandler.removeCallbacks(null);

        unregisterReceiver(broadcastReceiver);
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
                    Log.d(Constant.TAG, "run: currentThread " + Thread.currentThread());
                    Log.d(Constant.TAG, "run: is same thread: " + (mThread == Thread.currentThread()));
                    updateNoticeText("Hello, set text one by child thread.");
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
                    Log.d(Constant.TAG, "run, currentThread " + Thread.currentThread());
                    updateNoticeText("Hello, set text two by child thread.");

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
                updateNoticeText("Hello, set text three by main thread.");
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
        layoutParams.y = 30;

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
                case WHAT_MESSAGE_ID: {
                    String target = (String) msg.obj;
                    if (null != target) {
                        updateNoticeText(target);
                    }
                }
                break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private Handler myHandler = new MyHandler(Looper.getMainLooper());

    public static final String TEST_ACTION =  "com.cw.updateuifromchildthread.action.TEST";
    public static final String ACTION_EXTRA_KEY =  "action_key_update_textview";

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.cw.updateuifromchildthread.action.TEST".equals(intent.getAction())) {
                String value = intent.getStringExtra(ACTION_EXTRA_KEY);
                updateNoticeText(null != value ? value : "");
            }
        }
    };

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter(TEST_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);

        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    private LocalBroadcastManager localBroadcastManager;

    private void childThreadSwitch2MainThread(final int method) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                switch (method) {
                    case 0:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateNoticeText("Hello, set text by runOnUiThread.");
                            }
                        });
                        break;
                    case 1: {
                        notice_changeable.post(new Runnable() {
                            @Override
                            public void run() {
                                updateNoticeText("Hello, set text by post.");
                            }
                        });
                    }
                    break;

                    case 2: {
                        myHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                updateNoticeText("Hello, set text by Handler.post.");
                            }
                        });
                    }
                    break;

                    case 3: {
                        // 或者
                        Message message = Message.obtain();
                        message.what = WHAT_MESSAGE_ID;
                        message.obj = "Hello, set text by Handler.sendMessage.";
                        myHandler.sendMessage(message);
                    }
                    break;

                    case 4: {
                        Intent intent = new Intent(TEST_ACTION);
                        intent.putExtra(ACTION_EXTRA_KEY, "Hello, set text by sendBroadcast.");
                        sendBroadcast(intent);
                    }
                    break;

                    case 5: {
                        Intent localIntent = new Intent(TEST_ACTION);
                        localIntent.putExtra(ACTION_EXTRA_KEY, "Hello, set text by LocalBroadcastManager.sendBroadcast.");
                        localBroadcastManager.sendBroadcast(localIntent);
                    }
                    break;

                    case 6: {
                        ChildThreadSwitch2MainThreakTask task = new ChildThreadSwitch2MainThreakTask();
                        task.execute("");
                    }
                    break;

                    default:
                        break;
                }

                if (method < 6 ) {
                    methodIndex = method + 1;
                } else {
                    methodIndex = 0;
                }
            }
        }).start();
    }

    private void updateNoticeText(String str) {
        notice_changeable.setText(str);
    }

    private class ChildThreadSwitch2MainThreakTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(Constant.TAG, "onPreExecute: 开始执行异常操作");
        }

        protected String doInBackground(String... params) {
            for (int i = 0; i < 6; i++) {
                setProgress(i * 20);
                publishProgress(i * 20);   // publishProgress will invoke onProgressUpdate callback
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (TASK_CMD_ANR.equals(params[0])) {
                synchronized (this) {
                    try {
                        Log.d(Constant.TAG, "doInBackground: begin sleep, notify 5 seconds later...");
                        TimeUnit.MILLISECONDS.sleep(6_000);
                        notify();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            // 返回结果回调到onPostExecute
            return "Hello, set text by AsyncTask done.";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            updateNoticeText("Current progress: " + values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(Constant.TAG, "onPreExecute: 结束执行异常操作");
            updateNoticeText(result);
        }
    }
}