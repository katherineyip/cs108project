package edu.stanford.cs108;

/*
    This is a generic Shape class that define a shape.
    There are three subclasses that extend Shape - ImageShape, TextShape, RectShape.
 */
public class Shape {
    private String shapeName;
    private boolean isHidden;
    private boolean isMovable;
    boolean isInventory; // if not in inventory, this shape will be associated to a particular page
    public String shapeScript; //comes in from editor, used to make scriptMap
    public Map<String, Script.actionPairs[]> scriptMap;
    private float left, right, top, bottom; // TODO: get rid of bottom, top and add width height
    //private float width, height;
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
                 String shapeScript){
        this.shapeName = shapeName;
        this.isHidden = isHidden;
        this.isMovable = isMovable;
        this.shapeScript = shapeScript;
        this.isInventory = isInventory;
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

    public float getLeft() {
        return left;
    }

    public float getRight() {
        return right;
    }

    public float getTop() {
        return top;
    }

    public float getBottom() {
        return bottom;
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

    public void setLeft(float newLeft) {
        left = newLeft;
    }

    public void setRight(float newRight) {
        right = newRight;
    }

    public void setTop(float newTop) {
        top = newTop;
    }

    public void setBottom(float newBottom) {
        bottom = newBottom;
    }
}
