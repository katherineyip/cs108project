package edu.stanford.cs108;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

public class GameActivity extends AppCompatActivity {
    // Data
    // Data
    SingletonData singletonData = SingletonData.getInstance(); // Store list of games in memory
    Game game = singletonData.getCurrentGame();
    List<Page> pageList = game.getPageList();
    Page currentPage = game.getCurrentPage();

    // DB
    SharedPreferences gameProgressSharedPref;
    static final String GAME_PROGRESS_SHARED_PREF_FILE = "TempGameProgressPrefs";

    // UI
    PageView pageView; // which has a canvas
    InventoryView inventoryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Get game progress sharedPref file
        gameProgressSharedPref = getSharedPreferences(GAME_PROGRESS_SHARED_PREF_FILE, MODE_PRIVATE);
    }

    /**
     * Menu dropdown:
     * - leave and save game
     * - restart game
     * TODO: Consider adding discard gam
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_player_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuLeaveAndSaveGame:
                saveGame();
                Intent intentEditPage = new Intent(GameActivity.this, MainActivity.class);
                startActivity(intentEditPage);
                return true;
            case R.id.menuRestartGame:
                restartGame();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Save data to Game Progress sharePrefs
     */
    private void saveGame() {
        SharedPreferences.Editor sharedPrefEditor = gameProgressSharedPref.edit();
        // Serialize game object
        Gson gson = new Gson();
        String jsonString = gson.toJson(game);
        System.out.println("LOG: Printing json  --- " + jsonString);

        // Put serialized game object into sharedPrefs file
        sharedPrefEditor.putString(game.getGameID(), jsonString);
        sharedPrefEditor.apply();
        Toast.makeText(GameActivity.this, "Successfully saved " + game.gameName + ".", Toast.LENGTH_SHORT);
        System.out.println("Successfully saved game to sp. Game ID: " + game.getGameID() + ". Game Name: " + game.gameName);
    }

    private void restartGame() {

    }
}