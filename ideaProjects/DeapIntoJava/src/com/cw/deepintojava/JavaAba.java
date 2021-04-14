package com.cw.deepintojava;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

public class JavaAba {

    public static void main(String[] args) {
	// write your code here
        // AtomicStampedReference to fix ABA issue sample
        Integer expectedReference = 10;
        int newStamp = (int) System.currentTimeMillis() / 1000;
        AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference(expectedReference, newStamp);

        Thread thread1 = new Thread(
            new Runnable() {
                @Override
                public void run() {
                    int stamp = atomicStampedReference.getStamp();
                    Integer reference = atomicStampedReference.getReference();
                    System.out.println(Thread.currentThread().getName() + ", stamp:" + stamp + ", reference:" + reference);

                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Integer newReference = 100;
                    boolean result = atomicStampedReference.compareAndSet(expectedReference, newReference, newStamp, newStamp + 1);
                    System.out.println(result + ", " + Thread.currentThread().getName() + ", stamp:" + atomicStampedReference.getStamp() + ", reference:" + atomicStampedReference.getReference());
                    result = atomicStampedReference.compareAndSet(newReference, expectedReference, newStamp + 1, newStamp + 2);
                    System.out.println(result + ", " + Thread.currentThread().getName() + ", stamp:" + atomicStampedReference.getStamp() + ", reference:" + atomicStampedReference.getReference());
                }
            }
        );
        thread1.setName("thread1");
        thread1.start();


        Thread thread2 = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(Thread.currentThread().getName() + ", stamp:" + atomicStampedReference.getStamp() + ", reference:" + atomicStampedReference.getReference());

                        try {
                            TimeUnit.SECONDS.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        Integer newReference = 100;
                        boolean result = atomicStampedReference.compareAndSet(expectedReference, newReference, newStamp, newStamp + 3);
                        System.out.println(result + ", " + Thread.currentThread().getName() + ", stamp:" + atomicStampedReference.getStamp() + ", reference:" + atomicStampedReference.getReference());
                    }
                }
        );
        thread2.setName("thread2");
        thread2.start();
    }
}
