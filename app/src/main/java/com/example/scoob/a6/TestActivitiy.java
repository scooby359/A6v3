package com.example.scoob.a6;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TestActivitiy extends AppCompatActivity {

    private static final String TAG = "TestActivity";
    private static final int REQUEST_CODE = 123;

    TextView mTextField;
    Button mTestButton;

    // TODO: 18/04/2018 - Continue read file 
    //This works!! Opens file selector, returns URI to file.
    //Needs permission for external read access in manifest
    //Need to follow on, how to read contents of file.
    //And at somepoint, how to write!
    //https://developer.android.com/guide/topics/providers/document-provider.html
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_activitiy);

        mTextField = findViewById(R.id.pt_TestText);
        mTestButton = findViewById(R.id.btn_TestIntent);

        mTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("*/*");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Uri uri = null;
            if (data != null){
                uri = data.getData();
                Log.d(TAG, "onActivityResult: File Select Result = " + uri);
            }
            mTextField.append(uri.toString());
        }

    }
}
