package com.cw.firstapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.os.TransactionTooLargeException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.cw.secondapp.IMessengerService;

import java.io.ByteArrayOutputStream;
import java.util.List;


/*
指定广播接收者权限,如果接收者无权限提示错误
W/BroadcastQueue(  927): Permission Denial: receiving Intent { act=com.cw.firstapp.action.TEST flg=0x10 } to
com.cw.secondapp/.MyBroadcastReceiver requires com.cw.firstapp.permission.TEST due to sender com.cw.firstapp (uid 10084)

广播发送者被接收者限制权限，如果发送者无权限提示错误
W/BroadcastQueue(  927): Permission Denial: broadcasting Intent { act=com.cw.secondapp.action.TEST flg=0x10 } from
com.cw.firstapp (pid=11561, uid=10084) requires com.cw.permission.TEST due to receiver com.cw.secondapp/.MyBroadcastReceiver

 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = "jcw";
    public static final String BROADCAST_ACTION_TEST = "com.cw.firstapp.action.TEST";
    public static final String BROADCAST_RECEIVE_RPERMISSION_TEST = "com.cw.firstapp.permission.TEST";
    public static final String PROVIDER_AUTHORITIES = "content://businessprovider.authorities";

    Button mBtSendBroadcast;
    Button mBtAccessContentProvider;
    Button mBtMiltiCallProvider;
    Button mBtToNextActivity;

    IMessengerService mIMessengerService;

    Context context;
    Handler handler;

    private static class OufengContentObserver extends ContentObserver {

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public OufengContentObserver(Context context, Handler handler) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);

            Log.d(TAG, "onChange: " + selfChange + ", " + uri.toString());

            try {
                throw new NullPointerException("onChange fake exception for print callstack");
            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.d(TAG, "onChange: in MainLooper " + (Thread.currentThread() == Looper.getMainLooper().getThread()));
        }

        private Context context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        handler = new Handler(getApplication().getMainLooper());

        mBtSendBroadcast = (Button)findViewById(R.id.bt_sendbroadcast);
        mBtSendBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onKeyDown: " + "sendBroadcast");
                // 指定广播接收者权限
//        sendBroadcast(new Intent(BROADCAST_ACTION_TEST), BROADCAST_RECEIVE_RPERMISSION_TEST);

                // 广播发送者被接收者限制权限
                sendBroadcast(new Intent("com.cw.secondapp.action.TEST"));
            }
        });

        mBtAccessContentProvider = (Button)findViewById(R.id.bt_access_cp);
        mBtAccessContentProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> segmentList = Uri.parse("content://businessprovider.authorities/descendant").getPathSegments();

                getContentResolver().registerContentObserver(Uri.parse("content://businessprovider.authorities/descendant"), true,
                        new OufengContentObserver(getApplicationContext(), null));
//                getContentResolver().registerContentObserver(Uri.parse("content://businessprovider.authorities/descendant"), true,
//                        new OufengContentObserver(getApplicationContext(), handler));

                getContentResolver().call(Uri.parse(PROVIDER_AUTHORITIES), "onClick", null, null);
            }
        });

        mBtMiltiCallProvider = (Button)findViewById(R.id.bt_multi_call_cp);
        mBtMiltiCallProvider.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: client start call");
                // first thread
                Thread firstThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 10; i++) {
                            getContentResolver().call(Uri.parse(PROVIDER_AUTHORITIES), "Thread 1 call index: " + i, null, null);
                        }
                    }
                });
                firstThread.setName("first thread");
                firstThread.start();

                // second thread
                Thread secondThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 10; i++) {
                            getContentResolver().call(Uri.parse(PROVIDER_AUTHORITIES), "Thread 2 call index: " + i, null, null);
                        }
                    }
                });
                secondThread.setName("second thread");
                secondThread.start();

                Log.d(TAG, "onClick: client end call");
            }
        });

        mBtToNextActivity = (Button) findViewById(R.id.bt_to_next_activity);
        mBtToNextActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent();
//                i.setClass(getApplicationContext(), NextActivity.class);
//                i.setAction(Intent.ACTION_MAIN);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(i);

//                chooseActivity();

                transferBitBitmapFail();
            }
        });
    }

    @Override
    protected void onStop() {
        // verify onStop call stack
        try {
            throw new NullPointerException("call fake exception for print callstack");
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (null != mIMessengerService) {
            return;
        }

        Intent service = new Intent();
        service.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        service.setClassName("com.cw.secondapp", "com.cw.secondapp.MessengerService");
        startService(service);
        bindService(service, new ServiceConnection() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mIMessengerService = IMessengerService.Stub.asInterface(service);
                Log.d(TAG, "---------- onServiceConnected: ----------");

                try {
                    mIMessengerService.basicTypes("hi, me is first app");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

/*
                try {
                    // aidl可以直接传递> 1M Bitmap，关键就出在它是使用ashmem，共享内存
                    // platform/tvos-h/base/ipc/binder/Parcel.cpp
                    // status_t Parcel::writeBlob(size_t len, WritableBlob* outBlob)
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.big_jpg);
                    int size = bitmap.getByteCount();  // 长 ＊ 宽 ＊ 16
                    Log.d(TAG, "---------- onServiceConnected, bitmap size(KB): " + (float)size / 1024 / 8);
                    mIMessengerService.transferBitMap(bitmap);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
*/
/*
                E/JavaBinder( 8707): !!! FAILED BINDER TRANSACTION !!!
                W/System.err( 8707): android.os.TransactionTooLargeException
                W/System.err( 8707): 	at android.os.BinderProxy.transactNative(Native Method)
                W/System.err( 8707): 	at android.os.BinderProxy.transact(Binder.java:504)
                W/System.err( 8707): 	at com.cw.secondapp.IMessengerService$Stub$Proxy.transferRawData(IMessengerService.java:222)
                W/System.err( 8707): 	at com.cw.firstapp.MainActivity$5.onServiceConnected(MainActivity.java:227)
                W/System.err( 8707): 	at android.app.LoadedApk$ServiceDispatcher.doConnected(LoadedApk.java:1259)
                W/System.err( 8707): 	at android.app.LoadedApk$ServiceDispatcher$RunConnection.run(LoadedApk.java:1276)
                W/System.err( 8707): 	at android.os.Handler.handleCallback(Handler.java:815)
                W/System.err( 8707): 	at android.os.Handler.dispatchMessage(Handler.java:104)
                W/System.err( 8707): 	at android.os.Looper.loop(Looper.java:224)
                W/System.err( 8707): 	at android.app.ActivityThread.main(ActivityThread.java:5958)
                W/System.err( 8707): 	at java.lang.reflect.Method.invoke(Native Method)
                W/System.err( 8707): 	at java.lang.reflect.Method.invoke(Method.java:372)
                W/System.err( 8707): 	at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:1113)
                W/System.err( 8707): 	at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:879)
*/
                // 裸数据 > 1M 是传不过去的，例如byte[]
                try {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.big_jpg);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] bitmapdata = stream.toByteArray();
                    mIMessengerService.transferRawData(bitmapdata);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

//                transferBitBitmapFail();

//                transferBitBitmapSuccess();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mIMessengerService = null;
                Log.d(TAG, "---------- onServiceDisconnected: ----------");
            }
        }, Service.BIND_AUTO_CREATE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "Action: " + event.getAction() + ", onKeyDown keyCode: " + keyCode);
        try {
            throw new NullPointerException("fake NullPointerException");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return super.onKeyDown(keyCode, event);
    }

    private void chooseActivity() {
        // 选择Activitiy
        Intent intent = new Intent();
        intent.setAction("com.cw.firstapp.action.SELFDEFINE");
//        intent.addCategory("com.cw.firstapp.intent.category.SELFDEFINE");
        startActivity(intent);
    }

    /*
        case FAILED_TRANSACTION:
        ALOGE("!!! FAILED BINDER TRANSACTION !!!");
        // TransactionTooLargeException is a checked exception, only throw from certain methods.
        // FIXME: Transaction too large is the most common reason for FAILED_TRANSACTION
        //        but it is not the only one.  The Binder driver can return BR_FAILED_REPLY
        //        for other reasons also, such as if the transaction is malformed or
        //        refers to an FD that has been closed.  We should change the driver
        //        to enable us to distinguish these cases in the future.
        jniThrowException(env, canThrowRemoteException
                ? "android/os/TransactionTooLargeException"
                        : "java/lang/RuntimeException", NULL);
        break;
     */
    // TransactionTooLargeException 文件太大，传不过去
    private void transferBitBitmapFail() {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.big_jpg);
            intent.putExtra("bitmap", bitmap);
            intent.setClassName("com.cw.thirdsystemapp", "com.cw.thirdsystemapp.FullscreenActivity");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 借助putBinder可以突破1M限制  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void transferBitBitmapSuccess() {
        try {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putBinder("bitmap", new ITransferBitmap.Stub() {
                @Override
                public Bitmap getBitmap() throws RemoteException {
                    return BitmapFactory.decodeResource(getResources(), R.drawable.big_jpg);
                }
            });
            intent.putExtras(bundle);
            intent.setClassName("com.cw.thirdsystemapp", "com.cw.thirdsystemapp.FullscreenActivity");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
