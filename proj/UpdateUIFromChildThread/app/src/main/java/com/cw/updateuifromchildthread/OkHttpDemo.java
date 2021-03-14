package com.cw.updateuifromchildthread;

import android.os.Looper;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpDemo {
    private OkHttpClient okHttpClient = new OkHttpClient();

    public String testSyncOkhttp(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            Log.d(Constant.TAG, "testSyncOkhttp: " + response.body().string());
            return response.body().string();
        } catch (IOException e) {
            Log.e(Constant.TAG, "onFailure: " + e.getMessage());
        }

        return null;
    }

    public void testAsyncOkHttp(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(Constant.TAG, "onFailure: " + e.getMessage());

                // notice: 子线程中返回
                Log.d(Constant.TAG, "onFailure: callback at child thread: " + !Thread.currentThread().equals(Looper.getMainLooper()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.d(Constant.TAG, "onResponse: ");
                    return;
                }

                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    Log.d(Constant.TAG, "onResponse: key/values-> " + headers.name(i) + " : " + headers.value(i));
                }

                Log.d(Constant.TAG, "onResponse: " + response.body().string());

                // notice: 子线程中返回
                Log.d(Constant.TAG, "onFailure: callback at child thread: " + !Thread.currentThread().equals(Looper.getMainLooper()));
            }
        });
    }
}
