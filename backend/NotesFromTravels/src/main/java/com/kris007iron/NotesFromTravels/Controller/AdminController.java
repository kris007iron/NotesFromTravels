package com.kris007iron.NotesFromTravels.Controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/v1/admin")
public class AdminController {
    @GetMapping()
    public String greetAdmin(){
        return "Hello Admin";
    }
}
