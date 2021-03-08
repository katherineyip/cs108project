package edu.stanford.cs108;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

// An Editor View has 1:N pages, a couple buttons to add shapes
public class PageView extends View {
    Canvas canvas;
    Page page;

//    DELETE
    float x, y;
    Paint paint;
    float SQUARE_SIZE = 30.0f;
//    DELETE


    public PageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.rgb(140,21,21));

    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(x-SQUARE_SIZE, y-SQUARE_SIZE,
                x+SQUARE_SIZE, y+SQUARE_SIZE, paint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                x = event.getX();
                y = event.getY();
                invalidate();
        }
        return true;
    }
}
