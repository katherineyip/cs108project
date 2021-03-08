package edu.stanford.cs108;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashSet;
import java.util.Set;

public class InventoryView extends View {
    Set<Shape> shapeSet;

    public InventoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        shapeSet = new HashSet<Shape>();
    }

    //need some keep on onMove listneer / ondrop
}
