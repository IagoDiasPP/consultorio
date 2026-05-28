package com.consultorio.repository;

import com.consultorio.enums.AppointmentStatus;
import com.consultorio.model.Appointment;
import com.consultorio.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    boolean existsByDoctorAndDateAndStartTime(
            Doctor doctor,
            LocalDate date,
            LocalTime startTime
    );

    List<Appointment> findByStatusOrderByIdAsc(AppointmentStatus status);

    Optional<Appointment> findByDoctorAndDateAndStartTime(
            Doctor doctor,
            LocalDate date,
            LocalTime startTime
    );

    boolean existsByDoctorAndDateAndStartTimeAndStatus(
            Doctor doctor,
            LocalDate date,
            LocalTime startTime,
            AppointmentStatus status
    );

    Optional<Appointment>
    findByDoctorAndDateAndStartTimeAndStatus(
            Doctor doctor,
            LocalDate date,
            LocalTime startTime,
            AppointmentStatus status
    );

    boolean existsByDoctorAndDateAndStartTimeAndStatusIn(
            Doctor doctor,
            LocalDate date,
            LocalTime startTime,
            List<AppointmentStatus> statuses
    );

    Optional<Appointment>
    findByDoctorAndDateAndStartTimeAndStatusIn(
            Doctor doctor,
            LocalDate date,
            LocalTime startTime,
            List<AppointmentStatus> statuses
    );
}
