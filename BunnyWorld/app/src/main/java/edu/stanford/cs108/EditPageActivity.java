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

public class EditPageActivity extends AppCompatActivity {
    // Data
    SingletonData singletonData = SingletonData.getInstance(); // Store list of games in memory
    Game game = singletonData.getCurrentGame();
    Page currentPage = game.getCurrentPage();

    // UI
    EditText inputPageName;
    CheckBox checkboxIsStarterPage;
    Button buttonSavePageChanges;
    Button buttonCancelPageChanges;
    Button buttonRemovePage;
    Button buttonNewScript;
    String scriptToAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_page);
        scriptToAdd = "";

        // Display this page's current data
        inputPageName = findViewById(R.id.inputPageName);
        inputPageName.setText(currentPage.getPageName());
        checkboxIsStarterPage = findViewById(R.id.checkboxIsStarterPage);
        checkboxIsStarterPage.setChecked(currentPage.isStarterPage());

        // If this page is a starter page, user cannot make this page a non-starter page.
        if (checkboxIsStarterPage.isChecked() && currentPage.isStarterPage) {
            checkboxIsStarterPage.setEnabled(false);
        }

        //Populate spinners
        Spinner eventSpinner = findViewById(R.id.evSpinPage);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.events, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventSpinner.setAdapter(adapter);

        Spinner actionSpinner = findViewById(R.id.acSpinPage);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.actions, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actionSpinner.setAdapter(adapter2);

        // Set up onClickListener on "Save" to save page changes bring user back to EditorActivity
        buttonSavePageChanges = findViewById(R.id.buttonSavePageChanges);
        buttonSavePageChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ensure there is no duplicated page names
                String newPageName = inputPageName.getText().toString();
                if (isValidName(newPageName)) {
                    setPageName(newPageName);
                    setStarterPage();
                    checkScripts();
                    setPageScript();
                    Intent intent = new Intent(EditPageActivity.this, EditorActivity.class);
                    startActivity(intent);
                } else {
                    inputPageName.setError("This page name already exists.");
                }

            }
        });

        // Set up onClickListener on "Cancel" to bring user back to EditorActivity
        buttonCancelPageChanges = findViewById(R.id.buttonCancelPageChanges);
        buttonCancelPageChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditPageActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // Set up onClickListener on "Remove Page" to remove page and bring user back to EditorActivity
        buttonRemovePage = findViewById(R.id.buttonRemovePage);
        buttonRemovePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removePage(currentPage);
            }
        });

        buttonNewScript = findViewById(R.id.newScriptButtonPage);
        buttonNewScript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText scriptScript = findViewById(R.id.imgScriptPage);
                EditText targetScript = findViewById(R.id.imgTargetPage);
                if (checkScripts()) {

                    String newScript = getPageScript();
                    if (!newScript.equals("")) {
                        scriptToAdd = Script.combineScripts(scriptToAdd, newScript);
                    }

                    actionSpinner.setSelection(0);
                    eventSpinner.setSelection(0);
                    scriptScript.setText("");
                    targetScript.setText("");

                    Toast.makeText(view.getContext(), "Script Added!", Toast.LENGTH_SHORT).show();
                    
                }
            }
        });
    }

    public void removePage(Page page) {
        if (page.isStarterPage()) {
            Toast.makeText(this, "Cannot remove a starter page", Toast.LENGTH_SHORT).show();
        } else {
            game.removePage(page);
            currentPage = game.getStarterPage(); // Direct user back to the starter page
            Toast.makeText(this,
                    "Successfully removed " + page.toString(),
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(EditPageActivity.this, EditorActivity.class);
            startActivity(intent);
        }
    }

    public void setPageName(String newName) {
        currentPage.setPageName(newName);
    }

    public void setStarterPage() {
        // If other page is a starter page, make the other page a non-starter page and make this page a starter page
        if (checkboxIsStarterPage.isChecked() && !currentPage.isStarterPage()) {
            game.setStarterPage(currentPage);
            Toast.makeText(this,
                    "Successfully assigned " + currentPage.toString() + " as starter page",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void setPageScript() {
        currentPage.updateScript(scriptToAdd);
    }


    public String getPageScript() {
        Spinner actionSpinner = findViewById(R.id.acSpinPage);
        Spinner eventSpinner = findViewById(R.id.evSpinPage);
        EditText scriptScript = findViewById(R.id.imgScriptPage);
        EditText scriptTarget = findViewById(R.id.imgTargetPage);

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
            if (actionSpinner.getSelectedItem().toString().equals("goto")) {
                scrpt += game.getPageIDFromName(scriptScript.getText().toString());
            } else if (actionSpinner.getSelectedItem().toString().equals("hide") || actionSpinner.getSelectedItem().toString().equals("show")) {
                scrpt += game.getShapeIDFromName(scriptScript.getText().toString());
            } else if (actionSpinner.getSelectedItem().toString().equals("play")) {
                scrpt += scriptScript.getText().toString();
            }
            scrpt += ";";

        }
        return scrpt;
    }


    private boolean isValidName(String name) {
        if (name.equals("") || name == null) {
            return false;
        }

        if (name.equals(currentPage.getPageName())) { // No change is acceptable
            return true;
        }

        for (Page page : game.getPageList()) {
            if (page.getPageName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkScripts() {
        Spinner eventSpinner = findViewById(R.id.evSpinPage);
        if (eventSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Must enter script", Toast.LENGTH_SHORT).show();
            return false;
        }

        Spinner actionSpinner = findViewById(R.id.acSpinPage);
        EditText scriptScript = findViewById(R.id.imgScriptPage);
        String objName = scriptScript.getText().toString();
        String[] sounds = new String[7];
        sounds[0] = "carrotcarrotcarrot";
        sounds[1] = "evillaugh";
        sounds[2] = "fire";
        sounds[3] = "hooray";
        sounds[4] = "munch";
        sounds[5] = "munching";
        sounds[6] = "woof";

        if (eventSpinner.getSelectedItem().toString().equals("on drop")) {
            EditText dropTarget = findViewById(R.id.imgTargetPage);
            String shapeName = dropTarget.getText().toString();
            String shapeID = game.getShapeIDFromName(shapeName);
            if (shapeID == null || shapeID == "") {
                Toast.makeText(this, "Must enter Shape with on drop", Toast.LENGTH_SHORT).show();
                return false;
            }
        }


        switch(actionSpinner.getSelectedItem().toString()) {
            case "goto":
                boolean goTo = false;
                for (Page page : game.pageList) {
                    if (page.getPageName().equals(objName)) {
                        goTo = true;
                    }
                }
                if (!goTo) {
                    Toast.makeText(this, "Must enter Page with goto", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
            case "hide":
                boolean hide = false;
                for (Page page : game.pageList) {
                    for (Shape shape : page.shapeList)
                        if (shape.getShapeName().equals(objName)) {
                            hide = true;
                        }
                }
                if (!hide) {
                    Toast.makeText(this, "Must enter Shape with hide", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
            case "show":
                boolean show = false;
                for (Page page : game.pageList) {
                    for (Shape shape : page.shapeList)
                        if (shape.getShapeName().equals(objName)) {
                            show = true;
                        }
                }
                if (!show) {
                    Toast.makeText(this, "Must enter Shape with show", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
            case "play":
                boolean play = false;
                for (String sound : sounds) {
                    if (sound.equals(objName)) {
                        play = true;
                    }
                }
                if (!play) {
                    Toast.makeText(this, "Must enter Sound with play", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
        }

        return true;
    }
}
