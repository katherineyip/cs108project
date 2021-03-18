package edu.stanford.cs108;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.List;

public class GameActivity extends AppCompatActivity {
    // Data
    Game game;
    List<Page> pageList;

    // UI
    PageView pageView; // which has a canvas
    InventoryView inventoryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }
}