package com.cw.rxandroidsample.observerpattern;

import android.util.Log;

/**
 * Create by robin On 20-11-27
 * 观察者实现类
 */
public class ConcreteObserver implements LtObserver {
    public static final String TAG = "JCW";
    private String name;

    ConcreteObserver(String name) {
        this.name = name;
    }

    @Override
    public void onCompleted() {
        Log.d(TAG, name + " receive onCompleted: ");
    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, name + " receive onError: ");
    }

    @Override
    public void onNext(Object object) {
        Log.d(TAG, name + " receive onNext: ");
    }
}
