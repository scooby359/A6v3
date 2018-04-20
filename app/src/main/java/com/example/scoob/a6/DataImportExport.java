package com.example.scoob.a6;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class DataImportExport {

    private static final String TAG = "DataImportExport";

    public void readDataIn(Context context, Uri uri) throws IOException {

        Log.d(TAG, "readDataIn: context = " + context);
        AppDatabase db = AppDatabase.getDatabase(context);
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        assert inputStream != null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        boolean headerLine = true;
        while((line = reader.readLine()) != null){
            List<String> tempList = Arrays.asList(line.split(","));
            NoteEntity newNote = new NoteEntity(tempList.get(0),tempList.get(1),tempList.get(2));
            Log.d(TAG, "readDataIn: Status =  " + tempList.get(1));
            if (headerLine) //ignore first line and discard
            {
                headerLine = false;
            }else {
                db.noteDao().addNote(newNote);
            }
        }
        inputStream.close();
    }

}
