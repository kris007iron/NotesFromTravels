package com.kris007iron.NotesFromTravels.Model.Response;

import java.util.List;

public record UserResponseDTO(long id, String login, List<NoteResponseDTO> notes) {
}
