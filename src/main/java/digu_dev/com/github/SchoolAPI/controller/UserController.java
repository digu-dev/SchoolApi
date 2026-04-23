package digu_dev.com.github.SchoolAPI.controller;

import java.net.URI;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import digu_dev.com.github.SchoolAPI.dto.UserDto;
import digu_dev.com.github.SchoolAPI.entity.UserEntity;
import digu_dev.com.github.SchoolAPI.exception.ResourceAlreadyExistsException;
import digu_dev.com.github.SchoolAPI.exception.UnauthorizedException;
import digu_dev.com.github.SchoolAPI.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Object> createLogin(@RequestBody UserDto dto) {
        try {
            UserEntity createdUser = userService.create(dto);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdUser.getId()).toUri();
            return ResponseEntity.created(location).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ResourceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    
    @PutMapping("/{username}")
    public ResponseEntity<Object> updateLogin(@PathVariable("username") String username, @RequestBody UserDto dto) {
        try {
           Optional<UserEntity> existingUser = userService.findByUsername(username);
           if(existingUser.isEmpty()){
            return ResponseEntity.notFound().build();
        }else{
            UserEntity user = existingUser.get();
            user.setUsername(dto.username());
            user.setPassword(dto.password());
            user.setRole(dto.role());
            userService.update(user);
            return ResponseEntity.noContent().build();
        }
        
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (ResourceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    
    @DeleteMapping("/{username}")
    public ResponseEntity<Object> deleteLogin(@PathVariable("username") String username) {
        Optional<UserEntity> existingUser = userService.findByUsername(username);
        if (existingUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        userService.delete(existingUser.get());
        return ResponseEntity.noContent().build();
    }

    
    @GetMapping("/{username}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<UserDto> findLoginByUsername(@PathVariable("username") String username) {
        Optional<UserEntity> existingUser = userService.findByUsername(username);
        if(existingUser.isPresent()){
            UserEntity user = existingUser.get();
            UserDto dto = new UserDto(user.getUsername(),
            user.getPassword(),
            user.getRole());
            return ResponseEntity.ok(dto);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

}

