package com.example.pinatala.tree_discoverer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by YouYang on 23/11/16.
 */

public class DisplayActivity extends AppCompatActivity {

    private ImageView treeImageView;
    private ImageView leafImageView;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_layout);

        Intent data = getIntent();
        Bundle bundle = data.getBundleExtra("extras");
        byte[] treeImageByte = bundle.getByteArray("tree_image");
        byte[] leafImageByte = bundle.getByteArray("leaf_image");

        Bitmap treeImage = Utilities.byteArrayToBitmap(treeImageByte);
        Bitmap leafImage = Utilities.byteArrayToBitmap(leafImageByte);

        treeImageView = (ImageView) findViewById(R.id.treeImageView);
        leafImageView = (ImageView) findViewById(R.id.leafImageView);

        treeImageView.setImageBitmap(treeImage);
        leafImageView.setImageBitmap(leafImage);

    }

}
