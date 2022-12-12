package escola.ebisco.projetoboletins;


import escola.ebisco.projetoboletins.Domain.ERole;
import escola.ebisco.projetoboletins.Domain.Role;
import escola.ebisco.projetoboletins.Repo.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjetoBoletinsApplication {

	@Autowired
	static RoleRepository roleRepository;
	public static void main(String[] args) {
		SpringApplication.run(ProjetoBoletinsApplication.class, args);
	}


}
