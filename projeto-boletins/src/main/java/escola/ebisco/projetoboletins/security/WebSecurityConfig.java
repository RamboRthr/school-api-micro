package escola.ebisco.projetoboletins.security;

import escola.ebisco.projetoboletins.Domain.ERole;
import escola.ebisco.projetoboletins.Domain.Role;
import escola.ebisco.projetoboletins.Repo.RoleRepository;
import escola.ebisco.projetoboletins.security.Services.UserDetailsServiceImpl;
import escola.ebisco.projetoboletins.security.jwt.AuthEntryPointJwt;
import escola.ebisco.projetoboletins.security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
        // jsr250Enabled = true,
        prePostEnabled = true)
public class WebSecurityConfig {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Autowired
    RoleRepository roleRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void setUpRolesAfterStartup() {
        if (roleRepository.findAll().isEmpty()) {
            roleRepository.save(new Role(ERole.ROLE_STUDENT));
            roleRepository.save(new Role(ERole.ROLE_PROFESSOR));
            roleRepository.save(new Role(ERole.ROLE_ADMIN));
        }
    }

    @Value("${app.privateKey}")
    private String privateKey;
    @Value("${app.publicKey}")
    private String publicKey;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /*@EventListener(ApplicationReadyEvent.class)
        private void generateKeys() throws NoSuchAlgorithmException, IOException {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair keyPair = keyGen.generateKeyPair();

            String priv;
            try (FileWriter writer = new FileWriter("private.pem")) {
                priv = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
                writer.write(priv);
            }
            String pub;
            try (FileWriter writer = new FileWriter("public.pem")) {
                pub = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
                writer.write(pub);
            }
        }*/

    @EventListener(ApplicationReadyEvent.class)
    private void setKeyFromEnv() throws IOException {

        //privateKey = Files.readString(Paths.get("private.pem"));
        //publicKey = Files.readString(Paths.get("public.pem"));

        redisTemplate.opsForValue().set("privateKey", privateKey);
        redisTemplate.opsForValue().set("publicKey", publicKey);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/api/auth/**", "/forgotMyPassword/**").permitAll()
                .antMatchers("/student/**").permitAll()
                .antMatchers("/professor/**").permitAll()
                .antMatchers("/classroom/**", "/student/**","/professor/**").permitAll()
                .anyRequest().authenticated();

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

