package edu.stanford.cs108;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class PageView extends View {
    SingletonData singletonData = SingletonData.getInstance();

    Game currentGame = singletonData.getCurrentGame();
    Page currentPage = currentGame.getCurrentPage();
    List<Shape> shapeList = currentPage.getShapeList();
    Shape currentShape = null;


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

        // Sammy's edits

        for (Shape shape : currentPage.getShapeList()) {
            // print the shapes

            // System.out.println(shape);

            if (shape instanceof RectShape) {
                // print the rectangle

                // System.out.println("This shape is a RectShape");
                // System.out.println(shape.getX());
                // System.out.println(shape.getY());

                Paint rectColor = new Paint(Color.BLACK); // TODO: change linePaint to receive color from RectShape
                canvas.drawRect(shape.getX(), shape.getY(),
                        shape.getX()+shape.getWidth(), shape.getY()+shape.getHeight(), rectColor);

            }
        }
    }

    // these float variables are only used in this function
    float offsetX;
    float offsetY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (int i = shapeList.size() - 1; i >= 0; i--) {
                    if (shapeList.get(i).isClicked(event.getX(), event.getY())) {
                        currentShape = shapeList.get(i);
                        offsetX = event.getX() - currentShape.getX();
                        offsetY = event.getY() - currentShape.getY();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (currentShape != null) {
                    currentShape.setX(event.getX() - offsetX);
                    currentShape.setY(event.getY() - offsetY);
                }
                break;
            case MotionEvent.ACTION_UP:
                float shapeY = currentShape.getY();
                float shapeHeight = currentShape.getHeight();
                if (lineHeight >= shapeY && lineHeight <= shapeY + shapeHeight) { // shape on inventory line
                    if (lineHeight >= shapeY + shapeHeight / 2) { // shape moreso in page
                        currentShape.setY(lineHeight - shapeHeight - 5);
                        // not in inventory
                    } else { // moreso in inventory
                        currentShape.setY(lineHeight + 5);
                        // in inventory
                    }
                }
                this.inInventory = y > lineHeight;
                currentShape = null; // deselect the shape
                break;
        }
        invalidate();
        System.out.println(this.inInventory);
        return true;
    }



}