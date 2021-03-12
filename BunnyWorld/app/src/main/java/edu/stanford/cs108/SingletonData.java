package edu.stanford.cs108;

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

    /*
    public void setCurrentPage(Page currentPage) {
        this.currentPage = currentPage;
    }

    public Page getCurrentPage() {
        return currentPage;
    }


    public void addImageShape(Game game, String pageName, ImageShape newImage) {
        game.getPage(pageName).addShape(newImage);
    }
     */
}
