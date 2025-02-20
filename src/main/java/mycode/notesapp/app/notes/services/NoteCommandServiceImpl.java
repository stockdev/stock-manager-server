package mycode.notesapp.app.notes.services;

import lombok.AllArgsConstructor;
import mycode.notesapp.app.notes.repository.NoteRepository;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class NoteCommandServiceImpl implements NoteCommandService{

    private NoteRepository noteRepository;
}
