package escola.ebisco.projetoboletins.Services;

import escola.ebisco.projetoboletins.Domain.Classroom;
import escola.ebisco.projetoboletins.Domain.Professor;
import escola.ebisco.projetoboletins.Domain.Student;
import escola.ebisco.projetoboletins.Repo.ClassroomRepository;
import escola.ebisco.projetoboletins.Repo.ProfessorRepository;
import escola.ebisco.projetoboletins.security.Services.ClassroomService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ClassroomServiceTest {
    @InjectMocks
    ClassroomService classroomService;

    @Mock
    ClassroomRepository classroomRepository;

    @Mock
    ProfessorRepository professorRepository;

    @Test
    void getAll(){
        var classrooms = List.of(new Classroom(1L));

        when(classroomRepository.findAll()).thenReturn(classrooms);

        var all = classroomService.getAll();

        assertEquals(all.get(0).getId(), classrooms.get(0).getId());
    }

    @Test
    void getById(){
        var classroom = new Classroom(1L);
        when(classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));

        var res = classroomService.getById(classroom.getId());

        assertEquals(res.getId(), classroom.getId());

    }

    @Test
    void insert () {
        var mockClassroom = new Classroom(1L);
        mockClassroom.setStudents(new ArrayList<>());
        var mockResponseEntity = new ResponseEntity<Classroom>(HttpStatus.ACCEPTED);
        when(classroomRepository.save(Mockito.any(Classroom.class))).thenReturn(mockClassroom);

        var res = classroomService.insert(mockClassroom);

        assertEquals(res.getStatusCode(), mockResponseEntity.getStatusCode());
        assertFalse(res.hasBody());
    }

    @Test
    void getByNbrStudents(){
        var classrooms = List.of(new Classroom(1L));
        when(classroomRepository.findByNbrStudents(Mockito.anyInt())).thenReturn(classrooms);

        var res = classroomService.getByNbrStudents(Mockito.anyInt());

        assertEquals(res.get(0).getNbrStudents(), classrooms.get(0).getNbrStudents());

    }

    @Test
    void getByStudents(){
        var mockClassroom = new Classroom(1L);
        var student = new Student(1L, "Carlos", 1L, 0.5, 8, 6, 9);
        mockClassroom.setStudents(new ArrayList<>());
        mockClassroom.addStudent(student);

        when(classroomRepository.findByStudents(student)).thenReturn(mockClassroom);

        var res = classroomService.getByStudents(student);

        assertEquals(mockClassroom.getId(), res.getId());

        assertTrue(res.getStudents().contains(mockClassroom.getStudents().get(0)));
    }

    @Test
    void delete(){
    when(classroomRepository.findById(1L)).thenReturn(Optional.of(new Classroom(1L)));
    doNothing().when(classroomRepository).delete(new Classroom(1L));

    }

    @Test
    void addProfessorToClassroom(){
        var responseEntity = new ResponseEntity(HttpStatus.OK);
        var classroom = new Classroom(1L);
        var professor = new Professor(1L);
        classroom.setProfessors(new HashSet<>());
        professor.setClassrooms(new HashSet<>());
        when(classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));
        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        when(classroomRepository.save(any(Classroom.class))).thenReturn(classroom);
        when(professorRepository.save(any(Professor.class))).thenReturn(professor);

        var res = classroomService.addProfessorToClassroom(classroom.getId(), professor.getId());

        assertEquals(res.getStatusCode(), responseEntity.getStatusCode());
;
    }











}
