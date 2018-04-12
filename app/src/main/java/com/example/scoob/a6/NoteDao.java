package com.example.scoob.a6;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by scoob on 12/04/2018.
 */

@Dao
public interface  NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addNote(Note note);

    @Query("select * from note")
    public List<Note> getAllNotes();

    @Query("select * from note where id = :id")
    public List<Note> getNote(long id);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateNote(Note note);

    @Query("delete from note")
    void removeAllNotes();
}
