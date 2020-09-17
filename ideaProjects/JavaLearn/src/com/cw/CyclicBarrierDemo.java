package com.cw;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class CyclicBarrierDemo {
//    CyclicBarrier与CountDownLatch在计数上恰好相反，CountDownLatch是递减减1，CyclicBarrier是递增加1

    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(10, new Runnable() {
            @Override
            public void run() {
                System.out.printf("All 10 class members come to meeting room, Let's begin meeting\n");
            }
        });

        System.out.printf("Create Read thread start\n");
        for (int i = 0; i < 10; i++) {
            final int index = i;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.MILLISECONDS.sleep(getRandom(5, 15));
                        System.out.printf(index + " class member come to meeting room before await\n");
                        cyclicBarrier.await();
                        System.out.printf(index + " class member come to meeting room after await\n");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.setName("Thread_" + i);
            thread.start();
        }
        System.out.printf("Create Read thread end\n");
    }

    public static int getRandom(int min, int max) {
        return (int) ((Math.random() * (max + 1 - min)) + min);
    }
}
