package edu.stanford.cs108;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class EditGameActivity extends AppCompatActivity {
    // Data
    SingletonData singletonData = SingletonData.getInstance();

    // UI
    EditText inputGameName;
    Button buttonSaveGameChanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_game);

        inputGameName = findViewById(R.id.inputGameName);
        inputGameName.setText(singletonData.getCurrentGame().gameName);

        buttonSaveGameChanges = findViewById(R.id.buttonSaveGameChanges);
        buttonSaveGameChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputGameName = findViewById(R.id.inputGameName);

                if (isValidGameName(inputGameName.getText().toString())) {
                    Game game = singletonData.getCurrentGame();
                    game.setGameName(inputGameName.getText().toString());
                    Intent intent = new Intent(EditGameActivity.this, EditorActivity.class);
                    startActivity(intent);
                } else {
                    inputGameName.setError("This game name already exists.");
                }
            }
        });
    }

    // Check to ensure no games have duplicated names
    private boolean isValidGameName(String gameName) {
        if (gameName.equals(singletonData.getCurrentGame().gameName)) { // No change is acceptable
            return true;
        }

        List<Game> gameList = singletonData.getGameConfigList();

        for (Game game : gameList) {
            if (game.getGameName().equals(gameName)) {
                return false;
            }
        }
        return true;
    }
}