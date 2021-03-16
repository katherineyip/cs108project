package edu.stanford.cs108;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class ImageShapeActivity extends AppCompatActivity {
    // Data
    SingletonData singletonData = SingletonData.getInstance(); // Store list of games in memory
    Game game = singletonData.getCurrentGame();

    // UI
    Button buttonSetImage;
    Button buttonCancelSetImage;
    // TODO: Need UI element for setting scripts

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_shape);

        // By default set shape names as "shape 1", "shape 2", etc
        EditText inputImageShapeName = findViewById(R.id.inputImageShapeName);
        int numAllShapeCounts = game.getNumShapesInGame() + 1;
        inputImageShapeName.setText("shape " + numAllShapeCounts);

        // Set up onClickListener on "Submit" to bring user back to EditorActivity
        buttonSetImage = findViewById(R.id.buttonSetImage);
        buttonSetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmit(view); // Create new imageShape
                System.out.println("game's current page is " + game.currentPage.getPageName());

                Intent intent = new Intent(ImageShapeActivity.this, EditorActivity.class);
                startActivity(intent); // Direct user back to EditorActivity
            }
        });

        // Set up onClickListener on "Cancel" to bring user back to EditorActivity
        buttonCancelSetImage = findViewById(R.id.buttonCancelSetImage);
        buttonCancelSetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("game's current page is " + game.currentPage.getPageName());
                Intent intent = new Intent(ImageShapeActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onSubmit(View view) {
        EditText inputImageShapeName = findViewById(R.id.inputImageShapeName);
        EditText spinnerImageName = findViewById(R.id.spinnerImageName); // TODO: Remove the EditText placeholder and replace with Spinner later
        EditText spinnerImagePage = findViewById(R.id.spinnerImagePage); // TODO: Remove the EditText placeholder and replace with Spinner later
        //Spinner spinnerImageName = findViewById(R.id.spinnerImageName);
        //Spinner spinnerImagePage = findViewById(R.id.spinnerImagePage);
        CheckBox checkboxImageIsHidden = findViewById(R.id.checkboxImageIsHidden);
        CheckBox checkboxImageIsMovable = findViewById(R.id.checkboxImageIsMovable);
        CheckBox checkboxImageIsInventory = findViewById(R.id.checkboxImageIsInventory);
        EditText editImageXPosition = findViewById(R.id.editImageXPosition);
        EditText editImageYPosition = findViewById(R.id.editImageYPosition);
        EditText editImageWidth = findViewById(R.id.editImageWidth);
        EditText editImageHeight = findViewById(R.id.editImageHeight);
        //EditText shapeScript = findViewById(R.id.script);

        float x = Float.parseFloat(String.valueOf(editImageXPosition.getText()));
        float y = Float.parseFloat(String.valueOf(editImageYPosition.getText()));
        // TODO: if the user wants the item to be in Inventory, then we should throw an error if the (x,y) is invalid (i.e. not within the Inventory box)

        float width = Float.parseFloat(String.valueOf(editImageWidth.getText()));
        float height = Float.parseFloat(String.valueOf(editImageHeight.getText()));

        ImageShape newImage = new ImageShape("spinnerImageName.getSelectedItem().toString()",
                inputImageShapeName.getText().toString(),
                checkboxImageIsHidden.isChecked(),
                checkboxImageIsMovable.isChecked(),
                checkboxImageIsInventory.isChecked(),
                "shape script",
                x, y, width, height);
        String destination = spinnerImagePage.getText().toString(); // TODO: Need to get Page instead of string

        //TODO: Set image position

        if (checkboxImageIsInventory.isChecked()) {
            game.addInventory(newImage);
        } else {
            game.getCurrentPage().addShape(newImage); //TODO: Need to pass in user's page selection
        }
        System.out.println("Singleton after adding image " + game.getCurrentPage().getNumShapesOnPage());
    }
}