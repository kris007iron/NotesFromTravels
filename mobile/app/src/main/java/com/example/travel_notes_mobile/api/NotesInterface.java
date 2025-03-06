package com.example.travel_notes_mobile.api;

import com.example.travel_notes_mobile.model.AuthResponse;
import com.example.travel_notes_mobile.model.MessageResponse;
import com.example.travel_notes_mobile.model.Note;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NotesInterface {

    @GET("notes")
    Call<List<Note>> getNotes(@Header("Authorization") String token);

    @GET("notes/{id}")
    Call<Note> getNoteById(@Path("id") String id);

    @POST("notes")
    Call<Note> postNote(@Header("Authorization") String token, @Body Note note);

    @PUT("notes/{id}")
    Call<Note> putNoteById(@Path("id") String id, @Body Note note);

    @DELETE("notes/{id}")
    Call<MessageResponse> deleteNoteById(@Path("id") String id);

    @POST("/oauth/token")
    Call<AuthResponse> postAuthData(@Header("Authorization") String token);

}
