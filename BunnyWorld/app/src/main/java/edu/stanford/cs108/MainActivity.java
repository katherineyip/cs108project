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
import com.google.gson.reflect.TypeToken;
import android.widget.SpinnerAdapter;

// DB Stuff
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//implements AdapterView
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    // Data
    Game selectedGame;
    SingletonData singletonData = SingletonData.getInstance();

    // DB
    SharedPreferences sharedPref;
    static final String SHARED_PREF_FILE = "TempGamePrefs";
    List<Game> gameListFromDB;

    // UI
    Spinner spinnerGameNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get sharedPref file
        sharedPref = getSharedPreferences(SHARED_PREF_FILE, MODE_PRIVATE);
        gameListFromDB = new ArrayList<>();

        spinnerGameNames = findViewById(R.id.spinnerGameNames);
        spinnerGameNames.setOnItemSelectedListener(this);
        loadGamesDataFromSharedPrefs();
        loadSpinnerGameListData();

       // Set up onClick listener on button to create new game using editor
        final Button buttonCreateNewGame = findViewById(R.id.buttonCreateNewGame);
        buttonCreateNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set up a new game
                int nextGameCount = getNumGames() + 1;
                Game game = new Game("Game" + nextGameCount, "Game" + nextGameCount);

                //SingletonData singletonData = SingletonData.getInstance();
                singletonData.addGameToList(game);
                singletonData.setCurrentGame(game);
                game.setCurrentPage(game.getPageList().get(0));

                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        final Button buttonStartGame = findViewById(R.id.buttonStartGame);
        buttonStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singletonData.setCurrentGame(selectedGame);
                // TODO: For starting a game, we need to make a clone, rather than playing the game template
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        final Button buttonEditGame = findViewById(R.id.buttonEditGame);
        buttonEditGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set selectedGame in Singleton according to user's dropdown choice
                selectedGame = (Game) spinnerGameNames.getSelectedItem();
                singletonData.setCurrentGame(selectedGame);
                Toast.makeText(MainActivity.this, "Current game is: " + selectedGame.toString(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
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
     * Function to load the spinner data from Singleton / SQLite database
     **/
    private void loadSpinnerGameListData() {
        // TODO: Temp remove for testing
        //SingletonData singletonData = SingletonData.getInstance();
        //List<Game> gameList = singletonData.getGameList();
        //List<Game> gameList = new ArrayList<>();

        // Creating adapter for spinner
        ArrayAdapter<Game> dataAdapter = new ArrayAdapter<Game>(this, android.R.layout.simple_spinner_item, gameListFromDB);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        Spinner spinnerGameNames = findViewById(R.id.spinnerGameNames);
        spinnerGameNames.setAdapter(dataAdapter);
    }

    // Get a count of all saved games for new game's naming purpose
    public int getNumGames() {
        return gameListFromDB.size();
    }

    private void loadGamesDataFromSharedPrefs() {
        // Get all game templates from sharedPrefs
        Map<String, ?> allGameEntries = sharedPref.getAll();
        for (Map.Entry<String, ?> entry : allGameEntries.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());

            // Deserialize each game
            Gson gson = new Gson();
            String json = sharedPref.getString(entry.getKey(), null);
            Game thisGame = gson.fromJson(json, Game.class);
            gameListFromDB.add(thisGame);

            System.out.println("printing stuff inside deserialization: ");
            System.out.println("entry key: " + entry.getKey());
            System.out.println("game object name" + thisGame.toString());
        }

        // Store gameList into Singleton
        singletonData.loadGameListFromDB(gameListFromDB);
    }


}