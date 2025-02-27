package com.example.travel_notes_mobile.view;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travel_notes_mobile.databinding.ActivityMainBinding;
import com.example.travel_notes_mobile.model.Notes;
import com.example.travel_notes_mobile.viewmodel.NotesViewModel;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(Color.argb(255, 0, 0, 0));

        NotesViewModel notesViewModel = new NotesViewModel();

        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data != null && data.isHierarchical()) {
            String token = data.getQueryParameter("token");
            Log.d("ELOOO", token);

// zdobyc token, zapisac go w shared preferences,
//            notesViewModel.fetchNotes();
//            Notes notes = notesViewModel.getObservedNotes().getValue();



        }
    }
}