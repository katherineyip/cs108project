package edu.stanford.cs108;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

public class EditorPageView extends View {
        SingletonData singletonData = SingletonData.getInstance();

        Game currentGame = singletonData.getCurrentGame();
        Page currentPage = currentGame.getCurrentPage();
        List<Shape> shapeList = currentPage.getShapeList();
        Shape currentShape = null;


        BitmapDrawable img;
        Bitmap toDraw;
        boolean inInventory;

        // constants and variables related to the Inventory line

        // how many px above/below the Inventory line do you want to reposition the shape, if the user drags it onto the line?
        static final int INVENTORY_LINE_BUFFER = 5;
        // how far down the page should the inventory line be?
        static final float INVENTORY_LINE_POSITION_RATIO = (float)4 / 5;
        final Paint linePaint; // TODO: MEETING TOPIC: why is this "final"?
        float lineHeight;
        Paint textPaint;

        // TODO: remove this line once we can automatically get rectColor from the RectShape object itself.
        Paint rectColor = new Paint(Color.BLACK);




        public EditorPageView(Context context, AttributeSet attrs) {
            super(context, attrs);

            this.linePaint = new Paint(); // TODO: (related to line 32) we can move this into init() if it's not final
            this.textPaint = new Paint();
            init();
        }

        private void init() {
            textPaint.setColor(Color.BLACK);


            linePaint.setColor(Color.BLACK);
            linePaint.setStrokeWidth(5);

            inInventory = false;
            this.lineHeight = 0; // this might not be necessary; discussion topic
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            this.lineHeight = getHeight() * INVENTORY_LINE_POSITION_RATIO;
            canvas.drawLine(0, this.lineHeight, getWidth(), this.lineHeight, linePaint);

            // Sammy's edits

            

            for (Shape shape : currentPage.getShapeList()) {
                // TODO: add if statements for all types of Shape. Alternatively,
                //  have a method in each of the classes so that they can draw themselves.
                //  This might be a better solution because it allows Editor to avoid duplicating code.
                if(validLocation(shape, canvas)) {

                    if (shape instanceof RectShape) {

                        rectColor.setColor(((RectShape) shape).backgroundColor);
                        canvas.drawRect(shape.getX(), shape.getY(),
                                shape.getX() + shape.getWidth(), shape.getY() + shape.getHeight(), rectColor);
                    }
                    if (shape instanceof ImageShape) {

                        if ((((ImageShape) shape).imageName.equals("carrot"))) {
                            img = (BitmapDrawable) getResources().getDrawable(R.drawable.carrot);
                        } else if ((((ImageShape) shape).imageName.equals("carrot2"))) {
                            img = (BitmapDrawable) getResources().getDrawable(R.drawable.carrot2);
                        } else if ((((ImageShape) shape).imageName.equals("death"))) {
                            img = (BitmapDrawable) getResources().getDrawable(R.drawable.death);
                        } else if ((((ImageShape) shape).imageName.equals("duck"))) {
                            img = (BitmapDrawable) getResources().getDrawable(R.drawable.duck);
                        } else if ((((ImageShape) shape).imageName.equals("fire"))) {
                            img = (BitmapDrawable) getResources().getDrawable(R.drawable.fire);
                        } else {
                            img = (BitmapDrawable) getResources().getDrawable(R.drawable.mystic);
                        }

                        toDraw = img.getBitmap();
                        canvas.drawBitmap(toDraw, null, new RectF(shape.getX(), shape.getY(),
                                shape.getX() + shape.getWidth(), shape.getY() + shape.getHeight()), null);
                    }
                    if (shape instanceof TextShape) {
                        textPaint.setTextSize(((TextShape) shape).fontSize);
                        canvas.drawText(((TextShape) shape).text, shape.getX(), shape.getY(), textPaint);
                    }
                }
            }
        }

        // these float variables are only used in this function
        private float offsetX;
        private float offsetY;
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
                    if (currentShape != null) {
                        float shapeY = currentShape.getY();
                        float shapeHeight = currentShape.getHeight();
                        if (lineHeight >= shapeY && lineHeight <= shapeY + shapeHeight) { // shape on inventory line
                            if (lineHeight >= shapeY + shapeHeight / 2) { // shape moreso in page
                                currentShape.setY(lineHeight - shapeHeight - INVENTORY_LINE_BUFFER);
                                // not in inventory
                            } else { // moreso in inventory
                                currentShape.setY(lineHeight + INVENTORY_LINE_BUFFER);
                                // in inventory
                            }
                        }
                        this.inInventory = currentShape.getY() > lineHeight;
                        currentShape = null; // deselect the shape
                    }
                    break;
            }
            invalidate();
            System.out.println(this.inInventory);
            return true;
        }

    public boolean validLocation(Shape s, Canvas canvas){
            int width = canvas.getWidth();
            int height = (int)(canvas.getHeight() * INVENTORY_LINE_POSITION_RATIO);

            //check the bounds of the shape
            if(s.getX() < 0 || s.getX() >= width || s.getY() < 0 ) return false;
            if(s.getX() + s.getWidth() >= width ) return false;

            //if the shape is below the inventory line check if inInventory
            if(s.getY() + s.getHeight() >= height || s.getY() >= height){
                if(s.isInventory){
                    if(s.getY() + s.getHeight() >= canvas.getHeight()) return false;
                    return true;
                } else return false;
            }

            return true;
    }

}

