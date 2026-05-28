package com.consultorio.repository;

import com.consultorio.model.Schedule;
import com.consultorio.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByDoctor(Doctor doctor);

    List<Schedule> findByDayOfWeek(DayOfWeek dayOfWeek);

    boolean existsByDoctorAndDayOfWeek(
            Doctor doctor,
            DayOfWeek dayOfWeek
    );
}
