package com.example.scoob.a6;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "TestActivity";
    private static final int REQUEST_CODE = 123;
    private static final int MY_PERMISSION_WRITE_ACCESS = 99;


    private Context context;
    private Button mImportButton;
    private Button mExportButton;
    private Button mDeleteAllButton;

    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = SettingsActivity.this;

        Log.d(TAG, "onCreate: context = " + context);

        setContentView(R.layout.activity_settings);

        mImportButton = findViewById(R.id.btn_selectImportFile);
        mExportButton = findViewById(R.id.btn_ExportData);
        mDeleteAllButton = findViewById(R.id.btn_DeleteAllNotes);

        mImportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("text/*");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        mExportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Check write permissions
                int writePermission =
                        ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (writePermission == PackageManager.PERMISSION_GRANTED){
                    Log.d(TAG, "writeDataOut: Write permission granted");
                    ExportData();
                }else{
                    Log.d(TAG, "writeDataOut: Write permission not given, requesting");
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSION_WRITE_ACCESS);
                }
            }
        });

        mDeleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to delete all notes?");
                builder.setPositiveButton(R.string.settingsDeleteNotesDialogConfirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AppDatabase db = AppDatabase.getDatabase(context);
                        db.noteDao().removeAllNotes();
                        Toast.makeText(context, "All notes deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       //no action 
                    }
                });
                builder.create();
                builder.show();
            }
        });
    }

    private void ExportData(){
        DataImportExport dataImportExport = new DataImportExport();
        dataImportExport.writeDataOut(context);
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
       @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSION_WRITE_ACCESS:{
                if (grantResults.length > 0
                        &&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permission granted - call export function
                    ExportData();
                }else{
                    Log.d(TAG, "onRequestPermissionsResult: write permission denied");
                    Toast.makeText(context, "Write permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
