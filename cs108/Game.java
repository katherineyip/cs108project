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
            if (pageList.get(i).getPageName() == pageName) {
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

    public void addInventory(Shape shape) {
        inventoryShapeList.add(shape);
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

    @Override
    // This allow spinners to display String gameName rather than the object name
    public String toString() {
        return gameName;
    }
}