package com.consultorio.repository;

import com.consultorio.model.Receptionist;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceptionistRepository extends JpaRepository <Receptionist, Long> {
    List<Receptionist> findByName(String name);

    boolean existsByEmail(@NotBlank @Email String email);
}
