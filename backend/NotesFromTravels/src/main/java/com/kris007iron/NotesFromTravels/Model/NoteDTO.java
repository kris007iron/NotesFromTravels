package com.kris007iron.NotesFromTravels.Model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class NoteDTO {
    private String title;
    private String content;
    private LocationDTO location;
    private String imageUrl;

}
