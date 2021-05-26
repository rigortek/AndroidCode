package com.cw.rxandroidsample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static com.cw.rxandroidsample.MainActivity.TAG;

/**
 * Create by robin On 2021/5/25
 */
public class RxJavaOperatorSample {

    // map操作符测试
    public void mapOperator_String2Bitmap() {
         // 目的就是将路径String > Bitmap
        Observable.just("raw/test.png").map(new Function<String, Bitmap>() {
            @Override
            public Bitmap apply(@NotNull String strings) throws Exception {
                // 生成Bitmap
                return BitmapFactory.decodeFile(strings);  // just for test
            }
        }).subscribe(new Consumer<Bitmap >() {
            // 消费Bitmap
            @Override
            public void accept(Bitmap bitmap) throws Exception {
                Log.d(TAG, "accept: " + bitmap);
            }
        });
    }

    public void mapOperator_StringList2BitmapList() {
        List<String> parameters = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            parameters.add("i");
        }

        // 目的就是将路径String List > Bitmap List
        Observable.just(parameters).map(new Function<List<String>, List<Bitmap> >() {
            @Override
            public List<Bitmap> apply(@NotNull List<String> strings) throws Exception {
                // 生成Bitmap
                List<Bitmap> bitmaps = new ArrayList<>();
                for (int i = 0; i < strings.size(); i++) {
                    bitmaps.add(i, BitmapFactory.decodeFile(String.valueOf(i)));  // just for test
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
                "https://www.baidu.com",
                "https://www.sohu.com").flatMap(new Function<String, Observable<String>>() {
            @Override
            public Observable<String> apply(@NotNull String s) throws Exception {
                return createObservable(s);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "accept: " + s);
            }
        });
    }

    private Observable createObservable(String str) {
        return null;
    }

}
