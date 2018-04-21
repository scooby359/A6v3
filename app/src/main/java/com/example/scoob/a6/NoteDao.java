package com.example.scoob.a6;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
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
    void addNote(NoteEntity note);

    @Query("select * from noteentity ORDER BY title DESC")
    List<NoteEntity> getAllNotes();

    @Query("select * from noteentity where id = :id")
    NoteEntity getNote(int id);

    @Query("select * from noteentity where title LIKE :search")
    List<NoteEntity> searchNoteTitles(String search);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateNote(NoteEntity note);

    @Delete
    void deleteNote(NoteEntity note);

    @Query("delete from noteentity")
    void removeAllNotes();


}
