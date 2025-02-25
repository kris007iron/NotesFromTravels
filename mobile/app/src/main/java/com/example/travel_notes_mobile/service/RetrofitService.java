package com.example.travel_notes_mobile.service;

import com.example.travel_notes_mobile.api.NotesInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private static final String BASE_URL = "http://localhost:8080/";

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static NotesInterface getNotesInterface() {
        return retrofit.create(NotesInterface.class);
    }

}
