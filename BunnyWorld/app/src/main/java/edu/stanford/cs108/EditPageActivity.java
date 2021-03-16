package edu.stanford.cs108;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_page);

        // Display this page's current data
        inputPageName = findViewById(R.id.inputPageName);
        inputPageName.setText(currentPage.getPageName());
        checkboxIsStarterPage = findViewById(R.id.checkboxIsStarterPage);
        checkboxIsStarterPage.setChecked(currentPage.isStarterPage());

        // If this page is a starter page, user cannot make this page a non-starter page.
        if (checkboxIsStarterPage.isChecked() && currentPage.isStarterPage) {
            checkboxIsStarterPage.setEnabled(false);
        }

        // Set up onClickListener on "Save" to save page changes bring user back to EditorActivity
        buttonSavePageChanges = findViewById(R.id.buttonSavePageChanges);
        buttonSavePageChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EditText inputPageName = findViewById(R.id.inputPageName);
                //CheckBox checkboxIsStarterPage = findViewById(R.id.checkboxIsStarterPage);

                setPageName();
                setStarterPage();

                // TODO: currentPage.setScript();

                Intent intent = new Intent(EditPageActivity.this, EditorActivity.class);
                startActivity(intent);
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

    public void setPageName() {
        currentPage.setPageName(inputPageName.getText().toString());
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
}