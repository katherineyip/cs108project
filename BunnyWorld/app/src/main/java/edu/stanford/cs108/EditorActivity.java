package edu.stanford.cs108;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class EditorActivity extends AppCompatActivity {
    // Data
    SingletonData singletonData = SingletonData.getInstance(); // Store list of games in memory
    Game game = singletonData.getCurrentGame();
    Page currentPage = singletonData.getCurrentGame().getCurrentPage();

    // UI
    PageView pageView; // which has a canvas
    TextView pageName;
    Button buttonAddPage;
    Button buttonEditPage;
    Button buttonAddImage; // controls to add shapes to a page
    Button buttonAddRect;
    Button buttonAddText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        System.out.println("Entered editorActivity. Game is " + game.toString());

        // Display current page & the content inside this page
        pageName = findViewById(R.id.pageName);
        pageName.setText(currentPage.getPageName());
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
        buttonAddImage = findViewById(R.id.buttonAddImage);
        buttonAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditorActivity.this, ImageShapeActivity.class);
                startActivity(intent);
            }
        });

        // Set up onClick listener on button to add new Rect shape
        buttonAddRect = findViewById(R.id.buttonAddRect);
        buttonAddRect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditorActivity.this, RectShapeActivity.class);
                startActivity(intent);
            }
        });

        // Set up onClick listener on button to add new Text shape
        buttonAddText = findViewById(R.id.buttonAddText);
        buttonAddText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditorActivity.this, TextShapeActivity.class);
                startActivity(intent);
            }
        });

        // TODO: Remove later. This is for debugging purpose.
        System.out.println("game list size: " + singletonData.getGameList().size());
        System.out.println("page list size: " + singletonData.getCurrentGame().getPageList().size());
        System.out.println("inventory list size: " + singletonData.getCurrentGame().getInventoryShapeList().size());
        System.out.println("shape list size on this page: " + game.getCurrentPage().getShapeList().size());
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
        pageName.setText(newPage.getPageName());
    }

    public void renamePage(Page page, String newPageName) {
        if (!game.getPageList().contains(newPageName)) {
            page.setPageName(newPageName);
        }
        // TODO: Throw error if a page name already exist
    }

    public void removePage(Page page) {
        game.getPageList().remove(page); // TODO: Need to pass in an index instead of the page..?
    }
}