package com.consultorio.service;

import com.consultorio.dto.doctor.DoctorCreateDto;
import com.consultorio.dto.doctor.DoctorUpdateDto;
import com.consultorio.exeption.BadRequestException;
import com.consultorio.mapper.DoctorMapper;
import com.consultorio.model.Doctor;
import com.consultorio.model.Specialty;
import com.consultorio.repository.DoctorRepository;
import com.consultorio.repository.SpecialtyRepository;
import com.consultorio.util.DoctorCreate;
import com.consultorio.util.DoctorDtoCreate;
import com.consultorio.util.SpecialtyCreate;
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
import static org.mockito.Mockito.never;

@ExtendWith(SpringExtension.class)
class DoctorServiceTest {

    @InjectMocks
    DoctorService doctorService;

    @Mock
    DoctorRepository doctorRepository;

    @Mock
    DoctorMapper doctorMapper;

    @Mock
    SpecialtyService specialtyService;

    @Mock
    SpecialtyRepository specialtyRepository;

    @Test
    void save_ReturnsDoctor_WhenSuccessful() {

        // Arrange
        DoctorCreateDto dto =
                DoctorDtoCreate.creatDoctorDtoValid();

        Doctor doctor =
                DoctorCreate.creatDoctorValid();

        Specialty specialty =
                SpecialtyCreate.creatSpecialtyValid();

        BDDMockito.when(
                        specialtyRepository.findById(any())
                )
                .thenReturn(Optional.of(specialty));

        BDDMockito.when(
                        doctorMapper.toDoctor(dto)
                )
                .thenReturn(doctor);

        BDDMockito.when(
                        doctorRepository.save(any(Doctor.class))
                )
                .thenAnswer(invocation -> {
                    Doctor doctorSaved =
                            invocation.getArgument(0);

                    doctorSaved.setId(1L);

                    return doctorSaved;
                });

        // Act
        Doctor doctorSaved =
                doctorService.save(dto);

        // Assert
        Assertions.assertThat(doctorSaved)
                .isNotNull();

        Assertions.assertThat(doctorSaved.getId())
                .isEqualTo(1L);

        Assertions.assertThat(doctorSaved.getSpecialty())
                .isEqualTo(specialty);

        BDDMockito.then(doctorMapper)
                .should()
                .toDoctor(dto);

        BDDMockito.then(doctorRepository)
                .should()
                .save(doctor);
    }

    @Test
    void save_ThrowsException_WhenSpecialtyNotFound() {

        // Arrange
        DoctorCreateDto dto =
                DoctorDtoCreate.creatDoctorDtoValid();

        BDDMockito.when(
                        specialtyRepository.findById(any())
                )
                .thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThatThrownBy(() ->
                        doctorService.save(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Specialty not found");

        BDDMockito.then(doctorRepository)
                .should(never())
                .save(any());
    }

    @Test
    void findAll_ReturnsPageOfDoctors_WhenSuccessful() {

        // Arrange
        Doctor doctor =
                DoctorCreate.creatDoctorValid();

        Pageable pageable =
                PageRequest.of(0, 10);

        Page<Doctor> doctors =
                new PageImpl<>(List.of(doctor));

        BDDMockito.when(
                        doctorRepository.findAll(pageable)
                )
                .thenReturn(doctors);

        // Act
        Page<Doctor> result =
                doctorService.findAll(pageable);

        // Assert
        Assertions.assertThat(result)
                .isNotNull();

        Assertions.assertThat(result.getContent())
                .hasSize(1);

        Assertions.assertThat(result.getContent().get(0))
                .usingRecursiveComparison()
                .isEqualTo(doctor);

        BDDMockito.then(doctorRepository)
                .should()
                .findAll(pageable);
    }

    @Test
    void findBySpecialty_ReturnsDoctors_WhenSuccessful() {

        // Arrange
        Doctor doctor =
                DoctorCreate.creatDoctorValid();

        Long specialtyId = 1L;

        Pageable pageable =
                PageRequest.of(0, 10);

        Page<Doctor> doctors =
                new PageImpl<>(List.of(doctor));

        BDDMockito.when(
                        doctorRepository.findBySpecialtyId(
                                specialtyId,
                                pageable
                        )
                )
                .thenReturn(doctors);

        // Act
        Page<Doctor> result =
                doctorService.findBySpecialty(
                        specialtyId,
                        pageable
                );

        // Assert
        Assertions.assertThat(result)
                .isNotNull();

        Assertions.assertThat(result.getContent())
                .hasSize(1);

        BDDMockito.then(doctorRepository)
                .should()
                .findBySpecialtyId(specialtyId, pageable);
    }

    @Test
    void findByIdOrThrowRequestException_ReturnsDoctor_WhenSuccessful() {

        // Arrange
        Doctor doctor =
                DoctorCreate.creatDoctorValid();

        Long id = 1L;

        BDDMockito.when(
                        doctorRepository.findById(id)
                )
                .thenReturn(Optional.of(doctor));

        // Act
        Doctor doctorFound =
                doctorService.findByIdOrThrowRequestException(id);

        // Assert
        Assertions.assertThat(doctorFound)
                .usingRecursiveComparison()
                .isEqualTo(doctor);

        BDDMockito.then(doctorRepository)
                .should()
                .findById(id);
    }

    @Test
    void findByIdOrThrowRequestException_ThrowsException_WhenDoctorNotFound() {

        // Arrange
        Long id = 1L;

        BDDMockito.when(
                        doctorRepository.findById(id)
                )
                .thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThatThrownBy(() ->
                        doctorService
                                .findByIdOrThrowRequestException(id))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Não encontrado");

        BDDMockito.then(doctorRepository)
                .should()
                .findById(id);
    }

    @Test
    void findByName_ReturnsDoctors_WhenSuccessful() {

        // Arrange
        Doctor doctor =
                DoctorCreate.creatDoctorValid();

        String name = "Carlos";

        List<Doctor> doctors =
                List.of(doctor);

        BDDMockito.when(
                        doctorRepository.findByName(name)
                )
                .thenReturn(doctors);

        // Act
        List<Doctor> result =
                doctorService.findByName(name);

        // Assert
        Assertions.assertThat(result)
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(result.get(0))
                .usingRecursiveComparison()
                .isEqualTo(doctor);

        BDDMockito.then(doctorRepository)
                .should()
                .findByName(name);
    }

    @Test
    void update_UpdatesDoctor_WhenSuccessful() {

        // Arrange
        Long id = 1L;

        Doctor doctor =
                DoctorCreate.creatDoctorValid();

        DoctorUpdateDto dto =
                DoctorDtoCreate.creatDoctorUpdateDtoValid();

        Specialty specialty =
                SpecialtyCreate.creatSpecialtyValid();

        BDDMockito.when(
                        doctorRepository.findById(id)
                )
                .thenReturn(Optional.of(doctor));

        BDDMockito.when(
                        specialtyService
                                .findByIdOrThrowRequestException(any())
                )
                .thenReturn(specialty);

        BDDMockito.when(
                        doctorRepository.existsByEmail(any())
                )
                .thenReturn(false);

        BDDMockito.when(
                        doctorRepository.save(any(Doctor.class))
                )
                .thenReturn(doctor);

        // Act
        Doctor doctorUpdated =
                doctorService.update(id, dto);

        // Assert
        Assertions.assertThat(doctorUpdated)
                .isNotNull();

        Assertions.assertThat(doctorUpdated.getSpecialty())
                .isEqualTo(specialty);

        BDDMockito.then(doctorRepository)
                .should()
                .save(doctor);
    }

    @Test
    void update_ThrowsException_WhenEmailAlreadyExists() {

        // Arrange
        Long id = 1L;

        Doctor doctor =
                DoctorCreate.creatDoctorValid();

        DoctorUpdateDto dto =
                DoctorDtoCreate.creatDoctorUpdateDtoValid();

        dto.setEmail("email@gmail.com");

        doctor.setEmail("outro@gmail.com");

        Specialty specialty =
                SpecialtyCreate.creatSpecialtyValid();

        BDDMockito.when(
                        doctorRepository.findById(id)
                )
                .thenReturn(Optional.of(doctor));

        BDDMockito.when(
                        specialtyService
                                .findByIdOrThrowRequestException(any())
                )
                .thenReturn(specialty);

        BDDMockito.when(
                        doctorRepository.existsByEmail(any())
                )
                .thenReturn(true);

        // Act & Assert
        Assertions.assertThatThrownBy(() ->
                        doctorService.update(id, dto))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Email já cadastrado");

        BDDMockito.then(doctorRepository)
                .should(never())
                .save(any());
    }

    @Test
    void delete_RemovesDoctor_WhenSuccessful() {

        // Arrange
        Long id = 1L;

        Doctor doctor =
                DoctorCreate.creatDoctorValid();

        BDDMockito.when(
                        doctorRepository.findById(id)
                )
                .thenReturn(Optional.of(doctor));

        // Act
        doctorService.delete(id);

        // Assert
        BDDMockito.then(doctorRepository)
                .should()
                .delete(doctor);
    }
}

