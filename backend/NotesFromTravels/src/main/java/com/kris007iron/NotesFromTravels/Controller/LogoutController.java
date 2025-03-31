package com.kris007iron.NotesFromTravels.Controller;

import com.kris007iron.NotesFromTravels.Model.Response.MessageResponseDTO;
import com.kris007iron.NotesFromTravels.Service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@RequiredArgsConstructor
@Validated
public class LogoutController {
    private final TokenService tokenService;

    @GetMapping("/api/v1/logout")
    public ResponseEntity<MessageResponseDTO> logout(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ") && authHeader.length() > 7) {
            String token = authHeader.substring(7);
            tokenService.expireToken(token);
            return new ResponseEntity<>(new MessageResponseDTO("Logout successfully"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new MessageResponseDTO("No token in Authorization header"), HttpStatus.BAD_REQUEST);
    }
}
