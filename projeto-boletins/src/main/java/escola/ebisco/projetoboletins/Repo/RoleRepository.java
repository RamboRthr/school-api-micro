package escola.ebisco.projetoboletins.Repo;

import escola.ebisco.projetoboletins.Domain.ERole;
import escola.ebisco.projetoboletins.Domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
