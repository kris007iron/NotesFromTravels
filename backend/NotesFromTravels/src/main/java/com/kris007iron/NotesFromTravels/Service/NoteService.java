package com.kris007iron.NotesFromTravels.Service;

import com.kris007iron.NotesFromTravels.Model.Location;
import com.kris007iron.NotesFromTravels.Model.Note;
import com.kris007iron.NotesFromTravels.Model.NoteDTO;
import com.kris007iron.NotesFromTravels.Model.Response.LocationResponseDTO;
import com.kris007iron.NotesFromTravels.Model.Response.NoteResponseDTO;
import com.kris007iron.NotesFromTravels.Model.Response.UserResponseDTO;
import com.kris007iron.NotesFromTravels.Model.User;
import com.kris007iron.NotesFromTravels.Repository.LocationRepository;
import com.kris007iron.NotesFromTravels.Repository.NoteRepository;
import com.kris007iron.NotesFromTravels.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NoteService {
    public final NoteRepository noteRepository;
    public final UserService userService;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

    public NoteService(NoteRepository noteRepository, UserService userService, LocationRepository locationRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userService = userService;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
    }

    public Optional<List<NoteResponseDTO>> findNotesByUserLogin(String login) {
        Optional<User> user = userService.getUserByLogin(login);
        System.out.println(user);
        ArrayList<NoteResponseDTO> noteResponseDTOS = new ArrayList<>();
        for (int i = 0; i < user.get().getNotes().size(); i++) {
            noteResponseDTOS.add(new NoteResponseDTO(user.get().getNotes().get(i).getId(),
                    user.get().getNotes().get(i).getTitle(),
                    user.get().getNotes().get(i).getContent(),
                    new LocationResponseDTO(user.get().getNotes().get(i).getLocation().getId(), user.get().getNotes().get(i).getLocation().getLatitude(), user.get().getNotes().get(i).getLocation().getLongitude()),
                    user.get().getNotes().get(i).getImageUrl()));
        }
        new UserResponseDTO(
                user.get().getId(),
                user.get().getLogin(),
                noteResponseDTOS);
        return Optional.of(noteResponseDTOS);
    }

    public Optional<Note> findUsersNoteById(String login, Long id) {
        Optional<User> user = userService.getUserByLogin(login);
        Optional<Note> note = noteRepository.findById(id);
        if (note.isPresent() && user.isPresent()){
            if (user.get().getNotes().contains(note.get())){
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
        locationRepository.save(newLocation);
        newNote.setTitle(note.getTitle());
        newNote.setContent(note.getContent());
        newNote.setLocation(newLocation);
        newNote.setUser(userRepository.findByLogin(login).get());
        newNote.setImageUrl(note.getImageUrl());
        noteRepository.save(newNote);
        return newNote;
    }
}
