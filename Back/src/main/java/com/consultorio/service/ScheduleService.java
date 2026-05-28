package com.consultorio.service;

import com.consultorio.dto.schedule.ScheduleCreateDto;
import com.consultorio.dto.schedule.ScheduleResponseDto;
import com.consultorio.dto.schedule.ScheduleUpdateDto;
import com.consultorio.exeption.BadRequestException;
import com.consultorio.mapper.ScheduleMapper;
import com.consultorio.model.Doctor;
import com.consultorio.model.Schedule;
import com.consultorio.repository.DoctorRepository;
import com.consultorio.repository.ScheduleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;

@Service
@AllArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final DoctorService doctorService;
    private final ScheduleMapper scheduleMapper;
    private final DoctorRepository doctorRepository;

    public Schedule save(ScheduleCreateDto dto) {

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        boolean jaExiste =
                scheduleRepository.existsByDoctorAndDayOfWeek(
                        doctor,
                        dto.getDayOfWeek()
                );

        if (jaExiste) {
            throw new BadRequestException(
                    "Esse médico já possui agenda nesse dia"
            );
        }

        Schedule schedule = scheduleMapper.toSchedule(dto);

        schedule.setDoctor(doctor);

        return scheduleRepository.save(schedule);
    }

    public List<ScheduleResponseDto> findAll() {
        return scheduleRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public Schedule findByIdOrThrowRequestException(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() ->
                        new BadRequestException("Não foi encontrado agenda com o id:" + id));
    }

    public List<Schedule> findByDoctor(Long doctorId) {
        Doctor doctor = doctorService.findByIdOrThrowRequestException(doctorId);
        return scheduleRepository.findByDoctor(doctor);
    }

    public List<Schedule> findByDayOfWeek(DayOfWeek dayOfWeek) {
        return scheduleRepository.findByDayOfWeek(dayOfWeek);
    }

    public Schedule update(Long id, ScheduleUpdateDto scheduleDto) {

        Schedule schedule = findByIdOrThrowRequestException(id);

        scheduleMapper.updateScheduleFromDto(scheduleDto, schedule);

        return scheduleRepository.save(schedule);
    }

    public void delete(Long id) {
        scheduleRepository.delete(findByIdOrThrowRequestException(id));
    }

    private ScheduleResponseDto toResponseDto(Schedule schedule) {

        return ScheduleResponseDto.builder()

                .id(schedule.getId())

                .doctorId(schedule.getDoctor().getId())

                .doctorName(schedule.getDoctor().getName())

                .dayOfWeek(schedule.getDayOfWeek().name())

                .startTime(schedule.getStartTime().toString())

                .endTime(schedule.getEndTime().toString())

                .breakStart(schedule.getBreakStart().toString())

                .breakEnd(schedule.getBreakEnd().toString())

                .build();
    }
}

