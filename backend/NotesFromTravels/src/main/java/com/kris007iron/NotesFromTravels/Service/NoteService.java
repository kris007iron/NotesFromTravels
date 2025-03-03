package com.kris007iron.NotesFromTravels.Service;

import com.kris007iron.NotesFromTravels.Model.Note;
import com.kris007iron.NotesFromTravels.Model.NoteDTO;
import com.kris007iron.NotesFromTravels.Model.User;
import com.kris007iron.NotesFromTravels.Repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class NoteService {
    public final NoteRepository noteRepository;
    public final UserService userService;
    public NoteService(NoteRepository noteRepository, UserService userService) {
        this.noteRepository = noteRepository;
        this.userService = userService;
    }

    public Optional<List<Note>> findNotesByUserLogin(String login) {
        Optional<User> user = userService.getUserByLogin(login);
        return user.map(value -> noteRepository.findByUserId(value.getId()));
    }

    public Optional<Note> findUsersNoteById(String login, Long id) {
        Optional<User> user = userService.getUserByLogin(login);
        Optional<Note> note = noteRepository.findById(id);
        if (note.isPresent() && user.isPresent()){
            if (Objects.equals(note.get().getUser(), user.get())){
                return note;
            }
        }
        return Optional.empty();
    }

    public Note createNote(NoteDTO note) {
        Note newNote = new Note();

        noteRepository.save(newNote);
        return newNote;
    }
}
