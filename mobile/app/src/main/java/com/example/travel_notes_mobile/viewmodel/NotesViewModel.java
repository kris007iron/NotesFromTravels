package com.example.travel_notes_mobile.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_notes_mobile.database.NotesDatabase;
import com.example.travel_notes_mobile.model.Note;
import com.example.travel_notes_mobile.service.RetrofitService;

import java.io.Closeable;
import java.util.List;

import retrofit2.Call;

public class NotesViewModel extends ViewModel {

    private MutableLiveData<List<Note>> mutableLiveList;
    private MutableLiveData<String> token;
    private NotesDatabase notesDatabase;


    public NotesViewModel(Application application) {
        mutableLiveList = new MutableLiveData<>();
        token = new MutableLiveData<>();
        notesDatabase = NotesDatabase.getInstance(application);
    }

    public void fetchNotes(){
        String tokenValue = this.token.getValue();
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
                mutableLiveList.setValue(null);
            }
        });
    }


    public MutableLiveData<List<Note>> getObservedNotes() {
        return mutableLiveList;
    }
    public MutableLiveData<String> getObservedToken() {
        return token;
    }
    public void setToken(String token) {
        this.token.setValue(token);
    }

}
