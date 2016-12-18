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
import com.example.pinatala.tree_discoverer.database.TreeDataSource;
import com.example.pinatala.tree_discoverer.model.TreeMarker;
import com.example.pinatala.tree_discoverer.ui.MapsActivity;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by YouYang on 05/12/16.
 */

public class TestClickTree extends AppCompatActivity {
    private TextView test;
    private String type;
    private String treeImage;
    private String leafImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_click_layout);

        Intent data = getIntent();
        String message = data.getStringExtra(MapsActivity.TEST_MESSAGE);
        if (message == "I am here!") {
            test = (TextView) findViewById(R.id.titleTextView);
            test.setTextSize(40);
            test.setText(message);
        } else {

            int id = Integer.parseInt(message);


            TreeDataSource dataSource = new TreeDataSource(this.getApplicationContext());
            ArrayList<TreeMarker> treeMarkers = dataSource.read();
            TreeMarker currentTree = treeMarkers.get(id);
//        if (id == currentTree.getId()) {
//            test = (TextView) findViewById(R.id.titleTextView);
//            test.setTextSize(40);
//            test.setText("id :" + id + " OK");
//        } else{
//            test = (TextView) findViewById(R.id.titleTextView);
//            test.setTextSize(40);
//            test.setText("id :" + id + " NO");
//        }
            type = currentTree.getTreeType();
            treeImage = currentTree.getTreeImageName();
            leafImage = currentTree.getLeafImageName();

            test = (TextView) findViewById(R.id.titleTextView);
            test.setTextSize(40);
            test.setText(type);

            File root = Environment.getExternalStorageDirectory();
            ImageView IVTree = (ImageView) findViewById(R.id.treeImageView);
            Bitmap bMapTree = BitmapFactory.decodeFile(root
                    + "/Android/data/"
                    + getApplicationContext().getPackageName()
                    + "/Files/"
                    + treeImage);
            IVTree.setImageBitmap(bMapTree);

            ImageView IVLeaf = (ImageView) findViewById(R.id.leafImageView);
            Bitmap bMapLeaf = BitmapFactory.decodeFile(root
                    + "/Android/data/"
                    + getApplicationContext().getPackageName()
                    + "/Files/"
                    + leafImage);
            IVLeaf.setImageBitmap(bMapTree);

        }

    }
}
