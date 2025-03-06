package com.example.travel_notes_mobile.fragments;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.travel_notes_mobile.databinding.FragmentBottomBinding;
import com.example.travel_notes_mobile.model.Location;
import com.example.travel_notes_mobile.model.Note;
import com.example.travel_notes_mobile.view.MapsActivity;
import com.example.travel_notes_mobile.viewmodel.NotesViewModel;
import com.example.travel_notes_mobile.viewmodel.NotesViewModelFactory;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class BottomFragment extends BottomSheetDialogFragment {
    private FragmentBottomBinding binding;

    public BottomFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentBottomBinding.inflate(getLayoutInflater());
        Application application = requireActivity().getApplication();

        NotesViewModelFactory factory = NotesViewModelFactory.getInstance(application);
        NotesViewModel notesViewModel = new ViewModelProvider(requireActivity(), factory).get(NotesViewModel.class);

        binding.chooseCords.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), MapsActivity.class);
            Log.d("MAPPA", "JESTEM WE FRAGMENCIE");
            startActivity(intent);
        });

        binding.saveNote.setOnClickListener(v -> {
            String title = binding.title.getText().toString();
            String content = binding.desc.getText().toString();
            Location location = new Location(0, 0);

            Note note = new Note(0, title, content, location, null, null, null);
            notesViewModel.postNote(note);
            Toast.makeText(getContext(), "Note saved", Toast.LENGTH_SHORT).show();

            dismiss();
        });

        return binding.getRoot();
    }
}