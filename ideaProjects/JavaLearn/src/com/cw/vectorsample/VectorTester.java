package com.cw.vectorsample;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

public class VectorTester {

//    static List<String> list = new Vector();
    static Vector<String> list = new Vector();

    public static void main(String[] args) {
        runAddOnMainThread();

        runGetThread();

        runDeleteThread();
    }

    public static void runAddOnMainThread() {
        for (int i = 0; i < 500; i++) {
            list.add(String.valueOf(i));
        }
    }

    public static void runDeleteThread() {
        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                int size = list.size();
                for (int i = size - 1; i >= 0; i--) {
                    try {
                        // sleep用于模拟耗时任务，让出CPU时间片，以让其它线程执行
                        TimeUnit.MILLISECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    list.remove(i);
                    System.out.println("remove: " + i);
                }
            }
        });

        thread1.start();
    }

    public static void runGetThread() {
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                // Vector的单个方法使用synchronized修饰，是多线程安全的，但是组合式复杂使用，即不一定多线程安全，
                // 例如此处size()之后到get()之间，可能其它线程已修改了Vector，自然会导致get异常

                // 对于此类复杂组全调用，必须由调用者自己对Vector再添加synchronized来解决

//                Exception in thread "Thread-0" java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 174
//                at java.util.Vector.get(Vector.java:748)
//                at com.cw.vectorsample.VectorTester$2.run(VectorTester.java:56)
//                at java.lang.Thread.run(Thread.java:745)

//                synchronized (list) {
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    try {  // sleep用于模拟耗时任务，让出CPU时间片，以让其它线程执行
                        TimeUnit.MILLISECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("get: " + list.get(i));
                }
//                }
            }
        });

        thread2.start();
    }
}
