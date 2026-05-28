package com.consultorio.controller;

import com.consultorio.dto.patient.PatientCreateDto;
import com.consultorio.dto.patient.PatientUpdateDto;
import com.consultorio.model.Patient;
import com.consultorio.service.PatientService;
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
@RequestMapping("/patients")

public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<Patient> save (@Valid @RequestBody PatientCreateDto patientDto){
        return new ResponseEntity<>(patientService.save(patientDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<Patient>> findAll(Pageable pageable){
        return ResponseEntity.ok(patientService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> findById (@PathVariable Long id) {
        return ResponseEntity.ok(patientService.findByIdOrThrowRequestException(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Patient>> findByName (@RequestParam String name) {
        return  ResponseEntity.ok(patientService.findByName(name));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> update(
            @PathVariable Long id,
            @Valid  @RequestBody PatientUpdateDto patientDto){
        return ResponseEntity.ok(patientService.update(id,patientDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        patientService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
