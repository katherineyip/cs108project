package edu.stanford.cs108;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class ShapeEditorActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{


        SingletonData singletonData = SingletonData.getInstance(); // Store list of games in memory
        Game game = singletonData.getCurrentGame();
        Shape currentShape = game.currentShape;

        // UI
        Page selectedPage;
        Button buttonSetShape;
        Button buttonCancelSetShape;
        Button buttonNewScript;

        String scriptToAdd = currentShape.getScript();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_shape);

            scriptToAdd = "";

            // Populate our Spinners
            Spinner eventSpinner = findViewById(R.id.evSpin);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.events, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            eventSpinner.setAdapter(adapter);

            Spinner actionSpinner = findViewById(R.id.acSpin);
            ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.actions, android.R.layout.simple_spinner_item);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            actionSpinner.setAdapter(adapter2);

            Spinner imageSpinner = findViewById(R.id.spinnerImageName);
            ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.images, android.R.layout.simple_spinner_item);
            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            imageSpinner.setAdapter(adapter3);

            Spinner fColorSpin = findViewById(R.id.inputFontColor);
            ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(this, R.array.colors, android.R.layout.simple_spinner_item);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            fColorSpin.setAdapter(adapter5);

            Spinner bGColorSpin = findViewById(R.id.inputBackgroundColor);
            bGColorSpin.setAdapter(adapter5);

            loadShapeData();

            // Set up onClickListener on "Submit" to bring user back to EditorActivity
            buttonSetShape = findViewById(R.id.buttonSetShape);
            buttonSetShape.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(inputCheck()) {
                        onSubmit(view); // Create new imageShape
                        System.out.println("game's current page is " + game.currentPage.getPageName());

                        Intent intent = new Intent(edu.stanford.cs108.ShapeEditorActivity.this, EditorActivity.class);
                        startActivity(intent); // Direct user back to EditorActivity
                    }
                }
            });

            // Set up onClickListener on "Cancel" to bring user back to EditorActivity
            buttonCancelSetShape = findViewById(R.id.buttonCancelSetShape);
            buttonCancelSetShape.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("game's current page is " + game.currentPage.getPageName());
                    Intent intent = new Intent(edu.stanford.cs108.ShapeEditorActivity.this, EditorActivity.class);
                    startActivity(intent);
                }
            });

            buttonNewScript = findViewById(R.id.newScriptButton);
            buttonNewScript.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String newScript = getScript();
                    if (!newScript.equals("")){
                        //scriptToAdd = Script.combineScripts(scriptToAdd, newScript);
                    }
                }
            });
        }

        public void onSubmit(View view) {
            // Use to create image
            Spinner spinnerImageName = findViewById(R.id.spinnerImageName);

            // Use to create text
            EditText inputText = findViewById(R.id.inputText);
            EditText inputFontSize = findViewById(R.id.inputFontSize);

            // Shared property across all shapes
            EditText inputShapeName = findViewById(R.id.inputShapeName);
            CheckBox checkboxShapeIsHidden = findViewById(R.id.checkboxShapeIsHidden);
            CheckBox checkboxShapeIsMovable = findViewById(R.id.checkboxShapeIsMovable);
            EditText editShapeXPosition = findViewById(R.id.editShapeXPosition);
            EditText editShapeYPosition = findViewById(R.id.editShapeYPosition);
            EditText editShapeWidth = findViewById(R.id.editShapeWidth);
            EditText editShapeHeight = findViewById(R.id.editShapeHeight);


            //String scrpt = getScript();

            //setting values
            float x = getVal(editShapeXPosition, 20.f);
            float y = getVal(editShapeYPosition, 20.f);

            float width = getVal(editShapeWidth, 100.f);

            float height = getVal(editShapeHeight, 100.f);


            int fZ = (int)getVal(inputFontSize, 40.f);
            int fC = colorPick(R.id.inputFontColor);
            int bC = colorPick(R.id.inputBackgroundColor);

            String  imageName = spinnerImageName.getSelectedItem().toString();
            if(spinnerImageName.getSelectedItemPosition() == 0) {
                imageName = "";
            }

            currentShape.setImage(imageName);
            currentShape.setText(inputText.getText().toString());
            currentShape.setFontSize(fZ);
            currentShape.setFontColor(fC);
            currentShape.setBackgroundColor(bC);
            currentShape.setShapeName(inputShapeName.getText().toString());
            currentShape.setHiddenState(checkboxShapeIsHidden.isChecked());
            currentShape.setMovableState(checkboxShapeIsMovable.isChecked());
            currentShape.setScript(scriptToAdd);
            currentShape.setX(x);
            currentShape.setY(y);
            currentShape.setWidth(width);
            currentShape.setHeight(height);
        }

        public float getVal(EditText et, float def){
            //if empty return default value
            if(et.getText().toString().equals("")){
                return def;
            }

            //if not empty return value of the editText View
            return Float.parseFloat(String.valueOf(et.getText()));
        }

        public int colorPick(int id){
            Spinner colorSpin = findViewById(id);

            if(colorSpin.getSelectedItemPosition() == 1){
                return Color.WHITE;
            } else if(colorSpin.getSelectedItemPosition() == 2){
                return Color.BLUE;
            } else if(colorSpin.getSelectedItemPosition() == 3){
                return Color.RED;
            } else if(colorSpin.getSelectedItemPosition() == 4){
                return Color.GREEN;
            } else if(colorSpin.getSelectedItemPosition() == 5){
                return Color.BLACK;
            }

            return Color.GRAY;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // On selecting a spinner item
            //String gameName = parent.getItemAtPosition(position).toString();
            selectedPage = (Page) parent.getItemAtPosition(position);
            System.out.println("Selected a game: " + selectedPage);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

        public boolean inputCheck(){
            EditText inputShapeName = findViewById(R.id.inputShapeName);
            Spinner spinnerShapePage = findViewById(R.id.spinnerShapePage);

            if(inputShapeName.getText().toString().equals("")){
                Toast.makeText(this, "Must Give Shape Name", Toast.LENGTH_SHORT).show();
                return false;
            }

            return true;
        }

        public String getScript(){
            Spinner actionSpinner = findViewById(R.id.acSpin);
            Spinner eventSpinner = findViewById(R.id.evSpin);
            EditText scriptScript = findViewById(R.id.imgScript);
            EditText scriptTarget = findViewById(R.id.imgTarget);

            String scrpt = "";
            if(eventSpinner.getSelectedItemPosition()!= 0){
                scrpt = eventSpinner.getSelectedItem().toString();

                if(scrpt.equals("on drop")){
                    scrpt += " ";
                    scrpt += scriptTarget.getText().toString();
                }
                scrpt += " ";
                scrpt += actionSpinner.getSelectedItem().toString();
                scrpt += " ";
                scrpt += scriptScript.getText().toString();
                scrpt += ";";

            }
            //System.out.println("Script is: " + scrpt);
            return scrpt;
        }

        public void loadShapeData(){
            EditText inputText = findViewById(R.id.inputText);
            EditText inputFontSize = findViewById(R.id.inputFontSize);
            EditText inputShapeName = findViewById(R.id.inputShapeName);
            CheckBox checkboxShapeIsHidden = findViewById(R.id.checkboxShapeIsHidden);
            CheckBox checkboxShapeIsMovable = findViewById(R.id.checkboxShapeIsMovable);
            EditText editShapeXPosition = findViewById(R.id.editShapeXPosition);
            EditText editShapeYPosition = findViewById(R.id.editShapeYPosition);
            EditText editShapeWidth = findViewById(R.id.editShapeWidth);
            EditText editShapeHeight = findViewById(R.id.editShapeHeight);

            Spinner bColorSpin = findViewById(R.id.inputBackgroundColor);
            Spinner fColorSpin = findViewById(R.id.inputFontColor);

            inputText.setText(currentShape.getText());
            inputFontSize.setText(String.valueOf(currentShape.getFontSize()));
            inputShapeName.setText(currentShape.getShapeName());
            checkboxShapeIsHidden.setChecked(currentShape.isHidden());
            checkboxShapeIsMovable.setChecked(currentShape.isMovable());
            editShapeXPosition.setText(String.valueOf(currentShape.getX()));
            editShapeYPosition.setText(String.valueOf(currentShape.getY()));
            editShapeWidth.setText(String.valueOf(currentShape.getWidth()));
            editShapeHeight.setText(String.valueOf(currentShape.getHeight()));

            System.out.println("Paint color: " + currentShape.getRectPaint().getColor());
            System.out.println("Blue color: " + Color.BLUE);

            int color = currentShape.getRectPaint().getColor();

            if (color == Color.BLACK) {
                bColorSpin.setSelection(5);
            }else if (color == Color.GREEN) {
                bColorSpin.setSelection(4);
            }else if (color == Color.RED) {
                bColorSpin.setSelection(3);
            }else if (color == Color.BLUE) {
                bColorSpin.setSelection(2);
            }else if (color == Color.WHITE) {
                bColorSpin.setSelection(1);
            }


            color = currentShape.getTextPaint().getColor();

            if (color == Color.BLACK) {
                fColorSpin.setSelection(5);
            }else if (color == Color.GREEN) {
                fColorSpin.setSelection(4);
            }else if (color == Color.RED) {
                fColorSpin.setSelection(3);
            }else if (color == Color.BLUE) {
                fColorSpin.setSelection(2);
            }else if (color == Color.WHITE) {
                fColorSpin.setSelection(1);
            }
        }
    }

