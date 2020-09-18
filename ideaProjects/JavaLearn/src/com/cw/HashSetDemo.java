package com.cw;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


// https://docs.oracle.com/javase/8/docs/api/java/util/HashSet.html

// https://fastthread.io/index.jsp

// https://crossoverjie.top/2018/11/08/java-senior/JVM-concurrent-HashSet-problem/

public class HashSetDemo {
    public static void main(String[] args) {
        HashSet<String> hashSet = new HashSet<>();
//        Set<String> hashSet = Collections.synchronizedSet(new HashSet<>());

        for (int i = 0; i < 10; i++) {
            hashSet.add(String.valueOf(i));
        }

        removeElementFun1(hashSet);
        removeElementFun2(hashSet);

        // TODO can web generate ConcurrentModificationException by multi-thread add/remove?
    }

    public static void removeElementFun1(HashSet<String> hashSet) {
        Iterator it = hashSet.iterator();
        String key = null;
        while (it.hasNext()) {
            key = (String) it.next();
            if (key.equals("8")) {
                System.out.println("remove " + key);
                it.remove();  // remove by Iterator is OK
            }
        }
    }

    public static void removeElementFun2(HashSet<String> hashSet) {
        Iterator it = hashSet.iterator();
        String key = null;
        while (it.hasNext()) {
            key = (String) it.next();
            if (key.equals("7")) {
                System.out.println("remove " + key);
                hashSet.remove(key);  // remove by HashSet will leads to "Exception in thread "main" java.util.ConcurrentModificationException"
            }
        }
    }
}
