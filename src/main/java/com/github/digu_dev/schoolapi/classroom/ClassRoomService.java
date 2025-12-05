package com.github.digu_dev.schoolapi.classroom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassRoomService {

    @Autowired
    ClassRoomRepository classRoomRepository;

    public ClassRoomEntity save (ClassRoomDTO dto){
        ClassRoomEntity classRoom = new ClassRoomEntity();
        classRoom.setPeriod(dto.period());
        return classRoomRepository.save(classRoom);
    }

    public Optional<ClassRoomEntity> findById(Long id) { return classRoomRepository.findById(id);}

    public List<ClassRoomEntity> findAll(){ return  classRoomRepository.findAll();}

    public void updateById(ClassRoomEntity classRoom){
        if(classRoom.getId() == null){
            throw new IllegalArgumentException("ClassRoom ID not found");
        }
        classRoomRepository.save(classRoom);
    }

    public void delete (ClassRoomEntity classRoom){
        if(classRoom.getId() == null){
            throw new IllegalArgumentException("ClassRoom ID not found");
        }
        classRoomRepository.delete(classRoom);
    }
}
