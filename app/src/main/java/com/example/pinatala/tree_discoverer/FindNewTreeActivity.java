package com.example.pinatala.tree_discoverer;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pinatala.tree_discoverer.database.TreeDataSource;
import com.example.pinatala.tree_discoverer.database.TreeDatabaseOpenHelper;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    private Spinner mSpinner;
    private Button submitButton;
    private String selectedTreeType;
    private TreeMarker treeMarkerClick;
    private int id;

    public static final String MARKER_TREE_TYPE = "tree_type";

    Activity thisActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findnewtree_layout);

        addSubmitButton();
        addSpinner();

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



        Log.d("TreeAct", "onCreateCalled");

    }

    private void addSpinner() {
        mSpinner = (Spinner) findViewById(R.id.spinner);
        //selectedTreeType = String.valueOf(mSpinner.getSelectedItem());
    }



    private void addSubmitButton() {
        submitButton = (Button) findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTreeType = String.valueOf(mSpinner.getSelectedItem());
                id = getTreesCount();
                Toast.makeText(FindNewTreeActivity.this, selectedTreeType + "["+id+"]"
                        + lat + lon + img1 + img2, Toast.LENGTH_LONG).show();
                //new SaveToDatabase().execute();
                treeMarkerClick = new TreeMarker(id , img1 ,img2, "", lat,lon, selectedTreeType);
//                treeMarkerClick.setTreeImageName(img1);
//                treeMarkerClick.setLeafImageName(img2);
//                treeMarkerClick.setCreateDate("");
//                treeMarkerClick.setLatitude(lat);
//                treeMarkerClick.setLongitude(lon);
//                treeMarkerClick.setTreeType(selectedTreeType);

                TreeDataSource dataSource = new TreeDataSource(FindNewTreeActivity.this);
                dataSource.create(treeMarkerClick);

                ArrayList<TreeMarker> testMarkers = dataSource.read();


//                Intent intent = new Intent(FindNewTreeActivity.this, MapsActivity.class);
//                intent.putExtra("lat", lat);
//                intent.putExtra("lon", lon);
//                intent.putExtra("img1", img1);
//                intent.putExtra("img2", img2);
//                intent.putExtra("type", selectedTreeType);
//
//                startActivity(intent);
                finish();
            }
        });
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
            values.put(TreeDatabaseOpenHelper.KEY_TYPE_NAME, selectedTreeType);

            long inserted = database.insert(TreeDatabaseOpenHelper.TREE_TABLE_NAME, null, values);

            Log.d("Database", "Insert operation result "+ inserted);
            database.close();
            return null;
        }
    }
    public int getTreesCount() {
        TreeDatabaseOpenHelper treeDatabaseOpenHelper = new TreeDatabaseOpenHelper(this.getApplicationContext());
        String countQuery = "SELECT  * FROM " + TreeDatabaseOpenHelper.TREE_TABLE_NAME;
        SQLiteDatabase db =  treeDatabaseOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }



}
