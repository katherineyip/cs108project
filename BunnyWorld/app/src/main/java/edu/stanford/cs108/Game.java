package edu.stanford.cs108;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Game implements Cloneable {
    String id; // unique ID that is used in sharedPrefs as key
    String gameName;
    List<Page> pageList;
    Page starterPage;
    int nextShapeID; //does not subtract for shapes removed so as to avoid duplicates
    int nextPageID; //same^

    Page currentPage;
    List<Shape> inventoryShapeList;
    Shape currentShape = null;
    //boolean isFinished?? // TODO: Determine whether a new game should be created
    //TODO: maybe a game state

    transient Context context;

    public Game(String id, String name) {
        this.id = id;
        this.gameName = name;
        this.pageList = new ArrayList<>();
        this.inventoryShapeList = new ArrayList<>();

        this.nextShapeID = 1;
        this.nextPageID = 1;

        // Each game must have a starter page
        Page firstPage = new Page("page 1", true, nextPageID, null);
        addPage(firstPage);
        currentPage = firstPage;
        starterPage = firstPage;
    }

    public void setCurrentShape(Shape shape){
        this.currentShape = shape;
    }

    public List<Page> getPageList() {
        return pageList;
    }

    public Page getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Page currentPage) {
        this.currentPage = currentPage;
    }

    public void addPage(Page newPage) {
        nextPageID++;
        pageList.add(newPage);
        //currentPage = newPage;
    }

    // User can only remove non-starter pages.
    public void removePage(Page page) {
        if (!page.isStarterPage) {
            pageList.remove(page); // This should also remove the entire shapeList on this page
            currentPage = this.getStarterPage();
        }
        System.out.println("After removing page, number of total page is: " + this.getPageList().size());
    }

    public List<Shape> getInventoryShapeList() {
        return inventoryShapeList;
    }

    public boolean isInventory(Shape shape) {
        return inventoryShapeList.contains(shape);
    }

    // note: Use only on the Editor side!!!
    // TODO: check where this function is called, then delete when all is said and done
    public void addInventory(Shape shape) {
        inventoryShapeList.add(shape);
    }

    // note: you're probably looking for moveToCurrentPage(), which does inventory removal automatically
    // TODO: delete this after it's safe to do so
    public void removeInventory (Shape shape) {
        inventoryShapeList.remove(shape);
    }

    public void moveToInventory (Shape shape) {
        if (currentPage.getShapeList().contains(shape)) {
            currentPage.removeShape(shape);
        } else {
            System.out.println("WARNING: moveToInventory() was called, but shape was not in current page. Where was it moved from??");
        }
        inventoryShapeList.add(shape);
    }

    public void moveToCurrentPage (Shape shape) {
        currentPage.addShape(shape);

        if (inventoryShapeList.contains(shape)) {
            inventoryShapeList.remove(shape);
        } else {
            System.out.println("WARNING: moveToCurrentPage() was called, but shape was not in inventory. Where was it moved from??");
        }
    }

    public Page getStarterPage() {
        for (Page page : pageList) {
            if (page.isStarterPage()) {
                return page;
            }
        }
        return null;
    }

    public void setStarterPage(Page newStarterPage) {
        for (Page page : pageList) {
            if (page.isStarterPage()) {
                page.isStarterPage = false; // Make original start page a non-starter page
            }
        }
        newStarterPage.isStarterPage = true; // Make new page a start page
        starterPage = newStarterPage;
    }

    // Get a count of all shapes for new shape's naming purpose
    // Will have shapes with same name if one in the middle is deleted
    public int getNumShapesInGame() {
        int numShapes = 0;
        for (Page page : this.getPageList()) {
            numShapes += page.getNumShapesOnPage();
        }
        System.out.println("get num shapes in game " + numShapes);
        return numShapes;
    }

    public void setGameName(String newGameName) {
        this.gameName = newGameName;
    }

    public String getGameID() {
        return id;
    }

    public String getGameName() {
        return gameName;
    }

    @Override
    // This allow spinners to display String gameName rather than the object name
    public String toString() {
        return gameName;
    }

    private Shape getShapeFromID(String shapeIDString) {
        for (Shape shape : getCurrentPage().shapeList) {
            if (shape.getShapeID().contentEquals(shapeIDString)) {
                return shape;
            }
        }
        return null;
    }

    public String getShapeIDFromName(String shapeName) {
        for (Page page : pageList) {
            for (Shape shape : page.shapeList) {
                if (shape.getShapeName().equals(shapeName)) {
                    return shape.getShapeID();
                }
            }
        }
        return null;
    }

    public String getPageIDFromName(String pageName) {
        for (Page page : pageList) {
            if (page.getPageName().equals(pageName)) {
                return page.getPageID();
            }
        }
        return null;
    }

    private Page getPageFromID(String pageIDString) {
        for (Page page : pageList) {
            if (page.getPageID().contentEquals(pageIDString)) {
                return page;
            }
        }
        return null;
    }

    private void hide(String shapeString) {
        Shape shape = getShapeFromID(shapeString);
        if (shape != null) {
            shape.setHiddenState(true);
        }
    }

    private void show(String shapeString) {
        Shape shape = getShapeFromID(shapeString);
        if (shape != null) {
            shape.setHiddenState(false);
        }
    }

    private void goTo(String pageString) {
        Page page = getPageFromID(pageString);
        Page oldPage = currentPage;
        if (page != null) {
            setCurrentPage(page);
        }
        if (oldPage != currentPage) {
            onEnter(currentPage.scriptMap);
            for (Shape shape : currentPage.shapeList) {
                onEnter(shape.scriptMap);
            }
        }
    }

    private void play(String sound) { // TODO: make sure sounds are in raw
        String noise = "R.raw.";
        noise += sound;
		//MediaPlayer mp = MediaPlayer.create(getContext(), noise); //TODO: getContext() for mediaplayer
		//mp.start();

    }

    private void performActions(Script.actionPairs[] actions) {
        for (Script.actionPairs act : actions) {
            switch(act.action) {
                case "goto":
                    goTo(act.target);
                    break;
                case "hide":
                    hide(act.target);
                    break;
                case "play":
                    play(act.target);
                    break;
                case "show":
                    show(act.target);
                    break;
            }
        }

    }

    public void onClick(Map<String, Script.actionPairs[]> scriptMap) {
        if (scriptMap.isEmpty()) {
            return;
        }
        for (String key : scriptMap.keySet()) {
            if (key.contentEquals("on click")) {
                performActions(scriptMap.get(key));
            }
        }
    }


    private void onEnter(Map<String, Script.actionPairs[]> scriptMap) {
        if (scriptMap.isEmpty()) {
            return;
        }
        for (String key : scriptMap.keySet()) {
            if (key.contentEquals("on enter")) {
                performActions(scriptMap.get(key));
            }
        }
    }

    public void onDrop(Shape dropped, Map<String, Script.actionPairs[]> scriptMap) {
        if (scriptMap.isEmpty()) {
            return;
        }
        for (String key : scriptMap.keySet()) {
            if (key.contains("on drop") && scriptMap.get(key)[0].target.equals(dropped.getShapeID())) {
                performActions(scriptMap.get(key));
            }
        }
    }

    public List<Shape> validDropTargets(Shape currentShape, List<Shape> potentialTargetShapes) {
        List<Shape> targetShapes = new ArrayList<Shape>(potentialTargetShapes.size());
        for (Shape s : potentialTargetShapes) {
            if (s.scriptMap.isEmpty()) {
                continue;
            }
            for (String key : s.scriptMap.keySet()) {
                if (key.contains("on drop")) {
                    String targetShapeID = s.scriptMap.get(key)[0].target;
                    if (currentShape.getShapeID().equals(targetShapeID)) {
                        targetShapes.add(s);
                    }
                }
            }
        }
        return targetShapes;
    }

    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }


}
