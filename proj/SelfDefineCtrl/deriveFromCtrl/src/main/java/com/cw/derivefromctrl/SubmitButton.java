package com.cw.derivefromctrl;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/*
* 自定义控件，继承自Android现有控件
 */
public class SubmitButton extends androidx.appcompat.widget.AppCompatButton {

    public SubmitButton(@NonNull Context context) {
        super(context);
        init();
    }

    public SubmitButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SubmitButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackgroundResource(R.drawable.button_selector);
        setText("default text");
        setTextSize(20);
        setTextColor(0xffffffff);
        setAllCaps(false);

        setPadding(20, 5, 20, 5);
    }
}
