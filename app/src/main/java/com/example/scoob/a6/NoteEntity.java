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
    private int id;
    public String title;
    private String status;
    private String note;

    public NoteEntity(){}

    @Ignore
    public NoteEntity(String title, String status, String note){
        this.title = title.toUpperCase();
        this.note = note;
        switch (status) {
            case "0":
                this.status = MainActivity.BAD;
                break;
            case "1":
                this.status = MainActivity.GOOD;
                break;
            case "3":
                this.status = MainActivity.WARNING;
                break;
            default:
                this.status = MainActivity.NONE;
                break;
        }
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