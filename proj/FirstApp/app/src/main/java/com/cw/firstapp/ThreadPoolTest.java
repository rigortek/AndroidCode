package com.cw.firstapp;

import android.util.Log;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTest {


    private static final int CPU_COUNT = 4;  // Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT;
    private static final int KEEP_ALIVE_SECONDS = 60;

    private static final BlockingQueue<Runnable> sPoolWorkQueue =
            new ArrayBlockingQueue<>(2);

    /**
     * An {@link Executor} that can be used to execute tasks in parallel.
     */
    public static final Executor THREAD_POOL_EXECUTOR;


    //  模仿DiscardOldestPolicy实现自己的RejectedExecutionHandler，可以获取被替换的Runnable。
    public static class MyDiscardOldestPolicy implements RejectedExecutionHandler {
        /**
         * Creates a {@code DiscardOldestPolicy} for the given executor.
         */
        public MyDiscardOldestPolicy() { }

        /**
         * Obtains and ignores the next task that the executor
         * would otherwise execute, if one is immediately available,
         * and then retries execution of task r, unless the executor
         * is shut down, in which case task r is instead discarded.
         *
         * @param r the runnable task requested to be executed
         * @param e the executor attempting to execute this task
         */
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            if (r instanceof MyRunnable) {
                Runnable oldRunnable = e.getQueue().peek();  // queue头端即最老的，为被替换的Runnable
                if (oldRunnable instanceof MyRunnable) {
                    MyRunnable myOldRunnable = (MyRunnable) oldRunnable;
                    MyRunnable myNextRunnable = (MyRunnable) r;
                    // 7替换5, 8替换6
                    Log.d(MainActivity.TAG, myNextRunnable.mId + " 替换 " + myOldRunnable.mId);
                }
            }

            if (!e.isShutdown()) {
                e.getQueue().poll();
                e.execute(r);
            }
        }
    }

    static {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
                sPoolWorkQueue, new MyDiscardOldestPolicy());
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        THREAD_POOL_EXECUTOR = threadPoolExecutor;
    }

    class MyRunnable implements Runnable {
        public MyRunnable(int id) {
            mId = id;
        }

        @Override
        public void run() {
            Log.d(MainActivity.TAG, "run: 当前执行的任务id is -> " + mId);

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private int mId;
    }

    public void testSelfDefineThreadPool() {

        for (int i = 0; i < 8; i++) {
            // 执行8个，5和6将会被7和8替换
            THREAD_POOL_EXECUTOR.execute(new MyRunnable(i + 1));
        }

    }
}
