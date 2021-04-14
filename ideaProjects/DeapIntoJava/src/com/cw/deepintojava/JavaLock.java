package com.cw.deepintojava;

import com.sun.corba.se.impl.orbutil.concurrent.ReentrantMutex;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class JavaLock {
    public static void main(String[] args) {
//        公平锁，非公平锁
//        递归锁和可重入锁，非递归锁和不可重入锁

//        公平锁：
//        获取不到锁的时候，会自动加入队列，等待释放后，队列的第一个线程获取锁

//        非公平锁:
//        获取不到锁的时候，会自动加入队列，等待线程释放锁后所有等待的线程同时去竞争
//
//        什么是可重入？
//        同一个线程可以反复获取锁多次，然后需要释放多次
        Lock lock = new ReentrantLock();  // 默认.非公平锁,可以重入
        Lock lock1 = new ReentrantLock(true);  // 公平锁

        // synchronized 是非公平锁，可以重入
        Object object = new Object();
        synchronized (object) {
            int test = 0;
            synchronized (object) {
                test = 1;
            }
        }

        testSelfDefineLock();

//        synchronized -> ReentrantLock -> ReentrantReadWriteLock 锁的进阶

//        ReentrantLock reentrantLock = new ReentrantLock();
//        reentrantLock.lock();
//        reentrantLock.unlock();

        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        try {
            reentrantReadWriteLock.readLock().lock();
        } finally {
            reentrantReadWriteLock.readLock().unlock();
        }

        try {
            reentrantReadWriteLock.writeLock().lock();
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }


        ReentrantMutex reentrantMutex = new ReentrantMutex();
        try {
            reentrantMutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            reentrantMutex.release();
        }
    }

    private static void testSelfDefineLock() {
        SelfDefineLock selfDefineLock = new SelfDefineLock();

        new Thread(new Runnable() {
            @Override
            public void run() {
                selfDefineLock.lock();
                try {
                    System.out.printf(Thread.currentThread().getName() + ", now work...\n");
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                selfDefineLock.unLock();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                selfDefineLock.lock();
                try {
                    System.out.printf(Thread.currentThread().getName() + ", now  work...\n");
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                selfDefineLock.unLock();
            }
        }).start();
    }
}
