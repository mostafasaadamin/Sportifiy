package com.work.unknown.sportifiy.Retrofit;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * Created by unknown on 6/18/2018.
 */

public class common {
    public static  IGoCordinates getGeoCodeService()
    {

        return retrofitCleint.getClient("https://maps.googleapis.com").create(IGoCordinates.class);
    }
    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight)
    {
        Bitmap ScaleBitmap=Bitmap.createBitmap(newWidth,newHeight,Bitmap.Config.ARGB_8888);
        float scalex=newWidth/(float)bitmap.getWidth();
        float scaley=newHeight/(float)bitmap.getHeight();
        float pivotx=0;float pivoty=0;
        Matrix scaleMatrix=new Matrix();
        scaleMatrix.setScale(scalex,scaley,pivotx,pivoty);
        Canvas canvas=new Canvas(ScaleBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap,0,0,new Paint(Paint.FILTER_BITMAP_FLAG));
        return ScaleBitmap;


    }

}
