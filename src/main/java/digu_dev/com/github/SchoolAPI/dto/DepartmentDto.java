package digu_dev.com.github.SchoolAPI.dto;

import digu_dev.com.github.SchoolAPI.entity.Department;

public record DepartmentDto(Long id, String name) {

    public DepartmentDto(Department department) {
        this(department.getId(), department.getName());
    }

}
