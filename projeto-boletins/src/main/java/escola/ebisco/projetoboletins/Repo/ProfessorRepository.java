package escola.ebisco.projetoboletins.Repo;

import escola.ebisco.projetoboletins.Domain.Classroom;
import escola.ebisco.projetoboletins.Domain.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    @Modifying(clearAutomatically = true)
    @Query("update Professor p set p.name = :name, p.salary = :salary where p.id = :id")
    void update(@Param("id") Long id, @Param("name") String name, @Param("salary") Double salary);
}
