package escola.ebisco.projetoboletins.Repo;

import escola.ebisco.projetoboletins.Domain.Professor;
import escola.ebisco.projetoboletins.Domain.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class StudentRepositoryTest {
    @Autowired
    private StudentRepository studentRepository;

    @Test
    void checkNotNull(){
        Assertions.assertNotNull(studentRepository);
    }

    @Test
    void insertAndFind(){
        var insertStudent = new Student();
        insertStudent.setName("jorge");

        var savedStudent = studentRepository.save(insertStudent);
        var foundStudent = studentRepository.findById(insertStudent.getId());

        assertEquals("jorge", savedStudent.getName());
        assertNull(savedStudent.getClassroomId());
        assertEquals(savedStudent.getId(), foundStudent.get().getId());
    }

    @Test
    void update(){
        var student = new Student();
        student.setId(1L);
        student.setName("joão");
        var savedStudent = studentRepository.save(student);
        savedStudent.setName("jonas");
        var updatedProfessor = studentRepository.save(savedStudent);

        assertNotEquals(student.getName(), updatedProfessor.getName());
    }

    @Test
    void delete(){
        var student = new Student();
        student.setName("mário");
        studentRepository.save(student);
        studentRepository.deleteById(student.getId());
        assertFalse(studentRepository.findById(student.getId()).isPresent() );
        assertTrue(studentRepository.findByName("mário").isEmpty());

    }
}
