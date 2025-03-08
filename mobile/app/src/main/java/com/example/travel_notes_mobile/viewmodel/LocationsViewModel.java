package com.example.travel_notes_mobile.viewmodel;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_notes_mobile.database.NotesDatabase;
import com.example.travel_notes_mobile.model.Location;
import com.example.travel_notes_mobile.model.Note;

import java.util.List;

public class LocationsViewModel extends ViewModel {

    private NotesDatabase notesDatabase;
    private MutableLiveData<Location[]> mutableLiveList;
    private Application application;

    public LocationsViewModel(Application application) {
        this.application = application;
        notesDatabase = NotesDatabase.getInstance(application);
        mutableLiveList = new MutableLiveData<>();
    }
    public void getLocationsFromDatabase(){
        Location[] locations = notesDatabase.notesDAO().getAllLocations();
        mutableLiveList.setValue(locations);
    }

    public MutableLiveData<Location[]> getObservedLocations(){
        return mutableLiveList;
    }
}
