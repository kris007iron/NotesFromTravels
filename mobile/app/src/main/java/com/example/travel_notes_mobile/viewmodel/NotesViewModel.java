package com.example.travel_notes_mobile.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_notes_mobile.model.Notes;
import com.example.travel_notes_mobile.service.RetrofitService;

import retrofit2.Call;

public class NotesViewModel extends ViewModel {

    private MutableLiveData<Notes> mutableLiveList;


    public NotesViewModel() {
        mutableLiveList = new MutableLiveData<>();
    }

    public void fetchNotes(){
        Call<Notes> notes = RetrofitService.getNotesInterface().getNotes();
        notes.enqueue(new retrofit2.Callback<Notes>() {
            @Override
            public void onResponse(Call<Notes> call, retrofit2.Response<Notes> response) {
                mutableLiveList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Notes> call, Throwable t) {
                mutableLiveList.setValue(null);
            }
        });
    }


    public MutableLiveData<Notes> getObservedNotes() {
        return mutableLiveList;
    }

}
