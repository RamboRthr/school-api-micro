package escola.ebisco.projetoboletins.security.Services;

import escola.ebisco.projetoboletins.Domain.Professor;
import escola.ebisco.projetoboletins.Repo.ClassroomRepository;
import escola.ebisco.projetoboletins.Repo.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professor")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ProfessorService {

    @Autowired
    ProfessorRepository professorRepository;
    @Autowired
    ClassroomRepository classroomRepository;
    @RequestMapping(method = RequestMethod.GET)
    public List<Professor> getAll(){
        return professorRepository.findAll();
    }
    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public ResponseEntity saveOrUpdate(@RequestBody Professor professor){
        professorRepository.save(professor);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.PUT)
    @Transactional
    public ResponseEntity update(@RequestBody Professor professor){
        professorRepository.update(professor.getId(), professor.getName(), professor.getSalary());
        return ResponseEntity.accepted().build();
    }
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity delete(@RequestParam("id") Long id){
        professorRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}

