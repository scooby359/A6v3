package com.example.scoob.a6;

//todo - set backup flag
//todo - import and export features

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String GOOD = "GOOD";
    public static final String BAD = "BAD";
    public static final String WARNING = "WARNING";
    public static final String NONE = "NONE";

    private AppDatabase database;
    private ListView listView;
    private CustomAdapter mAdapter;
    private EditText searchText;
    private ImageView newButton;
    private ImageView cancelButton;

    private Context context;
    private List<NoteEntity> ListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        listView = findViewById(R.id.lv_mainList);
        searchText = findViewById(R.id.etSearchField);
        newButton = findViewById(R.id.ivNewIcon);
        cancelButton = findViewById(R.id.iv_searchCancel);
        setSupportActionBar(toolbar);

        //Get db instance
        database = AppDatabase.getDatabase(context);

        //Get current list from DB and populate adapter
        ListData = database.noteDao().getAllNotes();
        mAdapter = new CustomAdapter(this, ListData);
        listView.setAdapter(mAdapter);

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //null
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String fieldText = editable.toString();
                String query = "%" + fieldText + "%";
                ListData = database.noteDao().searchNoteTitles(query);
                mAdapter = null;
                mAdapter = new CustomAdapter(context, ListData);
                listView.setAdapter(mAdapter);

                for (int i=0; i < ListData.size(); i++){
                    NoteEntity temp = ListData.get(i);
                    Log.d("DB Response", "Title = " + temp.getTitle() + " Notes = " + temp.getNote() + " Status = " + temp.getStatus());
                }
            }
        });

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditItemActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(getResources().getString(R.string.BUNDLE_ID), -1);
                bundle.putString(getString(R.string.BundleSearchStringKey), searchText.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchText.setText("");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter = new CustomAdapter(this, database.noteDao().getAllNotes());
        listView.setAdapter(mAdapter);
        searchText.setText("");
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
            Intent intent = new Intent(context, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    public class CustomAdapter extends ArrayAdapter<NoteEntity>{

        private Context mContext;
        private List<NoteEntity> noteList;

        CustomAdapter(Context context, List<NoteEntity> list){
            super(context, 0, list);
            mContext = context;
            noteList = list;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View listItem = convertView;
            if (listItem == null){
                listItem = LayoutInflater.from(mContext)
                        .inflate(R.layout.list_item,parent,false);
            }
            final NoteEntity currentNote = noteList.get(position);

            ImageView indicator = listItem.findViewById(R.id.iv_ListItemIndicator);
            TextView title = listItem.findViewById(R.id.tv_ListItemTitle);

            title.setText(currentNote.title);
            indicator.setVisibility(View.VISIBLE);
            indicator.setImageResource(R.drawable.ic_brightness_1_black_24dp);

           if (currentNote.getStatus().equals(getResources().getString(R.string.STATUS_NONE))){
                indicator.setVisibility(View.INVISIBLE);
            }
            if (currentNote.getStatus().equals(getResources().getString(R.string.STATUS_GOOD))){
                indicator.setColorFilter(getResources().getColor(R.color.STATUS_GOOD));
            }
            if (currentNote.getStatus().equals(getResources().getString(R.string.STATUS_BAD))){
                indicator.setColorFilter(getResources().getColor(R.color.STATUS_BAD));
            }
            if (currentNote.getStatus().equals(getResources().getString(R.string.STATUS_WARNING))){
                indicator.setColorFilter(getResources().getColor(R.color.STATUS_WARNING));
            }

            listItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), EditItemActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(getResources()
                            .getString(R.string.BUNDLE_ID), currentNote.getId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            return listItem;
        }


    }

}
