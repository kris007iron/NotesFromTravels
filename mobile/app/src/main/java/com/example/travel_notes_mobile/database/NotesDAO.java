package com.example.travel_notes_mobile.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.travel_notes_mobile.model.Location;
import com.example.travel_notes_mobile.model.Note;

import retrofit2.http.DELETE;

@Dao
public interface NotesDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertNote(Note note);

    @Query("DELETE FROM notes")
    void deleteAllNotes();

    @Query("DELETE FROM notes WHERE id = :id")
    void deleteNoteById(int id);

    @Query("UPDATE notes SET title = :title, content = :content, location = :location, image_url = :imageUrl, updated_at = :updatedAt WHERE id = :id")
    void updateNoteById(int id, String title, String content, Location location, String imageUrl, String updatedAt);

}
