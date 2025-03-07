package com.example.travel_notes_mobile.view;

import android.content.Intent;
import android.icu.text.MessagePattern;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.GenericLifecycleObserver;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.travel_notes_mobile.R;
import com.example.travel_notes_mobile.databinding.ActivitySingleNoteBinding;
import com.example.travel_notes_mobile.model.Location;
import com.example.travel_notes_mobile.model.Note;
import com.example.travel_notes_mobile.viewmodel.NotesViewModel;
import com.example.travel_notes_mobile.viewmodel.NotesViewModelFactory;

public class SingleNoteActivity extends AppCompatActivity {
    private NotesViewModel notesViewModel;
    private ActivitySingleNoteBinding binding;
    private boolean isBeingEdited = false;
    private double changed_latitude = 0;
    private double changed_longitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySingleNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Note note = (Note) getIntent().getSerializableExtra("note");
        if (note == null){
            Log.e("ELOOO", "Note is null");
        }
        else{
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

            binding.editNoteButton.setOnClickListener(v->{

                isBeingEdited = !isBeingEdited;
                if (isBeingEdited){
                    binding.editNoteButton.setImageResource(R.drawable.check_24px);
                    binding.textContent.setEnabled(true);
                    binding.textLocation.setEnabled(true);
                    binding.changeLocationButton.setVisibility(View.VISIBLE);
                    
                    binding.changeLocationButton.setOnClickListener(view->{
                        Intent intent = new Intent(SingleNoteActivity.this, MapsActivity.class);
                        startActivityForResult(intent, 1);
                    });
                    
                }
                else{
                    binding.editNoteButton.setImageResource(R.drawable.draw_24px);
                    binding.textContent.setEnabled(false);
                    binding.textLocation.setEnabled(false);
                    binding.changeLocationButton.setVisibility(View.INVISIBLE);

                    String newContent = binding.textContent.getText().toString();
                    String newLocation = binding.textLocation.getText().toString();
                    Location loc = new Location(changed_latitude, changed_longitude);

                    Note newNote = new Note(note.getId(), note.getTitle(), newContent, loc, note.getImageUrl(), null, null);

                    NotesViewModelFactory factory = NotesViewModelFactory.getInstance(getApplication());
                    notesViewModel = new ViewModelProvider(this, factory).get(NotesViewModel.class);
                    notesViewModel.updateNote(newNote);

                    Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();

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
}