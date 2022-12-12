package escola.ebisco.projetoboletins.controller;

import escola.ebisco.projetoboletins.Domain.Role;
import escola.ebisco.projetoboletins.Domain.User;
import escola.ebisco.projetoboletins.Repo.RoleRepository;
import escola.ebisco.projetoboletins.Repo.UserRepository;
import escola.ebisco.projetoboletins.payload.request.LoginRequest;
import escola.ebisco.projetoboletins.payload.request.SignupRequest;
import escola.ebisco.projetoboletins.security.Services.UserDetailsImpl;
import escola.ebisco.projetoboletins.security.jwt.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class AuthControllerTest {
    @InjectMocks
    AuthController authController;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;

    @Mock
    PasswordEncoder encoder;

    @Mock
    JwtUtils jwtUtils;

    @Test
    void signupUsernameExists(){
        var responseBadRequest = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        when(userRepository.existsByUsername(Mockito.anyString())).thenReturn(true);

        var request = new SignupRequest();
        request.setUsername("username");

        var res = authController.registerUser(request);

        assertEquals(responseBadRequest.getStatusCode(), res.getStatusCode());
        assertTrue(res.hasBody());
        verify(userRepository, times(1)).existsByUsername(any());
    }

    @Test
    void signupEmailExists(){
        var responseBadRequest = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        doReturn(true).when(userRepository).existsByEmail(Mockito.anyString());
        var request = new SignupRequest();
        request.setEmail("email");

        var res = authController.registerUser(request);

        assertEquals(responseBadRequest.getStatusCode(), res.getStatusCode());
        assertTrue(res.hasBody());
        verify(userRepository, times(1)).existsByEmail(any());
    }

    @Test
    void signupNullRoles(){
        var request = new SignupRequest();

        when(roleRepository.findByName(Mockito.any())).thenReturn(null);

        assertThatThrownBy(() -> authController.registerUser(request))
                .isInstanceOf(RuntimeException.class);

        verify(roleRepository, times(1)).findByName(Mockito.any());
        verify(encoder, times(1)).encode(Mockito.any());

    }

    @Test
    void signupSuccess(){
        var request = new SignupRequest();
        request.setRole(Set.of("student", "prof", "admin"));

        when(roleRepository.findByName(any())).thenReturn(Optional.of(new Role()));

        var res = authController.registerUser(request);

        assertEquals(res.getStatusCode(), HttpStatus.OK);

        verify(roleRepository, times(3)).findByName(any());
        verify(userRepository, times(1)).save(any());

    }

    @Test
    void singinSuccess(){

        var request = new LoginRequest();

        Authentication authentication = Mockito.mock(Authentication.class);
        authentication.setAuthenticated(true);
        var user = new User(
                "username",
                "email",
                "12345678"
        );
        user.setId(1L);
        user.setRoles(new HashSet<>());

        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        when(authentication.getPrincipal()).thenReturn(
                UserDetailsImpl.build(user)
        );

        var res = authController.authenticateUser(new LoginRequest());

        assertEquals(res.getStatusCode(), HttpStatus.OK);


    }

    @Test
    void singinFailure(){

        var request = new LoginRequest();

        doThrow(new AuthenticationException("message") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        }).when(authenticationManager).authenticate(any());


        assertThatThrownBy(() -> authController.authenticateUser(request))
                .isInstanceOf(AuthenticationException.class);

    }
}
