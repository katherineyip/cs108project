package edu.stanford.cs108;

import java.util.ArrayList;
import java.util.List;

public class Page {
    String pageName;
    boolean isStarterPage;
    List<Shape> shapeList;

    // Page Constructor
    public Page(String pageName, boolean isStartPage) {
        this.pageName = pageName;
        this.isStarterPage = isStartPage;
        this.shapeList = new ArrayList<>();
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String newPageNew) {
        pageName = newPageNew;
    }

    public int getNumShapesOnPage() {
        return shapeList.size();
    }

    public void addShape(Shape shape) {
        shapeList.add(shape);
    }

    public void addImageShape(ImageShape newImageShape) {
        shapeList.add(newImageShape);
    }

    public void addTextShape(TextShape newTextShape) {
        shapeList.add(newTextShape);
    }

    public void addRectShape(RectShape newRectShape) {
        shapeList.add(newRectShape);
    }

    public void removeShape(Shape shape) {
        shapeList.remove(shape);
    }

    // TODO: Remove? This should be done through Game Class
    public Page getPageObject(String pageName) {
        if (this.pageName == pageName) {
            return this;
        }
        return null;
    }
}
