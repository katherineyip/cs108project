package edu.stanford.cs108;

import android.graphics.Color;

public class TextShape extends Shape {
    String text;
    int fontSize;
    //int fontColor;

    public TextShape(String text,
                     int fontSize,
                     String shapeName,
                     String page,
                     boolean isHidden,
                     boolean isMovable,
                     String shapeScript) {
        super(shapeName, page, isHidden, isMovable, shapeScript);
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
