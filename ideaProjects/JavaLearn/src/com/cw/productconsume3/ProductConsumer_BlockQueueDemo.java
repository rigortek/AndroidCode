package com.cw.productconsume3;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class MyResource {
    private volatile boolean FLAG = true;  // 默认开启， 进行生产 + 消费
    private AtomicInteger atomicInteger = new AtomicInteger();

    BlockingQueue<String> blockingQueue = null;
    public MyResource(BlockingQueue<String> blockingQueue) {
        this.blockingQueue = blockingQueue;
        System.out.println(blockingQueue.getClass().getName());
    }

    public void product() {
        String data = null;
        boolean retValue = false;

        while (FLAG) {
            data = atomicInteger.incrementAndGet() + "";
            try {
                retValue = blockingQueue.offer(data, 2L, TimeUnit.SECONDS);

                System.out.println(Thread.currentThread().getName() + "生产者插入队列数据：" + data
                        + (retValue ? " success" : " failed"));

                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("停止生产动作");
    }

    public void consumeProduct() {
        String result = null;

        while (FLAG) {
            try {
                result = blockingQueue.poll(2L, TimeUnit.SECONDS);
                if (null == result || result.isEmpty()) {
                    FLAG = false;
                    System.out.println("Fetch timeout, abort");
                    return;
                }
                System.out.println(Thread.currentThread().getName() + "消费 " + result + " success");
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("停止消费动作");
    }

    public void stop() {
        FLAG = false;
    }


}
public class ProductConsumer_BlockQueueDemo {

    public static void main(String[] args) {
        MyResource myResource = new MyResource(new ArrayBlockingQueue<>(10));

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("生产线程启动");
                myResource.product();
            }
        });
        thread.setName("Product thread");
        thread.start();

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("消费线程启动");
                myResource.consumeProduct();
            }
        });
        thread2.setName("Consume thread");
        thread2.start();


        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("下达停止生产+消费命令");
                myResource.stop();
            }
        });
        thread3.setName("Stop thread");
        thread3.start();
    }
}





