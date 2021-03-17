package edu.stanford.cs108;

import java.util.ArrayList;
import java.util.List;

public class Game {
    String gameName;
    List<Page> pageList;
    Page currentPage;
    Page starterPage;

    List<Shape> inventoryShapeList;
    // TODO: keep track of count of all shapes for naming purpose
    //boolean isFinished?? // TODO: Determine whether a new game should be created
    //TODO: maybe a game state

    public Game(String name) {
        this.gameName = name;
        this.pageList = new ArrayList<>();
        this.inventoryShapeList = new ArrayList<>();

        // Each game must have a starter page
        Page firstPage = new Page("page 1", true, null);
        addPage(firstPage);
        currentPage = firstPage;
        starterPage = firstPage;
    }

    public List<Page> getPageList() {
        return pageList;
    }

    // TODO: Prob removable since we'll always have direct acccess to the page object with spinner
    public Page getPage(String pageName) {
        for (int i = 0; i < pageList.size(); i++) {
            if (pageList.get(i).getPageName().equals(pageName)) {
                return pageList.get(i);
            }
        }
        return null;
    }

    public Page getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Page currentPage) {
        this.currentPage = currentPage;
    }

    public void addPage(Page newPage) {
        pageList.add(newPage);
        currentPage = newPage;
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
    public int getNumShapesInGame() {
        int numShapes = 0;
        for (Page page : this.getPageList()) {
            numShapes += page.getNumShapesOnPage();
        }
        System.out.println("get num shapes in game " + numShapes);
        return numShapes;
    }

    @Override
    // This allow spinners to display String gameName rather than the object name
    public String toString() {
        return gameName;
    }
}