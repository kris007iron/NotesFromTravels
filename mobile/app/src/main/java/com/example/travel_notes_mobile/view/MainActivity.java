package com.example.travel_notes_mobile.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.travel_notes_mobile.adapters.NotesAdapter;
import com.example.travel_notes_mobile.databinding.ActivityMainBinding;
import com.example.travel_notes_mobile.fragments.BottomFragment;
import com.example.travel_notes_mobile.model.Note;
import com.example.travel_notes_mobile.viewmodel.NotesViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements BottomFragment.BottomFragmentListener {
    private ActivityMainBinding binding;
    private NotesViewModel notesViewModel;
    private NotesAdapter notesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(Color.argb(255, 0, 0, 0));

        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data != null && data.isHierarchical()) {
            String token = data.getQueryParameter("token");
            SharedPreferences sharedPreferences = getSharedPreferences("travel_notes_mobile", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("token", token);
            editor.apply();

            notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);

            notesViewModel.getObservedNotes().observe(this, notes -> {
                if (notes != null) {
                    notesAdapter = new NotesAdapter(this, notes, this);
                    binding.notesGrid.setAdapter(notesAdapter);
                }
            });
        }

        binding.addNoteButton.setOnClickListener(v -> {
            BottomFragment bottomFragment = new BottomFragment();
            bottomFragment.show(getSupportFragmentManager(), bottomFragment.getTag());

        });
        binding.mapButton.setOnClickListener(v -> {
            Intent intent1 = new Intent(this, MapsActivity.class);
            intent1.putExtra("all_locations", true);
            startActivity(intent1);
        });
        binding.logout.setOnClickListener(v->{
            notesViewModel.logout_user();

            Intent log_out = new Intent(this, AuthActivity.class);
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            startActivity(log_out);
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Note[] notatki = (Note[]) data.getSerializableExtra("new_notes");
            List<Note> notes = new ArrayList<>();
            if (notatki != null) {
                Collections.addAll(notes, notatki);

                MutableLiveData<List<Note>> mutableLiveData = new MutableLiveData<>();
                mutableLiveData.setValue(notes);
                notesViewModel.setMutableLiveList(mutableLiveData);

                notesAdapter.updateNotes(notes);


            }
        }
    }
    @Override
    public void onNoteSaved(Note note){
        Note[] notes = notesViewModel.getAllNotes();
        List<Note> notesList = new ArrayList<>();
        Collections.addAll(notesList, notes);
        notesViewModel.setMutableLiveList(new MutableLiveData<>(notesList));
        notesAdapter.updateNotes(notesList);
    }
}