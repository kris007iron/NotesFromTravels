package com.kris007iron.NotesFromTravels.Controller;

import com.kris007iron.NotesFromTravels.Security.OAuth2AuthenticationService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final OAuth2AuthenticationService authService;

    public AuthController(OAuth2AuthenticationService authService) {
        this.authService = authService;
    }

    @GetMapping("/oauth2/login")
    public String login(OAuth2AuthenticationToken authToken) {
        return authService.authenticateUser(authToken);
    }
}
