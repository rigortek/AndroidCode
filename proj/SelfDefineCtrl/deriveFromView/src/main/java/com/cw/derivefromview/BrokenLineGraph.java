package com.cw.derivefromview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by robin On 21-4-19
 */
public class BrokenLineGraph extends View {

    IDrawMethod iDrawMethod = null;
    List<PointF> pointFList = new ArrayList<>();


    private static final int METHOD_CATMULL = 0;
    private static final int METHOD_BEZIER = 1;

    private int algorithm = 0;

    private int xyColor = Color.BLUE;
    private int lineColor = Color.BLACK;

    public BrokenLineGraph(Context context) {
        super(context);
        initDrawMethod();
    }

    public BrokenLineGraph(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttribute(context, attrs);
        initDrawMethod();
    }

    public BrokenLineGraph(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttribute(context, attrs);
        initDrawMethod();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BrokenLineGraph(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttribute(context, attrs);
        initDrawMethod();
    }

    private void initAttribute(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BrokenLineGraph);

        algorithm = a.getInt(R.styleable.BrokenLineGraph_algorithm, 0);
        xyColor = a.getColor(R.styleable.BrokenLineGraph_xyColor, Color.BLUE);
        lineColor = a.getColor(R.styleable.BrokenLineGraph_lineColor, Color.BLACK);

        a.recycle();
    }

    private List<PointF> preparePoints(List<PointF> pointFList) {
        float xPosition = 80;
        float yPosition = 200;
        pointFList.add(new PointF(xPosition, yPosition));

        // first
        float firstPostionStartX = xPosition;
        float firstPostionStartY = yPosition;
        float firstPostionEndX = firstPostionStartX + 20;
        float firstPostionEndY = firstPostionStartY - 50;
        pointFList.add(new PointF(firstPostionEndX, firstPostionEndY));

        // second
        float secondPostionStartX = firstPostionEndX;
        float secondPostionStartY = firstPostionEndY;
        float secondPostionEndX = secondPostionStartX + 40;
        float secondPostionEndY = secondPostionStartY - 30;
        pointFList.add(new PointF(secondPostionEndX, secondPostionEndY));

        // third
        float thirdPostionStartX = secondPostionEndX;
        float thirdPostionStartY = secondPostionEndY;
        float thirdPostionEndX = thirdPostionStartX + 50;
        float thirdPostionEndY = thirdPostionStartY + 60;
        pointFList.add(new PointF(thirdPostionEndX, thirdPostionEndY));

        // four
        float fourPostionStartX = thirdPostionEndX;
        float fourPostionStartY = thirdPostionEndY;
        float fourPostionEndX = fourPostionStartX + 15;
        float fourPostionEndY = fourPostionStartY - 40;
        pointFList.add(new PointF(fourPostionEndX, fourPostionEndY));

        // five
        float fivePostionStartX = fourPostionEndX;
        float fivePostionStartY = fourPostionEndY;
        float fivePostionEndX = fivePostionStartX + 45;
        float fivePostionEndY = fivePostionStartY - 55;
        pointFList.add(new PointF(fivePostionEndX, fivePostionEndY));

        return pointFList;
    }

    private void initDrawMethod() {
        if (METHOD_BEZIER == algorithm) {
            iDrawMethod = new CatmullDrawImpl();
        } else {
            iDrawMethod = new BezierDrawImpl();
        }

        iDrawMethod.supplyPoints(preparePoints(pointFList));
        iDrawMethod.supplylineColor(lineColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawXYLine(canvas);
        iDrawMethod.drawPoints(canvas, pointFList);
    }

    private void drawXYLine(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(xyColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(3);

        float xPosition = 80;
        float yPosition = 200;

        float arrowMargin = 4;
        float arrowLength = 12;

        // x
        canvas.drawLine(xPosition, yPosition - 180, xPosition, yPosition, paint);

        // x left arrow
        canvas.drawLine(xPosition - arrowMargin, yPosition - 180 + arrowLength, xPosition, yPosition - 180, paint);
        // x right arrow
        canvas.drawLine(xPosition + arrowMargin, yPosition - 180 + arrowLength, xPosition, yPosition - 180, paint);

        // y
        canvas.drawLine(xPosition, yPosition, xPosition + 180, yPosition, paint);
        // y top arrow
        canvas.drawLine(xPosition + 180 - arrowLength, yPosition - arrowMargin, xPosition + 180, yPosition, paint);
        // y buttom arrow
        canvas.drawLine(xPosition + 180 - arrowLength, yPosition + arrowMargin, xPosition + 180, yPosition, paint);
    }
}
