package com.kris007iron.NotesFromTravels.Controller;

import com.kris007iron.NotesFromTravels.Model.Note;
import com.kris007iron.NotesFromTravels.Service.JwtService;
import com.kris007iron.NotesFromTravels.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class NoteController {
    private final UserService userService;

    @GetMapping("/api/v1/example_note")
    public Note exampleNote(HttpServletRequest request) {
        Note example_note = new Note();

        if (request.getHeader("Authorization") != null)
        {
            if(request.getHeader("Authorization").startsWith("Bearer ")) {
                String token = request.getHeader("Authorization").substring(7);
                String login = JwtService.getLoginFromToken(token);
                System.out.println("login: " + login);
                if (userService.getUserByLogin(login).isPresent()) {
                    example_note.setUser(userService.getUserByLogin(login).get());
                }
            }
        }
        //test data
        example_note.setContent("In Tatra mountains beneath goat hill");
        example_note.setTitle("Tatra expedition");
        return example_note;
    }
}
