package com.cw.derivefromlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

public class LineItemView extends RelativeLayout {
    public LinearLayout rootLinear;
    public TextView tvLeft;
    public TextView tvRight;
    public ImageView ivArrow;

    public LineItemView(Context context) {
        super(context);
        init(context);
    }

    public LineItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

        initAttribute(context, attrs);
    }

    public LineItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

        initAttribute(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LineItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);

        initAttribute(context, attrs);
    }

    private void initAttribute(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LineItemView);

        String leftText = a.getString(R.styleable.LineItemView_leftText);
        String rightText = a.getString(R.styleable.LineItemView_rightText);
        Boolean showArrow = a.getBoolean(R.styleable.LineItemView_showArrow, false);

        setTvLeftText(leftText);
        setTvRightText(rightText);
        setIvArrowVisibility(showArrow);

        a.recycle();

    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.line_item_layout, this);
        rootLinear = findViewById(R.id.rootLinear);
        tvLeft = (TextView) findViewById(R.id.tvLeft);
        tvRight = (TextView) findViewById(R.id.tvRight);
        ivArrow = (ImageView) findViewById(R.id.ivArrow);
    }

    public LineItemView setTvLeftText(String text) {
        if (null != text) {
            tvLeft.setText(text);
        }

        return this;
    }

    public LineItemView setTvRightText(String text) {
        if (null != text) {
            tvRight.setText(text);
        }

        return this;
    }


    public LineItemView setIvArrowVisibility(boolean visibility) {
        ivArrow.setVisibility(visibility ? View.VISIBLE : View.GONE);
        return this;
    }

    public void setOnClickListener(OnClickListener listener) {
        rootLinear.setOnClickListener(listener);
    }

    public void setOnFocusChangeListener(OnFocusChangeListener listener) {
        rootLinear.setOnFocusChangeListener(listener);
    }

    public String getLeftText() {
        return tvLeft.getText().toString();
    }

    public String getRightText() {
        return tvRight.getText().toString();
    }

}
