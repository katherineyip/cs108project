package edu.stanford.cs108;

import android.graphics.Color;
import android.graphics.Paint;

import com.google.android.material.internal.Experimental;

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
    public String shapeScript; //comes in from editor, used to make scriptMap
    public Map<String, Script.actionPairs[]> scriptMap;
    private String shapeID;
    private float x, y;
    private float width, height;

    private Paint rectPaint;
    private Paint textPaint;
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
                 int shapeIDNum,// TODO:will need to add game.nextShapeID when creating shapes
                 String shapeScript,
                 float x, float y,
                 float width, float height){
        this.shapeName = shapeName;
        this.isHidden = isHidden;
        this.isMovable = isMovable;
        this.shapeScript = shapeScript;
        setScript();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.imageName = imageName;
        this.text = text;
        this.fontSize = fontSize;
        this.fontColor = fontColor;
        this.backgroundColor = backgroundColor; ////TODO: allow the user to select their color
        this.shapeID = "s" + Integer.toString(shapeIDNum);

        this.rectPaint = new Paint();
        this.textPaint = new Paint();
    }

    // Public getter methods
    public int getFontSize(){ return fontSize; }

    public String getImageName() {
        return imageName;
    }

    public String getText() {
        return text;
    }

    public String getShapeName() {
        return shapeName;
    }

    public String getShapeID() {
        return shapeID;
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

    public int getTextSize() {
        return fontSize;
    }

    public String getScript() { return shapeScript; }

    public Paint getTextPaint() {
        // defaults from ShapeActivity: fontSize = 40, fontColor = Color.BLACK
        textPaint.setTextSize(fontSize);
        textPaint.setColor(fontColor);
        return textPaint;
    }

    public Paint getRectPaint() {
        // default from ShapeActivity: bgColor = Color.GRAY
        rectPaint.setColor(backgroundColor);
        return rectPaint;
    }



    // Public setter methods
    public void setScript(String s){ 
        shapeScript = s;
        setScript();
    }

    public void setShapeName(String newName) {
        shapeName = newName;
    }

    public void setHiddenState(boolean hiddenState) {
        isHidden = hiddenState;
    }

    public void setMovableState(boolean movableState) {
        isMovable = movableState;
    }

    public void setScript() {
        Script.setShapeScript(this);
        // will need to check scriptMap.isEmpty() before using (if a shape doesn't have an associated script)
    }
    
    public void updateScript(String additionalScript) {
    	shapeScript = Script.combineScripts(shapeScript, additionalScript);
    	setScript();
    	
    }

    public void replaceScript(String newShapeScript) {
        this.shapeScript = newShapeScript;
        setScript();
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
    public boolean hasText() {
        return (this.text != null && !this.text.equals(""));
    }

    public boolean hasImage() {
        return (this.imageName != null && !this.imageName.equals(""));
    }


    public boolean isOverlapping(Shape secondShape) {
        float x1, x2, y1, y2, w1, w2, h1, h2;
        x1 = this.x;
        x2 = secondShape.getX();
        y1 = this.y;
        y2 = secondShape.getY();
        w1 = this.width;
        w2 = secondShape.getWidth();
        h1 = this.height;
        h2 = secondShape.getHeight();

        if ((x2 > x1 && x2 < x1 + w1) || (x1 > x2 && x1 < x2 + w2)) {
            if ((y2 > y1 && y2 < y1 + h1) || (y1 > y2 && y1 < y2 + h2)) {
                return true;
            }
        }
        return false;
    }

    @Override
    // This allow spinners to display String shapeName rather than the object name
    public String toString() {
        return shapeName;
    }
}
