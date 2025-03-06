package com.example.travel_notes_mobile.service;

import com.example.travel_notes_mobile.api.NotesInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private static final String BASE_URL = "http://192.168.0.171:8080/api/v1/";
    private static final Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    public static NotesInterface getNotesInterface() {
        return retrofit.create(NotesInterface.class);
    }

}
