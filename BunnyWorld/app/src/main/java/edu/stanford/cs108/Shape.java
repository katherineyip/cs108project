package edu.stanford.cs108;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.Map;

/**
 * This is a generic Shape class that define a shape.
 * There are three subclasses that extend Shape - ImageShape, TextShape, RectShape.
 */
public class Shape {
    private String imageName;
    private String text;
    private int fontSize;
    private int fontColor;
    private int backgroundColor;
    private String shapeName;
    private boolean isHidden;
    private boolean isMovable;
    boolean isInventory; // if not in inventory, this shape will be associated to a particular page // TODO: delete this variable
    public String shapeScript; //comes in from editor, used to make scriptMap
    public Map<String, Script.actionPairs[]> scriptMap;
    private float x, y;
    private float width, height;
    // TODO: show green box when dropping another thing on top
    // TODO: each shape should have on onClickListener
    // TODO: add some method for a shape to draw itself??
    // TODO: bitmap drawing
    //onMoveListener, and a listener when something drops of itself

    // Constructor
    public Shape(String imageName, // use to create Image
                 String text, // use to create Text
                 int fontSize, // use to create Text
                 int fontColor, // use to create Text
                 int backgroundColor, // use to create Rect
                 String shapeName,
                 boolean isHidden,
                 boolean isMovable,
                 boolean isInventory,
                 String shapeScript,
                 float x, float y,
                 float width, float height){
        this.shapeName = shapeName;
        this.isHidden = isHidden;
        this.isMovable = isMovable;
        this.shapeScript = shapeScript;
        this.isInventory = isInventory;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.imageName = imageName;
        this.text = text;
        this.fontSize = fontSize;
        this.fontColor = fontColor;
        this.backgroundColor = backgroundColor; ////TODO: allow the user to select their color
    }

    // Public getter methods

    public String getImageName() {
        return imageName;
    }

    public String getText() {
        return text;
    }

    public String getShapeName() {
        return shapeName;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public boolean isMovable() {
        return isMovable;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Paint getTextPaint() {
        Paint textPaint = new Paint(fontColor);
        if (fontSize != 0) {
            textPaint.setTextSize(fontSize);
        } else {
            textPaint.setTextSize(10);
        }

        return textPaint;
    }

    public Paint getRectPaint() {
        Paint rectPaint;
        if (backgroundColor != 0) {
            rectPaint = new Paint(backgroundColor);
        } else {
            rectPaint = new Paint(Color.LTGRAY);
        }
        return rectPaint;
    }



    // Public setter methods
    public void setShapeName(String newName) {
        shapeName = newName;
    }

    public void setHiddenState(boolean hiddenState) {
        isHidden = hiddenState;
    }

    public void setMovableState(boolean movableState) {
        isMovable = movableState;
    }

    public void setInventoryState(boolean inventoryState) {
        isInventory = inventoryState;
    }

    public void setScript() {
        Script.setShapeScript(this);
        // will need to check scriptMap.isEmpty() before using (if a shape doesn't have an associated script)
    }

    public void setX(float newX) {
        x = newX;
    }

    public void setY(float newY) {
        y = newY;
    }

    public void setHeight(float newHeight) {
        height = newHeight;
    }

    public void setWidth(float newWidth) {
        width = newWidth;
    }

    public boolean isClicked(float clickX, float clickY) {
        return (clickX >= x && clickX <= x+width && clickY >= y && clickY <= y+height);
    }

    public void setImage(String newImageName) {
        imageName = newImageName;
    }

    public void setText(String newText) {
        text = newText;
    }

    public void setFontSize(int newFontSize) {
        fontSize = newFontSize;
    }

    public void setFontColor(int newFontColor) {
        fontColor = newFontColor;
    }

    public void setBackgroundColor(int newColor) {
        backgroundColor = newColor;
    }


    // custom functions for PageView and EditorPageView
    public boolean isTextShape() {
        return (this.text != null || !this.text.equals(""));
    }

    public boolean isImageShape() {
        return (this.imageName != null || !this.text.equals(""));
    }

    @Override
    // This allow spinners to display String shapeName rather than the object name
    public String toString() {
        return shapeName;
    }
}
