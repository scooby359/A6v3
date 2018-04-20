package com.example.scoob.a6;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "TestActivity";
    private static final int REQUEST_CODE = 123;

    Context context;
    Button mImportButton;

    Uri fileUri;

    // TODO: 18/04/2018 - Continue read file 
    //And at somepoint, how to write!
    //https://developer.android.com/guide/topics/providers/document-provider.html
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        Log.d(TAG, "onCreate: context = " + context);

        setContentView(R.layout.activity_settings);

        mImportButton = findViewById(R.id.btn_selectImportFile);

        mImportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("text/*");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            fileUri = null;
            if (data != null){
                fileUri = data.getData();
                Toast.makeText(this, "URI: " + fileUri, Toast.LENGTH_SHORT).show();
                DataImportExport dataImportExport = new DataImportExport();
                try {
                    dataImportExport.readDataIn(context, fileUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
