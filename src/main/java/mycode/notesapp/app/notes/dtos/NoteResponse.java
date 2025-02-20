package mycode.notesapp.app.notes.dtos;

import java.time.LocalDate;

public record NoteResponse(long id, String title, String description, LocalDate createDate) {
}
