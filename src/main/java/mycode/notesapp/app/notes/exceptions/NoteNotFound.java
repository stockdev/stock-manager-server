package mycode.notesapp.app.notes.exceptions;

public class NoteNotFound extends RuntimeException {
    public NoteNotFound(String message) {
        super(message);
    }
}
