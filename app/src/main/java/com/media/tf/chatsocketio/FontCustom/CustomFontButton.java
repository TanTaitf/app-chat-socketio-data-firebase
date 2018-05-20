package com.media.tf.chatsocketio.FontCustom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.media.tf.chatsocketio.R;

/**
 * Created by Windows 8.1 Ultimate on 12/08/2017.
 */

public class CustomFontButton extends Button {

    public CustomFontButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomFontButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomFontButton(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/myriadpro.ttf");
        setTypeface(tf);
    }

}