package com.cw.step2;

/*
题目：多线程之间按顺序调用，实现A->B->C三个线程启动，要求如下：
AA打印5次，BB打印10次，CC打印15次
紧接着
AA打印5次，BB打印10次，CC打印15次
...
来10轮
 */
public class SyncAndReentrantLockDemo {

    public static void main(String[] args) {
        synchronized_impl();
    }


    // 使用synchronized实现，由于notify的不准确性，难以100%保证时序完全正确。
    static volatile int curLoop = 0;
    static void synchronized_impl() {
        Object lock = new Object();

        int totalLoop = 10;

        // ThreadA
        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    while (curLoop < totalLoop) {
                        try {
                            lock.wait();
                            System.out.print(" A curLoop-> " + curLoop + " / ");
                            if (curLoop >= totalLoop) {
                                lock.notify();
                                break;
                            }
                            for (int i = 0; i < 5; i++) {
                                System.out.printf("AA");
                            }
                            System.out.print(" -> ");
                            lock.notify();
                        } catch(InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                }
                System.out.println("exit threadA");
            }
        });
        threadA.setName("A");
        threadA.start();

        /*************************************/
        // ThreadB
        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    while (curLoop <= totalLoop) {
                        try {
                            lock.wait();
                            System.out.print(" B curLoop-> " + curLoop + " / ");
                            if (curLoop >= totalLoop) {
                                break;
                            }
                            for (int i = 0; i < 10; i++) {
                                System.out.printf("BB");
                            }
                            System.out.print(" -> ");
                            lock.notify();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                System.out.println("exit threadB");
            }
        });
        threadB.setName("B");
        threadB.start();


        /*************************************/
        // ThreadC
        Thread threadC = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    lock.notify();
                    while (curLoop < totalLoop) {
                        try {
                            System.out.print(" C curLoop-> " + curLoop + " / ");
                            lock.wait();
                            for (int i = 0; i < 15; i++) {
                                System.out.printf("CC");
                            }
                            ++curLoop;
                            System.out.println("\n/////////////////////////////////// : " + curLoop);
                            lock.notify();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                System.out.println("exit threadC");
            }
        });
        threadC.setName("C");
        threadC.start();
    };
}


