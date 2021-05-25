package com.cw.rxandroidsample;


import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

import static com.cw.rxandroidsample.MainActivity.TAG;

// RX2最后一个版本是2.1.1
// https://search.maven.org/artifact/io.reactivex.rxjava2/rxandroid

public class RxJavaSample {

//    创建被观察者方式1
//    返回ObservableCreate
  private Observable observableCreate = Observable.create(new ObservableOnSubscribe<String>() {
        @Override
        public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
            emitter.onNext("first");
            emitter.onNext("second");

            emitter.onComplete();
        }
    });
    //    创建被观察者方式2
    // 返回ObservableFromArray对象
    private Observable observableJust = Observable.just("first", "second");

    //    创建被观察者方式3
    // 返回ObservableFromArray对象
    private Observable observableFrom = Observable.fromArray("first", "first");

    //    创建观察者
    private Observer<Object> observer = new Observer<Object>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {
            Log.d(TAG, "onSubscribe: ");
        }

        @Override
        public void onNext(@NonNull Object o) {
            Log.d(TAG, "onNext: ");
        }

        @Override
        public void onError(@NonNull Throwable e) {
            Log.d(TAG, "onError: ");
        }

        @Override
        public void onComplete() {
            Log.d(TAG, "onComplete: ");
        }
    };

    private Subscriber<String> subscriber = new Subscriber<String>() {
        @Override
        public void onSubscribe(Subscription s) {
            Log.d(TAG, "onSubscribe: ");
        }

        @Override
        public void onNext(String s) {
            Log.d(TAG, "onNext: ");
        }

        @Override
        public void onError(Throwable t) {
            Log.d(TAG, "onError: ");
        }

        @Override
        public void onComplete() {
            Log.d(TAG, "onComplete: ");
        }
    };

    // RX内部会将Observer转换为Subscriber
    public void doRxJava() {
//        observableCreate.subscribe(observer);
//        observableCreate.subscribe((Observer) subscriber);

        observableFrom.subscribe(observer);
    }
}
