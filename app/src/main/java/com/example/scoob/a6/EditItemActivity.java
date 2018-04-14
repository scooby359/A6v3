package com.example.scoob.a6;

//todo - save on edit
//todo - save if new
//todo - prompt on cancel

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class EditItemActivity extends AppCompatActivity {

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

        database = AppDatabase.getDatabase(getApplicationContext());

        etEnterTitle = (EditText) findViewById(R.id.pt_EditTitle);
        sStatus = (Spinner) findViewById(R.id.spin_EditStatus);
        etNotes = (EditText) findViewById(R.id.et_EditNotes);
        save = (Button) findViewById(R.id.btn_EditSave);

        //Set default spinner val
        sStatus.setSelection(0);

        //Load from db, then populate fields on screen
        id = -1;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //Get id from bundle, get note from db, then populate fields
            id = bundle.getInt(getResources().getString(R.string.BUNDLE_ID));
            NoteEntity note = database.noteDao().getNote(id);
            etEnterTitle.setText(note.getTitle());
            etNotes.setText(note.getNote());
            String status = note.getStatus();
            if (status.equals(getResources().getString(R.string.STATUS_GOOD))) {
                sStatus.setSelection(1);
            } else if (status.equals(getResources().getString(R.string.STATUS_BAD))) {
                sStatus.setSelection(2);
            } else if (status.equals(getResources().getString(R.string.STATUS_WARNING))) {
                sStatus.setSelection(3);
            }
        }

        //todo - prompt to save changes when back pressed
        //todo - should close screen when clicked
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        });
    }
}
