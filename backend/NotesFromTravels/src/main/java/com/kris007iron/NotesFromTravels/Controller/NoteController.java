package com.kris007iron.NotesFromTravels.Controller;

import com.kris007iron.NotesFromTravels.Model.Note;
import com.kris007iron.NotesFromTravels.Service.JwtService;
import com.kris007iron.NotesFromTravels.Service.NoteService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/notes")
public class NoteController {
    private final NoteService noteService;

    @GetMapping()
    public Optional<List<Note>> getNotes(HttpServletRequest request) {
        String login = JwtService.extractLoginFromAuth(request);
        return noteService.findNotesByUserLogin(login);
    }

    @GetMapping("/{id}")
    public Optional<Note> getNoteById(@PathVariable Long id, HttpServletRequest request) {
        String login = JwtService.extractLoginFromAuth(request);
        return  noteService.findUsersNoteById(login, id);
    }
}
