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

        //Populate our Spinners
        Spinner eventSpinner = findViewById(R.id.evSpin2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.events, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventSpinner.setAdapter(adapter);

        Spinner actionSpinner = findViewById(R.id.acSpin2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.actions, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actionSpinner.setAdapter(adapter2);

        Spinner colorSpin = findViewById(R.id.colorSpin);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.colors, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpin.setAdapter(adapter3);

        //Populate Spinner from Page list
        Spinner pageSpinner = findViewById(R.id.inputRectPage);
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
        buttonSetRect = findViewById(R.id.buttonSetRect);
        buttonSetRect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmit(view); // Create new rectShape
                if (errorCheck()) {
                    Intent intent = new Intent(RectShapeActivity.this, EditorActivity.class);
                    startActivity(intent); // Direct user back to EditorActivity
                }
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
        Spinner inputRectPage = findViewById(R.id.inputRectPage); //TODO: Change to dropdown later
        CheckBox checkboxRectIsHidden = findViewById(R.id.checkboxRectIsHidden);
        CheckBox checkboxRectIsMovable = findViewById(R.id.checkboxRectIsMovable);
        CheckBox checkboxRectIsInventory = findViewById(R.id.checkboxRectIsInventory);
        EditText editRectLeftPosition = findViewById(R.id.editRectLeftPosition);
        EditText editRectRightPosition = findViewById(R.id.editRectRightPosition);
        EditText editRectTopPosition = findViewById(R.id.editRectTopPosition);
        EditText editRectBottomPosition = findViewById(R.id.editRectBottomPosition);

        Spinner actionSpinner = findViewById(R.id.acSpin2);
        Spinner eventSpinner = findViewById(R.id.evSpin2);
        EditText scriptScript =findViewById(R.id.rectScript);
        EditText scriptTarget = findViewById(R.id.rectTarget);

        int shapeColor = colorPick();

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



        int index = inputRectPage.getSelectedItemPosition();
        if(index == 0) {
            System.out.println("Must Select Page to Add To");
            return;
        }

        String xs1 = editRectLeftPosition.getText().toString();
        String xs2 = editRectRightPosition.getText().toString();
        String ys1 = editRectTopPosition.getText().toString();
        String ys2 = editRectBottomPosition.getText().toString();
        int x1 = Integer.valueOf(xs1);
        int x2 = Integer.valueOf(xs2);
        int y1 = Integer.valueOf(ys1);
        int y2 = Integer.valueOf(ys2);

        int width = x2-x1;
        int height = y2 - y1;



        RectShape newRect = new RectShape(shapeColor,
                inputRectShapeName.getText().toString(),
                checkboxRectIsHidden.isChecked(),
                checkboxRectIsMovable.isChecked(),
                checkboxRectIsInventory.isChecked(), script,
                (float)x1, (float)y1, (float)width, (float)height); // TODO: Replace later





        //Page to add to
        Page addTo = null;
        String destination = inputRectPage.getSelectedItem().toString();
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

        if (checkboxRectIsInventory.isChecked()) {
            game.addInventory(newRect);
        } else {
            addTo.addShape(newRect); //TODO: Need to pass in user's page selection
        }
        System.out.println("Singleton after adding rect " + addTo.getNumShapesOnPage());
    }

    public int colorPick(){
        Spinner colorSpin = findViewById(R.id.colorSpin);

        if(colorSpin.getSelectedItemPosition() == 1){
            return Color.WHITE;
        } else if(colorSpin.getSelectedItemPosition() == 2){
            return Color.BLUE;
        } else if(colorSpin.getSelectedItemPosition() == 3){
            return Color.RED;
        } else if(colorSpin.getSelectedItemPosition() == 4){
            return Color.GREEN;
        }
        return Color.BLACK;
    }

    public boolean errorCheck(){
        EditText inputRectShapeName = findViewById(R.id.inputImageShapeName);
        Spinner inputRectPage = findViewById(R.id.inputRectPage);


        if(inputRectShapeName.getText().toString().equals("")){
            Toast.makeText(this, "Must Give Text Name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(inputRectPage.getSelectedItemPosition() == 0){
            Toast.makeText(this, "Must Select Page", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }
}