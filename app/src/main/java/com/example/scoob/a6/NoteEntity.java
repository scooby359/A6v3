package com.example.scoob.a6;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by scoob on 12/04/2018.
 */


@Entity
public class NoteEntity {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public String status;
    public String note;

    public NoteEntity(){}

    @Ignore
    public NoteEntity(String title, String status, String note){
        this.title = title.toUpperCase();
        this.note = note;
        if (status.equals("0")){this.status = MainActivity.BAD;}
        else if (status.equals("1")){this.status = MainActivity.GOOD;}
        else if (status.equals("3")){this.status = MainActivity.WARNING;}
        else{this.status = MainActivity.NONE;}
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Ignore
    void PrintNote(){
        Log.d("NoteEntity", "PrintNote: " + this.title + " " + this.status + " " + this.note);
    }
}