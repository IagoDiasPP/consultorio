package com.consultorio.service;

import com.consultorio.dto.receptionist.ReceptionistCreateDto;
import com.consultorio.dto.receptionist.ReceptionistUpdateDto;
import com.consultorio.exeption.BadRequestException;
import com.consultorio.mapper.ReceptionistMapper;
import com.consultorio.model.Receptionist;
import com.consultorio.repository.ReceptionistRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReceptionistService {

    private final ReceptionistRepository receptionistRepository;

    private final ReceptionistMapper receptionistMapper;

    public Receptionist save(ReceptionistCreateDto receptionistDto) {
        return receptionistRepository.save(receptionistMapper.toReceptionist(receptionistDto));
    }

    public Page<Receptionist> findAll(Pageable pageable) {
        return receptionistRepository.findAll(pageable);
    }

    public Receptionist findByIdOrThrowRequestException(Long id) {
        return receptionistRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Não encontrado"));
    }

    public List<Receptionist> findByName(String name) {
        return receptionistRepository.findByName(name);
    }

    public Receptionist update(Long id, ReceptionistUpdateDto recepcionistaDto) {

        Receptionist receptionist = findByIdOrThrowRequestException(id);

        if (!receptionist.getEmail().equals(recepcionistaDto.getEmail())
                && receptionistRepository.existsByEmail(recepcionistaDto.getEmail())) {
            throw new BadRequestException("Email já cadastrado");
        }

        receptionist.setName(recepcionistaDto.getName());
        receptionist.setEmail(recepcionistaDto.getEmail());

        return receptionistRepository.save(receptionist);

    }

    public void delete(Long id) {

        receptionistRepository.delete(findByIdOrThrowRequestException(id));
    }
}
