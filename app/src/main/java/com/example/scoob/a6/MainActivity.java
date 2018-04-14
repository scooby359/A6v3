package com.example.scoob.a6;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;

public class MainActivity extends AppCompatActivity {

    public static final String GOOD = "GOOD";
    public static final String BAD = "BAD";
    public static final String WARNING = "WARNING";
    public static final String NONE = "NONE";

    private AppDatabase database;
    private ListView listView;
    private CustomAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar
                = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.lv_mainList);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Get db instance
        database = AppDatabase.getDatabase(getApplicationContext());
        database.noteDao().removeAllNotes();

        //Load some data in for testing
        NoteEntity note = new NoteEntity();
        note.setTitle("V1");
        note.setStatus(getResources().getString(R.string.STATUS_GOOD));
        note.setNote("This is the 1st note");
        database.noteDao().addNote(note);

        NoteEntity note2 = new NoteEntity();
        note2.setTitle("V2");
        note2.setStatus(getResources().getString(R.string.STATUS_BAD));
        note2.setNote("This is the 2nd note");
        database.noteDao().addNote(note2);

        List<NoteEntity> tempList = database.noteDao().getAllNotes();

        for (int i = 0; i < tempList.size(); i++)
        {
            Log.d("Array " + i, tempList.get(i).title);
        }

        mAdapter = new CustomAdapter(this, database.noteDao().getAllNotes());
        listView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class CustomAdapter extends ArrayAdapter<NoteEntity>{

        private Context mContext;
        private List<NoteEntity> noteList = new ArrayList<>();

        public CustomAdapter(Context context, List<NoteEntity> list){
            super(context, 0, list);
            mContext = context;
            noteList = list;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View listItem = convertView;
            if (listItem == null){
                listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);
            }
            NoteEntity currentNote = noteList.get(position);

            ImageView indicator = (ImageView)listItem.findViewById(R.id.iv_ListItemIndicator);
            TextView title = (TextView)listItem.findViewById(R.id.tv_ListItemTitle);

            title.setText(currentNote.title);

            if (currentNote.getStatus().equals(getResources().getString(R.string.STATUS_GOOD))){
                indicator.setColorFilter(getResources().getColor(R.color.STATUS_GOOD));
            }//todo - the rest of them!

            return listItem;

        }
    }

}
