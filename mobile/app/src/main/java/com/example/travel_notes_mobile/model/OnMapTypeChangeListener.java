package com.example.travel_notes_mobile.model;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

public interface OnMapTypeChangeListener {
    void onMapClick(@NonNull LatLng latLng);

    void onMapTypeChanged(int mapType);
}
