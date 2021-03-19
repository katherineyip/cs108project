package edu.stanford.cs108;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

public class GameActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    // Data
    SingletonData singletonData = SingletonData.getInstance(); // Store list of games in memory
    Game currentGame = singletonData.getCurrentGame();
    List<Page> pageList = currentGame.getPageList();
    Page currentPage = currentGame.getCurrentPage();
    List<Game> gameConfigList = singletonData.getGameConfigList();

    // DB
    SharedPreferences gameProgressSharedPref;
    static final String GAME_PROGRESS_SHARED_PREF_FILE = "TempGameProgressPrefs";

    SharedPreferences gameConfigSharedPref;
    static final String GAME_CONFIG_SHARED_PREF_FILE = "TempGamePrefs";

    // UI
    View pageView; // which has a canvas
    InventoryView inventoryView;
    TextView gameName;
    TextView pageName;
    Spinner pageSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Get all game configs from sharedPref file
        gameConfigSharedPref = getSharedPreferences(GAME_CONFIG_SHARED_PREF_FILE, MODE_PRIVATE);

        // Get in-progress game from sharedPref file
        gameProgressSharedPref = getSharedPreferences(GAME_PROGRESS_SHARED_PREF_FILE, MODE_PRIVATE);

        // Set up current page
        if (currentGame.getCurrentPage() == null) {
            currentPage = currentGame.getStarterPage();
        } else {
            currentPage = currentGame.getCurrentPage();
        }

        // Get Page List from game
        pageList = currentGame.pageList;

        // Display current game name
        gameName = findViewById(R.id.gameName);
        gameName. setText(currentGame.toString());

        // Display current page name
        pageName = findViewById(R.id.pageName);
        pageName.setText(currentPage.getPageName());

        // Canvas
        pageView = findViewById(R.id.pageView);

        // Populate Spinner from Page list
        loadSpinnerPageListData();

        /**
         * Set up onClick listener on "Go to page" button to allow user navigate between pages
         */
        final Button buttonGoToPage = findViewById(R.id.buttonGoToPage);
        buttonGoToPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set selectedGame in Singleton according to user's dropdown choice
                currentPage = (Page) pageSpinner.getSelectedItem();
                currentGame.setCurrentPage(currentPage);
                pageName.setText(currentPage.toString());
                System.out.println("---LOG FROM GameActivity--- current page from singleton: " + singletonData.getCurrentGame().getCurrentPage());
                System.out.println("---LOG FROM GameActivity--- current page from currentPage: " + currentPage);
                Toast.makeText(GameActivity.this, "You're now on: " + currentPage.toString(), Toast.LENGTH_SHORT).show();

                //TODO: call pageView to redraw shapes on the new page
                pageView.invalidate();
            }
        });
    }

    /**
     * Menu dropdown:
     * - leave and save game
     * - restart game
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_player_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuLeaveAndSaveGame:
                saveGame();
                Intent intentEditPage = new Intent(GameActivity.this, MainActivity.class);
                startActivity(intentEditPage);
                return true;
            case R.id.menuRestartGame:
                restartGame();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Save current game's snapshot to Game Progress sharePrefs
     */
    private void saveGame() {
        SharedPreferences.Editor sharedPrefEditor = gameProgressSharedPref.edit();

        // Serialize game object
        Gson gson = new Gson();
        String jsonString = gson.toJson(currentGame);
        System.out.println("LOG: Printing json  --- " + jsonString);

        // Put serialized game object into sharedPrefs file
        sharedPrefEditor.putString(currentGame.getGameID(), jsonString);
        sharedPrefEditor.apply();
        Toast.makeText(GameActivity.this, "Successfully saved " + currentGame.gameName + ".", Toast.LENGTH_SHORT);
        System.out.println("Successfully saved game to sp. Game ID: " + currentGame.getGameID() + ". Game Name: " + currentGame.gameName);
    }

    /**
     * To restart a game:
     * 1. Find the game config using game config ID
     * 2. Create a new game by cloning game config
     */
    private void restartGame() {
        Game gameConfig = (Game) selectGameConfig(currentGame.getGameID());
        System.out.println("LOG --current game's config id is: " + currentGame.getGameID());
        try {
            Game newGame = (Game) gameConfig.clone();
            //currentGame = newGame;
            System.out.println("LOG --new game's config id is: " + currentGame.getGameID());

            singletonData.setCurrentGame(newGame);
            singletonData.getCurrentGame().setCurrentPage(newGame.getStarterPage());

            System.out.println("***RESTARTED GAME ID IS: " + singletonData.getCurrentGame().getGameID());
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);

            Toast.makeText(GameActivity.this, "Restarted game: " + currentGame.toString(), Toast.LENGTH_SHORT).show();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private Game selectGameConfig(String gameConfigID) {
        for (Game config : gameConfigList) {
            if (config.getGameID().equals(gameConfigID)) {
                return config;
            }
        }
        return null;
    }

    private void loadSpinnerPageListData() {
        // Creating adapter for spinner
        ArrayAdapter<Page> dataAdapter = new ArrayAdapter<Page>(this, android.R.layout.simple_spinner_item, pageList);

        // Drop down layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Attach data adapter to spinner
        pageSpinner = findViewById(R.id.pageSpinner);
        pageSpinner.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}