package mycode.notesapp.app.notes.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  NoteType {

    BUSINESS("business"),
    SOCIAL("social"),
    IMPORTANT("important");

    private final String stockType;
}
