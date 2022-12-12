package escola.ebisco.projetoboletins.Services;

import escola.ebisco.projetoboletins.Domain.Student;
import escola.ebisco.projetoboletins.Repo.ClassroomRepository;
import escola.ebisco.projetoboletins.Repo.StudentRepository;
import escola.ebisco.projetoboletins.security.Services.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class StudentServiceTest {

    @InjectMocks
    StudentService studentService;

    @Mock
    StudentRepository studentRepository;

    @Mock
    ClassroomRepository classroomRepository;

    @Test
    void getAll(){
        var student = new Student();
        student.setId(1L);
        var students = List.of(student);

        when(studentRepository.findAll()).thenReturn(students);

        var res = studentService.getAll();

        assertEquals(res.get(0).getId(), students.get(0).getId());

    }

    @Test
    void insertStudent(){
        var response = new ResponseEntity<>(HttpStatus.ACCEPTED);
        var student = new Student();
        student.setId(1L);
        student.setClassroomId(1L);

        when(studentRepository.save(new Student())).thenReturn(student);

        var res = studentService.insertStudent(student);
        assertEquals(res.getStatusCode(), response.getStatusCode());

    }

    @Test
    void deleteStudentFound(){
        var responseFound = new ResponseEntity<>(HttpStatus.OK);
        var student = new Student();
        student.setId(1L);

        when(studentRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(student));
        doNothing().when(studentRepository).deleteById(Mockito.anyLong());

        var resFound = studentService.deleteStudent(student.getId());


        assertEquals(resFound.getStatusCode(), responseFound.getStatusCode());

        verify(studentRepository, times(2)).findById(Mockito.anyLong());
        verify(studentRepository, times(1)).deleteById(Mockito.anyLong());

    }

    @Test
    void deleteStudentNotFound(){
        var responseNotFound = new ResponseEntity<>(HttpStatus.NOT_FOUND);

        when(studentRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        var resNotFound = studentService.deleteStudent(2L);

        assertEquals(resNotFound.getStatusCode(), responseNotFound.getStatusCode());
        verify(studentRepository, times(1)).findById(Mockito.anyLong());
    }



}
