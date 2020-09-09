package com.cw;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MyReadWriteData {
    // protect by ReentrantLock
    public Object getCache(String key) {
        try {
            lock.lock();
            System.out.printf(Thread.currentThread().getName() + " Entry readCache\n");
            TimeUnit.MILLISECONDS.sleep(50);
            return cache.get(key);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.printf(Thread.currentThread().getName() + " Leave readCache\n");
            lock.unlock();
        }

        return null;
    }

    public void setCache(String key, Object value) {
        try {
            lock.lock();
            System.out.printf(Thread.currentThread().getName() + " Entry writeCache\n");
            TimeUnit.MILLISECONDS.sleep(50);
            cache.put(key, value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.printf(Thread.currentThread().getName() + " Leave writeCache\n");
            lock.unlock();
        }
    }
    
    
    // protect by ReentrantReadWriteLock
    public Object getCache2(String key) {
        try {
            readWriteLock.readLock().lock();
            System.out.printf(Thread.currentThread().getName() + " Entry readCache\n");
            TimeUnit.MILLISECONDS.sleep(100);
            return cache.get(key);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.printf(Thread.currentThread().getName() + " Leave readCache\n");
            readWriteLock.readLock().unlock();
        }

        return null;
    }

    public void setCache2(String key, Object value) {
        try {
            readWriteLock.writeLock().lock();
            System.out.printf(Thread.currentThread().getName() + " Entry writeCache\n");
            TimeUnit.MILLISECONDS.sleep(50);
            cache.put(key, value);
            System.out.printf(Thread.currentThread().getName() + " Leave writeCache\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }
    

    private volatile Map<String, Object> cache = new HashMap();

    // 有读写结合的应用场景时，ReentrantReadWriteLock并发效率要高于ReentrantLock，后者是独占的。
    private Lock lock = new ReentrantLock();
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
}
