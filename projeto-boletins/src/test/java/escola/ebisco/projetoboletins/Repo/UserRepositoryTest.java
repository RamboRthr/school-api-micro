package escola.ebisco.projetoboletins.Repo;

import escola.ebisco.projetoboletins.Domain.ERole;
import escola.ebisco.projetoboletins.Domain.Role;
import escola.ebisco.projetoboletins.Domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Test
    void checkNotNull(){
        Assertions.assertNotNull(userRepository);
    }



    @Test
    void insert(){
        PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();
        var user = new User();

        var encoded = encoder.encode("senhaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        user.setPassword(encoded);
        user.setUsername("user");
        user.setEmail("email");
        user.setRoles(Set.of(new Role(ERole.ROLE_STUDENT)));

        userRepository.save(user);

        assertNotNull(user.getId());
        assertNotNull(user.getUsername());
    }

}
