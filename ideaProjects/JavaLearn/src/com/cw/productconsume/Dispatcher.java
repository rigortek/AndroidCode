package com.cw.productconsume;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// Productor 消息生产者
// Dispatcher 消息派发者
// Consumer 消息消费者
public class Dispatcher implements ITaskDone {
    //线程池
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private int idleCount;

    public static void main(String[] args) {
        Producter producter = new Producter();
        producter.startWork();

        new Dispatcher().setProductor(producter).start();
    }

    Producter producter;
    public Dispatcher setProductor(Producter productor) {
        this.producter = productor;
        return this;
    }

    private void start () {

        while (true) {
            //从 MQ 中获取数据
            String key = producter.fetchFirstMessage();

            if (null != key && !key.isEmpty()) {
                idleCount = 0;
                executor.submit(new Consumer(key, this)) ;
            } else {
                if (++idleCount > 100) {
                    System.out.println("Dispatcher idle for too long time, abort pull message");
                    break;
                }
            }

            try {
                TimeUnit.MILLISECONDS.sleep(getRandom(2, 10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onJobDone(String key) {
        producter.removeMessage(key);
    }

    public static int getRandom(int min, int max) {
        return (int) ((Math.random() * (max + 1 - min)) + min);
    }
}
