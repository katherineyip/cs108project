package edu.stanford.cs108;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class EditorActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    // Data
    SingletonData singletonData = SingletonData.getInstance(); // Store list of games in memory
    Game game = singletonData.getCurrentGame();
    Page currentPage = singletonData.getCurrentGame().getCurrentPage();

    // UI
    PageView pageView; // which has a canvas
    Spinner pageName;
    Button buttonAddPage;
    Button buttonEditPage;
    Button buttonAddShape; // controls to add shapes to a page

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        System.out.println("Entered editorActivity. Game is " + game.toString());

        // Display current page & the content inside this page
        pageName = findViewById(R.id.pageSpinner);
        pageName.setOnItemSelectedListener(this);
        loadSpinnerData();

        //System.out.println("printing current game: " + singletonData.getCurrentGame());
        //pageName.setText(singletonData.getCurrentGame().getCurrentPage().getPageName());
        // TODO: Render the canvas inside PageView

        // Set up onClick listener on button to add new pages
        buttonAddPage = findViewById(R.id.buttonAddPage);
        buttonAddPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewPage();
            }
        });

        // Set up onClick listener on button to edit the current page
        buttonEditPage = findViewById(R.id.buttonEditPage);
        buttonEditPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditorActivity.this, EditPageActivity.class);
                startActivity(intent);
            }
        });

        // Set up onClick listener on button to add new Image shape
        buttonAddShape = findViewById(R.id.buttonAddShape);
        buttonAddShape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(EditorActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        //String gameName = parent.getItemAtPosition(position).toString();
        currentPage = (Page) parent.getItemAtPosition(position);
        System.out.println("Selected a page: " + currentPage);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     Allow users to add a new page to a game within the Editor Mode.
     Note here we are calling the addPage method in Game Class while modifying our Singleton.
     */
    public void addNewPage() {
        int numPages = game.getPageList().size() + 1;
        Page newPage = new Page("page " + numPages, false, null);
        game.addPage(newPage);
        game.setCurrentPage(newPage);
        //pageName.setText(newPage.getPageName());
    }

    public void renamePage(Page page, String newPageName) {
        if (!game.getPageList().contains(newPageName)) {
            page.setPageName(newPageName);
        }
        // TODO: Throw error if a page name already exist
    }

    public void loadSpinnerData(){
        List<Page> pageList = game.getPageList();

        ArrayAdapter<Page> dataAdapter = new ArrayAdapter<Page>(this, android.R.layout.simple_spinner_item, pageList);
        dataAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        pageName.setAdapter(dataAdapter);

    }

    public void removePage(Page page) {
        game.getPageList().remove(page); // TODO: Need to pass in an index instead of the page..?
    }
}