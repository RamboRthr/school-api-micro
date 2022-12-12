package escola.ebisco.projetoboletins.Services;

import escola.ebisco.projetoboletins.Domain.Classroom;
import escola.ebisco.projetoboletins.Domain.Professor;
import escola.ebisco.projetoboletins.Repo.ProfessorRepository;
import escola.ebisco.projetoboletins.security.Services.ProfessorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ProfessorServiceTest {
    @InjectMocks
    ProfessorService professorService;

    @Mock
    ProfessorRepository professorRepository;

    @Test
    void getAll() {
        var professors = List.of(new Professor(1L));

        when(professorRepository.findAll()).thenReturn(professors);

        var all = professorService.getAll();

        assertEquals(all.get(0).getId(), professors.get(0).getId());
    }
    @Test
    void delete(){
        var response = new ResponseEntity(HttpStatus.OK);
        var professor = new Professor(1L);
        doNothing().when(professorRepository).deleteById(Mockito.anyLong());

        var res = professorService.delete(professor.getId());

        assertEquals(response.getStatusCode(), res.getStatusCode());

        verify(professorRepository, times(1)).deleteById(Mockito.anyLong());
    }



}


