package com.cw;

import java.util.concurrent.TimeUnit;

public class SynchronizedSample {

    private volatile boolean mFlag = false;
    private Object mObjectFlag = false;


    private void setFlag(boolean value) {
        System.out.println("start thread: " + Thread.currentThread().getName() + ", mFlag: " + mFlag);

        synchronized (mObjectFlag) {

            System.out.println("enter " + Thread.currentThread().getName() + ", mFlag: " + mFlag);
            try {
                TimeUnit.MILLISECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            mFlag = value;

            System.out.println("exit " + Thread.currentThread().getName()+ ", mFlag: " + mFlag + "\r\n");
        }

    }


//    start thread: Thread-0, flag: false
//    enter Thread-0, flag: false
//    start thread: Thread-1, flag: false
//    exit Thread-0, flag: true
//
//    enter Thread-1, flag: true
//    start thread: Thread-2, flag: true
//    enter Thread-2, flag: true
//    exit Thread-1, flag: false
//
//    exit Thread-2, flag: true

    private volatile Boolean flag = false;

    private void setFlagObject(Boolean value) {
        System.out.println("start thread: " + Thread.currentThread().getName() + ", flag: " + flag);

        //  synchronized 参数如果是可变的，且在加锁代码块中修改该参数，则无法保证多线程安全性，所以这里不能使用flag。
//        synchronized(flag) {
        synchronized(mObjectFlag) {
            System.out.println("enter " + Thread.currentThread().getName() + ", flag: " + flag);
            try {
                TimeUnit.MILLISECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            flag = value;

            System.out.println("exit " + Thread.currentThread().getName() + ", flag: " + flag + "\r\n");
        }
    }


    public void testMultiThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                setFlag(true);

                setFlagObject(true);
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
//                setFlag(false);
                setFlagObject(false);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
//                setFlag(false);
                setFlagObject(true);
            }
        }).start();
    }
}
