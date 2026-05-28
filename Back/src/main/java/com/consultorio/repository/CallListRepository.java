package com.consultorio.repository;

import com.consultorio.model.Appointment;
import com.consultorio.model.CallList;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface CallListRepository
        extends JpaRepository<CallList, Long> {

    @Transactional

    @Modifying

    void deleteByAppointment(
            Appointment appointment
    );

    boolean existsByAppointment(
            Appointment appointment
    );
}

