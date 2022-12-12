package escola.ebisco.projetoboletins.Domain;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double salary;
    @Nullable
    @ManyToMany
    @JoinTable(
            name = "professor_classroom",
            joinColumns = {
                    @JoinColumn(name = "professor_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "classroom_id")
            }
    )
    private Set<Classroom> classrooms;

    public Professor(Long professorId) {
        this.id = professorId;
    }

    public Professor(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long professorId) {
        this.id = professorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Set<Classroom> getClassrooms() {
        return classrooms;
    }

    public void addClassroom(Classroom classroom) {
        this.classrooms.add(classroom);
    }

    public void setClassrooms(@Nullable Set<Classroom> classrooms) {
        this.classrooms = classrooms;
    }

    public void setClassroom(Classroom classroom){
        this.classrooms.add(classroom);
    }
}
