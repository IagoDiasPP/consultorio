package com.consultorio.controller;

import com.consultorio.dto.doctor.DoctorUpdateDto;
import com.consultorio.dto.doctor.DoctorCreateDto;
import com.consultorio.model.Doctor;
import com.consultorio.service.DoctorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    public ResponseEntity<Doctor> save(@Valid @RequestBody DoctorCreateDto doctorDto) {
        return new ResponseEntity<>(doctorService.save(doctorDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<Doctor>> findAll(Pageable pageable) {
        return ResponseEntity.ok(doctorService.findAll(pageable));
    }

    @GetMapping("/by-specialty")
    public ResponseEntity<Page<Doctor>> findBySpecialty(
            @RequestParam Long specialtyId,
            Pageable pageable) {

        return ResponseEntity.ok(
                doctorService.findBySpecialty(specialtyId, pageable)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> findById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.findByIdOrThrowRequestException(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Doctor>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(doctorService.findByName(name));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> update(
            @PathVariable Long id,
            @Valid @RequestBody DoctorUpdateDto doctorDto) {
        return ResponseEntity.ok(doctorService.update(id, doctorDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        doctorService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
