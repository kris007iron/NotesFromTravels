package com.kris007iron.NotesFromTravels.Controller;

import com.kris007iron.NotesFromTravels.Model.Response.MessageResponseDTO;
import com.kris007iron.NotesFromTravels.Model.Response.UserResponseDTO;
import com.kris007iron.NotesFromTravels.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String greetAdmin(){
        return "Hello Admin";
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        return userService.getUsers();
    }

    @PutMapping("/users")
    public ResponseEntity<Optional<UserResponseDTO>> updateRole(@RequestBody UserResponseDTO userResponseDTO){
        return userService.updateUser(userResponseDTO);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<MessageResponseDTO> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
