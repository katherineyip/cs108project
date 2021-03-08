package edu.stanford.cs108;

import java.util.ArrayList;
import java.util.List;

public class Page {
    String pageName;
    boolean isStarterPage;
    List<Shape> shapeList; // consider what data structure makes more sense here

    public Page(String pageName, boolean isStartPage) {
        this.pageName = pageName;
        this.isStarterPage = isStartPage;
        this.shapeList = new ArrayList<Shape>();
    }

    public Page(String pageName, boolean isStartPage, List<Shape> shapeList) {
        this.pageName = pageName;
        this.isStarterPage = isStartPage;
        this.shapeList = shapeList;
    }

    public String getPageName() {
        return pageName;
    }

    public int getNumShapesOnPage() {
        return shapeList.size();
    }

    // should these setter method be on EditorAcitivty only?
    public void setPageName(String newPageNew) {
        pageName = newPageNew;
    }

    public void addShape(Shape newShape) {
        shapeList.add(newShape);
    }
}
