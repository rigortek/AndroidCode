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

    public void mapOperator() {
        List<String> parameters = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            parameters.add("i");
        }

        // 目的就是将路径String 转换-> Bitmap
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
}
