package com.cw.derivefromview;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * Create by robin On 21-4-21
 */
        
public class CatmullDrawImpl implements IDrawMethod {

    private Path mAllPath;
    private int color;

    @Override
    public IDrawMethod supplylineColor(int color) {
        this.color = color;
        return this;
    }

    @Override
    public IDrawMethod supplyPoints(List<PointF> pointFList) {
        if (pointFList == null || pointFList.size() < 2) {
            throw new InvalidParameterException("invalid points");
        }
        mAllPath = savePathCatmullRom(pointFList);
        return this;
    }

    @Override
    public void drawPoints(Canvas canvas, List<PointF> pointList) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(3);
        canvas.drawPath(mAllPath, paint);
    }


    private Path savePathCatmullRom(List<PointF> pointFList) {
        if (pointFList == null) {
            return null;
        }
        int length = pointFList.size();
        Path path = new Path();
        path.moveTo(pointFList.get(0).x, pointFList.get(0).y);
        for (int i = 0; i < length-1; i++) {
            for (float u = 0.0f; u < 1.0f; u += 0.001) {
                PointF pointF = interpolatedPosition(
                        pointFList.get(Math.max(0, i-1)),
                        pointFList.get(i),
                        pointFList.get(Math.min(i+1, length-1)),
                        pointFList.get(Math.min(i+2, length-1)),
                        u);
                path.lineTo(pointF.x, pointF.y);
            }
        }
        return path;
    }

    private PointF interpolatedPosition(PointF point0, PointF point1,
                                        PointF point2, PointF point3, float i) {
        float u3 = i * i * i;
        float u2 = i * i;
        float f1 = -0.5f * u3 + u2 - 0.5f * i;
        float f2 = 1.5f * u3 - 2.5f * u2 + 1.0f;
        float f3 = -1.5f * u3 + 2.0f * u2 + 0.5f * i;
        float f4 = 0.5f * u3 - 0.5f * u2;
        float x = point0.x * f1 + point1.x * f2 + point2.x * f3 + point3.x * f4;
        float y = point0.y * f1 + point1.y * f2 + point2.y * f3 + point3.y * f4;
        return new PointF(x, y);
    }
}