package escola.ebisco.projetoboletins.security;

import escola.ebisco.projetoboletins.Domain.ERole;
import escola.ebisco.projetoboletins.Domain.Role;
import escola.ebisco.projetoboletins.Repo.RoleRepository;
import escola.ebisco.projetoboletins.security.Services.UserDetailsServiceImpl;
import escola.ebisco.projetoboletins.security.jwt.AuthEntryPointJwt;
import escola.ebisco.projetoboletins.security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

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

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void setKeys() {
        redisTemplate.opsForValue().set("privateKey", "MIICXgIBAAKBgQDC0+vVgZ2aCb1HOgSIoUbBy1vlUtCfZRrqs8izwmFq7QbT6XfZn4Mkk6ojyTq6pcWIsucpzhLHwwYPTG/q9XbZUUFw3S/O03SiFKT0fv4nvx3W4D3pups95rSXR2meEbgcIV/Btp2h15N91T9Iwt5WvHr8jV7SMwGcMa3o2y4HnQIDAQABAoGAD+wZ3f0V0DzzhxqqvC/SBIyGGhvGiQBOTtgakvZT19U/NZpi/RoYMakPwpTzg8WAe0eDtNrulfzORfnNO7qL19oOdMkb83Zgnq2Isl8qWFkXirRfVCGJ4hm0L5W5Vby+weUCNlH3y+DBtu7tcxpGkQ8Eq2SLCI+yHL9V3CKkTAECQQD8/uwpAU1AduK6upsdElv+FDybSkcXtXYQnrVEGTGX0SAFlFPsm/S16OogtndazWhkWZxZtsi+JduVtn8TZF/BAkEAxSQsx0PkTorLGha1ipwYEJOmUXkPYWpKb+HResMpdg4NU53EY23M15nEbWT4Ch0B9ff2zK58CdtZTH/BUf7e3QJBAPEbHQODGxU5d6BPIG5XRdZhgNTZt+DvbaIvLj7E589wXF0U29pdUpxeaWpdmmet5DPmdqvFF5CnUZpfPsHDYcECQQCc8z/zJMoO/dDU5F+ECuHd0K8JDiiAle7NRCtSYS4RHv7dIy3HOxNqUrFfppMS+iUlflSuf/ugnVFq5gszDIbBAkEAhwxx/Ra7znThJGLxpShxW90UV4xwqWwCoa2eAruWHTn0Uk6OkUid5VwnEuwQfq54CjnI8hIhA/nJqDTQUqNblg==");
        redisTemplate.opsForValue().set("publicKey", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDC0+vVgZ2aCb1HOgSIoUbBy1vlUtCfZRrqs8izwmFq7QbT6XfZn4Mkk6ojyTq6pcWIsucpzhLHwwYPTG/q9XbZUUFw3S/O03SiFKT0fv4nvx3W4D3pups95rSXR2meEbgcIV/Btp2h15N91T9Iwt5WvHr8jV7SMwGcMa3o2y4HnQIDAQAB");
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

