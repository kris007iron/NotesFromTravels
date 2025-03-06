package com.example.travel_notes_mobile.view;

import android.icu.text.MessagePattern;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.GenericLifecycleObserver;

import com.bumptech.glide.Glide;
import com.example.travel_notes_mobile.R;
import com.example.travel_notes_mobile.databinding.ActivitySingleNoteBinding;
import com.example.travel_notes_mobile.model.Note;

public class SingleNoteActivity extends AppCompatActivity {
    private ActivitySingleNoteBinding binding;

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
            String location = "Coordinates: "  +latitude + ", " + longitude;
            binding.textLocation.setText(location);
//            Log.d("ELOOO", note.getImageUrl());


            Glide.with(this)
                    .load(note.getImageUrl())
                    .placeholder(R.drawable.def) // Set a placeholder image
                    .error(R.drawable.teren) // Set an error image
                    .into(binding.imageView);


        }


    }
}