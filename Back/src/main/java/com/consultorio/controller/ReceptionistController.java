package com.consultorio.controller;

import com.consultorio.dto.receptionist.ReceptionistCreateDto;
import com.consultorio.dto.receptionist.ReceptionistUpdateDto;
import com.consultorio.mapper.ReceptionistMapper;
import com.consultorio.model.Receptionist;
import com.consultorio.service.ReceptionistService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/receptionists")
@AllArgsConstructor

public class  ReceptionistController {

    private final ReceptionistService receptionistService;


    @PostMapping
    public ResponseEntity<Receptionist> save (@Valid  @RequestBody ReceptionistCreateDto receptionistDto){
        return new ResponseEntity<>(receptionistService.save(receptionistDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<Receptionist>> findAll(Pageable pageable){
        return ResponseEntity.ok(receptionistService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Receptionist> findById (@PathVariable Long id){
        return ResponseEntity.ok(receptionistService.findByIdOrThrowRequestException(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Receptionist>> findByName (@RequestParam String name){
        return ResponseEntity.ok(receptionistService.findByName(name));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Receptionist> update(
            @PathVariable Long id,
            @Valid @RequestBody ReceptionistUpdateDto receptionistDto){
        return ResponseEntity.ok(receptionistService.update(id, receptionistDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        receptionistService.delete(id);
        return  ResponseEntity.noContent().build();
    }

}
