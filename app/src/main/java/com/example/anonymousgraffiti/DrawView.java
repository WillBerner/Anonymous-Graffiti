package com.example.anonymousgraffiti;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DrawView extends View {

    // tracks current color of line
    private Paint col = new Paint();

    private float width = 1f;
    // Represents the actual lines being drawn
    private ArrayList<Line> lines = new ArrayList<>();
    // Assign x -1 as a messy way to track if a new path is started or not
    private float prevX = -1;
    private float prevY;
    private Bitmap img = null;
    // Records length of each drawn paths to be used when implementing undo
    private ArrayList<Integer> pathLengths = new ArrayList<>();
    private int currLength = 0;

    boolean draw = true;


    public DrawView(Context context) {
        super(context);
        col.setColor(Color.BLACK);
        col.setStrokeWidth(width);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        col.setColor(Color.BLACK);
        col.setStrokeWidth(width);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        col.setColor(Color.BLACK);
        col.setStrokeWidth(width);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DrawView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        col.setColor(Color.BLACK);
        col.setStrokeWidth(width);
    }

    // Used to change color of lines
    public void setColor(int color) {
        col = new Paint();
        col.setColor(color);
        col.setStrokeWidth(width);
    }

    // Supports ability to change line thickness if wanted
    public void setWidth(float rad) {
        width = rad;
        Paint temp = new Paint();
        temp.setColor(col.getColor());
        temp.setStrokeWidth(width);
        col = temp;
    }

    public void setBitmap(Bitmap i) {
        img = i;
    }

    public void addLine(float x, float y) {
        if (prevX != -1) {
            lines.add(new Line(prevX, prevY, x, y, col));
        }
        prevX = x;
        prevY = y;
        currLength++;
    }

    // Buggy, will fix
    public void undo() {
        if (pathLengths.size() != 0) {
            int length = pathLengths.get(pathLengths.size() - 1);
            for (int i = 0; i < length; i++) {
                if (lines.size() == 0) {
                    break;
                }
                lines.remove(lines.size() - 1);
            }
        }
    }

    // Called when user lifts their finger from the screen
    public void actionUp() {
        prevX = -1;
        pathLengths.add(currLength);
        currLength = 0;
    }

    public void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        if (draw) {
            if (img != null) {
                // Fit bitmap to drawView, need to work on making this not device-dependent
                canvas.drawBitmap(img, null, new Rect(0, 0, 375, 450), new Paint());
            }

            // draw all the recorded lines
            for (Line l : lines) {
                canvas.drawLine(l.getStartX(), l.getStartY(), l.getEndX(), l.getEndY(), l.getPaint());
            }

        }

    }






}
