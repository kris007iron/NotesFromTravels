package com.example.travel_notes_mobile.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.travel_notes_mobile.model.Location;
import com.example.travel_notes_mobile.model.Note;

@Dao
public interface NotesDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertNote(Note note);


}
