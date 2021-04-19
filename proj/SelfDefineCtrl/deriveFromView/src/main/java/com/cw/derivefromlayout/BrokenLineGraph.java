package com.cw.derivefromlayout;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * Create by robin On 21-4-19
 */
public class BrokenLineGraph extends View {

    public BrokenLineGraph(Context context) {
        super(context);
    }

    public BrokenLineGraph(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BrokenLineGraph(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BrokenLineGraph(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
