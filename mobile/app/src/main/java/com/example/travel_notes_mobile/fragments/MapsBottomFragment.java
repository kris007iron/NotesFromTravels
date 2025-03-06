package com.example.travel_notes_mobile.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.travel_notes_mobile.R;
import com.example.travel_notes_mobile.adapters.MapTypesAdapter;
import com.example.travel_notes_mobile.model.MapElement;
import com.example.travel_notes_mobile.model.OnMapTypeChangeListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class MapsBottomFragment extends BottomSheetDialogFragment {
    List<MapElement> lista;
    private OnMapTypeChangeListener onMapTypeChange;

    public MapsBottomFragment() {
        this.lista = List.of(
                new MapElement(1, GoogleMap.MAP_TYPE_NORMAL, "Droga", R.drawable.def),
                new MapElement(2, GoogleMap.MAP_TYPE_HYBRID, "Satelita", R.drawable.satelita),
                new MapElement(3, GoogleMap.MAP_TYPE_TERRAIN, "Teren", R.drawable.teren)
        );


    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Sprawdzamy, czy aktywność implementuje interfejs OnMapTypeChangeListener

        if (context instanceof OnMapTypeChangeListener) {
            onMapTypeChange = (OnMapTypeChangeListener) context;
        } else if (context instanceof Activity) {
            Activity activity = (Activity) context;
            throw new RuntimeException(activity.toString()
                    + " activity musi implementować OnMapTypeChangeListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_maps_bottom, container, false);

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GridView gridView = view.findViewById(R.id.grid);
        gridView.setAdapter(new MapTypesAdapter(getContext(), lista));
    }

}