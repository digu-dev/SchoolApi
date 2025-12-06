package com.github.digu_dev.schoolapi.classroom;

import com.github.digu_dev.schoolapi.exceptions.DuplicatedRegisterException;
import com.github.digu_dev.schoolapi.exceptions.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/classrooms")
public class ClassRoomController {

    @Autowired
    ClassRoomService classRoomService;

    @PostMapping
    public ResponseEntity<Object> save (@RequestBody ClassRoomDTO dto){
        try{
            ClassRoomEntity classRoom = dto.mappedByClassRoomEntity();
            classRoomService.save(dto);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(classRoom.getId()).toUri();
            return ResponseEntity.created(location).build();
        }catch (DuplicatedRegisterException e){
            ResponseError conflict = ResponseError.conflict(e.getMessage());
            return ResponseEntity.status(conflict.status()).body(conflict);

        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassRoomDTO> findById (@PathVariable("id") Long id){
        Optional<ClassRoomEntity> classRoomEntityOptional = classRoomService.findById(id);
        if(classRoomEntityOptional.isPresent()){
            ClassRoomEntity classRoom = classRoomEntityOptional.get();
            ClassRoomDTO classRoomDTO = new ClassRoomDTO(classRoom.getId(), classRoom.getPeriod());
            return ResponseEntity.ok(classRoomDTO);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ClassRoomEntity>> findAll(){
        List<ClassRoomEntity> classRooms = classRoomService.findAll();
        if(classRooms.isEmpty()){
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.ok(classRooms);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateById (@PathVariable("id") Long id, @RequestBody ClassRoomDTO dto){
        try{
            Optional<ClassRoomEntity> classRoomEntityOptional = classRoomService.findById(id);
            if(classRoomEntityOptional.isPresent()){
                var classRoom = classRoomEntityOptional.get();
                classRoom.setPeriod(dto.period());
                classRoomService.updateById(classRoom);
                return ResponseEntity.noContent().build();
            }else{
                return ResponseEntity.notFound().build();
            }
        }catch (DuplicatedRegisterException e){
            ResponseError conflict = ResponseError.conflict(e.getMessage());
            return ResponseEntity.status(conflict.status()).body(conflict);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById (@PathVariable("id") Long id){
        Optional<ClassRoomEntity> classRoomEntityOptional = classRoomService.findById(id);
        if(classRoomEntityOptional.isPresent()){
            classRoomService.delete(classRoomEntityOptional.get());
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
