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

        test = (TextView) findViewById(R.id.test_text);
        test.setTextSize(40);
        test.setText(message);

        File root = Environment.getExternalStorageDirectory();
        ImageView IV = (ImageView) findViewById(R.id.imageTest);
        Bitmap bMap = BitmapFactory.decodeFile(root
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files/"
                + "MI_07122016_1453.jpg");
        IV.setImageBitmap(bMap);

    }


}
