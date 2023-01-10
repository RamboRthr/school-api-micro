package com.service.crud.Repo;


import com.service.crud.Domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByName(String name);
    List<Student> findByClassroomId(Long classroomId);

    List<Student> findByFrequencyGreaterThanEqual(double frequency);
}
