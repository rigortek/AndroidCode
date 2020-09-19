package com.cw;

// 使用Semaphore来处理10个人抢5个厕所坑位的行为

// https://github.com/XU-ZHOU/Java

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreDemo {

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(5);

        for (int i = 0; i < 10; i++) {
            final int index = i;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        semaphore.acquire();
                        System.out.printf(Thread.currentThread().getName() + " -> Get toilet position\n");
                        TimeUnit.MILLISECONDS.sleep(CyclicBarrierDemo.getRandom(50, 150));
                        System.out.printf(Thread.currentThread().getName() + " -> Leave toilet position\n");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        semaphore.release();
                    }
                }
            });
            thread.setName("People " + i);
            thread.start();
        }
    }
}
