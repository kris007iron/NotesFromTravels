package com.kris007iron.NotesFromTravels.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)  // ✅ Prevent infinite loops
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include  // ✅ Only include `id`
    private Long id;

    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    private String imageUrl;
    @ManyToOne(fetch = FetchType.LAZY)  // ✅ Changed from `@OneToOne` to `@ManyToOne`
    @JoinColumn(name = "location_id")
    @EqualsAndHashCode.Exclude
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)  // ✅ Added user reference
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private User user;
}
