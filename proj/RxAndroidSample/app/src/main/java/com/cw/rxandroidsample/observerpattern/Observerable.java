package com.cw.rxandroidsample.observerpattern;

/**
 * Create by robin On 20-11-27
 * 被观察者接口类
 */
public interface Observerable {
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(Object t);
}
