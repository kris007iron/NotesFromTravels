package com.kris007iron.NotesFromTravels.Service;

import com.kris007iron.NotesFromTravels.Model.Response.UserResponseDTO;
import com.kris007iron.NotesFromTravels.Model.User;
import com.kris007iron.NotesFromTravels.Repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    public final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findOrCreateUser(String login, String name){
        if(getUserByLogin(login).isEmpty()){
            User newUser = new User();
            newUser.setLogin(login);
            newUser.setName(name);
            newUser.setRole("USER");
            return userRepository.save(newUser);
        }
        return getUserByLogin(login).get();
    }

    public Optional<User> getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        List<UserResponseDTO> users = new ArrayList<>();
        return new ResponseEntity<>(users, HttpStatus.OK );
    }
}
