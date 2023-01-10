package com.service.crud.Repo;

import com.service.crud.Domain.Classroom;
import com.service.crud.Domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    List<Classroom> findByNbrStudents(Integer nbrStudents);
    Classroom findByStudents(Student student);
    List<Classroom> findByMathMeanNoteBetween(Double min, Double max);



}
