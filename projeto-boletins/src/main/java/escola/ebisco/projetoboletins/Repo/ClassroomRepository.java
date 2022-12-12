package escola.ebisco.projetoboletins.Repo;

import escola.ebisco.projetoboletins.Domain.Classroom;
import escola.ebisco.projetoboletins.Domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    List<Classroom> findByNbrStudents(Integer nbrStudents);
    Classroom findByStudents(Student student);
    List<Classroom> findByMathMeanNoteBetween(Double min, Double max);



}
