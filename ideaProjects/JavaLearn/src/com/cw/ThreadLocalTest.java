package com.cw;

import java.util.concurrent.TimeUnit;

public class ThreadLocalTest {

    private static ThreadLocal<ThreadLocalTest> threadLocal;

    public static void main(String[] args) {

        threadLocal = new ThreadLocal<ThreadLocalTest>();

        for (int i = 0; i < 2; i++) {
            final int index = i;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ThreadLocalTest threadLocalTest = new ThreadLocalTest();
                        System.out.println(Thread.currentThread().getName() + ", set 1: " + threadLocalTest);
                        threadLocal.set(threadLocalTest);

                        threadLocalTest = new ThreadLocalTest();
                        System.out.println(Thread.currentThread().getName() + ", set 2: " + threadLocalTest);
                        threadLocal.set(threadLocalTest);

                        TimeUnit.MILLISECONDS.sleep(100);
                        System.out.println(Thread.currentThread().getName() + ", get 1: " + threadLocal.get());
                        System.out.println(Thread.currentThread().getName() + ", get 2: " + threadLocal.get());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.setName("People " + i);
            thread.start();
        }
    }

}
