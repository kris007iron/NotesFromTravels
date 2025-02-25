package com.kris007iron.NotesFromTravels.Controller;

import com.kris007iron.NotesFromTravels.Model.Note;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class NoteController {
    @GetMapping("/api/v1/example_note")
    public Note exampleNote() {
        //test data
        Note example_note = new Note();
        example_note.setContent("In Tatra mountains beneath goat hill");
        example_note.setTitle("Tatra expedition");
        return example_note;
    }
}
