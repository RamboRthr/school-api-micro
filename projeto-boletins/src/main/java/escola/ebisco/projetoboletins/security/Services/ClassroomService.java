package escola.ebisco.projetoboletins.security.Services;

import escola.ebisco.projetoboletins.Domain.Classroom;
import escola.ebisco.projetoboletins.Domain.Professor;
import escola.ebisco.projetoboletins.Domain.Student;
import escola.ebisco.projetoboletins.Repo.ClassroomRepository;
import escola.ebisco.projetoboletins.Repo.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/classroom")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ClassroomService {
    @Autowired
    private ClassroomRepository classroomRepository;
    @Autowired
    private ProfessorRepository professorRepository;
    @GetMapping
    public List<Classroom> getAll(){
        return classroomRepository.findAll();
    }

    @RequestMapping(value = "/byId", method = RequestMethod.GET)
    public Classroom getById(@RequestParam() Long id){ return classroomRepository.findById(id).get();};

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity insert(@RequestBody Classroom classroom){
        classroom.update();
        classroomRepository.save(classroom);
        return ResponseEntity.accepted().build();
    }

    @RequestMapping(value = "/byNbrStudents", method = RequestMethod.GET)
    public List<Classroom> getByNbrStudents(@RequestParam("nbr") Integer nbr){
        return classroomRepository.findByNbrStudents(nbr);
    }


    @RequestMapping(value = "/byStudent", method = RequestMethod.GET)
    public Classroom getByStudents(@RequestBody Student student){
        return classroomRepository.findByStudents(student);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void delete(@RequestParam("id") Long id){
        var classroom = classroomRepository.findById(id);
        classroom.ifPresent(value -> classroomRepository.delete(value));
    }
    @RequestMapping(value = "/mathNoteBetween", method = RequestMethod.GET)
    public List<Classroom> getByMathNoteBetween(@RequestParam("min") double min, @RequestParam("max") double max){
        return classroomRepository.findByMathMeanNoteBetween(min, max);
    }

    @RequestMapping(value = "/setProfessor", method = RequestMethod.PUT)
    public ResponseEntity addProfessorToClassroom(
            @RequestParam("classroomId") Long classroomId,
            @RequestParam("professorId") Long professorId
    ) {
        Classroom classroom = classroomRepository.findById(classroomId).get();
        Professor professor = professorRepository.findById(professorId).get();
        classroom.setProfessor(professor);
        professor.setClassroom(classroom);
        classroomRepository.save(classroom);
        professorRepository.save(professor);
        return ResponseEntity.ok().build();
    }
}
