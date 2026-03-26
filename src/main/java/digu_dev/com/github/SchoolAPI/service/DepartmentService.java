package digu_dev.com.github.SchoolAPI.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import digu_dev.com.github.SchoolAPI.dto.DepartmentDto;
import digu_dev.com.github.SchoolAPI.entity.Department;
import digu_dev.com.github.SchoolAPI.repository.DepartmentRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional(rollbackOn = Exception.class)
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    
    public Department create (DepartmentDto dto){
        Department department = new Department();
        department.setName(dto.name());
        return departmentRepository.save(department);
    }

    public void update(Department department){
        if (department.getId() == null) {
            throw new IllegalArgumentException("Department ID cannot be null for update.");
        }
        departmentRepository.save(department);

    }

    public void delete(Department department){
        if(department.getId() == null) {
            throw new IllegalArgumentException("Department ID cannot be null for deletion.");
        }
        departmentRepository.deleteById(department.getId());
    }

    public Optional<Department> findById(Long id){
        return departmentRepository.findById(id);
    }

    public List<Department> findAll(){
        return departmentRepository.findAll();
    }

    public Optional<Department> findByName(String name){
        return departmentRepository.findByName(name);
    }

    public Optional<Department> findByIdWithTeachers(Long id){
        return departmentRepository.findByIdWithTeachers(id);
    }

    
}
