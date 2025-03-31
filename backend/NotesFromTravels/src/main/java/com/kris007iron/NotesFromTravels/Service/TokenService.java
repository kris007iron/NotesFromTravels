package com.kris007iron.NotesFromTravels.Service;

import com.kris007iron.NotesFromTravels.Model.Token;
import com.kris007iron.NotesFromTravels.Repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    public boolean isTokenExpired(String token) {
        return tokenRepository.findByToken(token).isPresent();
    }

    public void expireToken(String token) {
        Token expiredToken = new Token();
        expiredToken.setToken(token);
        tokenRepository.save(expiredToken);
    }
}
