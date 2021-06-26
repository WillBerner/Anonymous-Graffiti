package com.example.anonymousgraffiti;

import android.graphics.Paint;

public class Line {
    private float startX;
    private float startY;
    private float endX;
    private float endY;
    private Paint paint;

    public Line(float sx, float sy, float ex, float ey, Paint p) {
        startX = sx;
        startY = sy;
        endX = ex;
        endY = ey;
        paint = p;
    }

    public float getStartX() {
        return startX;
    }

    public float getStartY() {
        return startY;
    }

    public float getEndX() {
        return endX;
    }

    public float getEndY() {
        return endY;
    }

    public Paint getPaint() {
        return paint;
    }
}
