package digu_dev.com.github.SchoolAPI.dto;

import digu_dev.com.github.SchoolAPI.entity.Department;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DepartmentDto(Long id, @NotBlank @Size(max = 100) String name) {

    public DepartmentDto(Department department) {
        this(department.getId(), department.getName());
    }

    public Department toEntity() {
        Department department = new Department();
        department.setId(this.id);
        department.setName(this.name);
        return department;
    }

}
