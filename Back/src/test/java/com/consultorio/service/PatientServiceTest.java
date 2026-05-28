package com.consultorio.service;

import com.consultorio.dto.patient.PatientCreateDto;
import com.consultorio.dto.patient.PatientUpdateDto;
import com.consultorio.exeption.BadRequestException;
import com.consultorio.mapper.PatientMapper;
import com.consultorio.model.Patient;
import com.consultorio.repository.PatienteRepository;
import com.consultorio.util.PatientCreate;
import com.consultorio.util.PatientDtoCreate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
class PatientServiceTest {

    @InjectMocks
    PatientService patientService;

    @Mock
    PatienteRepository patienteRepository;

    @Mock
    PatientMapper patientMapper;

    @Test
    void save_ReturnsPatient_WhenSuccessful() {

        // Arrange
        PatientCreateDto dto =
                PatientDtoCreate.patientCreatDtoValid();

        Patient patient =
                PatientCreate.creatPatientValid();

        BDDMockito.when(
                        patientMapper.toPatient(dto)
                )
                .thenReturn(patient);

        BDDMockito.when(
                        patienteRepository.save(any(Patient.class))
                )
                .thenAnswer(invocation -> {
                    Patient patientSaved =
                            invocation.getArgument(0);

                    patientSaved.setId(1L);

                    return patientSaved;
                });

        // Act
        Patient patientSaved =
                patientService.save(dto);

        // Assert
        Assertions.assertThat(patientSaved)
                .isNotNull();

        Assertions.assertThat(patientSaved.getId())
                .isEqualTo(1L);

        Assertions.assertThat(patientSaved)
                .usingRecursiveComparison()
                .isEqualTo(patient);

        BDDMockito.then(patientMapper)
                .should()
                .toPatient(dto);

        BDDMockito.then(patienteRepository)
                .should()
                .save(patient);
    }

    @Test
    void findAll_ReturnsPageOfPatients_WhenSuccessful() {

        // Arrange
        Patient patient =
                PatientCreate.creatPatientValid();

        Pageable pageable =
                PageRequest.of(0, 10);

        Page<Patient> patients =
                new PageImpl<>(List.of(patient));

        BDDMockito.when(
                        patienteRepository.findAll(pageable)
                )
                .thenReturn(patients);

        // Act
        Page<Patient> result =
                patientService.findAll(pageable);

        // Assert
        Assertions.assertThat(result)
                .isNotNull();

        Assertions.assertThat(result.getContent())
                .hasSize(1);

        Assertions.assertThat(result.getContent().get(0))
                .usingRecursiveComparison()
                .isEqualTo(patient);

        BDDMockito.then(patienteRepository)
                .should()
                .findAll(pageable);
    }

    @Test
    void findByIdOrThrowRequestException_ReturnsPatient_WhenSuccessful() {

        // Arrange
        Long id = 1L;

        Patient patient =
                PatientCreate.creatPatientValid();

        BDDMockito.when(
                        patienteRepository.findById(id)
                )
                .thenReturn(Optional.of(patient));

        // Act
        Patient patientFound =
                patientService.findByIdOrThrowRequestException(id);

        // Assert
        Assertions.assertThat(patientFound)
                .usingRecursiveComparison()
                .isEqualTo(patient);

        BDDMockito.then(patienteRepository)
                .should()
                .findById(id);
    }

    @Test
    void findByIdOrThrowRequestException_ThrowsException_WhenPatientNotFound() {

        // Arrange
        Long id = 1L;

        BDDMockito.when(
                        patienteRepository.findById(id)
                )
                .thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThatThrownBy(() ->
                        patientService
                                .findByIdOrThrowRequestException(id))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Não encontrado");

        BDDMockito.then(patienteRepository)
                .should()
                .findById(id);
    }

    @Test
    void findByName_ReturnsPatients_WhenSuccessful() {

        // Arrange
        String name = "Carlos";

        Patient patient =
                PatientCreate.creatPatientValid();

        List<Patient> patients =
                List.of(patient);

        BDDMockito.when(
                        patienteRepository.findByName(name)
                )
                .thenReturn(patients);

        // Act
        List<Patient> result =
                patientService.findByName(name);

        // Assert
        Assertions.assertThat(result)
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(result.get(0))
                .usingRecursiveComparison()
                .isEqualTo(patient);

        BDDMockito.then(patienteRepository)
                .should()
                .findByName(name);
    }

    @Test
    void update_UpdatesPatient_WhenSuccessful() {

        // Arrange
        Long id = 1L;

        Patient patient =
                PatientCreate.creatPatientValid();

        PatientUpdateDto dto =
                PatientDtoCreate.patientUpdateDtoValid();

        BDDMockito.when(
                        patienteRepository.findById(id)
                )
                .thenReturn(Optional.of(patient));

        BDDMockito.when(
                        patienteRepository.save(any(Patient.class))
                )
                .thenReturn(patient);

        // Act
        Patient patientUpdated =
                patientService.update(id, dto);

        // Assert
        Assertions.assertThat(patientUpdated)
                .isNotNull();

        Assertions.assertThat(patientUpdated.getName())
                .isEqualTo(dto.getName());

        Assertions.assertThat(patientUpdated.getPhone())
                .isEqualTo(dto.getPhone());

        Assertions.assertThat(patientUpdated.getBirthDate())
                .isEqualTo(dto.getBirthDate());

        BDDMockito.then(patienteRepository)
                .should()
                .save(patient);
    }

    @Test
    void delete_RemovesPatient_WhenSuccessful() {

        // Arrange
        Long id = 1L;

        Patient patient =
                PatientCreate.creatPatientValid();

        BDDMockito.when(
                        patienteRepository.findById(id)
                )
                .thenReturn(Optional.of(patient));

        // Act
        patientService.delete(id);

        // Assert
        BDDMockito.then(patienteRepository)
                .should()
                .delete(patient);
    }
}