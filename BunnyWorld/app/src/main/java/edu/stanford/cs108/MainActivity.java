package edu.stanford.cs108;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.List;

//implements AdapterView
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    // Data
    Game selectedGame;
    SingletonData singletonData = SingletonData.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinnerGameNames = findViewById(R.id.spinnerGameNames);
        spinnerGameNames.setOnItemSelectedListener(this);
        loadSpinnerGameListData();

       // Set up onClick listener on button to create new game using editor
        final Button buttonCreateNewGame = findViewById(R.id.buttonCreateNewGame);
        buttonCreateNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set up a new game
                Game game = new Game("New Game"); // TODO: Add some ways to programmatically assign new name?

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
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        final Button buttonEditGame = findViewById(R.id.buttonEditGame);
        buttonEditGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singletonData.setCurrentGame(selectedGame);
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);

                //Game game = //getGmae
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        //String gameName = parent.getItemAtPosition(position).toString();
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
        SingletonData singletonData = SingletonData.getInstance();
        List<Game> gameList = singletonData.getGameList();

        // TODO: Get game list from db later
        //DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        //List<Game> gameList = db.getAllGames();

        // Creating adapter for spinner
        ArrayAdapter<Game> dataAdapter = new ArrayAdapter<Game>(this, android.R.layout.simple_spinner_item, gameList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        Spinner spinnerGameNames = findViewById(R.id.spinnerGameNames);
        spinnerGameNames.setAdapter(dataAdapter);
    }
}