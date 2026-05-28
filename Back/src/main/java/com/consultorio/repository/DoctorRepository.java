package com.consultorio.repository;

import com.consultorio.model.Doctor;
import jakarta.validation.constraints.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Page<Doctor> findBySpecialtyId(Long specialtyId, Pageable pageable);

    List<Doctor> findByName(String name);

    boolean existsByEmail(@Email String email);
}
