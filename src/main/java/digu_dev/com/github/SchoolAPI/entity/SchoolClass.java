package digu_dev.com.github.SchoolAPI.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString
@Table(name = "school_class_tb")
public class SchoolClass {

    private Long id;

    private String classCode;

    private Integer gradeLevel;

    @OneToMany(mappedBy = "schoolClass", cascade= CascadeType.ALL, orphanRemoval = true)
    private List<Student> students;

    




}
