package com.cw.rxandroidsample;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.util.Log;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.cw.rxandroidsample.MainActivity.TAG;

/**
 * Create by robin On 2021/5/25
 */
public class RxJavaOperatorSample {

    // map操作符测试
    public void mapOperator_String2Bitmap(Context context) {
         // 目的就是将路径String > Bitmap
        Observable.just("pic_miniepg.jpg").map(new Function<String, Bitmap>() {
            @Override
            public Bitmap apply(@NotNull String fileName) throws Exception {
                // 生成Bitmap
                AssetManager assetManager = context.getAssets();
                InputStream inputStream = null;
                try {
                    inputStream = assetManager.open(fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return BitmapFactory.decodeStream(inputStream);
            }
        }).subscribe(new Consumer<Bitmap >() {
            // 消费Bitmap
            @Override
            public void accept(Bitmap bitmap) throws Exception {
                Log.d(TAG, "accept: " + bitmap);
            }
        });
    }

    public void mapOperator_StringList2BitmapList(Context context) {
        List<String> parameters = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            parameters.add("pic_miniepg.jpg");
        }

        // 目的就是将路径String List > Bitmap List
        Observable.just(parameters).map(new Function<List<String>, List<Bitmap> >() {
            @Override
            public List<Bitmap> apply(@NotNull List<String> strings) throws Exception {
                // 生成Bitmap
                AssetManager assetManager = context.getAssets();
                List<Bitmap> bitmaps = new ArrayList<>();
                for (int i = 0; i < strings.size(); i++) {
                    InputStream inputStream = null;
                    try {
                        inputStream = assetManager.open(strings.get(i));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    bitmaps.add(i, BitmapFactory.decodeStream(inputStream));  // just for test
                }
                return bitmaps;
            }
        }).subscribe(new Consumer<List<Bitmap> >() {
            // 消费Bitmap
            @Override
            public void accept(List<Bitmap> bitmaps) throws Exception {
                for (int i = 0; i < bitmaps.size(); i++) {
                    Log.d(TAG, i + " accept: " + bitmaps.get(i));
                }
            }
        });
    }

    // flatMap操作符测试
    // map操作符测试
    public void flatMapOperator() {
        // 目的就是将路径String > Bitmap
        Observable.just("https://www.sina.com.cn",
                "https://www.baidu.com").flatMap(new Function<String, Observable<String>>() {
            @Override
            public Observable<String> apply(@NotNull String s) throws Exception {
                Log.d(TAG, "apply: is MainThread false ? -> " + Looper.getMainLooper().equals(Looper.myLooper()));
                TimeUnit.SECONDS.sleep(6);

                return createObservable(s);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "accept: " + s);

                Log.d(TAG, "accept: is MainThread is true? -> " + Looper.getMainLooper().equals(Looper.myLooper()));
            }
        });
    }

    private Observable<String> createObservable(String str) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
                InetAddress address = InetAddress.getByName(str);
                String ip = address.getHostAddress();
                emitter.onNext(ip);

                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());
    }

}
