package edu.stanford.cs108;

import java.util.ArrayList;
import java.util.List;

public class Page {
    String pageName;
    boolean isStarterPage;
    // TODO: Might need a ivar for page script here
    // TODO: Add methods to draw shapes on Page Activity
    List<Shape> shapeList; // This keeps track of shapes that are NOT in inventory within a page

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

    // TODO: add script to page

    // use this list to draw shapes
    public List<Shape> getShapeList() {
        return shapeList;
    }

    public void addShape(Shape shape) {
        shapeList.add(shape);
    }

    public void removeShape(Shape shape) {
        shapeList.remove(shape);
    }

    // DEBUGGING
    public int getNumShapesOnPage() {
        return shapeList.size();
    }
}
