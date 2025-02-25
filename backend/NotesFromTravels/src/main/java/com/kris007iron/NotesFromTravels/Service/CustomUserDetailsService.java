package com.kris007iron.NotesFromTravels.Service;

import com.kris007iron.NotesFromTravels.Model.User;
import com.kris007iron.NotesFromTravels.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String login) throws UsernameNotFoundException {
        Optional<User> user = this.userRepository.findByLogin(login);
        if(user.isEmpty())
        {
            throw new UsernameNotFoundException("Unknown user with login: " + login);
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.get().getLogin())
                .password("")
                .build();
    }
}
