package com.example.sanraboss.myfirstandroidapp.Class;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by sanraboss on 11/20/17.
 */

public class FocusView extends View {
    private boolean haveTouch = false;
    private Rect touchArea;
    private Paint paint;

    public FocusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(0xeed7d7d7);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        haveTouch = false;
    }

    public void setHaveTouch(boolean val, Rect rect) {
        haveTouch = val;
        touchArea = rect;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if(haveTouch){
            canvas.drawRect(
                    touchArea.left, touchArea.top, touchArea.right, touchArea.bottom,
                    paint);
        }
    }
}