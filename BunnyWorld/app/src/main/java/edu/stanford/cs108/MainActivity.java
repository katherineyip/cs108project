package edu.stanford.cs108;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // Set up onClick listener on button to create new game using editor
        final Button buttonCreateNewGame = findViewById(R.id.buttonCreateNewGame);
        buttonCreateNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set up a new game
                Game game = new Game("New Test Game"); // TODO: Add some ways to programmatically assign new name?

                SingletonData singletonData = SingletonData.getInstance();
                singletonData.addGameToList(game);
                singletonData.setCurrentGame(game);
                game.setCurrentPage(game.getPageList().get(0));

                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // Set up onClick listener on button to play a game
        final Button buttonStartGame = findViewById(R.id.buttonStartGame);
        buttonStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });
    }
}