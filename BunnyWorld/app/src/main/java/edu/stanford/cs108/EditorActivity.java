package edu.stanford.cs108;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import java.util.List;

public class EditorActivity extends AppCompatActivity {
    // Data
    Game game;
    List<Page> pageList;
    //currentPage? -- so I can set current page name

    // UI
    PageView pageView; // which has a canvas
    Button buttonAddImage; // controls to add shapes to a page
    Button buttonAddRect;
    Button buttonAddText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
    }

    public void addNewPage() {

    }

    public void removePage() {

    }

    public void addImageShape() {

    }

    public void addNewTextShape() {

    }

    public void addNewRectShape() {

    }

    public void removeShape() {

    }
}