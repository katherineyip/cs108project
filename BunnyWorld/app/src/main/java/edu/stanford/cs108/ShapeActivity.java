package edu.stanford.cs108;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import java.util.List;

public class ShapeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    // Data
    SingletonData singletonData = SingletonData.getInstance(); // Store list of games in memory
    Game game = singletonData.getCurrentGame();

    // UI
    Page selectedPage;
    Button buttonSetShape;
    Button buttonCancelSetShape;
    Button buttonNewScript;

    String scriptToAdd;
    // TODO: Need UI element for setting scripts

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shape);

        scriptToAdd = "";

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
        ArrayAdapter<Page>pageadapter = new ArrayAdapter<Page>(this, android.R.layout.simple_spinner_item, pages);
        pageadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pageSpinner.setAdapter(pageadapter);


        // By default set shape names as "shape 1", "shape 2", etc
        EditText inputShapeName = findViewById(R.id.inputShapeName);
        int numAllShapeCounts = game.getNumShapesInGame() + 1;
        inputShapeName.setText("shape " + numAllShapeCounts);

        // Set up onClickListener on "Submit" to bring user back to EditorActivity
        buttonSetShape = findViewById(R.id.buttonSetShape);
        buttonSetShape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputCheck()) {
                    onSubmit(view); // Create new imageShape
                    System.out.println("game's current page is " + game.currentPage.getPageName());

                    Intent intent = new Intent(ShapeActivity.this, EditorActivity.class);
                    startActivity(intent); // Direct user back to EditorActivity
                }
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

        buttonNewScript = findViewById(R.id.newScriptButton);
        buttonNewScript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newScript = getScript();
                if (!newScript.equals("")){
                    //scriptToAdd = Script.combineScripts(scriptToAdd, newScript);
                }
            }
        });
    }

    public void onSubmit(View view) {
        // Use to create image
        Spinner spinnerImageName = findViewById(R.id.spinnerImageName);

        // Use to create text
        EditText inputText = findViewById(R.id.inputText);
        EditText inputFontSize = findViewById(R.id.inputFontSize);

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

        //String scrpt = getScript();

        //setting values
        float x = getVal(editShapeXPosition, 20.f);
        float y = getVal(editShapeYPosition, 20.f);

        float width = getVal(editShapeWidth, 100.f);

       float height = getVal(editShapeHeight, 100.f);


        int fZ = (int)getVal(inputFontSize, 40.f);
        int fC = colorPick(R.id.inputFontColor);
        int bC = colorPick(R.id.inputBackgroundColor);

        String  imageName = spinnerImageName.getSelectedItem().toString();
        if(spinnerImageName.getSelectedItemPosition() == 0) {
            imageName = "";
        }

        Shape newShape = new Shape(imageName,
                inputText.getText().toString(),
                fZ, fC, bC,
                inputShapeName.getText().toString(),
                checkboxShapeIsHidden.isChecked(),
                checkboxShapeIsMovable.isChecked(), game.nextShapeID,
                scriptToAdd,
                x, y, width, height);

        Page destination = (Page) spinnerShapePage.getSelectedItem();
        destination.addShape(newShape);

        System.out.println("Singleton after adding image " + game.getCurrentPage().getNumShapesOnPage());
    }

    public float getVal(EditText et, float def){
        //if empty return default value
        if(et.getText().toString().equals("")){
            return def;
        }

        //if not empty return value of the editText View
        return Float.parseFloat(String.valueOf(et.getText()));
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
            return Color.BLACK;
        }

        return Color.GRAY;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        //String gameName = parent.getItemAtPosition(position).toString();
        selectedPage = (Page) parent.getItemAtPosition(position);
        System.out.println("Selected a game: " + selectedPage);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public boolean inputCheck(){
        EditText inputShapeName = findViewById(R.id.inputShapeName);
        Spinner spinnerShapePage = findViewById(R.id.spinnerShapePage);

        if(inputShapeName.getText().toString().equals("")){
            inputShapeName.setError("Must provide shape name");
            return false;
        }

        // TODO: Ensure there is no duplicated shape names
        //String newShapeName = inputShapeName.getText().toString();
        //if (!isUniqueName(newShapeName)) {
        //    inputShapeName.setError("This shape name already exists.");
        //    return false;
        //}

        return true;
    }

    /*
    private boolean isUniqueName(String name) {
        for (Page page : game.getPageList()) {
            for (Shape shape : page.getShapeList()) {
                if (shape.getShapeName().equals(name)) {
                    return false;
                }
            }
        }
        return true;
    }
     */




    public String getScript(){
        Spinner actionSpinner = findViewById(R.id.acSpin);
        Spinner eventSpinner = findViewById(R.id.evSpin);
        EditText scriptScript = findViewById(R.id.imgScript);
        EditText scriptTarget = findViewById(R.id.imgTarget);

        String scrpt = "";
        if(eventSpinner.getSelectedItemPosition()!= 0){
            scrpt = eventSpinner.getSelectedItem().toString();

            if(scrpt.equals("on drop")){
                scrpt += " ";
                scrpt += scriptTarget.getText().toString();
            }
            scrpt += " ";
            scrpt += actionSpinner.getSelectedItem().toString();
            scrpt += " ";
            scrpt += scriptScript.getText().toString();
            scrpt += ";";

        }
        //System.out.println("Script is: " + scrpt);
        return scrpt;
    }
}