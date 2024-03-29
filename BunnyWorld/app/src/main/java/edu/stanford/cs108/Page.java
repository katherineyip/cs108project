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
    private String pageID;


    public Page(String pageName, boolean isStartPage, int pageIDNum, String pageScript) {
        this.pageName = pageName;
        this.isStarterPage = isStartPage;
        this.shapeList = new ArrayList<>();
        this.pageScript = pageScript;
        setScript();
        this.pageID = "p" + Integer.toString(pageIDNum);
    }

    public String getPageName() {
        return pageName;
    }

    public String getPageID() { return pageID; }

    public void setPageName(String newPageName) {
        pageName = newPageName;
    }
  
    public boolean isStarterPage() {
        return isStarterPage;
    }

    public void setScript(String s){ 
        pageScript = s; 
        setScript();
    }
    
    // TODO: add script to page
    public void setScript() {
    	Script.setPageScript(this);
    	// will need to check scriptMap.isEmpty() before using (if a page doesn't have an associated script)
    }
    
    public void updateScript(String additionalPageScript) {
        this.pageScript = Script.combineScripts(pageScript, additionalPageScript);
        setScript();
    }

    public void replaceScript(String newPageScript) {
        this.pageScript = newPageScript;
        setScript();
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

    public void moveShapeToBack(Shape shape) {
        shapeList.remove(shape);
        shapeList.add(shape);
    }

    @Override
    // This allow spinners to display String pageName rather than the object name
    public String toString() {
        return pageName;
    }

    // DEBUGGING
    public int getNumShapesOnPage() {
        return shapeList.size();
    }
}
