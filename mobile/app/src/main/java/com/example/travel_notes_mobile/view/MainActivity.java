package com.example.travel_notes_mobile.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travel_notes_mobile.adapters.NotesAdapter;
import com.example.travel_notes_mobile.databinding.ActivityMainBinding;
import com.example.travel_notes_mobile.model.Note;
import com.example.travel_notes_mobile.viewmodel.NotesViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private NotesViewModel notesViewModel;
    private NotesAdapter notesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(Color.argb(255, 0, 0, 0));

        Log.d("ELOOO", "czekam");
        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data != null && data.isHierarchical()) {
            String token = data.getQueryParameter("token");
            Log.d("ELOOO", token);
            SharedPreferences sharedPreferences = getSharedPreferences("travel_notes_mobile", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("token", token);
            editor.apply();

            notesViewModel = new NotesViewModel(getApplication());
            notesViewModel.fetchNotes();

            notesViewModel.getObservedNotes().observe(this, notes -> {
                if (notes != null){
                    notesAdapter = new NotesAdapter(this, notes);
                    binding.notesGrid.setAdapter(notesAdapter);
                }
            });


        }
    }
}