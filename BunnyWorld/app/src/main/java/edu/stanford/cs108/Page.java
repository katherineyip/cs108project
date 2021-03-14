package edu.stanford.cs108;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Page {
    String pageName;
    boolean isStarterPage;
    // TODO: Add methods to draw shapes on Page Activity
    List<Shape> shapeList; // This keeps track of shapes that are NOT in inventory within a page
    public String pageScript; //comes in from editor, used to make scriptMap
    public Map<String, Script.actionPairs[]> scriptMap;

    public Page(String pageName, boolean isStartPage, String pageScript) {
        this.pageName = pageName;
        this.isStarterPage = isStartPage;
        this.shapeList = new ArrayList<>();
        this.pageScript = pageScript;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String newPageNew) {
        pageName = newPageNew;
    }
  
    public boolean isStarterPage() {
        return isStarterPage;
    }

    // TODO: add script to page
    public void setScript() {
    	Script.setPageScript(this);
    	// will need to check scriptMap.isEmpty() before using (if a page doesn't have an associated script)
    }

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

    @Override
    public String toString() {
        return pageName;
    }

    // DEBUGGING
    public int getNumShapesOnPage() {
        return shapeList.size();
    }
}
