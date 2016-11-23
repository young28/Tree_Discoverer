package com.example.pinatala.tree_discoverer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class MapActivity extends AppCompatActivity {

    public static final String TAG = MapActivity.class.getSimpleName();

    static final int REQUEST_IMAGE_CAPTURE_FIRST = 1;
    static final int REQUEST_IMAGE_CAPTURE_SECOND = 2;

    private Button cameraButton;
    private Bundle imagesBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        cameraButton = (Button) findViewById(R.id.cameraButton);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePhotoIntent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePhotoIntent1.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePhotoIntent1, REQUEST_IMAGE_CAPTURE_FIRST);
                }

            }
        });

        imagesBundle = new Bundle();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE_FIRST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap image = (Bitmap) extras.get("data");
            imagesBundle.putByteArray("tree_image", Utilities.bitmapToByteArray(image));

            Intent takePhotoIntent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePhotoIntent2.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePhotoIntent2, REQUEST_IMAGE_CAPTURE_SECOND);
            }
        }
        else if (requestCode == REQUEST_IMAGE_CAPTURE_SECOND && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap image = (Bitmap) extras.get("data");
            imagesBundle.putByteArray("leaf_image", Utilities.bitmapToByteArray(image));

            Intent newActivityIntent = new Intent(this, DisplayActivity.class);
            newActivityIntent.putExtra("extras", imagesBundle);
            startActivity(newActivityIntent);
        }

    }







}
