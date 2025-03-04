package com.kris007iron.NotesFromTravels.Repository;

import com.kris007iron.NotesFromTravels.Model.Note;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository  extends JpaRepository<Note, Long> {
}
