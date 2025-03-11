package com.example.travel_notes_mobile.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_notes_mobile.database.NotesDatabase;
import com.example.travel_notes_mobile.model.Location;
import com.example.travel_notes_mobile.model.MessageResponse;
import com.example.travel_notes_mobile.model.Note;
import com.example.travel_notes_mobile.service.RetrofitService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Response;

public class NotesViewModel extends AndroidViewModel {

    public void setMutableLiveList(MutableLiveData<List<Note>> mutableLiveList) {
        this.mutableLiveList = mutableLiveList;
    }

    private MutableLiveData<List<Note>> mutableLiveList = new MutableLiveData<>();
    private NotesDatabase notesDatabase;
    private Application application;


    public NotesViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        notesDatabase = NotesDatabase.getInstance(application);
        fetchNotes();
        Log.d("NOTES_VIEW_MODEL_DEBUG_KONSTRUKTOR", "KONSTRUKTOR");
    }

    public void fetchNotes() {
        notesDatabase.notesDAO().deleteAllNotes();
        SharedPreferences sharedPreferences = application.getSharedPreferences("travel_notes_mobile", Context.MODE_PRIVATE);
        String tokenValue = sharedPreferences.getString("token", "");
        Call<List<Note>> notes = RetrofitService.getNotesInterface().getNotes("Bearer " + tokenValue);
        Log.d("NOTES_VIEW_MODEL_DEBUG", "FETCHUJE" + tokenValue);
        notes.enqueue(new retrofit2.Callback<List<Note>>() {
            @Override
            public void onResponse(Call<List<Note>> call, retrofit2.Response<List<Note>> response) {
                for (Note note : response.body()) {
                    Log.d("NOTES_VIEW_MODEL_DEBUG", note.getTitle());
                    notesDatabase.notesDAO().insertNote(note);
                }
                mutableLiveList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable t) {
                Log.d("NOTES_VIEW_MODEL_DEBUG", t.getMessage());
            }
        });
    }

    public void postNote(Note note) {
        SharedPreferences sharedPreferences = application.getSharedPreferences("travel_notes_mobile", Context.MODE_PRIVATE);
        String tokenValue = sharedPreferences.getString("token", "");
        Call<Note> notes = RetrofitService.getNotesInterface().postNote("Bearer " + tokenValue, note);
        Log.d("ELOOO", "POSTUJE");
        notes.enqueue(new retrofit2.Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, retrofit2.Response<Note> response) {
                Log.d("KUUPA", response.body().getTitle());
                List<Note> currentNotes = mutableLiveList.getValue();

                if (currentNotes != null) {
                    List<Note> updatedNotes = new ArrayList<>(currentNotes);
                    updatedNotes.add(response.body());
                    mutableLiveList.setValue(updatedNotes);
                    notesDatabase.notesDAO().insertNote(response.body());

                }
                Log.d("KUUPA", response.body().getTitle());
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                Log.d("KUUPA", t.getMessage());
            }
        });
    }

    public void deleteNote(Note note) {
        SharedPreferences sharedPreferences = application.getSharedPreferences("travel_notes_mobile", Context.MODE_PRIVATE);
        String tokenValue = sharedPreferences.getString("token", "");
        Call<MessageResponse> notes = RetrofitService.getNotesInterface().deleteNote("Bearer " + tokenValue, note.getId());
        notes.enqueue(new retrofit2.Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, retrofit2.Response<MessageResponse> response) {
                List<Note> currentNotes = mutableLiveList.getValue();
                if (currentNotes != null) {
                    List<Note> updatedNotes = new ArrayList<>(currentNotes);
                    updatedNotes.remove(note);
                    mutableLiveList.setValue(updatedNotes);
                    notesDatabase.notesDAO().deleteNoteById(note.getId());
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Log.d("KUUPA", t.getMessage());
            }
        });

    }

    public void updateNote(Note note) {
        Log.d("____UPDATE", "UPDATE");
        SharedPreferences sharedPreferences = application.getSharedPreferences("travel_notes_mobile", Context.MODE_PRIVATE);
        String tokenValue = sharedPreferences.getString("token", "");
        Call<MessageResponse> notes = RetrofitService.getNotesInterface().putNoteById(note.getId(), note, "Bearer " + tokenValue);

        notes.enqueue(new retrofit2.Callback<MessageResponse>() {

            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {}

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable throwable) {}
        });
    }


    public MutableLiveData<List<Note>> getObservedNotes() {
        return mutableLiveList;
    }

    public Note[] getAllNotes() {
        return notesDatabase.notesDAO().getAllNotes();
    }

    public void updateNoteInDatabase(Note note) {
        notesDatabase.notesDAO().updateNoteById(note.getId(), note.getTitle(), note.getContent(), note.getLocation(), note.getImageUrl(), note.getUpdatedAt());
    }

}
