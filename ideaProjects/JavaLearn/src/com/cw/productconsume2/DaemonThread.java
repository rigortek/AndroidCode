package com.cw.productconsume2;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DaemonThread extends Thread {

    private final Object sync = new Object();
    private volatile boolean mIsRun = true;
    private volatile boolean mIsWorking;

    public DaemonThread(ConcurrentLinkedQueue<CmdRequest> requestQueue) {
        mCacheRequestQueue = requestQueue;
    }


    private ConcurrentLinkedQueue<CmdRequest> mCacheRequestQueue;

    void notifyRun() {
        if (!mIsWorking) {
            synchronized (sync) {
                sync.notify();
            }
        }
    }

    void quit() {
        mIsRun = false;
        if (!mIsWorking) {
            synchronized (sync) {
                sync.notify();
            }
        }
    }

    @Override
    public void run() {
        super.run();
        System.out.println("start thread...");

        while (mIsRun) {
            synchronized (sync) {
                mIsWorking = true;
                try {
                    CmdRequest request = mCacheRequestQueue.poll();
                    if (request == null) {
                        mIsWorking = false;
                        sync.wait();
                        mIsWorking = true;
                    } else {
                        ConsumeModule.action(request);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    mIsWorking = false;
                }
            }
        }

        System.out.println("quit thread...");
    }
}
