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
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }

        });

        // TODO: finish setting this up
        final Button buttonStartGame = findViewById(R.id.buttonStartGame);
    }
}