package com.service.crud.Controllers;

import com.service.crud.Domain.User;
import com.service.crud.Repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @RequestMapping(method = RequestMethod.GET)
    public List<User> getAll(){
        return userRepository.findAll();
    }

    @Transactional
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity delete(@RequestParam("id") Long id){
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
