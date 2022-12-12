package escola.ebisco.projetoboletins.Domain;

import javax.persistence.*;

@Entity
public class Student {

    /*{
        "name": "Iberê",
            "classroomId": 2,
            "frequency": 0.8,
            "mathematics": 9,
            "portuguese": 7,
            "german:": 10
    }*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long classroomId;
    private double frequency;
    private double mathematics;
    private double portuguese;
    private double german;

    public Student(){

    }

    public Student(Long id, String name,Long classroomId, double frequency, double mathematics, double portuguese, double german) {
        this.id = id;
        this.name = name;
        this.classroomId = classroomId;
        this.frequency = frequency;
        this.mathematics = mathematics;
        this.portuguese = portuguese;
        this.german = german;
    }

    public Long getClassroomId() {
        return classroomId;
    }


    public void setClassroomId(Long classroomId) {
        this.classroomId = classroomId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public double getMathematics() {
        return mathematics;
    }

    public void setMathematics(double mathematics) {
        this.mathematics = mathematics;
    }

    public double getPortuguese() {
        return portuguese;
    }

    public void setPortuguese(double portuguese) {
        this.portuguese = portuguese;
    }

    public double getGerman() {
        return german;
    }

    public void setGerman(double german) {
        this.german = german;
    }
}