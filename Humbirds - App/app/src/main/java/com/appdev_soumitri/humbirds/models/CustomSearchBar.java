package com.appdev_soumitri.humbirds.models;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.LruCache;
import android.view.KeyEvent;


import com.appdev_soumitri.humbirds.R;

public class CustomSearchBar extends androidx.appcompat.widget.AppCompatEditText {

    private static LruCache<String, Typeface> sTypefaceCache =
            new LruCache<String, Typeface>(12);

    public CustomSearchBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Get our custom attributes
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.Typeface, 0, 0);

        try {
            String typefaceName = a.getString(
                    R.styleable.Typeface_typeface);

            if (!isInEditMode() && !TextUtils.isEmpty(typefaceName)) {
                Typeface typeface = sTypefaceCache.get(typefaceName);

                if (typeface == null) {
                    typeface = Typeface.createFromAsset(context.getAssets(),
                            String.format("fonts/%s.otf", typefaceName));


                    // Cache the Typeface object
                    sTypefaceCache.put(typefaceName, typeface);
                }
                setTypeface(typeface);

                // Note: This flag is required for proper typeface rendering
                setPaintFlags(getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
            }
        } finally {
            a.recycle();
        }
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK &&
                event.getAction() == KeyEvent.ACTION_UP) {
            this.clearFocus();
            return false;
        }
        return super.dispatchKeyEvent(event);
    }
}