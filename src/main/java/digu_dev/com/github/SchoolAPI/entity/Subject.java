package digu_dev.com.github.SchoolAPI.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity 
@Data
@ToString
@Table(name = "subject_tb")
public class Subject {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer creditHours;

    @ManyToMany(mappedBy = "subjects")
    private Set<Teacher> teacherSubject = new HashSet<>();

    @OneToMany(mappedBy = "subject", orphanRemoval = true)
    private List<GPA> gpaList;

}
