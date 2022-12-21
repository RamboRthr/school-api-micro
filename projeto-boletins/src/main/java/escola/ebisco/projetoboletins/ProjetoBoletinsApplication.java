package escola.ebisco.projetoboletins;


import escola.ebisco.projetoboletins.Repo.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

@SpringBootApplication
public class ProjetoBoletinsApplication {

	@Autowired
	static RoleRepository roleRepository;
	public static void main(String[] args) { SpringApplication.run(ProjetoBoletinsApplication.class, args);}


}
