package com.cw.rxandroidsample.observerpattern;

/**
 * Create by robin On 20-11-27
 * 被观察者接口类
 */
public interface Observer {
    void onCompleted();
    void onError(java.lang .Throwable e);
    void onNext(Object object);
}
