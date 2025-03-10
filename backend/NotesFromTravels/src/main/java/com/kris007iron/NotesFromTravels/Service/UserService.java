package com.kris007iron.NotesFromTravels.Service;

import com.kris007iron.NotesFromTravels.Model.Response.MessageResponseDTO;
import com.kris007iron.NotesFromTravels.Model.Response.UserResponseDTO;
import com.kris007iron.NotesFromTravels.Model.User;
import com.kris007iron.NotesFromTravels.Repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        List<UserResponseDTO> users = userRepository.findAll().stream().map(user -> new UserResponseDTO(user.getId(), user.getLogin(), user.getRole())).collect(Collectors.toList());

        return new ResponseEntity<>(users, HttpStatus.OK );
    }

    public ResponseEntity<Optional<UserResponseDTO>> updateUser(UserResponseDTO userResponseDTO) {
        Optional<User> userToUpdate = getUserByLogin(userResponseDTO.login());
        if(userToUpdate.isPresent()){
            userToUpdate.get().setRole(userResponseDTO.role());
            userRepository.save(userToUpdate.get());
            return new ResponseEntity<>(Optional.of(userResponseDTO), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(Optional.empty(), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<MessageResponseDTO> deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            userRepository.delete(user.get());
            return new ResponseEntity<>(new MessageResponseDTO("User deleted successfully."), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(new MessageResponseDTO("User doesn't exists"), HttpStatus.NOT_FOUND);
        }
    }
}
