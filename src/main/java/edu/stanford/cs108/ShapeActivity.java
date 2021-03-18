package edu.stanford.cs108;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ShapeActivity extends AppCompatActivity {
    // Data
    SingletonData singletonData = SingletonData.getInstance(); // Store list of games in memory
    Game game = singletonData.getCurrentGame();

    // UI
    Button buttonSetShape;
    Button buttonCancelSetShape;
    // TODO: Need UI element for setting scripts

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shape);

        // Populate our Spinners
        Spinner eventSpinner = findViewById(R.id.evSpin);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.events, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventSpinner.setAdapter(adapter);

        Spinner actionSpinner = findViewById(R.id.acSpin);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.actions, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actionSpinner.setAdapter(adapter2);

        Spinner imageSpinner = findViewById(R.id.spinnerImageName);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.images, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        imageSpinner.setAdapter(adapter3);

        Spinner fColorSpin = findViewById(R.id.inputFontColor);
        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(this, R.array.colors, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fColorSpin.setAdapter(adapter5);

        Spinner bGColorSpin = findViewById(R.id.inputBackgroundColor);
        bGColorSpin.setAdapter(adapter5);

        //Populate Spinner from Page list
        Spinner pageSpinner = findViewById(R.id.spinnerShapePage);
        List<Page> pages = game.pageList;
        ArrayList<String> pageNames = new ArrayList<String>();
        pageNames.add("Pages");
        for(Page p : pages){
            pageNames.add(p.pageName);
        }
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, pageNames);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pageSpinner.setAdapter(adapter4);

        // By default set shape names as "shape 1", "shape 2", etc
        EditText inputShapeName = findViewById(R.id.inputShapeName);
        int numAllShapeCounts = game.getNumShapesInGame() + 1;
        inputShapeName.setText("shape " + numAllShapeCounts);

        // Set up onClickListener on "Submit" to bring user back to EditorActivity
        buttonSetShape = findViewById(R.id.buttonSetShape);
        buttonSetShape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmit(view); // Create new imageShape
                System.out.println("game's current page is " + game.currentPage.getPageName());

                Intent intent = new Intent(ShapeActivity.this, EditorActivity.class);
                startActivity(intent); // Direct user back to EditorActivity
            }
        });

        // Set up onClickListener on "Cancel" to bring user back to EditorActivity
        buttonCancelSetShape = findViewById(R.id.buttonCancelSetShape);
        buttonCancelSetShape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("game's current page is " + game.currentPage.getPageName());
                Intent intent = new Intent(ShapeActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onSubmit(View view) {
        // Use to create image
        Spinner spinnerImageName = findViewById(R.id.spinnerImageName); // TODO: Remove the EditText placeholder and replace with Spinner later

        // Use to create text
        EditText inputText = findViewById(R.id.inputText);
        EditText inputFontSize = findViewById(R.id.inputFontSize);
        //Spinner inputFontColor = findViewById(R.id.inputFontColor);

        // User to create rect
        //Spinner inputBackgroundColor = findViewById(R.id.inputBackgroundColor);

        // Shared property across all shapes
        EditText inputShapeName = findViewById(R.id.inputShapeName);
        CheckBox checkboxShapeIsHidden = findViewById(R.id.checkboxShapeIsHidden);
        CheckBox checkboxShapeIsMovable = findViewById(R.id.checkboxShapeIsMovable);
        EditText editShapeXPosition = findViewById(R.id.editShapeXPosition);
        EditText editShapeYPosition = findViewById(R.id.editShapeYPosition);
        EditText editShapeWidth = findViewById(R.id.editShapeWidth);
        EditText editShapeHeight = findViewById(R.id.editShapeHeight);

        // Shape page destination
        Spinner spinnerShapePage = findViewById(R.id.spinnerShapePage);

        float x = Float.parseFloat(String.valueOf(editShapeXPosition.getText()));
        float y = Float.parseFloat(String.valueOf(editShapeYPosition.getText()));
        // TODO: if the user wants the item to be in Inventory, then we should throw an error if the (x,y) is invalid (i.e. not within the Inventory box)

        float width = Float.parseFloat(String.valueOf(editShapeWidth.getText()));
        float height = Float.parseFloat(String.valueOf(editShapeHeight.getText()));


        int fZ = 40;
        int fC = colorPick(R.id.inputFontColor);
        int bC = colorPick(R.id.inputBackgroundColor);

        if(!inputFontSize.getText().toString().equals("")) {
            fZ = Integer.parseInt(inputFontSize.getText().toString());
        }


        Shape newShape = new Shape(spinnerImageName.getSelectedItem().toString(),
                inputText.getText().toString(),
                fZ, fC, bC,
                inputShapeName.getText().toString(),
                checkboxShapeIsHidden.isChecked(),
                checkboxShapeIsMovable.isChecked(),
                "shape script",
                x, y, width, height);
        Page destination = (Page) spinnerShapePage.getSelectedItem();

        // TODO: rule to draw image vs text when both are available
        //TODO: Set image position


        game.getCurrentPage().addShape(newShape); //TODO: Need to pass in user's page selection

        System.out.println("Singleton after adding image " + game.getCurrentPage().getNumShapesOnPage());
    }

    public int colorPick(int id){
        Spinner colorSpin = findViewById(id);

        if(colorSpin.getSelectedItemPosition() == 1){
            return Color.WHITE;
        } else if(colorSpin.getSelectedItemPosition() == 2){
            return Color.BLUE;
        } else if(colorSpin.getSelectedItemPosition() == 3){
            return Color.RED;
        } else if(colorSpin.getSelectedItemPosition() == 4){
            return Color.GREEN;
        } else if(colorSpin.getSelectedItemPosition() == 5){
            return Color.GRAY;
        }

        return Color.BLACK;
    }

    public boolean errorCheck(){
        EditText inputShapeName = findViewById(R.id.inputShapeName);
        Spinner spinnerImageName = findViewById(R.id.spinnerImageName); // TODO: Remove the EditText placeholder and replace with Spinner later
        Spinner spinnerShapePage = findViewById(R.id.spinnerShapePage);

        if(inputShapeName.getText().toString().equals("")){
            Toast.makeText(this, "Must Give Shape Name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(spinnerImageName.getSelectedItemPosition() == 0){
            Toast.makeText(this, "Must Select Image", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(spinnerShapePage.getSelectedItemPosition() == 0){
            Toast.makeText(this, "Must Select Page", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}