package com.cw.derivefromlayout;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

public class LineItemView extends RelativeLayout {
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
    }

    public LineItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LineItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.line_item_layout, this);
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

    public LineItemView setTvRightVisibility(boolean visibility) {
        tvRight.setVisibility(visibility ? View.VISIBLE : View.GONE);
        return this;
    }

    public LineItemView setIvArrayVisibility(boolean visibility) {
//        ivArrow.setVisibility(visibility ? View.VISIBLE : View.GONE);
        ivArrow.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
        return this;
    }


}
