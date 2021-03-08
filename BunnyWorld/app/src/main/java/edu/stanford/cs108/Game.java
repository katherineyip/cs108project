package edu.stanford.cs108;

import java.util.ArrayList;
import java.util.List;

public class Game {
    String gameName;
    List<Page> pageList;
    //TODO: maybe a game state

    public Game(String name) {
        this.gameName = name;
        this.pageList = new ArrayList<Page>();
    }

    public Game(String gameName, List<Page> pageList) {
        this.gameName = gameName;
        this.pageList = pageList;
    }
}