
package edu.stanford.cs108;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.List;
import java.util.Map;

import edu.stanford.cs108.Script.actionPairs;

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
    
    public Shape getShape(String shapeString) {
		for (Shape shape : game.getCurrentPage().shapeList) {
			if (shape.getShapeName().contentEquals(shapeString)) {
				return shape;
			}
		}
		return null;
	}

	public Page getPage(String pageString) {
		for (Page page : game.pageList) {
			if (page.getPageName().contentEquals(pageString)) {
				return page;
			}
		}
		return null;
	}


	public void hide(String shapeString) {
		Shape shape = getShape(shapeString);
		if (shape != null) {
			shape.setHiddenState(true);
		}
	}

	public void show(String shapeString) {
		Shape shape = getShape(shapeString);
		if (shape != null) {
			shape.setHiddenState(false);
		}
	}

	public void goTo(String pageString) {
		Page page = getPage(pageString);
		if (page != null) {
			game.setCurrentPage(page);
		}
	}

	public void play(String sound) { // needs to ensure that the sound is in the raw directory, does not end in .mp3
		String noise = "R.raw.";
		noise += sound;
		MediaPlayer mp = MediaPlayer.create(getContext(), noise);
		mp.start();

	}


	public void performActions(Script.actionPairs[] actions) {
		for (actionPairs act : actions) {
			switch(act.action) {
			case "goto":
				goTo(act.target);
				break;
			case "hide":
				hide(act.target);
				break;
			case "play":
				play(act.target);
				break;
			case "show":
				show(act.target);
				break;
			}
		}

	}


	public void performTrigger(String trigger, Map<String, Script.actionPairs[]> scriptMap) { 
		if (scriptMap.isEmpty()) {
			return;
		}
		for (String key : scriptMap.keySet()) {
			if (key.contentEquals(trigger)) {
				performActions(scriptMap.get(key));
			}
		}
	}
	
	// TODO: when switches page: performTrigger for all shapes/page on enter
	
	// TODO: integrate into use during view/onClick
	
	// TODO: add sounds to raw
	
}
