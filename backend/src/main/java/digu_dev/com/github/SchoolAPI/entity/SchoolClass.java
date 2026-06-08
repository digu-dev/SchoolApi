package digu_dev.com.github.SchoolAPI.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString
@Table(name = "school_class_tb")
public class SchoolClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String classCode;

    private Integer gradeLevel;

    @OneToMany(mappedBy = "schoolClass", cascade= CascadeType.ALL, orphanRemoval = true)
    private List<Student> students;

    




}
