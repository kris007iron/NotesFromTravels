package com.example.travel_notes_mobile.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_notes_mobile.database.NotesDatabase;
import com.example.travel_notes_mobile.model.Location;
import com.example.travel_notes_mobile.model.MessageResponse;
import com.example.travel_notes_mobile.model.Note;
import com.example.travel_notes_mobile.service.RetrofitService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class NotesViewModel extends ViewModel {

    private MutableLiveData<List<Note>> mutableLiveList;
    private NotesDatabase notesDatabase;
    private Application application;


    public NotesViewModel(Application application) {
        this.application = application;
        mutableLiveList = new MutableLiveData<>();
        notesDatabase = NotesDatabase.getInstance(application);
    }

    public void fetchNotes(){
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

    public void postNote(Note note){
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

                    Log.d("KUUPA", response.body().getTitle());

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
    public void deleteNote(Note note){
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
    public void updateNote(Note note){
        SharedPreferences sharedPreferences = application.getSharedPreferences("travel_notes_mobile", Context.MODE_PRIVATE);
        String tokenValue = sharedPreferences.getString("token", "");
        Call<MessageResponse> notes = RetrofitService.getNotesInterface().putNoteById(String.valueOf(note.getId()), note, "Bearer " + tokenValue);
        notes.enqueue(new retrofit2.Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, retrofit2.Response<MessageResponse> response) {
                List<Note> currentNotes = mutableLiveList.getValue();
                if (currentNotes != null) {
                    List<Note> updatedNotes = new ArrayList<>(currentNotes);
                    for (int i = 0; i < updatedNotes.size(); i++) {
                        if (updatedNotes.get(i).getId() == note.getId()) {
                            updatedNotes.set(i, note);
                            break;
                        }
                    }
                    mutableLiveList.setValue(updatedNotes);
                    notesDatabase.notesDAO().updateNoteById(note.getId(), note.getTitle(), note.getContent(), note.getLocation(), note.getImageUrl(), note.getUpdatedAt());
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Log.d("KUUPA", t.getMessage());
            }
        });
    }


    public MutableLiveData<List<Note>> getObservedNotes() {
        return mutableLiveList;
    }

}
