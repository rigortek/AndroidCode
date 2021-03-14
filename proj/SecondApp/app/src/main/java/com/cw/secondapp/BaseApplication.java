package com.cw.secondapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

public class BaseApplication extends Application {
    // 让所有的Activity执行一些功能的事务，有2种方式：
    // 1. 可以在Activity的生命周期回调函数中ActivityLifecycleCallbacks处理一些共通的事务
    // 2. 也可以让各个Activity继承共通的Activity
    private static ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };

    public BaseApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
