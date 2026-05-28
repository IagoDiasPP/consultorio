package com.consultorio.controller;

import com.consultorio.dto.appointment.AppointmentCreateDto;
import com.consultorio.dto.appointment.AppointmentUpdateDto;
import com.consultorio.model.Appointment;
import com.consultorio.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.DayOfWeek;
import com.consultorio.dto.appointment.DoctorSlotResponseDto;

import java.util.List;

@RestController
@RequestMapping("/appointments")
@AllArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<Appointment> save(
            @Valid @RequestBody AppointmentCreateDto dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(appointmentService.save(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                appointmentService.findByIdOrThrowRequestException(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> findAll() {

        return ResponseEntity.ok(
                appointmentService.findAll()
        );
    }

    @GetMapping("/waiting")
    public ResponseEntity<List<Appointment>> findWaiting() {
        return ResponseEntity.ok(
                appointmentService.findWaiting()
        );
    }

    @PostMapping("/process-queue")
    public ResponseEntity<Void> processQueue(Pageable pageable) {
        appointmentService.tentarAgendarFila(pageable);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appointment> update(
            @PathVariable Long id,
            @RequestBody AppointmentUpdateDto dto) {

        return ResponseEntity.ok(
                appointmentService.update(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        appointmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/doctor-slots")
    public ResponseEntity<List<DoctorSlotResponseDto>> getDoctorSlots(

            @RequestParam Long doctorId,

            @RequestParam DayOfWeek dayOfWeek
    ) {

        return ResponseEntity.ok(

                appointmentService.getDoctorSlots(
                        doctorId,
                        dayOfWeek
                )
        );
    }

    @PutMapping("/{id}/remarcar")
    public ResponseEntity<Appointment> remarcar(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                appointmentService
                        .remarcarProximaSemana(id)
        );
    }

    @PutMapping("/{id}/alta")
    public ResponseEntity<Appointment> alta(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                appointmentService
                        .darAlta(id)
        );
    }
}
