package edu.stanford.cs108;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class SingletonData {
    private static final SingletonData ourInstance = new SingletonData();

    public static SingletonData getInstance() {
        return ourInstance;
    }

    private List<Game> gameList;
    private Game currentGame;

    private SingletonData() {
        gameList = new ArrayList<>();
        currentGame = null;

        Game testGame1 = new Game("test game 1");
        Game testGame2 = new Game("test game 2");
        gameList.add(testGame1);
        gameList.add(testGame2);

        // DUMMY VARS for testing purposes only (added by Sammy and Nikita); remove later
        // TODO: test for different combos of shapes that are hidden, movable, in or out of inventory, etc
        testGame1.getPage("page 1").addShape(new Shape("", "Movable text", 100, Color.BLUE, Color.GREEN, "text1", false, true, false, "", 100, 100, 150, 100));
        testGame1.getPage("page 1").addShape(new Shape("", "Immovable text", 24, Color.BLUE, Color.GREEN, "text2", false, false, false, "", 100, 500, 150, 100));
        testGame1.getPage("page 1").addShape(new Shape("", "", 24, Color.BLACK, Color.BLACK, "box1", false, true, false, "", 100, 700, 150, 100));
        testGame1.getPage("page 1").addShape(new Shape("duck", "", 24, Color.BLACK, Color.BLACK, "ducky", false, true, false, "", 400, 800, 150, 100));

    }

    public List<Game> getGameList() {
        return gameList;
    }

    public void addGameToList(Game newGame) {
        gameList.add(newGame);
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }


//    public void setCurrentPage(Page currentPage) {
//        this.currentPage = currentPage;
//    }
//
//    public Page getCurrentPage() {
//        return currentPage;
//    }
//
//
//    public void addImageShape(Game game, String pageName, ImageShape newImage) {
//        game.getPage(pageName).addShape(newImage);
//    }

}
