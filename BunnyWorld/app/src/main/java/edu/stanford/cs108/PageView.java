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

    private BitmapDrawable img;
    private Bitmap toDraw;

    // constants and variables related to the Inventory line
    // how many px above/below the Inventory line do you want to reposition the shape, if the user drags it onto the line?
    private static final int INVENTORY_LINE_BUFFER = 5;
    // how far down the page should the inventory line be?
    private static final float INVENTORY_LINE_POSITION_RATIO = (float)4 / 5;
    private static final int INVENTORY_LINE_STROKE_WIDTH = 5;

    private float lineHeight;
    private final Paint linePaint = new Paint();


    public PageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(INVENTORY_LINE_STROKE_WIDTH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // unfortunately this has to be called here because we need access to the canvas to call getHeight()
        lineHeight = getHeight() * INVENTORY_LINE_POSITION_RATIO;

        canvas.drawLine(0, lineHeight, getWidth(), lineHeight, linePaint);
        drawVisibleShapes(canvas, pageShapeList);
        drawVisibleShapes(canvas, inventoryShapeList); // TODO: for an extension, replace this with a call to drawInventoryShapes() to implement ordered snapping
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

                    // System.out.println("Shape " + shapeInQuestion.getShapeName() + ", from Page, is hidden: " + shapeInQuestion.isHidden());
                    // System.out.println("Shape " + shapeInQuestion.getShapeName() + ", from Page, is clicked: " + shapeInQuestion.isClicked(event.getX(), event.getY()));

                    if (!shapeInQuestion.isHidden() && shapeInQuestion.isClicked(event.getX(), event.getY())) {
                        currentShape = shapeInQuestion;
                        offsetX = event.getX() - currentShape.getX();
                        offsetY = event.getY() - currentShape.getY();

                        System.out.println("Shape " + currentShape.getShapeName() + ", from Page, clicked!");

                        System.out.println("Moved " + currentShape.getShapeName() + " to back of page list!");
                        currentPage.moveShapeToBack(currentShape);
                        System.out.println("Page list is now " + pageShapeList + ".");

                        // TODO: on press, call the On Click trigger
                    }
                }

                for (int i = inventoryShapeList.size() - 1; i >= 0; i--) {
                    Shape shapeInQuestion = inventoryShapeList.get(i);
                    if (!shapeInQuestion.isHidden() && shapeInQuestion.isClicked(event.getX(), event.getY())) {
                        currentShape = shapeInQuestion;
                        offsetX = event.getX() - currentShape.getX();
                        offsetY = event.getY() - currentShape.getY();

                        System.out.println("Shape " + currentShape.getShapeName() + ", from Inventory, clicked!");
                        // TODO: do we process the trigger even if the item's just chillin in Inventory?
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (currentShape != null && currentShape.isMovable()) { // TODO: when implementing EditorPageView, get rid of the isMovable requirement
                    currentShape.setX(event.getX() - offsetX);
                    currentShape.setY(event.getY() - offsetY);

                    // System.out.println("Shape " + currentShape.getShapeName() + " is being moved");
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
                    //          if (this object completes a trigger)
                    //              call the On Drop trigger (also look at the extension this is related to)
                    //          else
                    //              setX and setY the currentShape back to its original position (do this with originalXoriginalY vars),
                    //              re-zero the orignalXY vars, null currentShape, and break;


                    // if shape has been freshly moved from Page to Inventory or vice versa,
                    // update the Game and Page to reflect the changes
                    if (currentShape.getY() > lineHeight && !currentGame.isInventory(currentShape)) { // if shape was moved into inventory
                        currentGame.moveToInventory(currentShape);
                        System.out.println("Moved " + currentShape.getShapeName() + " to Inventory!");
                    } else if (currentShape.getY() < lineHeight && currentGame.isInventory(currentShape)) { // if shape was moved out of inventory
                        System.out.println("Moved " + currentShape.getShapeName() + " out of Inventory!");
                        currentGame.moveToCurrentPage(currentShape);
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
                // TODO: implement drawing green rectangle around items that can receive onDrop() trigger from currentShape
                if (shape.hasText()) { // if it has text, only draw the text as it has priority
                    // TODO: the drawRect line is for debugging purposes only; delete it afterwards... unless we wanna have it be a feature (discuss)
                    canvas.drawRect(shape.getX(), shape.getY(), shape.getX() + shape.getWidth(), shape.getY() + shape.getHeight(), shape.getRectPaint());
                    canvas.drawText(shape.getText(), shape.getX(), shape.getY() + shape.getTextSize(), shape.getTextPaint());
                } else if (shape.hasImage()) { // if it has no text, but has an image, draw the image
                    // TODO: redo this to pull from database

                    if (shape.getImageName().equals("carrot")) {
                        img = (BitmapDrawable) getResources().getDrawable(R.drawable.carrot);
                    } else if (shape.getImageName().equals("carrot2")) {
                        img = (BitmapDrawable) getResources().getDrawable(R.drawable.carrot2);
                    } else if (shape.getImageName().equals("death")) {
                        img = (BitmapDrawable) getResources().getDrawable(R.drawable.death);
                    } else if (shape.getImageName().equals("duck")) {
                        img = (BitmapDrawable) getResources().getDrawable(R.drawable.duck);
                    } else if (shape.getImageName().equals("fire")) {
                        img = (BitmapDrawable) getResources().getDrawable(R.drawable.fire);
                    } else {
                        img = (BitmapDrawable) getResources().getDrawable(R.drawable.mystic);
                    }

                    toDraw = img.getBitmap();
                    canvas.drawBitmap(toDraw, null, new RectF(shape.getX(), shape.getY(),
                            shape.getX() + shape.getWidth(), shape.getY() + shape.getHeight()), null);
                } else {
                    canvas.drawRect(shape.getX(), shape.getY(),
                            shape.getX() + shape.getWidth(), shape.getY() + shape.getHeight(), shape.getRectPaint());
                }
            }
        }
    }

    private void drawInventoryShapes(Canvas canvas) {
        // TODO: extension: draw shapes from Inventory
    }

}