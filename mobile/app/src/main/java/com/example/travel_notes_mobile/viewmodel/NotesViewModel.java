package com.example.travel_notes_mobile.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_notes_mobile.model.Notes;

public class NotesViewModel extends ViewModel {

    private MutableLiveData<Notes> mutableLiveList;

    public NotesViewModel() {
        mutableLiveList = new MutableLiveData<>();
    }




    public MutableLiveData<Notes> getObservedNotes() {
        return mutableLiveList;
    }

}
