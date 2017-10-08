package com.pragyaware.anu.solarpack.mUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by sarbjit on 04/26/2017.
 */
public class TextViewRegular extends TextView {

    public TextViewRegular(Context context) {
        super(context);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "ClearSans-Regular.ttf");
        this.setTypeface(face);
    }

    public TextViewRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "ClearSans-Regular.ttf");
        this.setTypeface(face);
    }

    public TextViewRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "ClearSans-Regular.ttf");
        this.setTypeface(face);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


    }

}