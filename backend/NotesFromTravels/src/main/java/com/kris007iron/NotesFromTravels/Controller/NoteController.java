package com.kris007iron.NotesFromTravels.Controller;

import com.kris007iron.NotesFromTravels.Model.Note;
import com.kris007iron.NotesFromTravels.Model.NoteDTO;
import com.kris007iron.NotesFromTravels.Model.Response.MessageResponseDTO;
import com.kris007iron.NotesFromTravels.Model.Response.NoteResponseDTO;
import com.kris007iron.NotesFromTravels.Service.JwtService;
import com.kris007iron.NotesFromTravels.Service.NoteService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/notes")
public class NoteController {
    private final NoteService noteService;

    @GetMapping()
    public ResponseEntity<Optional<List<NoteResponseDTO>>> getNotes(HttpServletRequest request) {
        String login = JwtService.extractLoginFromAuth(request);
        return noteService.findNotesByUserLogin(login);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<NoteResponseDTO>> getNoteById(@PathVariable Long id, HttpServletRequest request) {
        String login = JwtService.extractLoginFromAuth(request);

        return  noteService.getUsersNoteById(login, id);
    }

    @PostMapping()
    public ResponseEntity<Optional<Note>> createNote(@RequestBody NoteDTO note, HttpServletRequest request)
    {
        String login = JwtService.extractLoginFromAuth(request);
        return noteService.createNote(note, login);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Optional<NoteResponseDTO>> updatedNote(@PathVariable Long id, @RequestBody NoteDTO note, HttpServletRequest request) {
        String login = JwtService.extractLoginFromAuth(request);
        return noteService.updateNote(note, id, login);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> deleteNote(@PathVariable Long id, HttpServletRequest request) {
        String login = JwtService.extractLoginFromAuth(request);
        return noteService.deleteNote(id, login);
    }
}
