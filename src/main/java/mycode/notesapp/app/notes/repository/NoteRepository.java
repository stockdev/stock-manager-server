package mycode.notesapp.app.notes.repository;

import mycode.notesapp.app.notes.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Integer> {
}
