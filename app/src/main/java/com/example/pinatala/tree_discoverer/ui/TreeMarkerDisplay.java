package com.example.pinatala.tree_discoverer.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pinatala.tree_discoverer.R;
import com.example.pinatala.tree_discoverer.database.TreeDataSource;
import com.example.pinatala.tree_discoverer.model.TreeMarker;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Matteo Pontiggia on 05/12/16.
 */

public class TreeMarkerDisplay extends AppCompatActivity {
    //Create the fields
    private int treeId;
    private TextView treeType;
    private String type;
    private String treeImage;
    private String leafImage;
    private String message;
    private Button deleteButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tree_marker_display_layout);

        deleteButton = (Button) findViewById(R.id.deleteButton);

        final Intent data = getIntent();
        message = data.getStringExtra(MapsActivity.MARKER_MESSAGE);
        if (message.equalsIgnoreCase("I am here!")) {
            treeType = (TextView) findViewById(R.id.titleTextView);
            treeType.setTextSize(40);
            treeType.setText(message);
        }
        else {

            treeId = Integer.parseInt(message);

            TreeDataSource dataSource = new TreeDataSource(this.getApplicationContext());
            ArrayList<TreeMarker> treeMarkers = dataSource.read();
            if (treeMarkers.size() > treeId) {
                TreeMarker currentTree = treeMarkers.get(treeId);

                // We use this to Test if the sent id is the real ID we want.
    //        if (id == currentTree.getId()) {
    //            treeType = (TextView) findViewById(R.id.titleTextView);
    //            treeType.setTextSize(40);
    //            treeType.setText("id :" + id + " OK");
    //        } else{
    //            treeType = (TextView) findViewById(R.id.titleTextView);
    //            treeType.setTextSize(40);
    //            treeType.setText("id :" + id + " NO");
    //        }

                type = currentTree.getTreeType();
                treeImage = currentTree.getTreeImageName();
                leafImage = currentTree.getLeafImageName();

                treeType = (TextView) findViewById(R.id.titleTextView);
                treeType.setTextSize(40);
                treeType.setText(type);

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
                IVLeaf.setImageBitmap(bMapLeaf);
        } else {
                treeType = (TextView) findViewById(R.id.titleTextView);
                treeType.setTextSize(40);
                treeType.setText("Tree not existing");
            }

        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTree(treeId);
                finish();
            }
        });

    }

    //Method to delete one tree marker by making it invisible on the map
    private void deleteTree(int id){
        TreeDataSource dataSource = new TreeDataSource(this.getApplicationContext());
        dataSource.update(id);
        //ArrayList<TreeMarker> testMarkers = dataSource.read();
    }
}
