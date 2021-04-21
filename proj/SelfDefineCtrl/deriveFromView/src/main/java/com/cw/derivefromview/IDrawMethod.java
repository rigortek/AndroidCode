package com.cw.derivefromview;

/**
 * Create by robin On 21-4-21
 */

import android.graphics.Canvas;
import android.graphics.PointF;

import java.util.List;


public interface IDrawMethod {

    void offerPoints(List<PointF> pointFS);

    void drawPoints(Canvas canvas, List<PointF> pointFS);

}