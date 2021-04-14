package com.cw;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

public class BlockQueueDemo {

    public static void main(String[] args) {
        BlockingQueue<String> blockingDeque = new ArrayBlockingQueue<String>(3);

        System.out.println(blockingDeque.add("1"));
        System.out.println(blockingDeque.add("2"));
        System.out.println(blockingDeque.add("3"));

        System.out.println(blockingDeque.remove());
        System.out.println(blockingDeque.remove());
        System.out.println(blockingDeque.remove());
        System.out.println(blockingDeque.remove());   // Exception in thread "main" java.util.NoSuchElementException

    }
}
