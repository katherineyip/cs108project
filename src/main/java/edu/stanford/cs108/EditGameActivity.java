package edu.stanford.cs108;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

                Game game = singletonData.getCurrentGame();
                game.setGameName(inputGameName.getText().toString());
                Intent intent = new Intent(EditGameActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
    }
}