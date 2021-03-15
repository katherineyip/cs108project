package edu.stanford.cs108;

import java.util.Map;

/**
 * This is a generic Shape class that define a shape.
 * There are three subclasses that extend Shape - ImageShape, TextShape, RectShape.
 */
public class Shape {
    private String shapeName;
    private boolean isHidden;
    private boolean isMovable;
    boolean isInventory; // if not in inventory, this shape will be associated to a particular page
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
    public Shape(String shapeName,
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
    }

    // Public getter methods
    public String getShapeName() {
        return shapeName;
    }

    public boolean getHiddenState() {
        return isHidden;
    }

    public boolean getMovableState() {
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

    @Override
    public String toString() {
        return shapeName;
    }
}
