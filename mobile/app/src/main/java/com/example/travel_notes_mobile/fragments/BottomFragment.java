package com.example.travel_notes_mobile.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.travel_notes_mobile.databinding.FragmentBottomBinding;
import com.example.travel_notes_mobile.model.Location;
import com.example.travel_notes_mobile.model.Note;
import com.example.travel_notes_mobile.view.MapsActivity;
import com.example.travel_notes_mobile.viewmodel.NotesViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.concurrent.atomic.AtomicBoolean;

public class BottomFragment extends BottomSheetDialogFragment {
    private FragmentBottomBinding binding;
    private double latitude = 0;
    private double longitude = 0;
    private String locationName = "";

    public interface BottomFragmentListener {
        void onNoteSaved(Note note);
    }

    private BottomFragmentListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (BottomFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement BottomFragmentListener");
        }
    }

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

        NotesViewModel notesViewModel = new ViewModelProvider(requireActivity()).get(NotesViewModel.class);

        binding.chooseCords.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), MapsActivity.class);
            startActivityForResult(intent, 1);
        });


        binding.saveNote.setOnClickListener(v -> {
            String title = binding.title.getText().toString();
            String content = binding.desc.getText().toString();
            Location location = new Location(latitude, longitude);
            String imageUrl = binding.backgroundLink.getText().toString();
            if (imageUrl.isEmpty()) {
                imageUrl = "https://images.pexels.com/photos/2265876/pexels-photo-2265876.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1";
            }

            Note note = new Note(0, title, content, location, imageUrl, null, null);
            MutableLiveData<Note> n = notesViewModel.postNote(note);
            AtomicBoolean created = new AtomicBoolean(false);
            n.observe(getViewLifecycleOwner(), note1 -> {
                if (created.get()) {
                    return;
                }
                note.setId(note1.getId());
                created.set(true);
                Toast.makeText(getContext(), "Note saved", Toast.LENGTH_SHORT).show();
                listener.onNoteSaved(note);
                dismiss();

            });


        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            latitude = data.getDoubleExtra("latitude", 0);
            longitude = data.getDoubleExtra("longitude", 0);
            locationName = data.getStringExtra("locationName");
            String message = locationName + ": " + latitude + ", " + longitude;

            binding.chooseCords.setText(message);

        }
    }
}