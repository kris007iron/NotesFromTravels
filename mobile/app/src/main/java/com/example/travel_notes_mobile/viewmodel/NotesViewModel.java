package com.example.travel_notes_mobile.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_notes_mobile.database.NotesDatabase;
import com.example.travel_notes_mobile.model.Location;
import com.example.travel_notes_mobile.model.MessageResponse;
import com.example.travel_notes_mobile.model.Note;
import com.example.travel_notes_mobile.service.RetrofitService;
import com.example.travel_notes_mobile.view.AuthActivity;

import java.io.IOException;
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
                if (response.code() == 401) {
                    logout();
                    return;
                }
                String newToken = response.headers().get("Authorization");
                if (newToken != null && !newToken.isEmpty()){
                    updateToken(newToken);
                }
                else{
                    Log.d("TOKENISKO", "cos jest nie tak fetch");
                }
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

    public MutableLiveData<Note> postNote(Note note) {
        MutableLiveData<Note> result = new MutableLiveData<>();
        SharedPreferences sharedPreferences = application.getSharedPreferences("travel_notes_mobile", Context.MODE_PRIVATE);
        String tokenValue = sharedPreferences.getString("token", "");
        Call<Note> notes = RetrofitService.getNotesInterface().postNote("Bearer " + tokenValue, note);
        Log.d("SIEMAAA", "POSTUJE");
        notes.enqueue(new retrofit2.Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, retrofit2.Response<Note> response) {
                Log.d("SIEMAAA", "RESPONSE");
                if (response.code() == 401) {
                    logout();
                    return;
                }

                String newToken = response.headers().get("Authorization");
                if (newToken != null && !newToken.isEmpty()){
                    updateToken(newToken);
                }
                else{
                    Log.d("TOKENISKO", "cos jest nie tak post");
                }

                List<Note> currentNotes = mutableLiveList.getValue();

                if (currentNotes != null) {
                    List<Note> updatedNotes = new ArrayList<>(currentNotes);
                    updatedNotes.add(response.body());
                    mutableLiveList.setValue(updatedNotes);
                    notesDatabase.notesDAO().insertNote(response.body());

                }

                    result.setValue(response.body());

            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                Log.d("NotesViewModelDebug", t.getMessage());
                Log.d("SIEMAAA", "FAILURE" + t.getMessage());

            }
        });
        return result;
    }

    public MutableLiveData<Boolean> deleteNote(Note note) {
        SharedPreferences sharedPreferences = application.getSharedPreferences("travel_notes_mobile", Context.MODE_PRIVATE);
        String tokenValue = sharedPreferences.getString("token", "");
        Call<MessageResponse> notes = RetrofitService.getNotesInterface().deleteNote("Bearer " + tokenValue, note.getId());
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        notes.enqueue(new retrofit2.Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, retrofit2.Response<MessageResponse> response) {
                if (response.code() == 401) {
                    logout();
                    result.setValue(false);
                    return;
                }
                String newToken = response.headers().get("Authorization");
                if (newToken != null && !newToken.isEmpty()){
                    updateToken(newToken);
                }
                else{
                    Log.d("TOKENISKO", "cos jest nie tak delete");
                }
                List<Note> currentNotes = mutableLiveList.getValue();
                if (currentNotes != null) {
                    List<Note> updatedNotes = new ArrayList<>(currentNotes);
                    updatedNotes.remove(note);
                    result.setValue(true);
                    mutableLiveList.setValue(updatedNotes);
                    notesDatabase.notesDAO().deleteNoteById(note.getId());
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Log.d("NotesViewModelDebug", t.getMessage());
            }
        });
        return result;
    }

    public MutableLiveData<Boolean> updateNote(Note note) {
        SharedPreferences sharedPreferences = application.getSharedPreferences("travel_notes_mobile", Context.MODE_PRIVATE);
        String tokenValue = sharedPreferences.getString("token", "");
        Call<Note> notes = RetrofitService.getNotesInterface().putNoteById(note.getId(), note, "Bearer " + tokenValue);
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        notes.enqueue(new retrofit2.Callback<Note>() {

            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                if (response.code() == 401) {
                    logout();
                    result.setValue(false);
                    return;
                }
                String newToken = response.headers().get("Authorization");
                if (newToken != null && !newToken.isEmpty()){
                    updateToken(newToken);
                    result.setValue(true);
                }
                else{
                    Log.d("TOKENISKO", "cos jest nie tak put");
                }
            }

            @Override
            public void onFailure(Call<Note> call, Throwable throwable) {}
        });
        return result;
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
    public void updateToken(String token){
        Log.e("TOKENISKO", token);
        SharedPreferences sharedPreferences = application.getSharedPreferences("travel_notes_mobile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();
    }
    public void logout(){
        String errorMessage = "Unauthorized";
        Toast.makeText(application, errorMessage, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(application, AuthActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        application.startActivity(intent);
    }
    public void logout_user(){
        SharedPreferences sharedPreferences = application.getSharedPreferences("travel_notes_mobile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("token");
        editor.apply();

        Call<MessageResponse> logout = RetrofitService.getNotesInterface().logout("Bearer " + sharedPreferences.getString("token", ""));
        logout.enqueue(new retrofit2.Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.code() == 401) {
                    logout();
                }
                notesDatabase.notesDAO().deleteAllNotes();
                mutableLiveList.setValue(new ArrayList<>());

            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Log.d("NotesViewModelDebug", t.getMessage());
            }
        });

    }

}
