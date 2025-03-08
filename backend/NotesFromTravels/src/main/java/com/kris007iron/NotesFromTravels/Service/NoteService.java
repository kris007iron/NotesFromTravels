package com.kris007iron.NotesFromTravels.Service;

import com.kris007iron.NotesFromTravels.Model.Location;
import com.kris007iron.NotesFromTravels.Model.Note;
import com.kris007iron.NotesFromTravels.Model.NoteDTO;
import com.kris007iron.NotesFromTravels.Model.Response.LocationResponseDTO;
import com.kris007iron.NotesFromTravels.Model.Response.MessageResponseDTO;
import com.kris007iron.NotesFromTravels.Model.Response.NoteResponseDTO;
import com.kris007iron.NotesFromTravels.Model.Response.UserResponseDTO;
import com.kris007iron.NotesFromTravels.Model.User;
import com.kris007iron.NotesFromTravels.Repository.LocationRepository;
import com.kris007iron.NotesFromTravels.Repository.NoteRepository;
import com.kris007iron.NotesFromTravels.Repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<Optional<NoteResponseDTO>> updateNote(NoteDTO note, Long id, String login) {
        Optional<Note> noteToUpdate = findUsersNoteById(login, id);
        if(noteToUpdate.isPresent()){
            noteToUpdate.get().setTitle(note.getTitle());
            noteToUpdate.get().setContent(note.getContent());
            Location locationToUpdate = noteToUpdate.get().getLocation();
            locationToUpdate.setLongitude(note.getLocation().getLongitude());
            locationToUpdate.setLatitude(note.getLocation().getLatitude());
            locationRepository.save(locationToUpdate);
            noteToUpdate.get().setImageUrl(note.getImageUrl());
            noteRepository.save(noteToUpdate.get());
        }
        Optional<NoteResponseDTO> body = createNoteResponseDTOFromNote(noteToUpdate);
        if(body.isPresent()){
            return new ResponseEntity<>(body, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        }
    }

    private Optional<NoteResponseDTO> createNoteResponseDTOFromNote(Optional<Note> note){
        return note.map(value -> new NoteResponseDTO(value.getId(),
                value.getTitle(),
                value.getContent(),
                new LocationResponseDTO(value.getLocation().getId(), value.getLocation().getLatitude(), value.getLocation().getLongitude()),
                value.getImageUrl()));
    }

    public ResponseEntity<MessageResponseDTO> deleteNote(Long id, String login) {
        Optional<Note> noteToDelete = findUsersNoteById(login, id);
        if(noteToDelete.isPresent()){
            try{
                System.out.println(noteToDelete.get().getId());
                noteToDelete.get().setUser(null);
                noteRepository.deleteById(noteToDelete.get().getId());
                return new ResponseEntity<>(new MessageResponseDTO("Note deleted successfully."), HttpStatus.OK);
            } catch (Exception e){
                return new ResponseEntity<>(new MessageResponseDTO("Internal server error: " + e), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else
        {
            return new ResponseEntity<>(new MessageResponseDTO("Note doesn't exists"), HttpStatus.NOT_FOUND);
        }
    }
}
