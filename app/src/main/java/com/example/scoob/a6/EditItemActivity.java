package com.example.scoob.a6;


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

    private NoteEntity LoadedNote;
    private int loadedStatus;
    private Context context;
    private EditText etEnterTitle;
    private Spinner sStatus;
    private EditText etNotes;
    private Button save;
    private Button delete;
    private AppDatabase database;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        context = getApplicationContext();
        database = AppDatabase.getDatabase(context);

        etEnterTitle = findViewById(R.id.pt_EditTitle);
        sStatus = findViewById(R.id.spin_EditStatus);
        etNotes = findViewById(R.id.et_EditNotes);
        save = findViewById(R.id.btn_EditSave);
        delete = findViewById(R.id.btn_EditDelete);

        //Set default spinner val
        sStatus.setSelection(0);
        loadedStatus = 0;

        //Load from db, then populate fields on screen
        id = -1;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //Get id from bundle, get note from db, then populate fields
            id = bundle.getInt(getResources().getString(R.string.BUNDLE_ID));

            if (id != -1){
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
            }else{
                String newTitle = bundle.getString(getResources().getString(R.string.BundleSearchStringKey));
                etEnterTitle.setText(newTitle);
            }
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveNote();
                finish();
            }
        });

        if (id == -1){
            delete.setVisibility(View.GONE);
        }
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditItemActivity.this);
                builder
                        .setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                database.noteDao().deleteNote(LoadedNote);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //No action..
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

    }

    private boolean CheckForChanges(){

        boolean changesMade = false;

        if (id == -1){ //Checks for new note changes
            if (etEnterTitle.getText().toString().length() != 0){
                changesMade = true;
            }
        }else{  //checks for changes to existing note
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
        }
        return changesMade;
    }

    private void SaveNote() {
        NoteEntity note = new NoteEntity();

        note.setTitle(etEnterTitle.getText().toString().toUpperCase());
        note.setNote(etNotes.getText().toString());
        if (id != -1) {
            note.setId(id);
        }

        int selectedSpin = sStatus.getSelectedItemPosition();

        switch (selectedSpin) {
            case 0:
                note.setStatus(getResources().getString(R.string.STATUS_NONE));
                break;
            case 1:
                note.setStatus(getResources().getString(R.string.STATUS_GOOD));
                break;
            case 2:
                note.setStatus(getResources().getString(R.string.STATUS_BAD));
                break;
            case 3:
                note.setStatus(getResources().getString(R.string.STATUS_WARNING));
                break;
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

        if (CheckForChanges()){
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
