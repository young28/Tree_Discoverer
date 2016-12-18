package com.example.pinatala.tree_discoverer.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pinatala.tree_discoverer.R;
import com.example.pinatala.tree_discoverer.ui.MapsActivity;

import java.io.File;

/**
 * Created by YouYang on 05/12/16.
 */

public class TestClickTree extends AppCompatActivity {
    private TextView test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_click_layout);

        Intent data = getIntent();
        String message = data.getStringExtra(MapsActivity.TEST_MESSAGE);
        int id = Integer.parseInt(message);
        test = (TextView) findViewById(R.id.titleTextView);
        test.setTextSize(40);
        test.setText("id :" + id);

        File root = Environment.getExternalStorageDirectory();
        ImageView IVTree = (ImageView) findViewById(R.id.treeImageView);
        Bitmap bMapTree = BitmapFactory.decodeFile(root
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files/"
                + "MI_07122016_1453.jpg");
        IVTree.setImageBitmap(bMapTree);

        ImageView IVLeaf = (ImageView) findViewById(R.id.treeImageView);
        Bitmap bMapLeaf = BitmapFactory.decodeFile(root
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files/"
                + "MI_07122016_1453.jpg");
        IVLeaf.setImageBitmap(bMapTree);

    }


}
