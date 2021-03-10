package edu.stanford.cs108;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

// An Editor View has 1:N pages, a couple buttons to add shapes
public class PageView extends View {
    Canvas canvas;
    Page page;

    public PageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onDraw(Canvas canvas) {

    }
}
