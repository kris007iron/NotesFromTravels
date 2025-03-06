package com.example.travel_notes_mobile.database;

import androidx.room.TypeConverter;

import com.example.travel_notes_mobile.model.Location;

public class Converters {
    @TypeConverter
    public static String fromLocation(Location location) {
        if (location == null) {
            return null;
        }
        return location.getLatitude() + "," + location.getLongitude();
    }

    @TypeConverter
    public static Location toLocation(String locationString) {
        if (locationString == null || locationString.isEmpty()) {
            return null;
        }
        String[] parts = locationString.split(",");
        return new Location(Float.parseFloat(parts[0]), Float.parseFloat(parts[1]));
    }
}