package com.kris007iron.NotesFromTravels.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@ToString(exclude = "notes")
@Entity
@Table(name="locations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double latitude;
    private double longitude;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Note> notes;
}
