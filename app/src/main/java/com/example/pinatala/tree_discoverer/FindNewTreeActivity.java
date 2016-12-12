package com.example.pinatala.tree_discoverer;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pinatala.tree_discoverer.database.TreeDatabaseOpenHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by YouYang on 23/11/16.
 */

public class FindNewTreeActivity extends AppCompatActivity {
    public static final String TAG = FindNewTreeActivity.class.getSimpleName();

    private ImageView treeImageView;
    private ImageView leafImageView;
    private Double lat;
    private Double lon;
    private String img1;
    private String img2;

    Activity thisActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_layout);
        thisActivity = this;
        Intent data = getIntent();
        Bundle bundle = data.getBundleExtra("extras");
        byte[] treeImageByte = bundle.getByteArray("tree_image");
        byte[] leafImageByte = bundle.getByteArray("leaf_image");

        Bitmap treeImage = Utilities.byteArrayToBitmap(treeImageByte);
        Bitmap leafImage = Utilities.byteArrayToBitmap(leafImageByte);

        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmSS").format(new Date());
        img1="tree_"+ timeStamp +".jpg";
        img2="leaf_"+ timeStamp +".jpg";

        storeImage(treeImage, img1);
        storeImage(leafImage, img2);

        Bundle locBundle = data.getBundleExtra("location");
        lat = (Double) locBundle.get("lat");
        lon = (Double) locBundle.get("lon");

        treeImageView = (ImageView) findViewById(R.id.treeImageView);
        leafImageView = (ImageView) findViewById(R.id.leafImageView);

        treeImageView.setImageBitmap(treeImage);
        leafImageView.setImageBitmap(leafImage);

//        new SaveToDatabase().execute();

        Log.d("TreeAct", "onCreateCalled");

    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d("TreeAct", "onResumeCalled");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d("TreeAct", "onPauseCalled");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d("TreeAct", "onStopCalled");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("TreeAct", "onDestroyCalled");
    }


    private void storeImage(Bitmap image, String fileName) {
        File pictureFile = getOutputMediaFile(fileName);
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    /** Create a File for saving an image or video */
    private  File getOutputMediaFile(String fileName){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + fileName);
        return mediaFile;
    }

    private class SaveToDatabase extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {

            TreeDatabaseOpenHelper databaseOpenHelper = new TreeDatabaseOpenHelper(getApplicationContext());
            SQLiteDatabase database = databaseOpenHelper.getWritableDatabase();
            String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:SS").format(new Date());

            ContentValues values = new ContentValues();
            values.put(TreeDatabaseOpenHelper.KEY_DATE, timeStamp);
            values.put(TreeDatabaseOpenHelper.KEY_LATITUDE, lat);
            values.put(TreeDatabaseOpenHelper.KEY_LONGITUDE, lon);
            values.put(TreeDatabaseOpenHelper.KEY_TREE_PHOTO_NAME, img1);
            values.put(TreeDatabaseOpenHelper.KEY_LEAF_PHOTO_NAME, img2);

            long inserted = database.insert(TreeDatabaseOpenHelper.TREE_TABLE_NAME, null, values);

            Log.d("Database", "Insert operation result "+ inserted);
            database.close();
            return null;
        }
    }

//    private void SaveImage(Bitmap finalBitmap) {
//
//        String root = Environment.getExternalStorageDirectory().toString();
//        File myDir = new File(root + "/saved_images");
//        myDir.mkdirs();
//        Random generator = new Random();
//        int n = 10000;
//        n = generator.nextInt(n);
//        String fname = "Image-"+ n +".jpg";
//        File file = new File (myDir, fname);
//        if (file.exists ()) file.delete ();
//        try {
//            FileOutputStream out = new FileOutputStream(file);
//            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
//            out.flush();
//            out.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
