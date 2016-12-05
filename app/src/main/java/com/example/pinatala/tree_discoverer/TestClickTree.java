package com.example.pinatala.tree_discoverer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

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
    }
}
