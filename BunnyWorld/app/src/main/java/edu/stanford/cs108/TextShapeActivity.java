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

public class TextShapeActivity extends AppCompatActivity {

    // Data
    SingletonData singletonData = SingletonData.getInstance(); // Store list of games in memory
    Game game = singletonData.getCurrentGame();

    // UI
    Button buttonSetTxt;
    Button buttonCancelTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_shape);

        //Populate our Spinners
        Spinner eventSpinner = findViewById(R.id.evSpin3);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.events, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventSpinner.setAdapter(adapter);

        Spinner actionSpinner = findViewById(R.id.acSpin3);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.actions, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actionSpinner.setAdapter(adapter2);

        //Populate Spinner from Page list
        Spinner pageSpinner = findViewById(R.id.inputTextPage);
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
        buttonSetTxt = findViewById(R.id.buttonSetText);
        buttonSetTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmit(view); // Create new rectShape
                if(errorCheck()) {
                    Intent intent = new Intent(TextShapeActivity.this, EditorActivity.class);
                    startActivity(intent); // Direct user back to EditorActivity
                }
            }
        });

        // Set up onClickListener on "Cancel" to bring user back to EditorActivity
        buttonCancelTxt = findViewById(R.id.buttonCancelText);
        buttonCancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("game's current page is " + game.currentPage.getPageName());

                Intent intent = new Intent(TextShapeActivity.this, EditorActivity.class);
                startActivity(intent);

            }

        });
    }

    public void onSubmit(View view) {
        EditText inputTxtShapeName = findViewById(R.id.inputTxtShapeName);
        EditText addedText = findViewById(R.id.inputText);
        EditText inputFontSz = findViewById(R.id.fontSize);
        Spinner inputTxtPage = findViewById(R.id.inputTextPage);
        CheckBox checkboxTxtIsHidden = findViewById(R.id.checkboxRectIsHidden);
        CheckBox checkboxTxtIsMovable = findViewById(R.id.checkboxRectIsMovable);
        CheckBox checkboxTxtIsInventory = findViewById(R.id.checkboxRectIsInventory);
        EditText editTxtLeftPosition = findViewById(R.id.editRectLeftPosition);
        EditText editTxtRightPosition = findViewById(R.id.editRectRightPosition);
        EditText editTxtTopPosition = findViewById(R.id.editRectTopPosition);
        EditText editTxtBottomPosition = findViewById(R.id.editRectBottomPosition);

        Spinner actionSpinner = findViewById(R.id.acSpin3);
        Spinner eventSpinner = findViewById(R.id.evSpin3);
        EditText scriptScript = findViewById(R.id.txtScript);
        EditText scriptTarget = findViewById(R.id.txtTarget);

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

        String txt = addedText.getText().toString();
        String fontSize = inputFontSz.getText().toString();


        String xs1 = editTxtLeftPosition.getText().toString();
        String xs2 = editTxtRightPosition.getText().toString();
        String ys1 = editTxtTopPosition.getText().toString();
        String ys2 = editTxtBottomPosition.getText().toString();
        int x1 = Integer.valueOf(xs1);
        int x2 = Integer.valueOf(xs2);
        int y1 = Integer.valueOf(ys1);
        int y2 = Integer.valueOf(ys2);

        int width = x2-x1;
        int height = y2-1;

        TextShape newTxt = new TextShape(txt,
                Integer.valueOf(fontSize),
                inputTxtShapeName.getText().toString(),
                checkboxTxtIsHidden.isChecked(),
                checkboxTxtIsMovable.isChecked(),
                checkboxTxtIsInventory.isChecked(),
                script, (float)x1, (float)y1, (float)width, (float)height); // TODO: Replace later



        //Page to add to
        Page addTo = null;
        String destination = inputTxtPage.getSelectedItem().toString();
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

        if (checkboxTxtIsInventory.isChecked()) {
            game.addInventory(newTxt);
        } else {
            addTo.addShape(newTxt); //TODO: Need to pass in user's page selection
        }
        System.out.println("Singleton after adding rect " + addTo.getNumShapesOnPage());
    }

    public boolean errorCheck(){
        EditText inputTxtShapeName = findViewById(R.id.inputTxtShapeName);
        EditText addedText = findViewById(R.id.inputText);
        EditText inputFontSz = findViewById(R.id.fontSize);
        Spinner inputTxtPage = findViewById(R.id.inputTextPage);

        if(inputTxtShapeName.getText().toString().equals("")){
            Toast.makeText(this, "Must Give Shape Name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(addedText.getText().toString().equals("")){
            Toast.makeText(this, "Must Enter Text", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(inputFontSz.getText().toString().equals("")){
            Toast.makeText(this, "Must Set Font Size", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(inputTxtPage.getSelectedItemPosition() == 0){
            Toast.makeText(this, "Must Select Page", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }
}
