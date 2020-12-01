package com.cw.rxandroidsample.observerpattern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Create by robin On 20-11-27
 * 被观察者实现类
 */
public class ConcreteObserverable implements Observerable {
    private List<Observer> observers = Collections.synchronizedList(new ArrayList<>());
    int currentState;

    @Override
    public void registerObserver(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Object object) {

        for (Observer observer:
             observers) {
            switch (currentState) {
                case 0:
                    observer.onNext(object);
                    ++currentState;
                    break;
                case 1:
                    observer.onCompleted();
                    ++currentState;
                    break;
                case 2:
                    observer.onError(new Throwable("dummy"));
                    currentState = 0;
                    break;
                default:
                    break;
            }
        }
    }
}
