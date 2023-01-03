package escola.ebisco.projetoboletins.security.Services;

import escola.ebisco.projetoboletins.Domain.Professor;
import escola.ebisco.projetoboletins.Domain.User;
import escola.ebisco.projetoboletins.Repo.ProfessorRepository;
import escola.ebisco.projetoboletins.Repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UserService {
    @Autowired
    UserRepository userRepository;

    @RequestMapping(method = RequestMethod.GET)
    public List<User> getAll(){
        return userRepository.findAll();
    }

    @Transactional
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity delete(@RequestParam("id") Long id){
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
