package edu.stanford.cs108;

import java.util.ArrayList;
import java.util.List;

public class Game {
    String gameName;
    List<Page> pageList;
    //TODO: maybe a game state

    public Game(String name) {
        this.gameName = name;
        this.pageList = new ArrayList<>();

        // Each game must have a starter page
        Page firstPage = new Page("page 1", true);
        addPage(firstPage);
    }

    public List<Page> getPageList() {
        return pageList;
    }

    public Page getPage(String pageName) {
        for (int i = 0; i < pageList.size(); i++) {
            if (pageList.get(i).getPageName() == pageName) {
                return pageList.get(i);
            }
        }
        return null;
    }

    public void addPage(Page newPage) {
        pageList.add(newPage);
    }

    public void removePage(Page page) {
        //TODO
    }
}