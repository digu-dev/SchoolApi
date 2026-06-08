package digu_dev.com.github.SchoolAPI.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "department_tb")
public class Department {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    
    private String name; 

    @OneToMany(mappedBy = "department", 
    cascade= CascadeType.ALL, 
    orphanRemoval = true, 
    fetch=FetchType.LAZY)
    private List<Teacher> teachers; 


}
