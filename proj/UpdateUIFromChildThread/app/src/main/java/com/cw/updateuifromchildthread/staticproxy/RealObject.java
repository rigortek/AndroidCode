package com.cw.updateuifromchildthread.staticproxy;

import android.util.Log;

/**
 * Create by robin On 21-3-23
 */
public class RealObject extends AbstrctObject {
    public static final String TAG = "jcw";
    @Override
    protected void operation() {
        Log.d(TAG, "operation: me is RealObject");
    }
}
