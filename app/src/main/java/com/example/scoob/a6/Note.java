package com.example.scoob.a6;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by scoob on 12/04/2018.
 */

@Entity
public class Note {

    public static final String GOOD = "GOOD";
    public static final String BAD = "BAD";
    public static final String WARNING = "WARNING";
    public static final String NONE = "NONE";

    @PrimaryKey
    private int id;
    private String title;
    private String status;
    private String note;

    public Note(int id, String title, String status, String note) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
