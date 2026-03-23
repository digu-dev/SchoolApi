package digu_dev.com.github.SchoolAPI.entity;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString
@Table(name = "student_tb")
public class Student {

    private Long id;

    private String name;

    private String registration;

    private LocalDate dateOfBirth;

    @NotNull
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "school_class_id", nullable = false)
    SchoolClass schoolClass;

    @OneToMany(mappedBy = "student", cascade= CascadeType.ALL, orphanRemoval = true)
    private List<GPA> grades;

    private StatusEnum status;
}
