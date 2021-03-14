package edu.stanford.cs108;

import java.util.ArrayList;
import java.util.List;

public class Game {
    String gameName;
    List<Page> pageList;
    List<Shape> inventoryShapeList;
    Page currentPage;
    //boolean isFinished?? // Determine whether a new game should be created
    //TODO: maybe a game state

    public Game(String name) {
        this.gameName = name;
        this.pageList = new ArrayList<>();
        this.inventoryShapeList = new ArrayList<>();

        // Each game must have a starter page
        Page firstPage = new Page("page 1", true);
        addPage(firstPage);
        currentPage = firstPage;
    }

    public List<Page> getPageList() {
        return pageList;
    }

    // Maybe removable
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
        System.out.println("***Added new page" + newPage.getPageName());
    }

    // User can only remove non-starter pages.
    public void removePage(Page page) {
        if (!page.isStarterPage) {
            pageList.remove(page);
        }
    }

    public List<Shape> getInventoryShapeList() {
        return inventoryShapeList;
    }

    public void addInventory(Shape shape) {
        inventoryShapeList.add(shape);
    }

    @Override
    public String toString() {
        return gameName;
    }
}