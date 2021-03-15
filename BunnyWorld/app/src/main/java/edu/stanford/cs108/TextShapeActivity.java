package edu.stanford.cs108;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class TextShapeActivity extends AppCompatActivity {
    // Data
    SingletonData singletonData = SingletonData.getInstance(); // Store list of games in memory
    Game game = singletonData.getCurrentGame();

    // UI
    Button buttonSetText;
    Button buttonCancelText;
    // TODO: Need UI element for setting scripts

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_shape);

        // Set up onClickListener on "Submit" to bring user back to EditorActivity
        buttonSetText = findViewById(R.id.buttonSetText);
        buttonSetText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmit(view); // Create new textShape
                System.out.println("game's current page is " + game.currentPage.getPageName());

                Intent intent = new Intent(TextShapeActivity.this, EditorActivity.class);
                startActivity(intent); // Direct user back to EditorActivity
            }
        });

        // Set up onClickListener on "Cancel" to bring user back to EditorActivity
        buttonCancelText = findViewById(R.id.buttonCancelText);
        buttonCancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TextShapeActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onSubmit(View view) {
        EditText inputTextShapeName = findViewById(R.id.inputTextShapeName);
        EditText inputText = findViewById(R.id.inputText);
        EditText inputFontSize = findViewById(R.id.inputFontSize);
        Spinner inputTextPage = findViewById(R.id.inputTextPage); // TODO: Need to set up Spinner/ArrayAdapater to display list of pages
        CheckBox checkboxTextIsHidden = findViewById(R.id.checkboxTextIsHidden);
        CheckBox checkboxTextIsMovable = findViewById(R.id.checkboxTextIsMovable);
        CheckBox checkboxTextIsInventory = findViewById(R.id.checkboxTextIsInventory);
        EditText editTextXPosition = findViewById(R.id.editTextXPosition);
        EditText editTextYPosition = findViewById(R.id.editTextYPosition);
        EditText editTextWidth = findViewById(R.id.editTextWidth);
        EditText editTextHeight = findViewById(R.id.editTextHeight);
        //EditText shapeScript = findViewById(R.id.script);

        float x = Float.parseFloat(String.valueOf(editTextXPosition.getText()));
        float y = Float.parseFloat(String.valueOf(editTextYPosition.getText()));
        // TODO: if the user wants the item to be in Inventory, then we should throw an error if the (x,y) is invalid (i.e. not within the Inventory box)

        float width = Float.parseFloat(String.valueOf(editTextWidth.getText()));
        float height = Float.parseFloat(String.valueOf(editTextHeight.getText()));

        TextShape newText = new TextShape(inputText.getText().toString(),
                Integer.parseInt(inputFontSize.getText().toString()),
                inputTextShapeName.getText().toString(),
                checkboxTextIsHidden.isChecked(),
                checkboxTextIsMovable.isChecked(),
                checkboxTextIsInventory.isChecked(),
                "shape script",
                x, y, width, height);
        //String destination = inputTextPage.getText().toString(); // TODO: Need to get Page instead of string

        if (checkboxTextIsInventory.isChecked()) {
            game.addInventory(newText);
        } else {
            game.getCurrentPage().addShape(newText); //TODO: Need to pass in user's page selection
        }
        System.out.println("Singleton after adding image " + game.getCurrentPage().getNumShapesOnPage());
    }
}