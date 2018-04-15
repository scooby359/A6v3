package com.example.scoob.a6;

//todo - save if new

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class EditItemActivity extends AppCompatActivity {

    NoteEntity LoadedNote;
    int loadedStatus;
    Context context;
    EditText etEnterTitle;
    Spinner sStatus;
    EditText etNotes;
    Button save;
    AppDatabase database;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        context = getApplicationContext();
        database = AppDatabase.getDatabase(context);

        etEnterTitle = (EditText) findViewById(R.id.pt_EditTitle);
        sStatus = (Spinner) findViewById(R.id.spin_EditStatus);
        etNotes = (EditText) findViewById(R.id.et_EditNotes);
        save = (Button) findViewById(R.id.btn_EditSave);

        //Set default spinner val
        sStatus.setSelection(0);
        loadedStatus = 0;

        //Load from db, then populate fields on screen
        id = -1;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //Get id from bundle, get note from db, then populate fields
            id = bundle.getInt(getResources().getString(R.string.BUNDLE_ID));
            LoadedNote = database.noteDao().getNote(id);
            etEnterTitle.setText(LoadedNote.getTitle());
            etNotes.setText(LoadedNote.getNote());
            String status = LoadedNote.getStatus();
            if (status.equals(getResources().getString(R.string.STATUS_GOOD))) {
                sStatus.setSelection(1);
                loadedStatus = 1;
            } else if (status.equals(getResources().getString(R.string.STATUS_BAD))) {
                sStatus.setSelection(2);
                loadedStatus = 2;
            } else if (status.equals(getResources().getString(R.string.STATUS_WARNING))) {
                sStatus.setSelection(3);
                loadedStatus = 3;
            }
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveNote();
                finish();
            }
        });

    }

    boolean CheckForChanges(){

        boolean changesMade = false;

        if (!etEnterTitle.getText().toString().equals(LoadedNote.getTitle())){
            changesMade = true;
        }
        if (!etNotes.getText().toString().equals(LoadedNote.getNote())){
            changesMade = true;
        }
        int newStatus = sStatus.getSelectedItemPosition();
        if (loadedStatus!=newStatus){
            changesMade = true;
        }

        return changesMade;
    }

    private void SaveNote() {
        NoteEntity note = new NoteEntity();

        note.setTitle(etEnterTitle.getText().toString());
        note.setNote(etNotes.getText().toString());
        if (id != -1) {
            note.setId(id);
        }

        int selectedSpin = sStatus.getSelectedItemPosition();

        if (selectedSpin == 0) {
            note.setStatus(getResources().getString(R.string.STATUS_NONE));
        } else if (selectedSpin == 1) {
            note.setStatus(getResources().getString(R.string.STATUS_GOOD));
        } else if (selectedSpin == 2) {
            note.setStatus(getResources().getString(R.string.STATUS_BAD));
        } else if (selectedSpin == 3) {
            note.setStatus(getResources().getString(R.string.STATUS_WARNING));
        }

        if (id == -1) {
            database.noteDao().addNote(note);
        } else {
            database.noteDao().updateNote(note);
        }
    }

    @Override
    public void onBackPressed() {
        //do some sort of check if changes made

        if ((LoadedNote != null) && CheckForChanges()){
            AlertDialog.Builder builder = new AlertDialog.Builder(EditItemActivity.this);
            builder
                    .setMessage(R.string.DialogWantToSave)
                    .setPositiveButton(R.string.DialogSave, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SaveNote();
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.DialogNo, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else {
            Log.d("Edit","Back with no changes called");
            finish();
        }
    }
}
