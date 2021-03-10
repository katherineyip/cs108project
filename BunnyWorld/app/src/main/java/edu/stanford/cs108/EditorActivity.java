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
    Game game; // Current game
    List<Page> pageList; // Just a reference to game.pageList
    Page currentPage;

    // UI
    PageView pageView; // which has a canvas
    TextView pageName;
    Button buttonAddPage;
    Button buttonAddImage; // controls to add shapes to a page
    Button buttonAddRect;
    Button buttonAddText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        /* TODO: Remove all these shit after setting up singleton
        Intent intent = getIntent();
        String imageNameString = intent.getStringExtra(ImageShapeActivity.IMAGE_NAME_EXTRA);
        String shapeNameString = intent.getStringExtra(ImageShapeActivity.SHAPE_NAME_EXTRA);
        String PageString = intent.getStringExtra(ImageShapeActivity.PAGE_EXTRA);
        boolean isImageHidden = intent.getBooleanExtra(ImageShapeActivity.IS_HIDDEN_EXTRA, false);
        boolean isImageMovable = intent.getBooleanExtra(ImageShapeActivity.IS_MOVABLE_EXTRA, false);
        int imageLeft = intent.getIntExtra(ImageShapeActivity.IMAGE_LEFT_EXTRA, 0);
        int imageRight = intent.getIntExtra(ImageShapeActivity.IMAGE_RIGHT_EXTRA, 0);
        int imageTop = intent.getIntExtra(ImageShapeActivity.IMAGE_TOP_EXTRA, 0);
        int imageBottom = intent.getIntExtra(ImageShapeActivity.IMAGE_BOTTOM_EXTRA, 0);
        */

        // Set up a new game
        game = new Game("New Test Game"); // TODO: Add some ways to programmatically assign new name?
        pageList = game.pageList;
        currentPage = pageList.get(0);
        singletonData.addGameToList(game);
        singletonData.setCurrentGame(game);
        System.out.println("Singleton Data to String: " + singletonData.toString());

        // Display current page & the content inside this page
        pageName = findViewById(R.id.pageName);
        pageName.setText(currentPage.getPageName());
        // TODO: Render the canvas inside PageView

        // Set up onClick listener on button to add new Image shape
        buttonAddPage = findViewById(R.id.buttonAddPage);
        buttonAddPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewPage();
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
    }

    /*
     Allow users to add a new page to a game within the Editor Mode.
     Note here we are calling the addPage method in Game Class while modifying our Singleton.
     */
    public void addNewPage() {
        int numPages = pageList.size() + 1;
        Page newPage = new Page("page " + numPages, false);
        game.addPage(newPage);
        // TODO: Prob doesn't have to call Singleton. all the time because the reference has already been established
        // singletonData.getCurrentGame().addPage(newPage);

        currentPage = newPage;
        pageName.setText(currentPage.getPageName());
    }


    public void renamePage(Page page, String newPageName) {
        if (!pageList.contains(newPageName)) {
            page.setPageName(newPageName);
        }
        // TODO: Throw error if a page name already exist
    }

    public void removePage(Page page) {
        pageList.remove(page); // TODO: Need to pass in an index instead of the page..?
    }

    public void addImageShape() {
        int numShapes = currentPage.shapeList.size() + 1;
        //ImageShape newImageShape = new ImageShape(); // TODO: need to pass in input from UI

        /*
        String imageName,
        String shapeName,
        Page page,
        boolean isHidden,
        boolean isMovable,
        String shapeScript
         */
    }

    public void addNewTextShape() {
        //TextShape newTextShape = new TextShape(); // TODO: need to pass in input from UI
    }

    public void addNewRectShape() {
        //RectShape newRectShape = new RectShape();
    }

    public void removeShape() {

    }
}