package com.consultorio.service;


import com.consultorio.dto.schedule.ScheduleCreateDto;
import com.consultorio.dto.schedule.ScheduleUpdateDto;
import com.consultorio.exeption.BadRequestException;
import com.consultorio.mapper.ScheduleMapper;
import com.consultorio.model.Doctor;
import com.consultorio.model.Schedule;
import com.consultorio.repository.DoctorRepository;
import com.consultorio.repository.ScheduleRepository;
import com.consultorio.util.DoctorCreate;
import com.consultorio.util.ScheduleCreate;
import com.consultorio.util.ScheduleDtoCreate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.consultorio.dto.schedule.ScheduleResponseDto;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

@ExtendWith(SpringExtension.class)
class ScheduleServiceTest {

    @InjectMocks
    ScheduleService scheduleService;

    @Mock
    ScheduleRepository scheduleRepository;

    @Mock
    DoctorService doctorService;

    @Mock
    ScheduleMapper scheduleMapper;

    @Mock
    DoctorRepository doctorRepository;

    @Test
    void save_ReturnsSchedule_WhenSuccessful() {

        // Arrange
        ScheduleCreateDto dto =
                ScheduleDtoCreate.scheduleCreatDtoValid();

        Doctor doctor =
                DoctorCreate.creatDoctorValid();

        Schedule schedule =
                ScheduleCreate.creatScheduleValid();

        BDDMockito.when(
                        doctorRepository.findById(any())
                )
                .thenReturn(Optional.of(doctor));

        BDDMockito.when(
                        scheduleMapper.toSchedule(dto)
                )
                .thenReturn(schedule);

        BDDMockito.when(
                        scheduleRepository.save(any(Schedule.class))
                )
                .thenAnswer(invocation -> {
                    Schedule scheduleSaved =
                            invocation.getArgument(0);

                    scheduleSaved.setId(1L);

                    return scheduleSaved;
                });

        // Act
        Schedule scheduleSaved =
                scheduleService.save(dto);

        // Assert
        Assertions.assertThat(scheduleSaved)
                .isNotNull();

        Assertions.assertThat(scheduleSaved.getId())
                .isEqualTo(1L);

        Assertions.assertThat(scheduleSaved.getDoctor())
                .isEqualTo(doctor);

        BDDMockito.then(scheduleMapper)
                .should()
                .toSchedule(dto);

        BDDMockito.then(scheduleRepository)
                .should()
                .save(schedule);
    }

    @Test
    void save_ThrowsException_WhenDoctorNotFound() {

        // Arrange
        ScheduleCreateDto dto =
                ScheduleDtoCreate.scheduleCreatDtoValid();

        BDDMockito.when(
                        doctorRepository.findById(any())
                )
                .thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThatThrownBy(() ->
                        scheduleService.save(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Doctor not found");

        BDDMockito.then(scheduleRepository)
                .should(never())
                .save(any());
    }

    @Test
    void findAll_ReturnsSchedules_WhenSuccessful() {

        // Arrange
        Schedule schedule =
                ScheduleCreate.creatScheduleValid();

        List<Schedule> schedules =
                List.of(schedule);

        BDDMockito.when(
                        scheduleRepository.findAll()
                )
                .thenReturn(schedules);

        // Act
        List<ScheduleResponseDto> result =
                scheduleService.findAll();

        // Assert
        Assertions.assertThat(result)
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(result.get(0).getId())
                .isEqualTo(schedule.getId());

        BDDMockito.then(scheduleRepository)
                .should()
                .findAll();
    }
    @Test
    void findByIdOrThrowRequestException_ReturnsSchedule_WhenSuccessful() {

        // Arrange
        Long id = 1L;

        Schedule schedule =
                ScheduleCreate.creatScheduleValid();

        BDDMockito.when(
                        scheduleRepository.findById(id)
                )
                .thenReturn(Optional.of(schedule));

        // Act
        Schedule scheduleFound =
                scheduleService.findByIdOrThrowRequestException(id);

        // Assert
        Assertions.assertThat(scheduleFound)
                .usingRecursiveComparison()
                .isEqualTo(schedule);

        BDDMockito.then(scheduleRepository)
                .should()
                .findById(id);
    }

    @Test
    void findByIdOrThrowRequestException_ThrowsException_WhenScheduleNotFound() {

        // Arrange
        Long id = 1L;

        BDDMockito.when(
                        scheduleRepository.findById(id)
                )
                .thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThatThrownBy(() ->
                        scheduleService
                                .findByIdOrThrowRequestException(id))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Não foi encontrado agenda com o id:" + id);

        BDDMockito.then(scheduleRepository)
                .should()
                .findById(id);
    }

    @Test
    void findByDoctor_ReturnsSchedules_WhenSuccessful() {

        // Arrange
        Long doctorId = 1L;

        Doctor doctor =
                DoctorCreate.creatDoctorValid();

        Schedule schedule =
                ScheduleCreate.creatScheduleValid();

        List<Schedule> schedules =
                List.of(schedule);

        BDDMockito.when(
                        doctorService
                                .findByIdOrThrowRequestException(doctorId)
                )
                .thenReturn(doctor);

        BDDMockito.when(
                        scheduleRepository.findByDoctor(doctor)
                )
                .thenReturn(schedules);

        // Act
        List<Schedule> result =
                scheduleService.findByDoctor(doctorId);

        // Assert
        Assertions.assertThat(result)
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(result.get(0))
                .usingRecursiveComparison()
                .isEqualTo(schedule);

        BDDMockito.then(scheduleRepository)
                .should()
                .findByDoctor(doctor);
    }

    @Test
    void findByDayOfWeek_ReturnsSchedules_WhenSuccessful() {

        // Arrange
        Schedule schedule =
                ScheduleCreate.creatScheduleValid();

        DayOfWeek dayOfWeek =
                DayOfWeek.MONDAY;

        List<Schedule> schedules =
                List.of(schedule);

        BDDMockito.when(
                        scheduleRepository.findByDayOfWeek(dayOfWeek)
                )
                .thenReturn(schedules);

        // Act
        List<Schedule> result =
                scheduleService.findByDayOfWeek(dayOfWeek);

        // Assert
        Assertions.assertThat(result)
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(result.get(0))
                .usingRecursiveComparison()
                .isEqualTo(schedule);

        BDDMockito.then(scheduleRepository)
                .should()
                .findByDayOfWeek(dayOfWeek);
    }

    @Test
    void update_UpdatesSchedule_WhenSuccessful() {

        // Arrange
        Long id = 1L;

        Schedule schedule =
                ScheduleCreate.creatScheduleValid();

        ScheduleUpdateDto dto =
                ScheduleDtoCreate.scheduleUpdateDtoValid();

        BDDMockito.when(
                        scheduleRepository.findById(id)
                )
                .thenReturn(Optional.of(schedule));

        BDDMockito.when(
                        scheduleRepository.save(any(Schedule.class))
                )
                .thenReturn(schedule);

        // Act
        Schedule scheduleUpdated =
                scheduleService.update(id, dto);

        // Assert
        Assertions.assertThat(scheduleUpdated)
                .isNotNull();

        BDDMockito.then(scheduleMapper)
                .should()
                .updateScheduleFromDto(dto, schedule);

        BDDMockito.then(scheduleRepository)
                .should()
                .save(schedule);
    }

    @Test
    void delete_RemovesSchedule_WhenSuccessful() {

        // Arrange
        Long id = 1L;

        Schedule schedule =
                ScheduleCreate.creatScheduleValid();

        BDDMockito.when(
                        scheduleRepository.findById(id)
                )
                .thenReturn(Optional.of(schedule));

        // Act
        scheduleService.delete(id);

        // Assert
        BDDMockito.then(scheduleRepository)
                .should()
                .delete(schedule);
    }
}