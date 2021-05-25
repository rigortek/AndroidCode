package com.cw.rxandroidsample.observerpattern;

/**
 * Create by robin On 20-11-27
 * 被观察者接口类
 */
public interface LtObserverable {
    void registerObserver(LtObserver observer);
    void removeObserver(LtObserver observer);
    void notifyObservers(Object t);
}
