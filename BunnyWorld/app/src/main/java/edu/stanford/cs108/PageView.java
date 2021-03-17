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
    private static final SingletonData singletonData = SingletonData.getInstance();

    private Game currentGame = singletonData.getCurrentGame();

    // TODO: test to ensure that we don't need to reassign currentPage and shapeList -
    //  my (Sammy's) current theory is that we don't, because every touch event calls invalidate(),
    //  after which everything will be redrawn from the (hopefully) current version of currentPage and shapeList.
    private Page currentPage = currentGame.getCurrentPage();
    private List<Shape> pageShapeList = currentPage.getShapeList();
    private List<Shape> inventoryShapeList = currentGame.getInventoryShapeList();
    private Shape currentShape = null;

    // constants and variables related to the Inventory line
    // how many px above/below the Inventory line do you want to reposition the shape, if the user drags it onto the line?
    private static final int INVENTORY_LINE_BUFFER = 5;
    // how far down the page should the inventory line be?
    private static final float INVENTORY_LINE_POSITION_RATIO = (float)4 / 5;

    private float lineHeight;
    private final Paint linePaint = new Paint();


    public PageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // unfortunately this has to be called here because we need access to the canvas to call getHeight()
        lineHeight = getHeight() * INVENTORY_LINE_POSITION_RATIO;

        canvas.drawLine(0, lineHeight, getWidth(), lineHeight, linePaint);
        drawVisibleShapes(canvas, pageShapeList);
        drawVisibleShapes(canvas, inventoryShapeList);
    }

    // these float variables are only used in this function
    private float offsetX;
    private float offsetY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (int i = pageShapeList.size() - 1; i >= 0; i--) {
                    Shape shapeInQuestion = pageShapeList.get(i);
                    if (!shapeInQuestion.isHidden() && shapeInQuestion.isClicked(event.getX(), event.getY())) {
                        currentShape = shapeInQuestion;
                        offsetX = event.getX() - currentShape.getX();
                        offsetY = event.getY() - currentShape.getY();

                        // TODO: on press, call the On Click trigger
                    }
                }

                for (int i = inventoryShapeList.size() - 1; i >= 0; i--) {
                    Shape shapeInQuestion = inventoryShapeList.get(i);
                    if (!shapeInQuestion.isHidden() && shapeInQuestion.isClicked(event.getX(), event.getY())) {
                        currentShape = shapeInQuestion;
                        offsetX = event.getX() - currentShape.getX();
                        offsetY = event.getY() - currentShape.getY();

                        // TODO: do we process the trigger even if the item's just chillin in Inventory?
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (currentShape != null && currentShape.isMovable()) { // TODO: when implementing EditorPageView, get rid of the isMovable requirement
                    currentShape.setX(event.getX() - offsetX);
                    currentShape.setY(event.getY() - offsetY);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (currentShape != null && currentShape.isMovable()) {
                    float shapeY = currentShape.getY();
                    float shapeHeight = currentShape.getHeight();

                    // if shape is on inventory line, move it to off of the line
                    if (lineHeight >= shapeY && lineHeight <= shapeY + shapeHeight) {
                        if (lineHeight >= shapeY + shapeHeight / 2) { // shape moreso in page
                            currentShape.setY(lineHeight - shapeHeight - INVENTORY_LINE_BUFFER);
                            // not in inventory
                        } else { // moreso in inventory
                            currentShape.setY(lineHeight + INVENTORY_LINE_BUFFER);
                            // in inventory
                        }
                    }

                    // TODO: if (the current held shape is on top of another shape)
                    //  call the On Drop trigger (also look at the extension this is related to)

                    // TODO: move object to back of list (remove from list then add to list) - only necessary if it didn't cross the Inventory line


                    // if shape has been freshly moved from Page to Inventory or vice versa,
                    // update the Game and Page to reflect the changes
                    if (currentShape.getY() > lineHeight && !currentGame.isInventory(currentShape)) {
                        currentGame.moveToInventory(currentShape);
                        System.out.println("Moved " + currentShape.getShapeName() + " to Inventory!");
                    } else if (currentShape.getY() < lineHeight) {
                        if (currentGame.isInventory(currentShape)) { // if shape was moved out of inventory
                            System.out.println("Moved " + currentShape.getShapeName() + " out of Inventory!");
                            currentGame.moveToCurrentPage(currentShape);
                        } else { // if item was just shuffled around the Page part of the screen
                            System.out.println("Moved " + currentShape.getShapeName() + " to back of page list!");
                            currentPage.moveShapeToBack(currentShape);
                        }
                    }
                }
                currentShape = null; // deselect the shape; if no shape was selected, does nothing
                break;
        }
        invalidate();
        return true;
    }

    private void drawVisibleShapes(Canvas canvas, List<Shape> list) {
        for (Shape shape : list) {
            if (!shape.isHidden()) {
                if (shape.isTextShape()) {
                    // canvas.drawRect(shape.getX(), shape.getY(), shape.getX() + shape.getWidth(), shape.getY() + shape.getHeight(), rectPaint);
                    canvas.drawText(shape.getText(), shape.getX(), shape.getY(), shape.getTextPaint());
                } else if (shape.isImageShape()) {
                    // TODO: implement using getImageName()
                } else {
                    canvas.drawRect(shape.getX(), shape.getY(),
                            shape.getX() + shape.getWidth(), shape.getY() + shape.getHeight(), shape.getRectPaint());
                }
            }
        }
    }

    // TODO: this method will be used in EditPageView.java
//    private void drawAllShapes(Canvas canvas, List<Shape> list) {
//        for (Shape shape : list) {
//            if (shape instanceof RectShape) {
//                // TODO: change linePaint to receive color from RectShape
//                canvas.drawRect(shape.getX(), shape.getY(),
//                        shape.getX() + shape.getWidth(), shape.getY() + shape.getHeight(), rectPaint);
//            } else if (shape instanceof TextShape) {
//                canvas.drawRect(shape.getX(), shape.getY(),
//                        shape.getX() + shape.getWidth(), shape.getY() + shape.getHeight(), rectPaint);
//                canvas.drawText(((TextShape) shape).text, shape.getX(), shape.getY(), textPaint);
//            } else if (shape instanceof ImageShape) {
//                // TODO: implement
//            }
//        }
//    }
}