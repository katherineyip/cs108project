package edu.stanford.cs108;

import android.graphics.Color;

public class RectShape extends Shape {
    int backgroundColor;

    public RectShape(int backgroundColor,
                     String shapeName,
                     boolean isHidden,
                     boolean isMovable,
                     boolean isInventory,
                     String shapeScript,
                     float x, float y,
                     float width, float height) {
        super(shapeName, isHidden, isMovable, isInventory, shapeScript, x, y, width, height); // side note: is there a more efficient way of calling the super constructor?
        this.backgroundColor = backgroundColor; //TODO: for this extension, we should allow the user to select their own background color (and add this var to the super constructor on the above line)
    }
}