package com.consultorio.controller;

import com.consultorio.dto.specialty.SpecialtyCreateDto;
import com.consultorio.dto.specialty.SpecialtyUpdateDto;
import com.consultorio.model.Specialty;
import com.consultorio.service.SpecialtyService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/specialties")
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    @PostMapping
    public ResponseEntity<Specialty> save (@Valid @RequestBody SpecialtyCreateDto specialtyDto){
        return new ResponseEntity<>(specialtyService.save(specialtyDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Specialty>> listAll(){
        return ResponseEntity.ok(specialtyService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Specialty> findById(@PathVariable Long id){
        return ResponseEntity.ok(specialtyService.findByIdOrThrowRequestException(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Specialty> update(
            @PathVariable Long id,
            @Valid @RequestBody SpecialtyUpdateDto specialtyDto){
        return ResponseEntity.ok(specialtyService.update(id,specialtyDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        specialtyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
