package com.cw.derivefromview;

/**
 * Create by robin On 21-4-21
 */

import android.graphics.Canvas;
import android.graphics.PointF;

import java.util.List;


public interface IDrawMethod {

    IDrawMethod supplyPoints(List<PointF> pointFS);
    IDrawMethod supplylineColor(int color);

    void drawPoints(Canvas canvas, List<PointF> pointFS);

}