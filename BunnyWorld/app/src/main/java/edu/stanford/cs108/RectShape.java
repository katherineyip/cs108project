package edu.stanford.cs108;

import android.graphics.Color;

public class RectShape extends Shape {
    int backgroundColor;

    public RectShape(int backgroundColor,
                     String shapeName,
                     //Page page,
                     boolean isHidden,
                     boolean isMovable,
                     String shapeScript) {
        super(shapeName, isHidden, isMovable, shapeScript);
        this.backgroundColor = Color.GRAY;
    }
}