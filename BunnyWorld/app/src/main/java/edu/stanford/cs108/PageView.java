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
    private List<Shape> dropTargets;

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
    private final Paint dropTargetPaint = new Paint();


    public PageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(INVENTORY_LINE_STROKE_WIDTH);

        dropTargetPaint.setColor(Color.GREEN);
        dropTargetPaint.setStrokeWidth(20);
        dropTargetPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        currentGame = singletonData.getCurrentGame();
        currentPage = currentGame.getCurrentPage();
        pageShapeList = currentPage.getShapeList();
        inventoryShapeList = currentGame.getInventoryShapeList();
        System.out.println("---LOG FROM PageView--- current page is: " + currentPage.getPageName());

        // unfortunately this has to be called here because we need access to the canvas to call getHeight()
        lineHeight = getHeight() * INVENTORY_LINE_POSITION_RATIO;

        canvas.drawLine(0, lineHeight, getWidth(), lineHeight, linePaint);
        drawVisibleShapes(canvas, pageShapeList);
        drawVisibleShapes(canvas, inventoryShapeList); // TODO: for an extension, replace this with a call to drawInventoryShapes() to implement ordered snapping

        if (currentShape != null) {
            // if there is currently a shape that is selected
            drawHighlightBorders(canvas);
        }
    }

    // these float variables are only used in this function
    private float offsetX;
    private float offsetY;
    private float originalMouseX;
    private float originalMouseY;
    private float originalShapeX;
    private float originalShapeY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (int i = pageShapeList.size() - 1; i >= 0; i--) {
                    Shape shapeInQuestion = pageShapeList.get(i);

                    // System.out.println("Shape " + shapeInQuestion.getShapeName() + ", from Page, is hidden: " + shapeInQuestion.isHidden());
                    // System.out.println("Shape " + shapeInQuestion.getShapeName() + ", from Page, is clicked: " + shapeInQuestion.isClicked(event.getX(), event.getY()));

                    if (shapeInQuestion != null && !shapeInQuestion.isHidden() && shapeInQuestion.isClicked(event.getX(), event.getY())) {
                        currentShape = shapeInQuestion;
//                        offsetX = event.getX() - currentShape.getX();
//                        offsetY = event.getY() - currentShape.getY();
                        originalMouseX = event.getX();
                        originalMouseY = event.getY();
                        originalShapeX = currentShape.getX();
                        originalShapeY = currentShape.getY();
                        offsetX = originalMouseX - originalShapeX;
                        offsetY = originalMouseY - originalShapeY;

                        System.out.println("Shape " + currentShape.getShapeName() + ", from Page, now currentShape!");

                        // System.out.println("Moved " + currentShape.getShapeName() + " to back of page list!");
                        currentPage.moveShapeToBack(currentShape);
                        System.out.println("Page list is now " + pageShapeList + ".");

                    }
                }

                for (int i = inventoryShapeList.size() - 1; i >= 0; i--) {
                    Shape shapeInQuestion = inventoryShapeList.get(i);
                    if (shapeInQuestion != null && !shapeInQuestion.isHidden() && shapeInQuestion.isClicked(event.getX(), event.getY())) {
                        currentShape = shapeInQuestion;

                        originalMouseX = event.getX();
                        originalMouseY = event.getY();
                        originalShapeX = currentShape.getX();
                        originalShapeY = currentShape.getY();
                        offsetX = originalMouseX - originalShapeX;
                        offsetY = originalMouseY - originalShapeY;

                        System.out.println("Shape " + currentShape.getShapeName() + ", from Inventory, now currentShape!");
                    }
                }

                // get the list of drop targets.
                // TODO: test this functionality by assigning dropTargets to various groups of shapes, instead of going thru this function
                dropTargets = currentGame.validDropTargets(currentShape, pageShapeList);

                break;
            case MotionEvent.ACTION_MOVE:
                if (currentShape != null && currentShape.isMovable()) {
                    currentShape.setX(event.getX() - offsetX);
                    currentShape.setY(event.getY() - offsetY);
                    // System.out.println("Shape " + currentShape.getShapeName() + " is being moved");
                }
                break;
            case MotionEvent.ACTION_UP:
                if (currentShape != null && currentShape.isMovable()) {
                    float shapeY = currentShape.getY();
                    float shapeHeight = currentShape.getHeight();

                    boolean onPage;

                    // if shape is on inventory line, move it to off of the line
                    if (lineHeight >= shapeY && lineHeight <= shapeY + shapeHeight) {
                        if (lineHeight >= shapeY + shapeHeight / 2) { // shape moreso in page
                            currentShape.setY(lineHeight - shapeHeight - INVENTORY_LINE_BUFFER);
                            onPage = true;
                        } else { // moreso in inventory
                            currentShape.setY(lineHeight + INVENTORY_LINE_BUFFER);
                            onPage = false;
                        }
                    } else {
                        if (shapeY < lineHeight) {
                            onPage = true;
                        } else {
                            onPage = false;
                        }
                    }


                    // TODO: this is the OnClick processor; double check to make sure it works
                    //  note: this processes the OnClick trigger even if the Shape is in Inventory
                    //  extension: I can easily add another condition to make this trigger also only operate when out of inventory
                    // for onClick, process the trigger if action up is within 2px of original position
                    if (Math.abs(event.getX() - originalMouseX) <= 1 && Math.abs(event.getY() - originalMouseY) <= 1) {
                        System.out.println("Shape " + currentShape.getShapeName() + " will execute its onClick() trigger");
                        currentGame.onClick(currentShape.scriptMap);
                    } else { // it has been moved, not clicked
                        if (onPage) {
                            for (int i = pageShapeList.size() - 1; i >= 0; i--) {
                                Shape potentialOverlappingShape = pageShapeList.get(i);
                                if (potentialOverlappingShape != null && !potentialOverlappingShape.isHidden()
                                        && currentShape.isOverlapping(potentialOverlappingShape)) {
                                    // i.e. if the current shape was dropped on some other shape,
                                    if (dropTargets.contains(potentialOverlappingShape)) {
                                        // if that other shape is a receiving shape, call the onDrop trigger, and break out of the loop
                                        System.out.println("onDrop() called!! Current shape: " + currentShape.getShapeName() +
                                                ", Drop target shape: " + potentialOverlappingShape.getShapeName());
                                        currentGame.onDrop(currentShape, potentialOverlappingShape.scriptMap);
                                        break;
                                    } else {
                                        // the item was dropped on some random foreign shape; snap it back to its original position
                                        currentShape.setX(originalShapeX);
                                        currentShape.setY(originalShapeY);
                                        break;
                                    }
                                }
                            }
                        }

                        if (onPage && currentGame.isInventory(currentShape)) { // if the shape has been moved to the page but originally was inventory,
                            System.out.println("Moved " + currentShape.getShapeName() + " out of Inventory!");
                            currentGame.moveToCurrentPage(currentShape);
                        }
                        if (!onPage && !currentGame.isInventory(currentShape)) {
                            System.out.println("Moved " + currentShape.getShapeName() + " to Inventory!");
                            currentGame.moveToInventory(currentShape);
                        }
                    }
                }
                currentShape = null; // deselect the shape; if no shape was selected, does nothing
                dropTargets = null; // empty dropTargets list
                break;
        }
        invalidate();
        return true;
    }

    private void drawVisibleShapes(Canvas canvas, List<Shape> list) {
        for (Shape shape : list) {
            if (shape != null && !shape.isHidden()) {
                if (shape.hasText()) { // if it has text, only draw the text as it has priority
                    // TODO: the drawRect line is for debugging purposes only; delete it afterwards... unless we wanna have it be a feature (discuss)
                    canvas.drawRect(shape.getX(), shape.getY(), shape.getX() + shape.getWidth(), shape.getY() + shape.getHeight(), shape.getRectPaint());
                    canvas.drawText(shape.getText(), shape.getX(), shape.getY() + shape.getTextSize(), shape.getTextPaint());
                } else if (shape.hasImage()) { // if it has no text, but has an image, draw the image
                    // TODO: extension: redo this to pull from database

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

    private void drawHighlightBorders(Canvas canvas) {
        for (Shape s : dropTargets) {
            if (s != null) {
                canvas.drawRect(s.getX(), s.getY(), s.getX() + s.getWidth(), s.getY() + s.getHeight(), dropTargetPaint);
            }
        }
        // TODO: extension: different color boxes in different situations
    }

    private void drawInventoryShapes(Canvas canvas) {
        // TODO: extension: draw shapes from Inventory in a neat, organized manner
        //  Also, figure out if there is a way to shrink an item while it is in inventory
    }

}