package com.kris007iron.NotesFromTravels.Model.Response;

public record NoteResponseDTO(long id, String title, String content, LocationResponseDTO location, String imageUrl) {
}
