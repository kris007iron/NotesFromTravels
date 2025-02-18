package com.kris007iron.NotesFromTravels.Repository;

import com.kris007iron.NotesFromTravels.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
