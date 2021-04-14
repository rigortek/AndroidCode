package com.cw.deepintojava;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class CollectionConcurrent {
    public static void main(String[] args) {
        // 集合类多线程不安全之－List异常修正方法
        // Exception in thread "Thread-8" java.util.ConcurrentModificationException

//        List<String> list = new ArrayList();
//        List<String> list = new Vector<>();  // 线程安全，解决方法一
//        List<String> list = Collections.synchronizedList(new ArrayList<>()); // 线程安全，解决方法二
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>(); // 线程安全，解决方法三， 写时复制

        // 可以看到List,Map, Set均非线程安全。
//        Collections.synchronizedCollection()
//        Collections.synchronizedMap()
//        Collections.synchronizedSet()
//        Collections.synchronizedNavigableMap()
//        Collections.synchronizedNavigableSet()
//        Collections.synchronizedSortedMap()
//        Collections.synchronizedSortedSet()

        for (int i=0; i<20; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    list.add(UUID.randomUUID().toString().substring(0, 10));
                    System.out.println(list);
                }
            }).start();
        }

        // 集合类多线程不安全之－Map异常修正方法
//        Map<Integer, Integer> map = new HashMap();
//        Map<Integer, Integer> map = Collections.synchronizedMap(new HashMap<>());
//        Map<Integer, Integer> map = new ConcurrentHashMap<>();

//        ConcurrentHashMap
    }
}
