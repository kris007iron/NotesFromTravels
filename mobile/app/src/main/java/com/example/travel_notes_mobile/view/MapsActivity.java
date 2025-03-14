package com.example.travel_notes_mobile.view;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.travel_notes_mobile.BuildConfig;
import com.example.travel_notes_mobile.R;
import com.example.travel_notes_mobile.databinding.ActivityMapsBinding;
import com.example.travel_notes_mobile.fragments.MapsBottomFragment;
import com.example.travel_notes_mobile.model.Location;
import com.example.travel_notes_mobile.model.OnMapTypeChangeListener;
import com.example.travel_notes_mobile.viewmodel.LocationsViewModel;
import com.example.travel_notes_mobile.viewmodel.NotesViewModel;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, OnMapTypeChangeListener, GoogleMap.OnMapClickListener {
    private ActivityMapsBinding binding;
    private GoogleMap map;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private List<Address> list;
    private Geocoder geocoder;
    private NotesViewModel notesViewModel;
    private double latitude;
    private double longitude;
    private String locationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ELOOO", "onCreate called");
//                setContentView(R.layout.activity_maps);
        try {
            binding = ActivityMapsBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            Log.d("ELOOO", "setContentView successful");
        } catch (Exception e) {
            Log.e("ELOOO", "Error in setContentView", e);
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            Log.d("ELOOO", "MapFragment found");
            mapFragment.getMapAsync(this);
        } else {
            Log.e("ELOOO", "MapFragment is null");
        }

        geocoder = new Geocoder(this);
        Log.d("ELOOO", BuildConfig.MAPS_API_KEY);
        Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        PlacesClient placesClient = Places.createClient(this);
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        if (autocompleteFragment != null) {
            autocompleteFragment.setActivityMode(AutocompleteActivityMode.FULLSCREEN);
            autocompleteFragment.getView().setBackgroundColor(0x0000ff);
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onError(@NonNull Status status) {
                    Log.e("ELOOO", "Error: " + status);
                }

                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    try{
                        LatLng latLng = place.getLatLng();
                        Log.d("ELOOO", "Place: " + place.getName() + ", " + latLng);
                        longitude = latLng.longitude;
                        latitude = latLng.latitude;
                        locationName = place.getName();
                    }
                    catch (Exception e){
                        Log.e("ELOOO", "Error in onPlaceSelected", e);
                    }

                    try {
                        geocode(place.getName());


                    } catch (IOException e) {
                        Log.e("ELOOO", "Geocode error", e);
                    }
                }
            });
        } else {
            Log.e("ELOOO", "AutocompleteFragment is null");
        }



        MapsBottomFragment bottomFragment = new MapsBottomFragment();
//        bottomFragment.show(getSupportFragmentManager(), "myBottom");
        FloatingActionButton fab = findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(v -> bottomFragment.show(getSupportFragmentManager(), "myBottom"));
        } else {
            Log.e("ELOOO", "FloatingActionButton is null");
        }

        binding.sendCords.setOnClickListener(v->{
            Intent intent = new Intent();
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
            intent.putExtra("locationName", locationName);
            intent.putExtra("requestCode", 1);
            setResult(RESULT_OK, intent);
            finish();

        });
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        setupMap();


        boolean allLocations = getIntent().getBooleanExtra("all_locations", false);
        if (allLocations){
            binding.sendCords.setVisibility(View.INVISIBLE);

            LocationsViewModel locationsViewModel = new LocationsViewModel(getApplication());
            locationsViewModel.getLocationsFromDatabase();
            Location[] visited_locations = locationsViewModel.getObservedLocations().getValue();
            if(visited_locations == null){
                visited_locations = new Location[0];
            }
            else{
                for (Location location : visited_locations) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    map.addMarker(new MarkerOptions().position(latLng).title(""));
                }
            }
        }
        else{
            map.setOnMapClickListener(this);
        }
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng){
        map.clear();
        map.addMarker(new MarkerOptions().position(latLng).title("My pin"));
        latitude = latLng.latitude;
        longitude = latLng.longitude;
        locationName = "My pin";

        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            locationName = addresses.get(0).getLocality();
        } catch (IOException e) {
            Log.e("ELOOO", "Geocode error", e);
        }
    }

    private void setupMap() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
                if (mapFragment != null) {
                    mapFragment.getMapAsync(this);
                }
            }
        }
    }

    private void geocode(String locationName) throws IOException {
        list = geocoder.getFromLocationName(locationName, 1);
        if (list != null && !list.isEmpty()) {
            double latitude = list.get(0).getLatitude();
            double longitude = list.get(0).getLongitude();

            LatLng latLng = new LatLng(latitude, longitude);
            map.addMarker(new MarkerOptions().position(latLng).title(locationName));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
            map.moveCamera(cameraUpdate);
            map.animateCamera(cameraUpdate);
        } else {
            Log.e("ELOOO", "Geocode returned no results");
        }
    }

    @Override
    public void onMapTypeChanged(int mapType) {
        if (map != null) {
            map.setMapType(mapType);
        } else {
            Log.e("ELOOO", "GoogleMap is null");
        }
    }
}