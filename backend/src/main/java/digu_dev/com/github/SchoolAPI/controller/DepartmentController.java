package digu_dev.com.github.SchoolAPI.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import digu_dev.com.github.SchoolAPI.dto.DepartmentDto;
import digu_dev.com.github.SchoolAPI.entity.Department;
import digu_dev.com.github.SchoolAPI.exception.ResourceAlreadyExistsException;
import digu_dev.com.github.SchoolAPI.service.DepartmentService;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<Object> createDepartment(@RequestBody DepartmentDto dto) {
       try{
        Department createdDepartment = dto.toEntity();
        departmentService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(createdDepartment.getId()).toUri();
        return ResponseEntity.created(location).build();
       }catch(IllegalArgumentException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
       }catch(ResourceAlreadyExistsException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
       }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Object> updateDepartment(@PathVariable("id") Long id, @RequestBody DepartmentDto dto) {
        try {
            Optional<Department> existingDepartment = departmentService.findById(id);
            if (existingDepartment.isEmpty()) {
                return ResponseEntity.notFound().build();
            }else{
                Department department = existingDepartment.get();
                department.setName(dto.name());
                departmentService.update(department);
                return ResponseEntity.noContent().build();
                }
                
            }catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }catch (ResourceAlreadyExistsException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
         }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteDepartment(@PathVariable("id") Long id) {
        Optional<Department> existingDepartment = departmentService.findById(id);
        if (existingDepartment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        departmentService.delete(existingDepartment.get());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDto> findDepartmentById(@PathVariable("id") Long id) {

        Optional<Department> existingDepartment = departmentService.findById(id);
        if(existingDepartment.isPresent()){
            Department department = existingDepartment.get();
            DepartmentDto dto = new DepartmentDto(department.getId(),
            department.getName());
            return ResponseEntity.ok(dto);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    
    
    @GetMapping
    public ResponseEntity<List<Department>> findAllDepartments() {
        List<Department> departments = departmentService.findAll();
        if (departments.isEmpty()) {
            return ResponseEntity.notFound().build();
        }else{
        return ResponseEntity.ok(departments);
        }
    } 

    @GetMapping("/name")
    public ResponseEntity<DepartmentDto> findByName(@RequestParam("name") String name) {
        Optional<Department> existingDepartment = departmentService.findByName(name);
        if(existingDepartment.isPresent()){
            Department department = existingDepartment.get();
            DepartmentDto dto = new DepartmentDto(department.getId(),
            department.getName());
            return ResponseEntity.ok(dto);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/teachers")
    public ResponseEntity<Department> findByIdWithTeachers(@PathVariable("id") Long id) {
        Optional<Department> existingDepartment = departmentService.findByIdWithTeachers(id);
        if(existingDepartment.isPresent()){
            return ResponseEntity.ok(existingDepartment.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
