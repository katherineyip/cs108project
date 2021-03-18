package edu.stanford.cs108;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddActivity extends AppCompatActivity {
    // Data
    SingletonData singletonData = SingletonData.getInstance(); // Store list of games in memory
    Game game = singletonData.getCurrentGame();
    Page currentPage = game.getCurrentPage();

    // UI
    Button buttonAddImage; // controls to add shapes to a page
    Button buttonAddRect;
    Button buttonAddText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        // Set up onClick listener on button to add new Image shape
        buttonAddImage = findViewById(R.id.buttonAddImage);
        buttonAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddActivity.this, ImageShapeActivity.class);
                startActivity(intent);
            }
        });

        // Set up onClick listener on button to add new Rect shape
        buttonAddRect = findViewById(R.id.buttonAddRect);
        buttonAddRect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddActivity.this, RectShapeActivity.class);
                startActivity(intent);
            }
        });

        // Set up onClick listener on button to add new Text shape
        buttonAddText = findViewById(R.id.buttonAddText);
        buttonAddText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddActivity.this, TextShapeActivity.class);
                startActivity(intent);
            }
        });

    }
}


