package com.example.travel_notes_mobile.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class NotesViewModelFactory implements ViewModelProvider.Factory {
    private static NotesViewModelFactory instance;
    private final Application application;

    private NotesViewModelFactory(Application application) {
        this.application = application;
    }

    public static synchronized NotesViewModelFactory getInstance(Application application) {
        if (instance == null) {
            instance = new NotesViewModelFactory(application);
        }
        return instance;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NotesViewModel.class)) {
            return (T) new NotesViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
