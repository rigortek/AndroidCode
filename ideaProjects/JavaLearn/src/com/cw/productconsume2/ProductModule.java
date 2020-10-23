package com.cw.productconsume2;

import java.util.concurrent.ConcurrentLinkedQueue;

// ProductModule：生产者
// DaemonThread：消费线程或又称Daemon线程
// ConsumeModule：消费者，即请求的执行者
// 每个动作请求封装在CmdRequest中
// mCacheRequestQueue：请求缓存队列
//
public class ProductModule {
    private static DaemonThread daemonThread;
    private static ConcurrentLinkedQueue<CmdRequest> mCacheRequestQueue;

    public static void main(String[] args) {
        init();

        for (int i = 0; i < 10; i++) {
            CmdRequest cmdRequest = new CmdRequest(i, "CommandName " + i);
            mCacheRequestQueue.add(cmdRequest);
            daemonThread.notifyRun();
        }

    }


    private static void init() {
        if (null == mCacheRequestQueue) {
            mCacheRequestQueue = new ConcurrentLinkedQueue<>();
        }
        if (null == daemonThread) {
            daemonThread = new DaemonThread(mCacheRequestQueue);
        }
        daemonThread.start();
    }

}
