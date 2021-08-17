package com.example.serenitea;

import android.graphics.Bitmap;
import android.view.View;

public class Screenshot {
    public static Bitmap takeScreenShot(View v){
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        return b;
    }
    public static Bitmap takeScreenShotOfRootView(View v){
        return takeScreenShot(v);
    }
}
