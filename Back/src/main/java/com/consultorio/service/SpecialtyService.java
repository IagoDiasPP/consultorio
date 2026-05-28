package com.consultorio.service;

import com.consultorio.dto.specialty.SpecialtyCreateDto;
import com.consultorio.dto.specialty.SpecialtyUpdateDto;
import com.consultorio.exeption.BadRequestException;
import com.consultorio.mapper.SpecialtyMapper;
import com.consultorio.model.Specialty;
import com.consultorio.repository.SpecialtyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SpecialtyService {

    private final SpecialtyRepository specialtyRepository;
    private final SpecialtyMapper specialtyMapper;

    public Specialty save(SpecialtyCreateDto specialtyDto) {
        return specialtyRepository.save(specialtyMapper.toSpecialty(specialtyDto));
    }

    public List<Specialty> listAll() {
        return specialtyRepository.findAll();
    }

    public Specialty findByIdOrThrowRequestException(Long id) {
        return specialtyRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Especialidade não encontrada"));
    }

    public Specialty update(Long id, SpecialtyUpdateDto specialtyDto) {

        Specialty specialty = findByIdOrThrowRequestException(id);

        specialty.setName(specialtyDto.getName());
        specialty.setActive(specialtyDto.getActive());

        return specialtyRepository.save(specialty);
    }

    public void delete(Long id) {
        specialtyRepository.delete(
                findByIdOrThrowRequestException(id)
        );
    }
}
