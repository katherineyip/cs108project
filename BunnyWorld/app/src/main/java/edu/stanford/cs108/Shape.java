package edu.stanford.cs108;

/*
    This is a generic Shape class that define
 */
public class Shape {
    private String shapeName;
    private String page; // change to Page Class later
    private boolean isHidden;
    private boolean isMovable;
    private String shapeScript;  // change to ShapeScript Class later
    private float left, right, top, bottom;
    //show green box when selected
    //each shape should have on onClickListener
    //onMoveListener, and a listener when something drops of itself

    // Constructor
    public Shape(String shapeName,
                 String page,
                 boolean isHidden,
                 boolean isMovable,
                 String shapeScript){
        this.shapeName = shapeName;
        //this.page = page;
        this.isHidden = isHidden;
        this.isMovable = isMovable;
        //this.shapeScript = shapeScript;
    }

    // Public getter methods
    public boolean getHiddenState() {
        return isHidden;
    }

    public boolean getMovableState() {
        return isMovable;
    }

    //public ShapeScript getShapeScript() {
    //    return shapeScript;
    //}

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

    //public void setPage(Page destination) {
    //    page = destination;
    //}

    public void setHiddenState(boolean hiddenState) {
        isHidden = hiddenState;
    }

    public void setMovableState(boolean movableState) {
        isMovable = movableState;
    }

    //public void setShapeScript(ShapeScript newScript) {
    //    shapeScript = newScript;
    //}

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

    // also need to remove the shape entirely

}
