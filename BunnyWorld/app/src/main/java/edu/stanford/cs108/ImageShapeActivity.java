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

    // UI
    Button buttonSetImage;
    Button buttonCancelSetImage;
    // TODO: Need UI element for setting scripts

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_shape);

        buttonSetImage = findViewById(R.id.buttonSetImage);
        buttonSetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmit(view); // Create new imageShape

                Intent intent = new Intent(ImageShapeActivity.this, EditorActivity.class);
                startActivity(intent); // Direct user back to EditorActivity
            }
        });

        buttonCancelSetImage = findViewById(R.id.buttonCancelSetImage);
        buttonCancelSetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        EditText editImageLeftPosition = findViewById(R.id.editImageLeftPosition);
        EditText editImageRightPosition = findViewById(R.id.editImageRightPosition);
        EditText editImageTopPosition = findViewById(R.id.editImageTopPosition);
        EditText editImageBottomPosition = findViewById(R.id.editImageBottomPosition);
        //EditText shapeScript = findViewById(R.id.script);

        ImageShape newImage = new ImageShape("spinnerImageName.getSelectedItem().toString()",
                inputImageShapeName.getText().toString(),
                checkboxImageIsHidden.isChecked(),
                checkboxImageIsMovable.isChecked(),
                "shape script");

        SingletonData singletonData = SingletonData.getInstance();
        singletonData.getCurrentGame().getPage("page 1").addShape(newImage); //TODO: Need to pass in user's page selection
        //singletonData.addImageShape(singletonData.getCurrentGame(),
        //        "page 1", newImage);
                //spinnerImagePage.getSelectedItem().toString(), newImage);

        System.out.println("Singleton after adding image " + singletonData.toString());
        System.out.println("Singleton after adding image " + singletonData.getCurrentGame().toString());
        System.out.println("Singleton after adding image " + singletonData.getCurrentGame().getPage("page 1").getNumShapesOnPage());
    }
}