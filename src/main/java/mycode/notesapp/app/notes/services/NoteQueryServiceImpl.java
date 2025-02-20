package mycode.notesapp.app.notes.services;

import lombok.AllArgsConstructor;
import mycode.notesapp.app.notes.repository.NoteRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NoteQueryServiceImpl implements NoteQueryService {

    private NoteRepository noteRepository;
}
