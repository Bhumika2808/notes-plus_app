package com.example.notesplus.model;

import com.google.firebase.Timestamp;

public class Note {
    String noteTitle;
    String noteContent;
    Timestamp timeStamp;

    public Note(){
    }
    public String getNoteTitle() {
        return noteTitle;
    }
    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }
    public String getNoteContent() {
        return noteContent;
    }
    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }
    public Timestamp getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }
}
