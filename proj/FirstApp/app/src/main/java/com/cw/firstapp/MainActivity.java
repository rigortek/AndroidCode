package com.cw.firstapp;

import androidx.annotation.Nullable;
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
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.TransactionTooLargeException;
import android.text.format.Formatter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cw.secondapp.ICallBack;
import com.cw.secondapp.IMessengerService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

//import libcore.io.IoUtils;

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

    public static final int REQUEST_CODE = 1000;

    Button mBtSendBroadcast;
    Button mBtAccessContentProvider;
    Button mBtMiltiCallProvider;
    Button mBtToNextActivity;
    Button mBtSendFileToOtherProcess;
    Button mBtDownloadRedirectUrl;
    Button exception_performance;

    ParcelFileDescriptor[] mFds;

    IMessengerService mIMessengerService;
    boolean mRegisteCallbacked;

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
        Log.d(TAG, "onCreate: MainActivity");

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

                // 伪造通知，自发自收，完全没有问题
                // getContentResolver().notifyChange(Uri.parse("content://businessprovider.authorities/descendant"), null);

                boolean isMainThread = Looper.getMainLooper().equals(Looper.myLooper());
                Log.d(TAG, "onClick: is main thread: " + isMainThread);
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
                        for (int i = 0; i < 5; i++) {
                            boolean isMainThread = Looper.getMainLooper().equals(Looper.myLooper());
                            Log.d(TAG, "onClick: is main thread: " + isMainThread);
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
                        for (int i = 0; i < 5; i++) {
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
                Intent i = new Intent();
                i.setClass(getApplicationContext(), NextActivity.class);
                i.setAction(Intent.ACTION_MAIN);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(i);
                startActivityForResult(i, REQUEST_CODE);

//                chooseActivity();

//                transferBitBitmapFail();
            }
        });

        createPipe();

        mBtSendFileToOtherProcess = (Button) findViewById(R.id.bt_sendfile_to_other_process);
        mBtSendFileToOtherProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (null != mIMessengerService) {
                        if (!mRegisteCallbacked) {
                            ICallBack iCallBack = new ICallBack.Stub() {
                                @Override
                                public void onReceive(String aString) throws RemoteException {
                                    Log.d(TAG, "onReceive: " + aString);
                                    final String tmp = aString;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "onReceive: " + tmp, Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            };

                            mIMessengerService.register(mFds[0], iCallBack);
                            mRegisteCallbacked = true;
                        }
                        // write data to server by pipe
                        writePipe();
                    }

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        mBtDownloadRedirectUrl = (Button) findViewById(R.id.bt_dowload_redirect_url);
        mBtDownloadRedirectUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: start download");
                        String url = "";
                        File dst = new File(getCacheDir().toString() + File.separator + SystemClock.currentThreadTimeMillis() + ".zip");
                        downloadFile(url, dst);
                        Log.d(TAG, "run: end download");
                    }
                }).start();
            }
        });

        exception_performance = findViewById(R.id.exception_performance);
        exception_performance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dumpHeapMemory();
            }
        });
    }


    // maxMemory默认与属性dalvik.vm.heapgrowthlimit一致
    // android:largeHeap="true", 与属性dalvik.vm.heapsize一致
    private void dumpHeapMemory() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = runtime.totalMemory() - freeMemory;

        Log.d(TAG, "onClick: maxMemory: " + Formatter.formatShortFileSize(MainActivity.this, maxMemory));
        Log.d(TAG, "onClick: freeMemory: " + Formatter.formatShortFileSize(MainActivity.this, runtime.freeMemory()));
        Log.d(TAG, "onClick: usedMemory: " + Formatter.formatShortFileSize(MainActivity.this, usedMemory));
    }

    private void createPipe() {
        try {
            mFds = ParcelFileDescriptor.createPipe();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writePipe() {  // write data > 1MB
        Log.d(TAG, ", writePipe: " + mFds[1] + ", " + mFds[1].getFileDescriptor());
        try {
            ParcelFileDescriptor.AutoCloseOutputStream output = new ParcelFileDescriptor.AutoCloseOutputStream(mFds[1]);
//            InputStream inStream = context.getResources().openRawResource(R.raw.big_jpg);
//            output.write(convertStreamToByteArray(inStream));
//            inStream.close();

            String test = "1234567890";
            output.write(test.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] convertStreamToByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[10240];
        int i = Integer.MAX_VALUE;
        while ((i = is.read(buff, 0, buff.length)) > 0) {
            baos.write(buff, 0, i);
        }

        return baos.toByteArray(); // be sure to close InputStream in calling function
    }
    
    

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: MainActivity");

//        try {
//            throw new NullPointerException("fake NullPointerException");
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }

//        try {
//            Thread.sleep(10_000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: MainActivity");
        // verify onStop call stack
        try {
            throw new NullPointerException("call fake exception for print callstack");
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy: MainActivity");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: MainActivity");
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

        try {
            startService(service);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume: MainActivity");

//        ThreadPoolTest threadPoolTest = new ThreadPoolTest();
//        threadPoolTest.testSelfDefineThreadPool();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.d(TAG, "onRestart: MainActivity");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: MainActivity, requestCode:" + requestCode + ", resultCode:" + resultCode);

        if (requestCode == REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    Log.d(TAG, "onActivityResult: operation succeeded");
                    break;
                case RESULT_CANCELED:
                    Log.d(TAG, "onActivityResult: operation canceled");
                    break;
                case RESULT_FIRST_USER:
                    Log.d(TAG, "onActivityResult: user-defined activity results");
                    break;
            }
        }

        try {
            throw new NullPointerException("fake NullPointerException");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "Action: " + event.getAction() + ", onKeyDown keyCode: " + keyCode);
//        try {
//            throw new NullPointerException("fake NullPointerException");
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }

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

    /**
     * Downloads a file from a URL
     *
     * @param fileURL HTTP URL of the file to be downloaded
     * @param file    File of save the file
     * @throws IOException
     */
    public int downloadFile(String fileURL, File file) {
        int result = -1;
        FileOutputStream fileOutput = null;
        InputStream inputStream = null;

        try {
            //set the download URL, a url that points to a file on the internet
            //this is the file to be downloaded
            URL url = new URL(fileURL);

            //create the new connection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(20_000);
            urlConnection.setReadTimeout(20_000);
//            urlConnection.setInstanceFollowRedirects(false);
            int responseCode = urlConnection.getResponseCode();

            // always check HTTP response code first
            if (HttpURLConnection.HTTP_OK == responseCode) {

                //set up some things on the connection
                urlConnection.setRequestMethod("GET");

                //and connect!
                urlConnection.connect();

                //this will be used to write the downloaded data into the file we created
                fileOutput = new FileOutputStream(file);

                //this will be used in reading the data from the internet
                inputStream = urlConnection.getInputStream();

                //this is the total size of the file
                //int totalSize = urlConnection.getContentLength();
                //variable to store total downloaded bytes
                int downloadedSize = 0;

                //create a buffer...
                byte[] buffer = new byte[1024];
                int bufferLength = 0; //used to store a temporary size of the buffer

                //now, read through the input buffer and write the contents to the file
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    //add the data in the buffer to the file in the file output stream (the file on the sd card
                    fileOutput.write(buffer, 0, bufferLength);
                    //add up the size so we know how much is downloaded
                    downloadedSize += bufferLength;
                    //this is where you would do something to report the prgress, like this maybe
                    // updateProgress(downloadedSize, totalSize);
                }

                result = 0;
            } else {
                Log.v(TAG, "error, response code: " + responseCode);
            }
            urlConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.v(TAG, "error, " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.v(TAG, "error, " + e.getMessage());
        } finally {
            //close the output stream when done
            try {
                if (null != fileOutput) {
                    fileOutput.close();
                }
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
