package com.consultorio.service;

import com.consultorio.dto.patient.PatientCreateDto;
import com.consultorio.dto.patient.PatientUpdateDto;
import com.consultorio.exeption.BadRequestException;
import com.consultorio.mapper.PatientMapper;
import com.consultorio.model.Patient;
import com.consultorio.repository.PatienteRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PatientService {

    private final PatienteRepository patienteRepository;
    private final PatientMapper patientMapper;

    public Patient save(PatientCreateDto pacienteDto) {
        return patienteRepository.save(patientMapper.toPatient(pacienteDto));

    }

    public Page<Patient> findAll(Pageable pageable) {
        return patienteRepository.findAll(pageable);
    }

    public Patient findByIdOrThrowRequestException(Long id) {
        return patienteRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Não encontrado"));
    }

    public List<Patient> findByName(String name) {
        return patienteRepository.findByName(name);
    }

    public  Patient update(Long id, PatientUpdateDto patientDto) {
        Patient patient = findByIdOrThrowRequestException(id);

        patient.setName(patientDto.getName());
        patient.setPhone(patientDto.getPhone());
        patient.setBirthDate(patientDto.getBirthDate());

        return patienteRepository.save(patient);
    }

    public void delete(Long id) {
        patienteRepository.delete(findByIdOrThrowRequestException(id));
    }
}
