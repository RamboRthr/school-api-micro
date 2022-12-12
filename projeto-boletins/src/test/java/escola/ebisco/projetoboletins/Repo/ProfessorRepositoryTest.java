package escola.ebisco.projetoboletins.Repo;

import escola.ebisco.projetoboletins.Domain.Classroom;
import escola.ebisco.projetoboletins.Domain.Professor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class ProfessorRepositoryTest {
    @Autowired
    private ProfessorRepository professorRepository;


    @Test
    void checkNotNull(){
        Assertions.assertNotNull(professorRepository);
    }

    @Test
    void insertAndFind(){
        var insertProfessor = new Professor();
        insertProfessor.setClassrooms(new HashSet<>());
        insertProfessor.setName("jorge");

        var savedProfessor = professorRepository.save(insertProfessor);
        var foundProfessor = professorRepository.findById(insertProfessor.getId());

        assertTrue(savedProfessor.getClassrooms().isEmpty());
        assertEquals(savedProfessor.getId(), foundProfessor.get().getId());
    }

    @Test
    void update(){
        var professor = new Professor(1L);
        professor.setName("joão");
        var savedProfessor = professorRepository.save(professor);
        savedProfessor.setName("jonas");
        var updatedProfessor = professorRepository.save(savedProfessor);

        assertNotEquals(professor.getName(), updatedProfessor.getName());
    }



    @Test
    void delete(){

        var professor = new Professor();
        professor.setName("victor");
        professorRepository.save(professor);
        professorRepository.deleteById(professor.getId());
        assertFalse(professorRepository.findById(professor.getId()).isPresent());


    }
}
