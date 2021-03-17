package edu.stanford.cs108;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class EditorActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    // Data
    SingletonData singletonData = SingletonData.getInstance(); // Store list of games in memory
    Game game = singletonData.getCurrentGame();
    Page currentPage = game.getCurrentPage();
    List<Page> pageList = game.pageList;

    // UI
    PageView EditorPageView; // which has a canvas
    TextView gameName;
    TextView pageName;
    Spinner pageSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        //SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences("Game Prefs", Context.MODE_PRIVATE);
        //String gameName = sharedPrefs.getString("game name", "");
        // TODO: t1.setText(name);

        // Display current game name
        gameName = findViewById(R.id.gameName);
        gameName. setText(game.toString());

        // Display current page name
        pageName = findViewById(R.id.pageName);
        pageName.setText(currentPage.getPageName());

        // Populate Spinner from Page list
        loadSpinnerPageListData();

        // TODO: Remove later. This is for debugging purpose.
        System.out.println("game list size: " + singletonData.getGameList().size());
        System.out.println("page list size: " + singletonData.getCurrentGame().getPageList().size());
        System.out.println("inventory list size: " + singletonData.getCurrentGame().getInventoryShapeList().size());
        System.out.println("shape list size on this page: " + game.getCurrentPage().getShapeList().size());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        currentPage = (Page) parent.getItemAtPosition(position);

        pageName.setText(currentPage.getPageName());
        System.out.println("Selected a new page: " + currentPage);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Menu dropdown for add / edit controls
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAddPage:
                addNewPage();
                return true;
            case R.id.menuEditPage:
                Intent intentEditPage = new Intent(EditorActivity.this, EditPageActivity.class);
                startActivity(intentEditPage);
                return true;
            case R.id.menuAddShape:
                Intent intentAddShape = new Intent(EditorActivity.this, ShapeActivity.class);
                startActivity(intentAddShape);
                return true;
            case R.id.menuEditGameDetails:
                Intent intentEditGame = new Intent(EditorActivity.this, EditGameActivity.class);
                startActivity(intentEditGame);
                return true;
                // TODO: Add save game
            default:
                return super.onContextItemSelected(item);
        }
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
        pageName.setText(newPage.toString());
        pageSpinner.setSelection(game.pageList.indexOf(currentPage));
        Toast.makeText(EditorActivity.this, "Successfully added " + newPage.getPageName() , Toast.LENGTH_SHORT);
    }

    /*
    public void renamePage(Page page, String newPageName) {
        if (!game.getPageList().contains(newPageName)) {
            page.setPageName(newPageName);
        }
        // TODO: Throw error if a page name already exist
    }
     */

   // public void removePage(Page page) {
    //    game.getPageList().remove(page); // TODO: Need to pass in an index instead of the page..?
   // }

    private void loadSpinnerPageListData() {
        //pageList = game.pageList; // TODO: Get game list from db later

        // Creating adapter for spinner
        ArrayAdapter<Page> dataAdapter = new ArrayAdapter<Page>(this, android.R.layout.simple_spinner_item, pageList);

        // Drop down layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Attach data adapter to spinner
        pageSpinner = findViewById(R.id.pageSpinner);
        pageSpinner.setAdapter(dataAdapter);

        // Always select the current page
        //pageSpinner.setSelection(dataAdapter.getPosition(currentPage));
    }

    @Override
    public void onResume() {
        super.onResume();
        loadSpinnerPageListData();
    }
}