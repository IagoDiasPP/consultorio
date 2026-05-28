package com.consultorio.repository;

import com.consultorio.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatienteRepository extends JpaRepository<Patient, Long> {

    List<Patient> findByName (String name);

}
