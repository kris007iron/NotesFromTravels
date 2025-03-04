package com.kris007iron.NotesFromTravels.Model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO {
    private double latitude;
    private double longitude;

    public LocationDTO(Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }
}
