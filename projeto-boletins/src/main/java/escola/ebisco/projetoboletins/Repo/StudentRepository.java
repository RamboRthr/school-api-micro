package escola.ebisco.projetoboletins.Repo;


import escola.ebisco.projetoboletins.Domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByName(String name);
    List<Student> findByClassroomId(Long classroomId);

    List<Student> findByFrequencyGreaterThanEqual(double frequency);
}
