package com.cw.step2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

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
//        synchronized_wait_notify_impl();  // 使用synchronized实现

        synchronized_condition_impl();
    }

    static volatile int  waitCondition = 0;
    static ReentrantLock reentrantLock = new ReentrantLock();
    static Condition condition1 = reentrantLock.newCondition();
    static Condition condition2 = reentrantLock.newCondition();
    static Condition condition3 = reentrantLock.newCondition();

    /**
     *
     * 使用ReentrantLock + Condition 实现，可以100%保证时序完全正确。
     */
    static void synchronized_condition_impl() {

        // ThreadA
        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        System.out.println(Thread.currentThread().getName() + " lock");
                        reentrantLock.lock();
                        while (waitCondition != 0) {
                            System.out.println(Thread.currentThread().getName() + " wait");
                            condition1.await();
                        }
                        for (int j = 0; j < 5; j++) {
                            System.out.printf("AA");
                        }
                        System.out.println("");
                        waitCondition = 1;
                        condition2.signal();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        System.out.println(Thread.currentThread().getName() + " unlock");
                        reentrantLock.unlock();
                    }
                }
            }
        });
        threadA.setName("A");
        threadA.start();

        /*************************************/
        // ThreadB
        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        System.out.println(Thread.currentThread().getName() + " lock");
                        reentrantLock.lock();
                        while (waitCondition != 1) {
                            System.out.println(Thread.currentThread().getName() + " wait");
                            condition2.await();
                        }
                        for (int j = 0; j < 5; j++) {
                            System.out.printf("BB");
                        }
                        System.out.println("");
                        waitCondition = 2;
                        condition3.signal();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        System.out.println(Thread.currentThread().getName() + " unlock");
                        reentrantLock.unlock();
                    }
                }
            }
        });
        threadB.setName("B");
        threadB.start();


        /*************************************/
        // ThreadC
        Thread threadC = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        System.out.println(Thread.currentThread().getName() + " lock");
                        reentrantLock.lock();
                        while (waitCondition != 2) {
                            System.out.println(Thread.currentThread().getName() + " wait");
                            condition3.await();
                        }
                        for (int j = 0; j < 5; j++) {
                            System.out.printf("CC");
                        }
                        System.out.println("");
                        waitCondition = 0;
                        condition1.signal();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        System.out.println(Thread.currentThread().getName() + " unlock");
                        reentrantLock.unlock();
                    }
                }
            }
        });
        threadC.setName("C");
        threadC.start();
    }
    
    

    /**
     * 
     * 使用synchronized + wait + notify 实现，由于notify的不准确性，难以100%保证时序完全正确。
     */
    static volatile int curLoop = 0;
    static void synchronized_wait_notify_impl() {
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


