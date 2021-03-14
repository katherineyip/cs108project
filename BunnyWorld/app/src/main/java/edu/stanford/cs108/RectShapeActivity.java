package edu.stanford.cs108;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class RectShapeActivity extends AppCompatActivity {
    // Data
    SingletonData singletonData = SingletonData.getInstance(); // Store list of games in memory
    Game game = singletonData.getCurrentGame();

    // UI
    Button buttonSetRect;
    Button buttonCancelRect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rect_shape);

        // Set up onClickListener on "Submit" to bring user back to EditorActivity
        buttonSetRect = findViewById(R.id.buttonSetRect);
        buttonSetRect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmit(view); // Create new rectShape

                Intent intent = new Intent(RectShapeActivity.this, EditorActivity.class);
                startActivity(intent); // Direct user back to EditorActivity
            }
        });

        // Set up onClickListener on "Cancel" to bring user back to EditorActivity
        buttonCancelRect = findViewById(R.id.buttonCancelRect);
        buttonCancelRect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("game's current page is " + game.currentPage.getPageName());
                Intent intent = new Intent(RectShapeActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onSubmit(View view) {
        EditText inputRectShapeName = findViewById(R.id.inputImageShapeName);
        EditText inputRectPage = findViewById(R.id.inputRectPage); //TODO: Change to dropdown later
        CheckBox checkboxRectIsHidden = findViewById(R.id.checkboxRectIsHidden);
        CheckBox checkboxRectIsMovable = findViewById(R.id.checkboxRectIsMovable);
        CheckBox checkboxRectIsInventory = findViewById(R.id.checkboxRectIsInventory);
        EditText editRectXPosition = findViewById(R.id.editRectXPosition);
        EditText editRectYPosition = findViewById(R.id.editRectYPosition);
        EditText editRectWidth = findViewById(R.id.editRectWidth);
        EditText editRectHeight = findViewById(R.id.editRectHeight);

        RectShape newRect = new RectShape(Color.GRAY,
                inputRectShapeName.getText().toString(),
                checkboxRectIsHidden.isChecked(),
                checkboxRectIsMovable.isChecked(),
                checkboxRectIsInventory.isChecked(),
                "shape script"); // TODO: Replace later
        String destination = inputRectPage.getText().toString(); // TODO: Need to get Page instead of string

        if (checkboxRectIsInventory.isChecked()) {
            game.addInventory(newRect);
        } else {
            game.getCurrentPage().addShape(newRect); //TODO: Need to pass in user's page selection
        }
        System.out.println("Singleton after adding rect " + game.getCurrentPage().getNumShapesOnPage());
    }
}