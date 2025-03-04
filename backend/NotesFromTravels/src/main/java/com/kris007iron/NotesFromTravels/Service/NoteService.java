package com.kris007iron.NotesFromTravels.Service;

import com.kris007iron.NotesFromTravels.Model.Location;
import com.kris007iron.NotesFromTravels.Model.Note;
import com.kris007iron.NotesFromTravels.Model.NoteDTO;
import com.kris007iron.NotesFromTravels.Model.User;
import com.kris007iron.NotesFromTravels.Repository.LocationRepository;
import com.kris007iron.NotesFromTravels.Repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class NoteService {
    public final NoteRepository noteRepository;
    public final UserService userService;
    private final LocationRepository locationRepository;

    public NoteService(NoteRepository noteRepository, UserService userService, LocationRepository locationRepository) {
        this.noteRepository = noteRepository;
        this.userService = userService;
        this.locationRepository = locationRepository;
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

    public Note createNote(NoteDTO note, String login) {
        Note newNote = new Note();
        Location newLocation = new Location();

        newLocation.setLatitude(note.getLocation().getLatitude());
        newLocation.setLongitude(note.getLocation().getLongitude());

        newNote.setTitle(note.getTitle());
        newNote.setContent(note.getContent());
        newNote.setLocation(newLocation);
        newNote.setUser(userService.getUserByLogin(login).get());
        locationRepository.save(newLocation);
        noteRepository.save(newNote);
        return newNote;
    }
}
