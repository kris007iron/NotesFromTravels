package com.kris007iron.NotesFromTravels.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoteDTO {
    private String title;
    private String content;
    private LocationDTO location;
    private String imageUrl;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    static class LocationDTO {
        private double latitude;
        private double longitude;
    }

}
