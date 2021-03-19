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
public class EditorPageView extends View {
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


    public EditorPageView(Context context, AttributeSet attrs) {
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

        currentGame = singletonData.getCurrentGame();
        currentPage = currentGame.getCurrentPage();
        pageShapeList = currentPage.getShapeList();
        inventoryShapeList = currentGame.getInventoryShapeList();
        System.out.println("---LOG FROM EditorPageView--- current page is: " + currentPage.getPageName());

        // unfortunately this has to be called here because we need access to the canvas to call getHeight()
        lineHeight = getHeight() * INVENTORY_LINE_POSITION_RATIO;

        canvas.drawLine(0, lineHeight, getWidth(), lineHeight, linePaint);
        drawAllShapes(canvas, pageShapeList);
        drawAllShapes(canvas, inventoryShapeList); // TODO: for an extension, replace this with a call to drawInventoryShapes() to implement ordered snapping
    }

    // these float variables are only used in this function
    private float offsetX;
    private float offsetY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                boolean notFound = true;
                for (int i = pageShapeList.size() - 1; i >= 0; i--) {
                    Shape shapeInQuestion = pageShapeList.get(i);

                    // System.out.println("Shape " + shapeInQuestion.getShapeName() + ", from Page, is hidden: " + shapeInQuestion.isHidden());
                    // System.out.println("Shape " + shapeInQuestion.getShapeName() + ", from Page, is clicked: " + shapeInQuestion.isClicked(event.getX(), event.getY()));

                    if (shapeInQuestion.isClicked(event.getX(), event.getY())) {
                        currentShape = shapeInQuestion;
                        notFound = false;
                        currentGame.setCurrentShape(currentShape);
                        offsetX = event.getX() - currentShape.getX();
                        offsetY = event.getY() - currentShape.getY();

                        System.out.println("Shape " + currentShape.getShapeName() + ", from Page, clicked!");

                        System.out.println("Moved " + currentShape.getShapeName() + " to back of page list!");
                        currentPage.moveShapeToBack(currentShape);
                        System.out.println("Page list is now " + pageShapeList + ".");
                    }
                }
                //if(notFound) currentShape = null;
                for (int i = inventoryShapeList.size() - 1; i >= 0; i--) {
                    Shape shapeInQuestion = inventoryShapeList.get(i);
                    if (shapeInQuestion.isClicked(event.getX(), event.getY())) {
                        currentShape = shapeInQuestion;
                        currentGame.setCurrentShape(currentShape);
                        offsetX = event.getX() - currentShape.getX();
                        offsetY = event.getY() - currentShape.getY();

                        System.out.println("here");
                        System.out.println("Shape " + currentShape.getShapeName() + ", from Inventory, clicked!");
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (currentShape != null) {
                    currentShape.setX(event.getX() - offsetX);
                    currentShape.setY(event.getY() - offsetY);

                    // System.out.println("Shape " + currentShape.getShapeName() + " is being moved");
                }
                break;
            case MotionEvent.ACTION_UP:
                if (currentShape != null) {
                    float shapeY = currentShape.getY();
                    float shapeHeight = currentShape.getHeight();

                    // if shape is on inventory line, move it to off of the line
                    if (lineHeight >= shapeY && lineHeight <= shapeY + shapeHeight) {
                        if (lineHeight >= shapeY + shapeHeight / 2) { // shape moreso in page
                            currentShape.setY(lineHeight - shapeHeight - INVENTORY_LINE_BUFFER);
                            // visually moved out of inventory
                        } else { // moreso in inventory
                            currentShape.setY(lineHeight + INVENTORY_LINE_BUFFER);
                            // visually moved into inventory
                        }
                    }

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


    private void drawInventoryShapes(Canvas canvas) {
        // draw shapes from Inventory
    }

    private void drawAllShapes(Canvas canvas, List<Shape> list) {
        for (Shape shape : list) {
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

    // TODO: this has been edited and moved to somewhere else in Jacob's code; find where he moved it to, and provide him with the functions he needs
    public boolean validLocation(Shape s, Canvas canvas){
        int width = canvas.getWidth();
        int height = (int)(canvas.getHeight() * INVENTORY_LINE_POSITION_RATIO);

        //check the bounds of the shape
        if(s.getX() < 0 || s.getX() >= width || s.getY() < 0 ) return false;
        if(s.getX() + s.getWidth() >= width ) return false;

        return true;
    }
}







