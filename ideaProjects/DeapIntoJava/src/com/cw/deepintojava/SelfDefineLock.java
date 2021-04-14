package com.cw.deepintojava;

import java.util.concurrent.atomic.AtomicReference;

public class SelfDefineLock {
    private AtomicReference<Thread> atomicReference = new AtomicReference<>();

    public boolean lock() {
        Thread thread = Thread.currentThread();
        System.out.printf(thread.getName() + "->lock \n");

        do {
        } while (!atomicReference.compareAndSet(null, thread));

        return true;
    }

    public boolean unLock() {
        Thread thread = Thread.currentThread();
        System.out.printf(thread.getName() + "->unLock \n");

        do {
        } while (!atomicReference.compareAndSet(thread, null));

        return true;
    }

}
