package com.cw;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class Main {

//    使用CountDownLatch在主线程中等待所有线程执行完毕，我们可以发现getCache2+setCache2耗时，明确要比getCache+setCache要少
//    ReentrantLock完全是独占的，无论是读或写操作，而ReentrantReadWriteLock，只有写操作时，才是独占的，而读是并发的，
//    所以在高并发的应用场景下，为了更高的效率，应该使用ReentrantReadWriteLock而不是的使用ReentrantLock或synchronized简单了事
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        System.out.printf(start + ", Start test\n");

        CountDownLatch countDownLatch = new CountDownLatch(20);
        multiThread1(countDownLatch);

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf(System.currentTimeMillis() - start + ", End test\n");


        Map<String, String>  compareMap1 = new LinkedHashMap<>();
        compareMap1.put("1", "2");
        Map<String, String>  compareMap2 = new LinkedHashMap<>();
        compareMap2.put("1", "1");
        compareMap(compareMap1, compareMap2);

//        multiThread2();
    }

    // 此方法即使设置了优先级，还是无法模拟出读写交替出现的并发情况，要么是所有读线程执行完了，再执行写线程；要么是写线程完了，再读线程
    // 而方法multiThread2可以
    private static void multiThread1(CountDownLatch countDownLatch) {
        MyReadWriteData myReadWriteData = new MyReadWriteData();

        System.out.printf("Create Read thread start\n");
        for (int i = 0; i < 10; i++) {
            final int index = i;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    myReadWriteData.getCache("" + index);
//                    myReadWriteData.getCache2("" + index);
                    countDownLatch.countDown();
                }
            });
            thread.setName("Thread_" + i);
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
        }

        System.out.printf("Create Read thread end\n");

        System.out.printf("Create Write thread start\n");
        for (int i = 0; i < 10; i++) {
            final int index = i;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    myReadWriteData.setCache("" + index, index);
//                    myReadWriteData.setCache2("" + index, index);
                    countDownLatch.countDown();
                }
            });
            thread.setName("Thread_" + i);
            thread.setPriority(Thread.MAX_PRIORITY);
            thread.start();
        }
        System.out.printf("Create Write thread end\n");
    }

    // multiThread2方法可以模拟出读写交替出现的并发情况
    private static void multiThread2() {
        MyReadWriteData myReadWriteData = new MyReadWriteData();


        for (int i = 0; i < 20; i++) {
            final int index = i;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (index % 2 == 0) {
                        myReadWriteData.getCache2("" + index);
                        myReadWriteData.setCache2("" + index, index);
                    } else {
                        myReadWriteData.setCache2("" + index, index);
                        myReadWriteData.getCache2("" + index);
                    }
                }
            });
            thread.setName("Thread_" + i);
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
        }
    }

    public static boolean compareMap(Map<String, String> commonMap1,
                                     Map<String, String> commonMap2) {
        boolean updated = false;
        if (commonMap1 != null) {
            updated = null == commonMap2 ? !commonMap1.isEmpty() : !commonMap2.equals(commonMap1);
        } else {
            updated = null != commonMap2 ? !commonMap2.isEmpty() : false;
        }

        return updated;
    }
}
