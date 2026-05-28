package com.consultorio.controller;

import com.consultorio.dto.schedule.ScheduleCreateDto;
import com.consultorio.dto.schedule.ScheduleResponseDto;
import com.consultorio.dto.schedule.ScheduleUpdateDto;
import com.consultorio.model.Schedule;
import com.consultorio.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequestMapping("/schedules")
@AllArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<Schedule> save(@Valid @RequestBody ScheduleCreateDto scheduleDto) {
        return new ResponseEntity<>(scheduleService.save(scheduleDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> findAll() {
        return ResponseEntity.ok(scheduleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Schedule> findById(@PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.findByIdOrThrowRequestException(id));
    }

    @GetMapping("/by-doctor")
    public ResponseEntity<List<Schedule>> findByDoctor(@RequestParam Long doctorId) {
        return ResponseEntity.ok(scheduleService.findByDoctor(doctorId));
    }

    @GetMapping("/by-day")
    public ResponseEntity<List<Schedule>> findByDayOfWeek(
            @RequestParam DayOfWeek dayOfWeek) {

        return ResponseEntity.ok(
                scheduleService.findByDayOfWeek(dayOfWeek)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Schedule> update(
            @PathVariable Long id,
            @Valid @RequestBody ScheduleUpdateDto scheduleDto) {
        return ResponseEntity.ok(scheduleService.update(id, scheduleDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        scheduleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
