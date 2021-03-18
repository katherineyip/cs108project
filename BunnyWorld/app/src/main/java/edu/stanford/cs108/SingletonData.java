package edu.stanford.cs108;

import java.util.ArrayList;
import java.util.List;

public class SingletonData {
    private static final SingletonData ourInstance = new SingletonData();

    public static SingletonData getInstance() {
        return ourInstance;
    }

    private List<Game> gameConfigList;
    private List<Game> gameProgressList;
    private Game currentGame;

    private SingletonData() {
        gameConfigList = new ArrayList<>();
        gameProgressList = new ArrayList<>();
        currentGame = null;
        
        // Game testGame1 = new Game("test game 1", "test game 1");
        //Game testGame2 = new Game("test game 2");

        // gameList.add(testGame1);
        //gameList.add(testGame2);

        // DUMMY VARS for testing purposes only (added by Sammy and Nikita); remove later
        // TODO: test for different combos of shapes that are hidden, movable, in or out of inventory, etc
        // testGame1.getStarterPage().addShape(new Shape("", "Movable text", 100, Color.BLUE, Color.GREEN, "text1", false, true, 0, "", 100, 100, 150, 100));
        // testGame1.getStarterPage().addShape(new Shape("", "Immovable text", 24, Color.BLUE, Color.GREEN, "text2", false, false, 1, "", 100, 500, 150, 100));
        // testGame1.getPage("page 1").addShape(new Shape("", "", 24, Color.BLACK, Color.BLACK, "box1", false, true, false, "", 100, 700, 150, 100));
        // testGame1.getPage("page 1").addShape(new Shape("duck", "", 24, Color.BLACK, Color.BLACK, "ducky", false, true, false, "", 400, 800, 150, 100));
    }
    
    public void loadGameConfigsFromDB(List<Game> gameConfigs) {
        this.gameConfigList = gameConfigs;
    }

    public void loadGameProgressListFromDB(List<Game> gameProgressList) {
        this.gameProgressList = gameProgressList;
    }

    public List<Game> getGameConfigList() {
        return gameConfigList;
    }

    public void addGameToList(Game newGame) {
        gameConfigList.add(newGame);
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }
}
