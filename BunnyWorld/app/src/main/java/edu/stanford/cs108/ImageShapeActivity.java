package edu.stanford.cs108;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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


        //Populate our Spinners
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

        //Populate Spinner from Page list
        Spinner pageSpinner = findViewById(R.id.spinnerImagePage);
        List<Page> pages = game.pageList;
        ArrayList<String> pageNames = new ArrayList<String>();
        pageNames.add("Pages");
        for(Page p : pages){
            pageNames.add(p.pageName);
        }
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, pageNames);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pageSpinner.setAdapter(adapter4);

        // Set up onClickListener on "Submit" to bring user back to EditorActivity
        buttonSetImage = findViewById(R.id.buttonSetImage);
        buttonSetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmit(view); // Create new imageShape
                System.out.println("game's current page is " + game.currentPage.getPageName());
                if(errorCheck()) {
                    Intent intent = new Intent(ImageShapeActivity.this, EditorActivity.class);
                    startActivity(intent); // Direct user back to EditorActivity
                }
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
        Spinner spinnerImageName = findViewById(R.id.spinnerImageName);
        Spinner spinnerImagePage = findViewById(R.id.spinnerImagePage);
        CheckBox checkboxImageIsHidden = findViewById(R.id.checkboxImageIsHidden);
        CheckBox checkboxImageIsMovable = findViewById(R.id.checkboxImageIsMovable);
        CheckBox checkboxImageIsInventory = findViewById(R.id.checkboxImageIsInventory);
        EditText editImageLeftPosition = findViewById(R.id.editImageLeftPosition);
        EditText editImageRightPosition = findViewById(R.id.editImageRightPosition);
        EditText editImageTopPosition = findViewById(R.id.editImageTopPosition);
        EditText editImageBottomPosition = findViewById(R.id.editImageBottomPosition);

        Spinner actionSpinner = findViewById(R.id.acSpin);
        Spinner eventSpinner = findViewById(R.id.evSpin);
        EditText scriptScript = findViewById(R.id.imgScript);
        EditText scriptTarget = findViewById(R.id.imgTarget);

        String script = "";
        if(eventSpinner.getSelectedItemPosition()!= 0){
            script = eventSpinner.getSelectedItem().toString();

            if(script.equals("on drop")){
                script += " ";
                script += scriptTarget.getText().toString();
            }
            script += " ";
            script += actionSpinner.getSelectedItem().toString();
            script += " ";
            script += scriptScript.getText().toString();
            script += ";";

        }

        String xs1 = editImageLeftPosition.getText().toString();
        String xs2 = editImageRightPosition.getText().toString();
        String ys1 = editImageTopPosition.getText().toString();
        String ys2 = editImageBottomPosition.getText().toString();
        int x1 = Integer.valueOf(xs1);
        int x2 = Integer.valueOf(xs2);
        int y1 = Integer.valueOf(ys1);
        int y2 = Integer.valueOf(ys2);

        int width = x2-x1;
        int height = y2-1;



        ImageShape newImage = new ImageShape("spinnerImageName.getSelectedItem().toString()",
                spinnerImageName.getSelectedItem().toString(),
                checkboxImageIsHidden.isChecked(),
                checkboxImageIsMovable.isChecked(),
                checkboxImageIsInventory.isChecked(),
                script, (float)x1, (float)y1, (float)width, (float)height);




        //Check to see if page has been selected
        if(spinnerImagePage.getSelectedItemPosition() == 0) {
            System.out.println("Must Select a Page");
            return;
        }

        //Page to add to
        Page addTo = null;
        String destination = spinnerImagePage.getSelectedItem().toString();
        //loop through the page list to find page with same name
        for(Page p: game.getPageList()){
            if (p.getPageName().equals(destination)){
                addTo = p;
            }
        }

        //If there is not page with this name
        if(addTo.equals(null)){
            System.out.println("Not a valid page.");
            return;
        }

        if (checkboxImageIsInventory.isChecked()) {
            game.addInventory(newImage);
        } else {
            addTo.addShape(newImage);
        }
        System.out.println("Singleton after adding image " + addTo.getNumShapesOnPage());
    }

    public boolean errorCheck(){
        EditText inputImageShapeName = findViewById(R.id.inputImageShapeName);
        Spinner spinnerImageName = findViewById(R.id.spinnerImageName); // TODO: Remove the EditText placeholder and replace with Spinner later
        Spinner spinnerImagePage = findViewById(R.id.spinnerImagePage);

        if(inputImageShapeName.getText().toString().equals("")){
            Toast.makeText(this, "Must Give Shape Name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(spinnerImagePage.getSelectedItemPosition() == 0){
            Toast.makeText(this, "Must Select Image", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(spinnerImagePage.getSelectedItemPosition() == 0){
            Toast.makeText(this, "Must Select Page", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }
}