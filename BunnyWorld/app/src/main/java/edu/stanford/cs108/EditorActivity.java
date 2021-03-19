package edu.stanford.cs108;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

public class EditorActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    // Data
    SingletonData singletonData = SingletonData.getInstance(); // Store list of games in memory
    Game game = singletonData.getCurrentGame();
    List<Page> pageList;
    Page currentPage;

    // DB
    SharedPreferences gameConfigSharedPref;
    static final String GAME_CONFIG_SHARED_PREF_FILE = "TempGamePrefs";

    // UI
    View editorPageView; // which has a canvas
    TextView gameName;
    TextView pageName;
    Spinner pageSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        //TODO: REMOVE DEBUG
        System.out.println("---LOG FROM EditorActivity--- Enter onCreate in EditorActivity ");

        // Get sharedPref file
        gameConfigSharedPref = getSharedPreferences(GAME_CONFIG_SHARED_PREF_FILE, MODE_PRIVATE);

        // Set up current page
        if (game.getCurrentPage() == null) {
            currentPage = game.getStarterPage();
        } else {
            currentPage = game.getCurrentPage();
        }

        // Get Page List from game
        pageList = game.pageList;

        // Display current game name
        gameName = findViewById(R.id.gameName);
        gameName. setText(game.toString());

        // Display current page name
        pageName = findViewById(R.id.pageName);
        pageName.setText(currentPage.getPageName());

        // Canvas
        editorPageView = findViewById(R.id.editorPageView);

        // Populate Spinner from Page list
        loadSpinnerPageListData();

        // TODO: Remove later. This is for debugging purpose.
        System.out.println("---LOG FROM EditorActivity--- Editing game from singleton.getCurrentGame: " + singletonData.getCurrentGame());
        System.out.println("---LOG FROM EditorActivity--- Editing game from game: " + game);
        System.out.println("---LOG FROM EditorActivity--- # of game config is: " + singletonData.getGameConfigList().size());

        /**
         * Set up onClick listener on "Go to page" button to allow user navigate between pages
         */
        final Button buttonGoToPage = findViewById(R.id.buttonGoToPage);
        buttonGoToPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set selectedGame in Singleton according to user's dropdown choice
                currentPage = (Page) pageSpinner.getSelectedItem();
                game.setCurrentPage(currentPage);
                pageName.setText(currentPage.toString());
                System.out.println("---LOG FROM EditorActivity--- current page from singleton: " + singletonData.getCurrentGame().getCurrentPage());
                System.out.println("---LOG FROM EditorActivity--- current page from currentPage: " + currentPage);
                Toast.makeText(EditorActivity.this, "You're now on: " + currentPage.toString(), Toast.LENGTH_SHORT).show();
                editorPageView.invalidate();
                //Intent intent = new Intent(EditorActivity.this, EditorActivity.class);
                //startActivity(intent);
            }
        });
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event){
//        List<Shape> pageShapeList = currentPage.getShapeList();
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                for (int i = pageShapeList.size() - 1; i >= 0; i--) {
//                    Shape shapeInQuestion = pageShapeList.get(i);
//
//                    if (shapeInQuestion.isClicked(event.getX(), event.getY())) {
//                        game.setCurrentShape(shapeInQuestion);
//                        return true;
//                    }
//                }
//                return true;
//        }
//        return true;
//    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Menu dropdown:
     * - adding shapes / pages
     * - editing pages/ game
     * - saving game
     * - discard edits
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
            case R.id.menuSaveGame:
                saveData();
                Intent intentSaveGame = new Intent(EditorActivity.this, MainActivity.class);
                startActivity(intentSaveGame);
                return true;
            case R.id.menuDiscardAllEdits:
                Intent intentDiscardAllEdits = new Intent(EditorActivity.this, MainActivity.class);
                startActivity(intentDiscardAllEdits);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     Allow users to add a new page to a game within the Editor Mode.
     Note here we are calling the addPage method in Game Class while modifying our Singleton.
     */
    public void addNewPage() {
        Page newPage = new Page("page " + game.nextPageID, false, game.nextPageID, null);
        game.addPage(newPage);
        pageSpinner.setSelection(game.pageList.indexOf(currentPage));
        Toast.makeText(EditorActivity.this, "Successfully added " + newPage.getPageName() , Toast.LENGTH_SHORT);
    }

    private void loadSpinnerPageListData() {
        // Creating adapter for spinner
        ArrayAdapter<Page> dataAdapter = new ArrayAdapter<Page>(this, android.R.layout.simple_spinner_item, pageList);

        // Drop down layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Attach data adapter to spinner
        pageSpinner = findViewById(R.id.pageSpinner);
        pageSpinner.setAdapter(dataAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadSpinnerPageListData();
    }

    /**
     * Save data to sharePrefs
     */
    private void saveData() {
        SharedPreferences.Editor sharedPrefEditor = gameConfigSharedPref.edit();

        // Serialize game object
        Gson gson = new Gson();
        String jsonString = gson.toJson(game);
        System.out.println("LOG: Printing json  --- " + jsonString);

        // Put serialized game object into sharedPrefs file
        sharedPrefEditor.putString(game.getGameID(), jsonString);
        sharedPrefEditor.apply(); //prob don't use sharedPrefEditor.commit();
        Toast.makeText(EditorActivity.this, "Successfully saved " + game.gameName + ".", Toast.LENGTH_SHORT);
        System.out.println("Successfully saved game to sp. Game ID: " + game.getGameID() + ". Game Name: " + game.gameName);
    }

    public void editShape(View view) {
        if(SingletonData.getInstance().getCurrentGame().currentShape == null) {
            Toast.makeText(this, "Must Select Shape", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intentEditShape = new Intent(EditorActivity.this, ShapeEditorActivity.class);
        startActivity(intentEditShape);
    }
}