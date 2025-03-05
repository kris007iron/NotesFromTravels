package com.example.travel_notes_mobile.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.travel_notes_mobile.model.Note;

@androidx.room.Database(entities = {Note.class}, version = 2)
@TypeConverters({Converters.class})
public abstract class NotesDatabase extends RoomDatabase {
    private static NotesDatabase roomDatabase;
    private static final String DATABASE_NAME = "notes_db";

    public static synchronized NotesDatabase getInstance(Context context) {
        if (roomDatabase == null) {
            roomDatabase = Room.databaseBuilder(context.getApplicationContext(), NotesDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return roomDatabase;
    }

    public abstract NotesDAO notesDAO();

}
