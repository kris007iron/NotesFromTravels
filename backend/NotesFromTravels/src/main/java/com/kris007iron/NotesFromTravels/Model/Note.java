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
    @Column(length = 2000)
    private String content;
    private String imageUrl;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    @EqualsAndHashCode.Exclude
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)  // ✅ Added user reference
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private User user;
}
