package com.cw.updateuifromchildthread.dynamicproxy;

/**
 * Create by robin On 21-3-23
 */
public class Man implements Subject {
    @Override
    public void shopping() {
        System.out.printf("Man shopping...");
    }

    /*
    public static void main(String []args) {
        Man man = new Man();
        Proxy proxy = new Proxy(man);

        Subject subject = (Subject) java.lang.reflect.Proxy.newProxyInstance(man.getClass().getClassLoader(),
                man.getClass().getInterfaces(), proxy);

        subject.shopping();
    }
    */
}
