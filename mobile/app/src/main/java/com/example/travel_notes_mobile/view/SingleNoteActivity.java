package com.example.travel_notes_mobile.view;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.bumptech.glide.Glide;
import com.example.travel_notes_mobile.R;
import com.example.travel_notes_mobile.database.NotesDatabase;
import com.example.travel_notes_mobile.databinding.ActivitySingleNoteBinding;
import com.example.travel_notes_mobile.model.Location;
import com.example.travel_notes_mobile.model.Note;
import com.example.travel_notes_mobile.viewmodel.NotesViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class SingleNoteActivity extends AppCompatActivity {
    private ActivitySingleNoteBinding binding;
    private boolean isBeingEdited = false;
    private double changed_latitude = 0;
    private double changed_longitude = 0;
    private NotesViewModel notesViewModel;
    private boolean isBeingUpdated = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySingleNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Note note = (Note) getIntent().getSerializableExtra("note");
        if (note == null) {
            Log.e("ELOOO", "Note is null");
        } else {
            binding.setNote(note);
            String latitude = String.valueOf(note.getLocation().getLatitude());
            String longitude = String.valueOf(note.getLocation().getLongitude());
            String location = latitude + ", " + longitude;
            binding.textLocation.setText(location);


            Glide.with(this)
                    .load(note.getImageUrl())
                    .placeholder(R.drawable.def)
                    .error(R.drawable.teren)
                    .into(binding.imageView);

            binding.editNoteButton.setOnClickListener(v -> {

                isBeingEdited = !isBeingEdited;
                if (isBeingEdited) {
                    binding.editNoteButton.setImageResource(R.drawable.check_24px);
                    binding.textContent.setEnabled(true);
                    binding.changeLocationButton.setVisibility(View.VISIBLE);

                    binding.changeLocationButton.setOnClickListener(view -> {
                        Intent intent = new Intent(SingleNoteActivity.this, MapsActivity.class);
                        startActivityForResult(intent, 1);
                    });

                } else {
                    binding.editNoteButton.setImageResource(R.drawable.draw_24px);
                    binding.textContent.setEnabled(false);
                    binding.changeLocationButton.setVisibility(View.INVISIBLE);

                    String newContent = binding.textContent.getText().toString();
                    String newLocation = binding.textLocation.getText().toString();
                    Location loc = new Location(changed_latitude, changed_longitude);
                    if (changed_latitude == 0 && changed_longitude == 0) {
                        loc = note.getLocation();
                    }

                    Note newNote = new Note(note.getId(), note.getTitle(), newContent, loc, note.getImageUrl(), null, null);
                    notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);

                    notesViewModel.updateNote(newNote);

                    notesViewModel.getObservedNotes().observe(this, notes -> {
                        Note[] all_notes = notesViewModel.getAllNotes();
                        if (all_notes != null) {
                            for (Note note1 : all_notes) {
                                if (note1.getId() == note.getId()) {
                                    notesViewModel.updateNoteInDatabase(newNote);
                                    Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
                                    isBeingUpdated = true;
                                    finish();
                                    break;
                                }
                            }
                        }
                    });

                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            changed_latitude = data.getDoubleExtra("latitude", 0);
            changed_longitude = data.getDoubleExtra("longitude", 0);
            String message = changed_latitude + ", " + changed_longitude;

            binding.textLocation.setText(message);

        }
    }

    @Override
    public void finish() {

        Intent data = new Intent();
        if (isBeingUpdated) {
            Note[] notes = notesViewModel.getAllNotes();
            data.putExtra("new_notes", notes);
        }
        setResult(RESULT_OK, data);

        super.finish();
    }
}