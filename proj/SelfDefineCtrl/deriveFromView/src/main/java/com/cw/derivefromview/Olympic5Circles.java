package com.cw.derivefromview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * Create by robin On 21-4-19
 */
public class Olympic5Circles extends View {

    private int radius;

    private int firstCircleColor;
    private int secondCircleColor;
    private int thirdCircleColor;
    private int fourCircleColor;
    private int fiveCircleColor;

    public Olympic5Circles(Context context) {
        super(context);
    }

    public Olympic5Circles(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initAttribute(context, attrs);
    }

    public Olympic5Circles(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttribute(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Olympic5Circles(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        initAttribute(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float cx = 120;
        float cy = 120;

        // top line 3 cycle
        float horizontalMargin = radius * 2 + radius / 5;
        canvas.drawCircle(cx, cy, radius, createPaint(firstCircleColor));
        canvas.drawCircle(cx + horizontalMargin, cy, radius, createPaint(secondCircleColor));
        canvas.drawCircle(cx + horizontalMargin * 2, cy, radius, createPaint(thirdCircleColor));

        // bottom line 2 cycle
        canvas.drawCircle(cx + radius + radius / 10,cy + radius, radius, createPaint(fourCircleColor));
        canvas.drawCircle(cx + (radius + radius / 10) + horizontalMargin,cy + radius, radius, createPaint(fiveCircleColor));
    }

    private void initAttribute(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Olympic5Circles);

        radius = a.getInt(R.styleable.Olympic5Circles_circleRadius, 50);
        firstCircleColor = a.getColor(R.styleable.Olympic5Circles_firstCircleColor, Color.BLUE);
        secondCircleColor = a.getColor(R.styleable.Olympic5Circles_secondCircleColor, Color.BLACK);
        thirdCircleColor = a.getColor(R.styleable.Olympic5Circles_thirdCircleColor, Color.RED);
        fourCircleColor = a.getColor(R.styleable.Olympic5Circles_fourCircleColor, Color.YELLOW);
        fiveCircleColor = a.getColor(R.styleable.Olympic5Circles_fiveCircleColor, Color.GREEN);

        a.recycle();

    }
    private Paint createPaint(int color){
        Paint paint = new Paint();

        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(6);
        paint.setAntiAlias(true);

        return paint;
    }
}
