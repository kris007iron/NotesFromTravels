package com.kris007iron.NotesFromTravels.Controller;

import com.kris007iron.NotesFromTravels.Model.Note;
import com.kris007iron.NotesFromTravels.Model.NoteDTO;
import com.kris007iron.NotesFromTravels.Model.Response.NoteResponseDTO;
import com.kris007iron.NotesFromTravels.Service.JwtService;
import com.kris007iron.NotesFromTravels.Service.NoteService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public Optional<List<NoteResponseDTO>> getNotes(HttpServletRequest request) {
        String login = JwtService.extractLoginFromAuth(request);
        System.out.println(login);
        System.out.println(noteService.findNotesByUserLogin(login));
        return noteService.findNotesByUserLogin(login);
    }

    @GetMapping("/{id}")
    public Optional<Note> getNoteById(@PathVariable Long id, HttpServletRequest request) {
        String login = JwtService.extractLoginFromAuth(request);

        return  noteService.findUsersNoteById(login, id);
    }

    @PostMapping()
    public ResponseEntity<Note> createNote(@RequestBody NoteDTO note, HttpServletRequest request)
    {
        String login = JwtService.extractLoginFromAuth(request);
        Note createdNote = noteService.createNote(note, login);
        System.out.println("Created Note:" + createdNote.toString());
        return new ResponseEntity<>(createdNote, HttpStatus.OK);
    }
}
