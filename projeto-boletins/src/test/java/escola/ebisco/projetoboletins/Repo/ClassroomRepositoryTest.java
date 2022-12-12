package escola.ebisco.projetoboletins.Repo;

import escola.ebisco.projetoboletins.Domain.Classroom;
import escola.ebisco.projetoboletins.Domain.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class ClassroomRepositoryTest {
    @Autowired
    private ClassroomRepository classroomRepository;


    @Test
    void checkNotNull(){
        Assertions.assertNotNull(classroomRepository);
    }

    @Test
    void insertAndFind(){
        var insertClassroom = new Classroom();
        insertClassroom.setStudents(new ArrayList<>());
        insertClassroom.setNbrStudents(2);

        var savedClassroom = classroomRepository.save(insertClassroom);
        var foundClassroom = classroomRepository.findById(insertClassroom.getId());

        assertTrue(savedClassroom.getStudents().isEmpty());
        assertEquals(savedClassroom.getId(), foundClassroom.get().getId());
    }

    @Test
    void update(){
        var classroom = new Classroom(1L);
        classroom.setNbrStudents(0);
        var savedClassroom = classroomRepository.save(classroom);
        savedClassroom.setNbrStudents(2);
        var updatedClassroom = classroomRepository.save(savedClassroom);

        assertNotEquals(classroom.getNbrStudents(), updatedClassroom.getNbrStudents());
    }

    @Test
    void delete(){

        var classroom = new Classroom();
        classroomRepository.save(classroom);
        classroomRepository.deleteById(classroom.getId());
        assertFalse(classroomRepository.findById(classroom.getId()).isPresent() );


    }
}
