package com.kris007iron.NotesFromTravels.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AuthController {
    @GetMapping("/")
    public String Start(){
        System.out.println("Start");
        return "it's get mapping for /";
    }
    @GetMapping("/error")
    public String Error(){
        System.out.println("Error");
        return "it's get mapping for error";
    }
}
