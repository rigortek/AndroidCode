package com.cw.updateuifromchildthread.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Create by robin On 21-3-23
 */
public class Proxy implements InvocationHandler {
    private Object target;  // 真实代理类对象

    public Proxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.printf("Proxy: " + proxy.getClass().getName() + " -> start");

        method.invoke(target, args);

        System.out.printf("Proxy: " + proxy.getClass().getName() + " -> end");
        return null;
    }
}
