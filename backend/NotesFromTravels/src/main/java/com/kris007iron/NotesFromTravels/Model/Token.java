package com.kris007iron.NotesFromTravels.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="expiredTokens")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 2000)
    private String token;
}
