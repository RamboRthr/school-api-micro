package com.service.crud.Controllers;

import com.service.crud.Domain.Classroom;
import com.service.crud.Domain.Student;
import com.service.crud.Repo.ClassroomRepository;
import com.service.crud.Repo.StudentRepository;
import com.service.crud.Repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")

public class StudentController
{
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ClassroomRepository classroomRepository;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    public List<Student> getAll(){
        return studentRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity insertStudent(@RequestBody Student student){
        studentRepository.save(student);
        setStudentIntoClassroom(student);
        return ResponseEntity.accepted().build();
    }
    @RequestMapping(method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity deleteStudent(@RequestParam("id") Long id){
        if (studentRepository.findById(id).isPresent()) {
            Student student = studentRepository.findById(id).get();
            studentRepository.deleteById(id);
            setStudentIntoClassroom(student);
            return new ResponseEntity<String>("Student" + student.getName() + " deleted", HttpStatus.OK);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }
    @RequestMapping(value = "/byId", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_STUDENT')")
    public ResponseEntity<Student> getStudentById(@RequestParam("id") Long id){

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String jwtUsername = userDetails.getUsername();
        if (userRepository.findByUsername(jwtUsername).get().getId().equals(id)){
            return new ResponseEntity<Student>(studentRepository.findById(id).get(), HttpStatus.OK);

        }
        else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }
    @RequestMapping(value = "/studentByName", method = RequestMethod.GET)
    public List<Student> getStudentByName(@RequestParam("name") String name){
        return studentRepository.findByName(name);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/byClassroomId", method = RequestMethod.GET)
    public List<Student> getStudentByClassroomId(@RequestParam("classroomId") Long classroomId){
        return studentRepository.findByClassroomId(classroomId);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/minFrequency", method = RequestMethod.GET)
    public List<Student> getStudentByMinFrequency(@RequestParam("min") double min){
        return studentRepository.findByFrequencyGreaterThanEqual(min);
    }

    private void setStudentIntoClassroom(Student student){
        Optional<Classroom> c = classroomRepository.findById(student.getClassroomId());
        Classroom classroom;
        if (c.isPresent()){
            classroom = c.get();
            classroom.update();
            classroomRepository.save(classroom);
        }
        else {
            System.out.println("Student registered in inexisting classroom.");
        }

    }

}
