package com.service.auth.Controller;

import Classes.SignupRequest;
import com.service.auth.Domain.ERole;
import com.service.auth.Domain.Role;
import com.service.auth.Domain.User;
import com.service.auth.Payload.request.LoginRequest;
import com.service.auth.Payload.response.JwtResponse;
import com.service.auth.Payload.response.MessageResponse;
import com.service.auth.Repo.RoleRepository;
import com.service.auth.Repo.UserRepository;
import com.service.auth.Security.Services.UserDetailsImpl;
import com.service.auth.Security.jwt.JwtUtils;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue;

    //@Autowired
    //EmailService emailService;

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role studentRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(studentRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "prof":
                        Role professorRole = roleRepository.findByName(ERole.ROLE_PROFESSOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(professorRole);

                        break;
                    default:
                        Role studentRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(studentRole);
                }
            });
        }

        user.setRoles(roles);
        user.setLoginAttempts(0);
        userRepository.save(user);

        send(signUpRequest);

        return ResponseEntity.ok(new MessageResponse("User registered successfully! Check out your e-mail box."));
    }

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        if (userRepository.existsByUsername(loginRequest.getUsername())) {
            User user = userRepository.findByUsername(loginRequest.getUsername()).get();
            if (user.getLoginAttempts() < 3) {
                try {
                    Authentication authentication = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    String jwt = jwtUtils.generateJwtToken(authentication);

                    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                    List<String> roles = userDetails.getAuthorities().stream()
                            .map(item -> item.getAuthority())
                            .collect(Collectors.toList());

                    user.setLoginAttempts(0);

                    return ResponseEntity.ok(new JwtResponse(jwt,
                            userDetails.getId(),
                            userDetails.getUsername(),
                            userDetails.getEmail(),
                            roles));

                } catch (Exception e) {
                        user.setLoginAttempts(user.getLoginAttempts() + 1);
                        userRepository.save(user);
                        return new ResponseEntity<>("Username ou senha incorretos", HttpStatus.UNAUTHORIZED);
                    }
                }
            else{
                return new ResponseEntity<>("Acesso bloqueado por excesso de tentativas.", HttpStatus.UNAUTHORIZED);
            }
        }

        return new ResponseEntity<>("User not found", HttpStatus.UNAUTHORIZED);
    }

    public void send(SignupRequest request) {
        rabbitTemplate.convertAndSend(this.queue.getName(), request);
    }

}
