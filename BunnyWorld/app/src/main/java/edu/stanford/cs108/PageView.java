package edu.stanford.cs108;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class PageView extends View {
    SingletonData singletonData = SingletonData.getInstance();
    final Paint linePaint;
    float lineHeight;


    //    DELETE
    float x, y;
    Paint shapePaint;
    float SQUARE_SIZE = 30.0f;

    boolean inInventory;
//    DELETE

    public PageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.linePaint = new Paint();
        this.shapePaint = new Paint();
        init();
    }

    private void init() {
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(5);

        shapePaint.setColor(Color.rgb(140,21,21));
        inInventory = false;
        this.lineHeight = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.lineHeight = canvas.getHeight() * 4 / 5;
        canvas.drawLine(0, this.lineHeight, canvas.getWidth(), this.lineHeight, linePaint);

        canvas.drawRect(x-SQUARE_SIZE, y-SQUARE_SIZE,
                x+SQUARE_SIZE, y+SQUARE_SIZE, shapePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                x = event.getX();
                y = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                // TODO: account for shape being on boundary line (move to available space)
                this.inInventory = y > lineHeight;
                break;
        }
        invalidate();
        System.out.println(this.inInventory);
        return true;
    }



}