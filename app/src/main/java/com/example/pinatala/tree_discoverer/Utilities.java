package com.example.pinatala.tree_discoverer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by YouYang on 23/11/16.
 */

public class Utilities {

    //Method to convert from the bitmap format to ByteArray
    public static byte[] bitmapToByteArray(Bitmap image){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        return byteArray;
    }

    //Method to convert from the ByteArray format to bitmap
    public static Bitmap byteArrayToBitmap(byte[] byteArray){
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return bitmap;
    }

}
