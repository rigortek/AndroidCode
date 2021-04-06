package com.cw.updateuifromchildthread.threadpool;

import android.util.Log;

import com.cw.updateuifromchildthread.Constant;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Create by robin On 21-4-6
 */
public class ThreadPoolSample {
    private static ThreadPoolExecutor mUploadVoiceExecutor;

    public ThreadPoolSample() {
        if (mUploadVoiceExecutor == null) {
//            int corePoolSize = 1;
//            int maxPoolSize = 1;
//            int queueCapacity = 1;
            int corePoolSize = 2;
            int maxPoolSize = 10;
            int queueCapacity = 3;
            long keepAliveTime = 60L;
            mUploadVoiceExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
                    keepAliveTime, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<Runnable>(queueCapacity),
                    new ThreadPoolExecutor.DiscardOldestPolicy());
        }
    }

    public void execute(Runnable command) {
        mUploadVoiceExecutor.execute(command);
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
