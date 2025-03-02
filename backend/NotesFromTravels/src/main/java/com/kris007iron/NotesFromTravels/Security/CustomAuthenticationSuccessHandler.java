package com.kris007iron.NotesFromTravels.Security;

import com.kris007iron.NotesFromTravels.Model.User;
import com.kris007iron.NotesFromTravels.Service.JwtService;
import com.kris007iron.NotesFromTravels.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final UserService userService;
    private final static long LOGIN_TOKEN_EXPIRATION_TIME = 86400000;

    public CustomAuthenticationSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        System.out.println("github auth success");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println(Optional.ofNullable(oAuth2User.getAttribute("login")));
        User user = userService.findOrCreateUser(oAuth2User.getAttribute("login"), oAuth2User.getAttribute("name"));
        String token = JwtService.generateToken(user.getLogin(), LOGIN_TOKEN_EXPIRATION_TIME);
        if(user.getRole().equals("USER")) {
            response.sendRedirect("/token?token=" + token);
        }else if(user.getRole().equals("ADMIN")) {
            response.sendRedirect("/dashboard");
        }
    }
}
