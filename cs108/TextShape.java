package edu.stanford.cs108;

import android.graphics.Color;

public class TextShape extends Shape {
    String text;
    int fontSize;
    //int fontColor;

    public TextShape(String text,
                     int fontSize,
                     String shapeName,
                     boolean isHidden,
                     boolean isMovable,
                     boolean isInventory,
                     String shapeScript,
                     float x, float y,
                     float width, float height) {
        super(shapeName, isHidden, isMovable, isInventory, shapeScript, x, y, width, height);
        this.text = text;
        this.fontSize = fontSize;
    }

    public void setText(String newText) {
        text = newText;
    }

    public void setFontSize(int newFontSize) {
        fontSize = newFontSize;
    }
}
