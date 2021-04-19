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
public class Olympic5Cycles extends View {

    public Olympic5Cycles(Context context) {
        super(context);
    }

    public Olympic5Cycles(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Olympic5Cycles(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Olympic5Cycles(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
