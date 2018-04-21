package com.example.scoob.a6;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

class DataImportExport {

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

    public void writeDataOut(Context context){

        //Set directory and check it exists
        File exportDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        //Check dir is writeable
        String state = Environment.getExternalStorageState();
        boolean writeable = Environment.MEDIA_MOUNTED.equals(state);
        Log.d(TAG, "writeDataOut: Writeable = " + writeable);

        if (writeable){
            try{
                PrintWriter printWriter;
                File file = new File(exportDir, "A6DbExport.csv");
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
                Log.d(TAG, "writeDataOut: file = " + file.getPath());
                printWriter = new PrintWriter(new FileWriter(file));

                AppDatabase db = AppDatabase.getDatabase(context);
                List<NoteEntity> notes = db.noteDao().getAllNotes();
                printWriter.println("Title,Status,Notes");

                for (int i = 0; i < notes.size(); i++){
                    NoteEntity thisNote = notes.get(i);
                    String entry =
                            thisNote.getTitle() + "," +
                                    thisNote.getStatus() + "," +
                                    thisNote.getNote();
                    Log.d(TAG, "writeDataOut: Enter = " + entry);
                    printWriter.println(entry);
                }
                printWriter.close();
                Toast.makeText(context, "File saved to Downloads folder", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                e.printStackTrace();
                Log.d(TAG, "writeDataOut: " + e.getMessage());
                Log.d(TAG, "writeDataOut: Error writing file");
            }
        }
    }
}
