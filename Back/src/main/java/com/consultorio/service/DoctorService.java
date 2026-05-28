package com.consultorio.service;


import com.consultorio.dto.doctor.DoctorCreateDto;
import com.consultorio.dto.doctor.DoctorUpdateDto;
import com.consultorio.exeption.BadRequestException;
import com.consultorio.mapper.DoctorMapper;
import com.consultorio.mapper.SpecialtyMapper;
import com.consultorio.model.Doctor;
import com.consultorio.model.Specialty;
import com.consultorio.repository.DoctorRepository;
import com.consultorio.repository.SpecialtyRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final SpecialtyService specialtyService;
    private final SpecialtyRepository specialtyRepository;

    public Doctor save(DoctorCreateDto dto) {

        Specialty specialty = specialtyRepository.findById(dto.getSpecialtyId())
                .orElseThrow(() -> new RuntimeException("Specialty not found"));

        Doctor doctor = doctorMapper.toDoctor(dto);

        doctor.setSpecialty(specialty);

        return doctorRepository.save(doctor);
    }

    public Page<Doctor> findAll(Pageable pageable) {
        return doctorRepository.findAll(pageable);
    }

    public Page<Doctor> findBySpecialty(Long specialtyId, Pageable pageable) {
        return doctorRepository.findBySpecialtyId(specialtyId, pageable);
    }

    public Doctor findByIdOrThrowRequestException(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Não encontrado"));
    }

    public List<Doctor> findByName(String name) {
        return doctorRepository.findByName(name);
    }

    public Doctor update(Long id, DoctorUpdateDto doutorDto) {
        Doctor doctor = findByIdOrThrowRequestException(id);

        Specialty specialty = specialtyService
                .findByIdOrThrowRequestException(doutorDto.getSpecialtyId());

        if (!doctor.getEmail().equals(doutorDto.getEmail())
                && doctorRepository.existsByEmail(doutorDto.getEmail())) {
            throw new BadRequestException("Email já cadastrado");
        }

        doctor.setName(doutorDto.getName());
        doctor.setEmail(doutorDto.getEmail());
        doctor.setPhone(doutorDto.getPhone());
        doctor.setSpecialty(specialty);

        return doctorRepository.save(doctor);
    }

    public void delete(Long id) {
        doctorRepository.delete(findByIdOrThrowRequestException(id));
    }
}
