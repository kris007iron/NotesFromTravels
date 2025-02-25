package com.kris007iron.NotesFromTravels.Service;

import com.kris007iron.NotesFromTravels.Model.User;
import com.kris007iron.NotesFromTravels.Repository.UserRepository;
import org.springframework.stereotype.Service;

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
}
