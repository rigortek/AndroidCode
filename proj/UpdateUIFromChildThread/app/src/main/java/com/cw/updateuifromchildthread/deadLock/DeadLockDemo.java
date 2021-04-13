package com.cw.updateuifromchildthread.deadLock;

import android.util.Log;

import java.util.concurrent.TimeUnit;
import com.cw.updateuifromchildthread.Constant;

/**
 * Create by robin On 21-4-12
 */
public class DeadLockDemo {

    private String mLockOne = "Lock One";
    private String mLockTwo = "Lock Two";

    private static class DeadLockRunnable implements Runnable {
        private String selfLock;
        private String targetLock;

        public DeadLockRunnable(String selfLock, String targetLock) {
            this.selfLock = selfLock;
            this.targetLock = targetLock;
        }

        @Override
        public void run() {
            synchronized (selfLock) {
                Log.d(Constant.TAG, Thread.currentThread().getName() + " owned: " + selfLock + ", try to lock target: " + targetLock);

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (targetLock) {
                    // Unable to run here
                    Log.d(Constant.TAG, Thread.currentThread().getName() + " owned: " + selfLock + ", success to lock target: " + targetLock);
                }
            }
        }
    }

    public void testDeadLock() {
        new Thread(new DeadLockRunnable(mLockOne, mLockTwo), "Thread one").start();
        new Thread(new DeadLockRunnable(mLockTwo, mLockOne), "Thread two").start();
    }

}
