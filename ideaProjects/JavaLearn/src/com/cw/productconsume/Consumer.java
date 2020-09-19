package com.cw.productconsume;

import java.util.concurrent.TimeUnit;

public class Consumer extends Thread {
    private String key;
    private ITaskDone callback;

    public Consumer(String key, ITaskDone callback) {
        this.key = key;
        this.callback = callback;
    }

    @Override
    public void run() {
        //数据库查询
        if (queryDB(key)) {
            System.out.println(key + " Job done");
        }

        callback.onJobDone(key);
    }

    private boolean queryDB(String key) {
        try {
            // for simulate query database cost time
            TimeUnit.MILLISECONDS.sleep(Dispatcher.getRandom(1, 5));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return true;
    }
}
