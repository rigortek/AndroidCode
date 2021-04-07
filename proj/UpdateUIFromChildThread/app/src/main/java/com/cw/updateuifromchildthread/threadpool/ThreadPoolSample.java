package com.cw.updateuifromchildthread.threadpool;

import android.util.Log;

import com.cw.updateuifromchildthread.Constant;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Create by robin On 21-4-6
 */
public class ThreadPoolSample {
    private static ThreadPoolExecutor mUploadExecutor;
    private static ExecutorService mExecutorService;

    public ThreadPoolSample() {
        if (mUploadExecutor == null) {
//            int corePoolSize = 1;
//            int maxPoolSize = 1;
//            int queueCapacity = 1;
            
            int corePoolSize = 2;
            int maxPoolSize = 2;
            int queueCapacity = 2;
            long keepAliveTime = 60L;
            mUploadExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
                    keepAliveTime, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<Runnable>(queueCapacity),
                    new ThreadPoolExecutor.DiscardOldestPolicy());
        }
        
        if (null == mExecutorService) {
            mExecutorService = Executors.newCachedThreadPool();
        }
    }

    public void execute(Runnable command) {
        mUploadExecutor.execute(command);

//        mExecutorService.submit(command);
    }

    public static final class MyRunnable implements Runnable {
        public int getWorkId() {
            return workId;
        }

        public MyRunnable setWorkId(int workId) {
            this.workId = workId;
            return this;
        }

        private int workId = -1;

        @Override
        public void run() {
            Log.d(Constant.TAG, "start run: " + workId);
            try {
                TimeUnit.SECONDS.sleep(6);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Log.d(Constant.TAG, "end run: " + workId);
        }
    }

}
