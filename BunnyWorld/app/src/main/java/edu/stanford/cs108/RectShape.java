package edu.stanford.cs108;

import android.graphics.Color;

public class RectShape extends Shape {
    int backgroundColor;

    public RectShape(int backgroundColor,
                     String shapeName,
                     boolean isHidden,
                     boolean isMovable,
                     boolean isInventory,
                     String shapeScript) {
        super(shapeName, isHidden, isMovable, isInventory, shapeScript);
        this.backgroundColor = Color.GRAY;
    }
}