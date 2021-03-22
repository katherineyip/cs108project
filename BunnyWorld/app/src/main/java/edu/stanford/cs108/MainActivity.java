package edu.stanford.cs108;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import com.google.gson.Gson;
import android.content.SharedPreferences;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//implements AdapterView
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    // Data
    Game selectedGame;
    SingletonData singletonData = SingletonData.getInstance();

    // DB - Game Config Files
    SharedPreferences gameConfigSharedPref;
    static final String GAME_CONFIG_SHARED_PREF_FILE = "TempGamePrefs";
    List<Game> gameConfigListFromDB;

    // DB - Game Progress Files
    SharedPreferences gameProgressSharedPref;
    static final String GAME_PROGRESS_SHARED_PREF_FILE = "TempGameProgressPrefs";
    List<Game> gameProgressListFromDB;

    // UI
    Spinner spinnerGameNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get all games from sharedPref file
        gameConfigSharedPref = getSharedPreferences(GAME_CONFIG_SHARED_PREF_FILE, MODE_PRIVATE);
        // for clearing test data
//         SharedPreferences.Editor sharedPrefEditor = gameConfigSharedPref.edit();
//         sharedPrefEditor.clear();
//         sharedPrefEditor.apply();
        gameConfigListFromDB = new ArrayList<>();
        loadGameConfigsFromSharedPrefs();
        if (gameConfigListFromDB == null) {
            SharedPreferences.Editor sharedPrefEditor = gameConfigSharedPref.edit();
            String defaultGame = "{&quot;currentPage&quot;:{&quot;isStarterPage&quot;:true,&quot;pageID&quot;:&quot;p1&quot;,&quot;pageName&quot;:&quot;page 1&quot;,&quot;scriptMap&quot;:{},&quot;shapeList&quot;:[{&quot;backgroundColor&quot;:-7829368,&quot;fontColor&quot;:-7829368,&quot;fontSize&quot;:40,&quot;height&quot;:100.0,&quot;imageName&quot;:&quot;&quot;,&quot;isHidden&quot;:false,&quot;isMovable&quot;:false,&quot;rectPaint&quot;:{&quot;mNativePaint&quot;:3074477408},&quot;scriptMap&quot;:{&quot;on click&quot;:[{&quot;action&quot;:&quot;goto&quot;,&quot;target&quot;:&quot;p2&quot;}]},&quot;shapeID&quot;:&quot;s1&quot;,&quot;shapeName&quot;:&quot;door 1&quot;,&quot;shapeScript&quot;:&quot;on click goto p2&quot;,&quot;text&quot;:&quot;&quot;,&quot;textPaint&quot;:{&quot;mNativePaint&quot;:3074479280},&quot;width&quot;:100.0,&quot;x&quot;:100.0,&quot;y&quot;:1100.0},{&quot;backgroundColor&quot;:-7829368,&quot;fontColor&quot;:-7829368,&quot;fontSize&quot;:40,&quot;height&quot;:100.0,&quot;imageName&quot;:&quot;&quot;,&quot;isHidden&quot;:true,&quot;isMovable&quot;:false,&quot;rectPaint&quot;:{&quot;mNativePaint&quot;:3093453296},&quot;scriptMap&quot;:{&quot;on click&quot;:[{&quot;action&quot;:&quot;goto&quot;,&quot;target&quot;:&quot;p3&quot;}]},&quot;shapeID&quot;:&quot;s2&quot;,&quot;shapeName&quot;:&quot;door 2&quot;,&quot;shapeScript&quot;:&quot;on click goto p3&quot;,&quot;text&quot;:&quot;&quot;,&quot;textPaint&quot;:{&quot;mNativePaint&quot;:3093457616},&quot;width&quot;:100.0,&quot;x&quot;:500.0,&quot;y&quot;:1100.0},{&quot;backgroundColor&quot;:-7829368,&quot;fontColor&quot;:-7829368,&quot;fontSize&quot;:40,&quot;height&quot;:100.0,&quot;imageName&quot;:&quot;&quot;,&quot;isHidden&quot;:false,&quot;isMovable&quot;:false,&quot;rectPaint&quot;:{&quot;mNativePaint&quot;:3074596640},&quot;scriptMap&quot;:{&quot;on click&quot;:[{&quot;action&quot;:&quot;goto&quot;,&quot;target&quot;:&quot;p4&quot;}]},&quot;shapeID&quot;:&quot;s3&quot;,&quot;shapeName&quot;:&quot;door 3&quot;,&quot;shapeScript&quot;:&quot;on click goto p4&quot;,&quot;text&quot;:&quot;&quot;,&quot;textPaint&quot;:{&quot;mNativePaint&quot;:3074596928},&quot;width&quot;:100.0,&quot;x&quot;:900.0,&quot;y&quot;:1100.0},{&quot;backgroundColor&quot;:-7829368,&quot;fontColor&quot;:-7829368,&quot;fontSize&quot;:40,&quot;height&quot;:100.0,&quot;imageName&quot;:&quot;&quot;,&quot;isHidden&quot;:false,&quot;isMovable&quot;:false,&quot;rectPaint&quot;:{&quot;mNativePaint&quot;:3055694048},&quot;scriptMap&quot;:{&quot;on click&quot;:[{&quot;action&quot;:&quot;goto&quot;,&quot;target&quot;:&quot;p2&quot;}]},&quot;shapeID&quot;:&quot;s6&quot;,&quot;shapeName&quot;:&quot;door 4&quot;,&quot;shapeScript&quot;:&quot;on click goto p2&quot;,&quot;text&quot;:&quot;&quot;,&quot;textPaint&quot;:{&quot;mNativePaint&quot;:3055694624},&quot;width&quot;:100.0,&quot;x&quot;:300.0,&quot;y&quot;:1100.0}]},&quot;currentShape&quot;:{&quot;backgroundColor&quot;:-7829368,&quot;fontColor&quot;:-7829368,&quot;fontSize&quot;:40,&quot;height&quot;:100.0,&quot;imageName&quot;:&quot;&quot;,&quot;isHidden&quot;:false,&quot;isMovable&quot;:false,&quot;rectPaint&quot;:{&quot;mNativePaint&quot;:3055694048},&quot;scriptMap&quot;:{&quot;on click&quot;:[{&quot;action&quot;:&quot;goto&quot;,&quot;target&quot;:&quot;p2&quot;}]},&quot;shapeID&quot;:&quot;s6&quot;,&quot;shapeName&quot;:&quot;door 4&quot;,&quot;shapeScript&quot;:&quot;on click goto p2&quot;,&quot;text&quot;:&quot;&quot;,&quot;textPaint&quot;:{&quot;mNativePaint&quot;:3055694624},&quot;width&quot;:100.0,&quot;x&quot;:300.0,&quot;y&quot;:1100.0},&quot;gameName&quot;:&quot;Bunny World Example&quot;,&quot;id&quot;:&quot;Game2&quot;,&quot;inventoryShapeList&quot;:[],&quot;nextPageID&quot;:6,&quot;nextShapeID&quot;:7,&quot;pageList&quot;:[{&quot;isStarterPage&quot;:true,&quot;pageID&quot;:&quot;p1&quot;,&quot;pageName&quot;:&quot;page 1&quot;,&quot;scriptMap&quot;:{},&quot;shapeList&quot;:[{&quot;backgroundColor&quot;:-7829368,&quot;fontColor&quot;:-7829368,&quot;fontSize&quot;:40,&quot;height&quot;:100.0,&quot;imageName&quot;:&quot;&quot;,&quot;isHidden&quot;:false,&quot;isMovable&quot;:false,&quot;rectPaint&quot;:{&quot;mNativePaint&quot;:3074477408},&quot;scriptMap&quot;:{&quot;on click&quot;:[{&quot;action&quot;:&quot;goto&quot;,&quot;target&quot;:&quot;p2&quot;}]},&quot;shapeID&quot;:&quot;s1&quot;,&quot;shapeName&quot;:&quot;door 1&quot;,&quot;shapeScript&quot;:&quot;on click goto p2&quot;,&quot;text&quot;:&quot;&quot;,&quot;textPaint&quot;:{&quot;mNativePaint&quot;:3074479280},&quot;width&quot;:100.0,&quot;x&quot;:100.0,&quot;y&quot;:1100.0},{&quot;backgroundColor&quot;:-7829368,&quot;fontColor&quot;:-7829368,&quot;fontSize&quot;:40,&quot;height&quot;:100.0,&quot;imageName&quot;:&quot;&quot;,&quot;isHidden&quot;:true,&quot;isMovable&quot;:false,&quot;rectPaint&quot;:{&quot;mNativePaint&quot;:3093453296},&quot;scriptMap&quot;:{&quot;on click&quot;:[{&quot;action&quot;:&quot;goto&quot;,&quot;target&quot;:&quot;p3&quot;}]},&quot;shapeID&quot;:&quot;s2&quot;,&quot;shapeName&quot;:&quot;door 2&quot;,&quot;shapeScript&quot;:&quot;on click goto p3&quot;,&quot;text&quot;:&quot;&quot;,&quot;textPaint&quot;:{&quot;mNativePaint&quot;:3093457616},&quot;width&quot;:100.0,&quot;x&quot;:500.0,&quot;y&quot;:1100.0},{&quot;backgroundColor&quot;:-7829368,&quot;fontColor&quot;:-7829368,&quot;fontSize&quot;:40,&quot;height&quot;:100.0,&quot;imageName&quot;:&quot;&quot;,&quot;isHidden&quot;:false,&quot;isMovable&quot;:false,&quot;rectPaint&quot;:{&quot;mNativePaint&quot;:3074596640},&quot;scriptMap&quot;:{&quot;on click&quot;:[{&quot;action&quot;:&quot;goto&quot;,&quot;target&quot;:&quot;p4&quot;}]},&quot;shapeID&quot;:&quot;s3&quot;,&quot;shapeName&quot;:&quot;door 3&quot;,&quot;shapeScript&quot;:&quot;on click goto p4&quot;,&quot;text&quot;:&quot;&quot;,&quot;textPaint&quot;:{&quot;mNativePaint&quot;:3074596928},&quot;width&quot;:100.0,&quot;x&quot;:900.0,&quot;y&quot;:1100.0},{&quot;backgroundColor&quot;:-7829368,&quot;fontColor&quot;:-7829368,&quot;fontSize&quot;:40,&quot;height&quot;:100.0,&quot;imageName&quot;:&quot;&quot;,&quot;isHidden&quot;:false,&quot;isMovable&quot;:false,&quot;rectPaint&quot;:{&quot;mNativePaint&quot;:3055694048},&quot;scriptMap&quot;:{&quot;on click&quot;:[{&quot;action&quot;:&quot;goto&quot;,&quot;target&quot;:&quot;p2&quot;}]},&quot;shapeID&quot;:&quot;s6&quot;,&quot;shapeName&quot;:&quot;door 4&quot;,&quot;shapeScript&quot;:&quot;on click goto p2&quot;,&quot;text&quot;:&quot;&quot;,&quot;textPaint&quot;:{&quot;mNativePaint&quot;:3055694624},&quot;width&quot;:100.0,&quot;x&quot;:300.0,&quot;y&quot;:1100.0}]},{&quot;isStarterPage&quot;:false,&quot;pageID&quot;:&quot;p2&quot;,&quot;pageName&quot;:&quot;page 2&quot;,&quot;scriptMap&quot;:{},&quot;shapeList&quot;:[{&quot;backgroundColor&quot;:-7829368,&quot;fontColor&quot;:-7829368,&quot;fontSize&quot;:40,&quot;height&quot;:300.0,&quot;imageName&quot;:&quot;mystic&quot;,&quot;isHidden&quot;:false,&quot;isMovable&quot;:false,&quot;rectPaint&quot;:{&quot;mNativePaint&quot;:3074509952},&quot;scriptMap&quot;:{&quot;on click&quot;:[{&quot;action&quot;:&quot;play&quot;,&quot;target&quot;:&quot;munching&quot;}],&quot;on enter&quot;:[{&quot;action&quot;:&quot;show&quot;,&quot;target&quot;:&quot;s2&quot;}]},&quot;shapeID&quot;:&quot;s4&quot;,&quot;shapeName&quot;:&quot;mystic bunny&quot;,&quot;shapeScript&quot;:&quot;on enter show s2;on click play munching&quot;,&quot;text&quot;:&quot;&quot;,&quot;textPaint&quot;:{&quot;mNativePaint&quot;:3093419600},&quot;width&quot;:300.0,&quot;x&quot;:400.0,&quot;y&quot;:400.0}]},{&quot;isStarterPage&quot;:false,&quot;pageID&quot;:&quot;p3&quot;,&quot;pageName&quot;:&quot;page 3&quot;,&quot;scriptMap&quot;:{},&quot;shapeList&quot;:[{&quot;backgroundColor&quot;:-7829368,&quot;fontColor&quot;:-7829368,&quot;fontSize&quot;:40,&quot;height&quot;:200.0,&quot;imageName&quot;:&quot;fire&quot;,&quot;isHidden&quot;:false,&quot;isMovable&quot;:false,&quot;rectPaint&quot;:{&quot;mNativePaint&quot;:3074653808},&quot;scriptMap&quot;:{&quot;on enter&quot;:[{&quot;action&quot;:&quot;play&quot;,&quot;target&quot;:&quot;fire&quot;}]},&quot;shapeID&quot;:&quot;s5&quot;,&quot;shapeName&quot;:&quot;fire&quot;,&quot;shapeScript&quot;:&quot;on enter play fire&quot;,&quot;text&quot;:&quot;&quot;,&quot;textPaint&quot;:{&quot;mNativePaint&quot;:3074652368},&quot;width&quot;:600.0,&quot;x&quot;:100.0,&quot;y&quot;:500.0}]},{&quot;isStarterPage&quot;:false,&quot;pageID&quot;:&quot;p4&quot;,&quot;pageName&quot;:&quot;page 4&quot;,&quot;scriptMap&quot;:{},&quot;shapeList&quot;:[]},{&quot;isStarterPage&quot;:false,&quot;pageID&quot;:&quot;p5&quot;,&quot;pageName&quot;:&quot;page 5&quot;,&quot;scriptMap&quot;:{},&quot;shapeList&quot;:[]}],&quot;starterPage&quot;:{&quot;isStarterPage&quot;:true,&quot;pageID&quot;:&quot;p1&quot;,&quot;pageName&quot;:&quot;page 1&quot;,&quot;scriptMap&quot;:{},&quot;shapeList&quot;:[{&quot;backgroundColor&quot;:-7829368,&quot;fontColor&quot;:-7829368,&quot;fontSize&quot;:40,&quot;height&quot;:100.0,&quot;imageName&quot;:&quot;&quot;,&quot;isHidden&quot;:false,&quot;isMovable&quot;:false,&quot;rectPaint&quot;:{&quot;mNativePaint&quot;:3074477408},&quot;scriptMap&quot;:{&quot;on click&quot;:[{&quot;action&quot;:&quot;goto&quot;,&quot;target&quot;:&quot;p2&quot;}]},&quot;shapeID&quot;:&quot;s1&quot;,&quot;shapeName&quot;:&quot;door 1&quot;,&quot;shapeScript&quot;:&quot;on click goto p2&quot;,&quot;text&quot;:&quot;&quot;,&quot;textPaint&quot;:{&quot;mNativePaint&quot;:3074479280},&quot;width&quot;:100.0,&quot;x&quot;:100.0,&quot;y&quot;:1100.0},{&quot;backgroundColor&quot;:-7829368,&quot;fontColor&quot;:-7829368,&quot;fontSize&quot;:40,&quot;height&quot;:100.0,&quot;imageName&quot;:&quot;&quot;,&quot;isHidden&quot;:true,&quot;isMovable&quot;:false,&quot;rectPaint&quot;:{&quot;mNativePaint&quot;:3093453296},&quot;scriptMap&quot;:{&quot;on click&quot;:[{&quot;action&quot;:&quot;goto&quot;,&quot;target&quot;:&quot;p3&quot;}]},&quot;shapeID&quot;:&quot;s2&quot;,&quot;shapeName&quot;:&quot;door 2&quot;,&quot;shapeScript&quot;:&quot;on click goto p3&quot;,&quot;text&quot;:&quot;&quot;,&quot;textPaint&quot;:{&quot;mNativePaint&quot;:3093457616},&quot;width&quot;:100.0,&quot;x&quot;:500.0,&quot;y&quot;:1100.0},{&quot;backgroundColor&quot;:-7829368,&quot;fontColor&quot;:-7829368,&quot;fontSize&quot;:40,&quot;height&quot;:100.0,&quot;imageName&quot;:&quot;&quot;,&quot;isHidden&quot;:false,&quot;isMovable&quot;:false,&quot;rectPaint&quot;:{&quot;mNativePaint&quot;:3074596640},&quot;scriptMap&quot;:{&quot;on click&quot;:[{&quot;action&quot;:&quot;goto&quot;,&quot;target&quot;:&quot;p4&quot;}]},&quot;shapeID&quot;:&quot;s3&quot;,&quot;shapeName&quot;:&quot;door 3&quot;,&quot;shapeScript&quot;:&quot;on click goto p4&quot;,&quot;text&quot;:&quot;&quot;,&quot;textPaint&quot;:{&quot;mNativePaint&quot;:3074596928},&quot;width&quot;:100.0,&quot;x&quot;:900.0,&quot;y&quot;:1100.0},{&quot;backgroundColor&quot;:-7829368,&quot;fontColor&quot;:-7829368,&quot;fontSize&quot;:40,&quot;height&quot;:100.0,&quot;imageName&quot;:&quot;&quot;,&quot;isHidden&quot;:false,&quot;isMovable&quot;:false,&quot;rectPaint&quot;:{&quot;mNativePaint&quot;:3055694048},&quot;scriptMap&quot;:{&quot;on click&quot;:[{&quot;action&quot;:&quot;goto&quot;,&quot;target&quot;:&quot;p2&quot;}]},&quot;shapeID&quot;:&quot;s6&quot;,&quot;shapeName&quot;:&quot;door 4&quot;,&quot;shapeScript&quot;:&quot;on click goto p2&quot;,&quot;text&quot;:&quot;&quot;,&quot;textPaint&quot;:{&quot;mNativePaint&quot;:3055694624},&quot;width&quot;:100.0,&quot;x&quot;:300.0,&quot;y&quot;:1100.0}]}}";
            sharedPrefEditor.putString("Game1", defaultGame);
            sharedPrefEditor.apply();
            System.out.println("ADDED DEFAULT GAME");
        }

        // Get all game progress from Game Progress sharedPref file
        gameProgressSharedPref = getSharedPreferences(GAME_PROGRESS_SHARED_PREF_FILE, MODE_PRIVATE);
        // for clearing test data
//         SharedPreferences.Editor sharedPrefEditor2 = gameProgressSharedPref.edit();
//         sharedPrefEditor2.clear();
//         sharedPrefEditor2.apply();
        gameProgressListFromDB = new ArrayList<>();
        loadGameProgressFromSharedPrefs();

        // Load game config options to game dropdown
        spinnerGameNames = findViewById(R.id.spinnerGameNames);
        spinnerGameNames.setOnItemSelectedListener(this);
        loadSpinnerGameConfigData();

        /**
         *          * Set up onClick listener on button to create new game using editor
         *          * 1. Create a new game
         *          * 2. Use Singleton to keep track of this new game as currentGame
         *          * User will have the option to save this new game or discard it inside the game editor.
         */
        final Button buttonCreateNewGame = findViewById(R.id.buttonCreateNewGame);
        buttonCreateNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set up a new game
                int nextGameCount = getNumGames() + 1;
                Game game = new Game("Game" + nextGameCount, "Game" + nextGameCount);

                // Store this game in memory with Singleton
                //singletonData.addGameToList(game); // TODO: Not sure if I actually need this
                singletonData.setCurrentGame(game);
                game.setCurrentPage(game.getStarterPage());

                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        /**
         * Set up onClick listener on button to fetch an in-progress game or to start new game
         * 1. Get the selected game config from spinner
         * 2. Look like progressGameSharePrefs to see if there's already a game with the same key (i.e. game ID)
         * 3. If yes, set that game from progressGameSharePrefs as selectedGame
         * 4. If no, clone that game config
         */
        final Button buttonStartGame = findViewById(R.id.buttonStartGame);
        buttonStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Game selectedGameConfig = (Game) spinnerGameNames.getSelectedItem();
                if (gameProgressSharedPref.contains(selectedGameConfig.getGameID())) {
                    selectedGame = selectInProgressGame(selectedGameConfig);
                    singletonData.setCurrentGame(selectedGame);
                    singletonData.getCurrentGame().setCurrentPage(selectedGame.getStarterPage());
                    Intent intent = new Intent(MainActivity.this, GameActivity.class);
                    startActivity(intent);
                } else {
                    try {
                        Game newProgressGame = (Game) selectedGameConfig.clone();
                        selectedGame = newProgressGame;
                        singletonData.setCurrentGame(newProgressGame);
                        singletonData.getCurrentGame().setCurrentPage(newProgressGame.getStarterPage());
                        Intent intent = new Intent(MainActivity.this, GameActivity.class);
                        startActivity(intent);
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }

                /*
                if (selectInProgressGame(selectedGameConfig) != null) {
                    selectedGame = selectInProgressGame(selectedGameConfig);
                    singletonData.setCurrentGame(selectedGame);
                    singletonData.getCurrentGame().setCurrentPage(selectedGame.getStarterPage());
                } else {
                    try {
                        System.out.println("START::::: TRYING TO START GAME 2");
                        Game newProgressGame = (Game) selectedGameConfig.clone();
                        selectedGame = newProgressGame;
                        singletonData.setCurrentGame(newProgressGame);
                        singletonData.getCurrentGame().setCurrentPage(newProgressGame.getStarterPage());
                        System.out.println("END::::: TRYING TO START GAME 2");
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }

                }

                 */
                System.out.println("***GAME ID IS: " + singletonData.getCurrentGame().getGameID());
                //Intent intent = new Intent(MainActivity.this, GameActivity.class);
                //startActivity(intent);
            }
        });

        /**
         * Set up onClick listener on button to edit an existing game config / template
         * 1. Get the selected game config from spinner
         * 2. Find the game inside gameConfigSharedPrefs with the same key (i.e. game ID)
         * 3. Set that game from gameConfigSharedPrefs as selectedGame
         */
        final Button buttonEditGame = findViewById(R.id.buttonEditGame);
        buttonEditGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set selectedGame in Singleton according to user's dropdown choice
                selectedGame = (Game) spinnerGameNames.getSelectedItem();
                System.out.println("Selected game ID is: " + selectedGame.getGameID());
                System.out.println("Selected game name is: " + selectedGame.getGameName());
                singletonData.setCurrentGame(selectedGame);
                singletonData.getCurrentGame().setCurrentPage(selectedGame.getStarterPage());
                Toast.makeText(MainActivity.this, "Current game is: " + selectedGame.toString(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
    }

    private Game selectInProgressGame(Game gameConfig) {
        for (Game inProgressGame : gameProgressListFromDB) {
            if (inProgressGame.getGameID().equals(gameConfig.getGameID())) {
                return inProgressGame;
            }
        }
        return null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        selectedGame = (Game) parent.getItemAtPosition(position);
        System.out.println("Selected a game: " + selectedGame);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Load the spinner data from SharedPref
     **/
    private void loadSpinnerGameConfigData() {
        // Creating adapter for spinner
        ArrayAdapter<Game> dataAdapter = new ArrayAdapter<Game>(this, android.R.layout.simple_spinner_item, gameConfigListFromDB);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        Spinner spinnerGameNames = findViewById(R.id.spinnerGameNames);
        spinnerGameNames.setAdapter(dataAdapter);
    }

    // Get a count of all saved games for new game's naming purpose
    public int getNumGames() {
        return gameConfigListFromDB.size();
    }

    /**
     * Loads game configs (default game "templates" defined using the Editor).
     */
    private void loadGameConfigsFromSharedPrefs() {
        // Get all game templates from sharedPrefs
        Map<String, ?> allGameEntries = gameConfigSharedPref.getAll();
        for (Map.Entry<String, ?> entry : allGameEntries.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());

            // Deserialize each game
            Gson gson = new Gson();
            String json = gameConfigSharedPref.getString(entry.getKey(), null);
            Game thisGame = gson.fromJson(json, Game.class);
            gameConfigListFromDB.add(thisGame);

            //System.out.println("printing stuff inside deserialization: ");
            //System.out.println("entry key: " + entry.getKey());
            //System.out.println("game object name" + thisGame.toString());
        }

        // Store game configs into Singleton
        singletonData.loadGameConfigsFromDB(gameConfigListFromDB);
    }

    /**
     * Loads game progress of games that have been played before with Game Player.
     */
    private void loadGameProgressFromSharedPrefs() {
        // Get all game progress from sharedPrefs
        Map<String, ?> allGameEntries = gameProgressSharedPref.getAll();
        for (Map.Entry<String, ?> entry : allGameEntries.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());

            // Deserialize each game
            Gson gson = new Gson();
            String json = gameProgressSharedPref.getString(entry.getKey(), null);
            Game thisGame = gson.fromJson(json, Game.class);
            gameProgressListFromDB.add(thisGame);

            //System.out.println("printing played games  inside deserialization: ");
            //System.out.println("entry key: " + entry.getKey());
            //System.out.println("played game object name" + thisGame.toString());
        }

        // Store gameProgressList into Singleton
        singletonData.loadGameProgressListFromDB(gameProgressListFromDB); // TODO: Is this right?
    }

}