package com.consultorio.service;

import com.consultorio.dto.appointment.AppointmentCreateDto;
import com.consultorio.dto.appointment.AppointmentUpdateDto;
import com.consultorio.enums.AppointmentStatus;
import com.consultorio.mapper.AppointmentMapper;
import com.consultorio.model.*;
import com.consultorio.repository.AppointmentRepository;
import com.consultorio.repository.CallListRepository;
import com.consultorio.repository.SpecialtyRepository;
import com.consultorio.util.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.anyList;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @InjectMocks
    AppointmentService appointmentService;

    @Mock
    AppointmentRepository appointmentRepository;

    @Mock
    DoctorService doctorService;

    @Mock
    ScheduleService scheduleService;

    @Mock
    AppointmentMapper appointmentMapper;

    @Mock
    CallListRepository callListRepository;

    @Mock
    PatientService patientService;

    @Mock
    SpecialtyRepository specialtyRepository;

    @Test
    void save_ReturnsPendingAppointment_WhenDoctorAvailable() {

        // Arrange
        AppointmentCreateDto dto =
                AppointmentDtoCreate.creatAppointmentDtoValid();

        Patient patient =
                PatientCreate.creatPatientValid();

        Specialty specialty =
                SpecialtyCreate.creatSpecialtyValid();

        Doctor doctor =
                DoctorCreate.creatDoctorValid();

        Schedule schedule =
                ScheduleCreate.creatScheduleValid();

        Page<Doctor> doctors =
                new PageImpl<>(List.of(doctor));

        BDDMockito.when(
                        patientService.findByIdOrThrowRequestException(any())
                )
                .thenReturn(patient);

        BDDMockito.when(
                        specialtyRepository.findById(any())
                )
                .thenReturn(Optional.of(specialty));

        BDDMockito.when(
                        doctorService.findBySpecialty(any(), any())
                )
                .thenReturn(doctors);

        BDDMockito.when(
                        scheduleService.findByDoctor(any())
                )
                .thenReturn(List.of(schedule));

        BDDMockito.when(
                        appointmentRepository
                                .existsByDoctorAndDateAndStartTimeAndStatusIn(
                                        any(),
                                        any(),
                                        any(),
                                        anyList()
                                )
                )
                .thenReturn(false);

        BDDMockito.when(appointmentRepository.save(any()))
                .thenAnswer(invocation -> {
                    Appointment appointment =
                            invocation.getArgument(0);

                    appointment.setId(1L);

                    return appointment;
                });

        // Act
        Appointment appointmentSaved =
                appointmentService.save(dto);

        // Assert
        Assertions.assertThat(appointmentSaved)
                .isNotNull();

        Assertions.assertThat(appointmentSaved.getId())
                .isEqualTo(1L);

        Assertions.assertThat(appointmentSaved.getStatus())
                .isEqualTo(AppointmentStatus.PENDING);

        Assertions.assertThat(appointmentSaved.getDoctor())
                .isEqualTo(doctor);

    }

    @Test
    void save_ReturnsWaitingAppointment_WhenNoDoctorAvailable() {

        // Arrange
        AppointmentCreateDto dto =
                AppointmentDtoCreate.creatAppointmentDtoValid();

        Patient patient =
                PatientCreate.creatPatientValid();

        Specialty specialty =
                SpecialtyCreate.creatSpecialtyValid();

        Doctor doctor =
                DoctorCreate.creatDoctorValid();

        Schedule schedule =
                ScheduleCreate.creatScheduleValid();

        Page<Doctor> doctors =
                new PageImpl<>(List.of(doctor));

        BDDMockito.when(
                        patientService.findByIdOrThrowRequestException(any())
                )
                .thenReturn(patient);

        BDDMockito.when(
                        specialtyRepository.findById(any())
                )
                .thenReturn(Optional.of(specialty));

        BDDMockito.when(
                        doctorService.findBySpecialty(any(), any())
                )
                .thenReturn(doctors);

        BDDMockito.when(
                        scheduleService.findByDoctor(any())
                )
                .thenReturn(List.of(schedule));

        BDDMockito.when(
                        appointmentRepository
                                .existsByDoctorAndDateAndStartTimeAndStatusIn(
                                        any(),
                                        any(),
                                        any(),
                                        anyList()
                                )
                )
                .thenReturn(true);

        BDDMockito.when(appointmentRepository.save(any()))
                .thenAnswer(invocation -> {
                    Appointment appointment =
                            invocation.getArgument(0);

                    appointment.setId(1L);

                    return appointment;
                });

        // Act
        Appointment appointmentSaved =
                appointmentService.save(dto);

        // Assert
        Assertions.assertThat(appointmentSaved)
                .isNotNull();

        Assertions.assertThat(appointmentSaved.getStatus())
                .isEqualTo(AppointmentStatus.WAITING);

        Assertions.assertThat(appointmentSaved.getDoctor())
                .isNull();

        BDDMockito.then(callListRepository)
                .should(never())
                .save(any(CallList.class));
    }

    @Test
    void tentarAgendarFila_UpdatesAppointment_WhenDoctorAvailable() {

        // Arrange
        Appointment appointment =
                AppointmentCreate.creatAppointment();

        appointment.setStatus(AppointmentStatus.WAITING);

        Doctor doctor =
                DoctorCreate.creatDoctorValid();

        Schedule schedule =
                ScheduleCreate.creatScheduleValid();

        Pageable pageable =
                PageRequest.of(0, 10);

        Page<Doctor> doctors =
                new PageImpl<>(List.of(doctor));

        BDDMockito.when(
                        appointmentRepository
                                .findByStatusOrderByIdAsc(
                                        AppointmentStatus.WAITING
                                )
                )
                .thenReturn(List.of(appointment));

        BDDMockito.when(
                        doctorService.findBySpecialty(any(), any())
                )
                .thenReturn(doctors);

        BDDMockito.when(
                        scheduleService.findByDoctor(any())
                )
                .thenReturn(List.of(schedule));

        BDDMockito.when(
                        appointmentRepository
                                .existsByDoctorAndDateAndStartTimeAndStatusIn(
                                        any(),
                                        any(),
                                        any(),
                                        anyList()
                                )
                )
                .thenReturn(false);

        // Act
        appointmentService.tentarAgendarFila(pageable);

        // Assert
        Assertions.assertThat(appointment.getStatus())
                .isEqualTo(AppointmentStatus.PENDING);

        BDDMockito.then(appointmentRepository)
                .should()
                .save(appointment);
    }

    @Test
    void findByIdOrThrowRequestException_ReturnsAppointment_WhenSuccessful() {

        // Arrange
        Appointment appointment =
                AppointmentCreate.creatAppointment();

        Long id = 1L;

        BDDMockito.when(appointmentRepository.findById(id))
                .thenReturn(Optional.of(appointment));

        // Act
        Appointment appointmentGet =
                appointmentService.findByIdOrThrowRequestException(id);

        // Assert
        Assertions.assertThat(appointmentGet)
                .usingRecursiveComparison()
                .isEqualTo(appointment);

        BDDMockito.then(appointmentRepository)
                .should()
                .findById(id);
    }

    @Test
    void findByIdOrThrowRequestException_ThrowsException_WhenAppointmentNotFound() {

        // Arrange
        Long id = 1L;

        BDDMockito.when(appointmentRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThatThrownBy(() ->
                        appointmentService
                                .findByIdOrThrowRequestException(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Appointment not found");

        BDDMockito.then(appointmentRepository)
                .should()
                .findById(id);
    }

    @Test
    void findWaiting_ReturnsWaitingAppointments_WhenSuccessful() {

        // Arrange
        Appointment appointment =
                AppointmentCreate.creatAppointment();

        List<Appointment> appointments =
                List.of(appointment);

        BDDMockito.when(
                        appointmentRepository
                                .findByStatusOrderByIdAsc(
                                        AppointmentStatus.WAITING
                                )
                )
                .thenReturn(appointments);

        // Act
        List<Appointment> result =
                appointmentService.findWaiting();

        // Assert
        Assertions.assertThat(result)
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(result)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(appointments);

        BDDMockito.then(appointmentRepository)
                .should()
                .findByStatusOrderByIdAsc(AppointmentStatus.WAITING);
    }

    @Test
    void update_UpdatesAppointment_WhenSuccessful() {

        // Arrange
        Long id = 1L;

        Appointment appointment =
                AppointmentCreate.creatAppointment();

        AppointmentUpdateDto dto =
                AppointmentDtoCreate.updateValid();

        BDDMockito.when(appointmentRepository.findById(id))
                .thenReturn(Optional.of(appointment));

        BDDMockito.when(
                        appointmentRepository.save(any(Appointment.class))
                )
                .thenReturn(appointment);

        // Act
        Appointment updated =
                appointmentService.update(id, dto);

        // Assert
        Assertions.assertThat(updated)
                .isNotNull();

        BDDMockito.then(appointmentMapper)
                .should()
                .updateFromDto(dto, appointment);

        BDDMockito.then(appointmentRepository)
                .should()
                .save(appointment);
    }

    @Test
    void delete_RemovesAppointment_WhenSuccessful() {

        // Arrange
        Long id = 1L;

        Appointment appointment =
                AppointmentCreate.creatAppointment();

        BDDMockito.when(appointmentRepository.findById(id))
                .thenReturn(Optional.of(appointment));

        // Act
        appointmentService.delete(id);

        // Assert
        BDDMockito.then(appointmentRepository)
                .should()
                .delete(appointment);
    }
}