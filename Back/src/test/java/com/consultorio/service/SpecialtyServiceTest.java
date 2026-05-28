package com.consultorio.service;


import com.consultorio.dto.specialty.SpecialtyCreateDto;
import com.consultorio.dto.specialty.SpecialtyUpdateDto;
import com.consultorio.exeption.BadRequestException;
import com.consultorio.mapper.SpecialtyMapper;
import com.consultorio.model.Specialty;
import com.consultorio.repository.SpecialtyRepository;
import com.consultorio.util.SpecialtyCreate;
import com.consultorio.util.SpecialtyDtoCreate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

@ExtendWith(SpringExtension.class)
class SpecialtyServiceTest {

    @InjectMocks
    SpecialtyService specialtyService;

    @Mock
    SpecialtyRepository specialtyRepository;

    @Mock
    SpecialtyMapper specialtyMapper;

    @Test
    void save_ReturnsSpecialty_WhenSuccessful() {

        // Arrange
        SpecialtyCreateDto dto =
                SpecialtyDtoCreate.creatSpecialtyDtoValid();

        Specialty specialty =
                SpecialtyCreate.creatSpecialtyValid();

        BDDMockito.when(
                        specialtyMapper.toSpecialty(dto)
                )
                .thenReturn(specialty);

        BDDMockito.when(
                        specialtyRepository.save(any(Specialty.class))
                )
                .thenAnswer(invocation -> {
                    Specialty specialtySaved =
                            invocation.getArgument(0);

                    specialtySaved.setId(1L);

                    return specialtySaved;
                });

        // Act
        Specialty specialtySaved =
                specialtyService.save(dto);

        // Assert
        Assertions.assertThat(specialtySaved)
                .isNotNull();

        Assertions.assertThat(specialtySaved.getId())
                .isEqualTo(1L);

        Assertions.assertThat(specialtySaved)
                .usingRecursiveComparison()
                .isEqualTo(specialty);

        BDDMockito.then(specialtyMapper)
                .should()
                .toSpecialty(dto);

        BDDMockito.then(specialtyRepository)
                .should()
                .save(specialty);
    }

    @Test
    void listAll_ReturnsSpecialties_WhenSuccessful() {

        // Arrange
        Specialty specialty =
                SpecialtyCreate.creatSpecialtyValid();

        List<Specialty> specialties =
                List.of(specialty);

        BDDMockito.when(
                        specialtyRepository.findAll()
                )
                .thenReturn(specialties);

        // Act
        List<Specialty> result =
                specialtyService.listAll();

        // Assert
        Assertions.assertThat(result)
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(result.get(0))
                .usingRecursiveComparison()
                .isEqualTo(specialty);

        BDDMockito.then(specialtyRepository)
                .should()
                .findAll();
    }

    @Test
    void findByIdOrThrowRequestException_ReturnsSpecialty_WhenSuccessful() {

        // Arrange
        Long id = 1L;

        Specialty specialty =
                SpecialtyCreate.creatSpecialtyValid();

        BDDMockito.when(
                        specialtyRepository.findById(id)
                )
                .thenReturn(Optional.of(specialty));

        // Act
        Specialty specialtyFound =
                specialtyService.findByIdOrThrowRequestException(id);

        // Assert
        Assertions.assertThat(specialtyFound)
                .usingRecursiveComparison()
                .isEqualTo(specialty);

        BDDMockito.then(specialtyRepository)
                .should()
                .findById(id);
    }

    @Test
    void findByIdOrThrowRequestException_ThrowsException_WhenSpecialtyNotFound() {

        // Arrange
        Long id = 1L;

        BDDMockito.when(
                        specialtyRepository.findById(id)
                )
                .thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThatThrownBy(() ->
                        specialtyService
                                .findByIdOrThrowRequestException(id))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Especialidade não encontrada");

        BDDMockito.then(specialtyRepository)
                .should()
                .findById(id);
    }

    @Test
    void update_UpdatesSpecialty_WhenSuccessful() {

        // Arrange
        Long id = 1L;

        Specialty specialty =
                SpecialtyCreate.creatSpecialtyValid();

        SpecialtyUpdateDto dto =
                SpecialtyDtoCreate.creatSpecialtyUpdateDtoValid();

        BDDMockito.when(
                        specialtyRepository.findById(id)
                )
                .thenReturn(Optional.of(specialty));

        BDDMockito.when(
                        specialtyRepository.save(any(Specialty.class))
                )
                .thenReturn(specialty);

        // Act
        Specialty specialtyUpdated =
                specialtyService.update(id, dto);

        // Assert
        Assertions.assertThat(specialtyUpdated)
                .isNotNull();

        Assertions.assertThat(specialtyUpdated.getName())
                .isEqualTo(dto.getName());

        Assertions.assertThat(specialtyUpdated.getActive())
                .isEqualTo(dto.getActive());

        BDDMockito.then(specialtyRepository)
                .should()
                .save(specialty);
    }

    @Test
    void delete_RemovesSpecialty_WhenSuccessful() {

        // Arrange
        Long id = 1L;

        Specialty specialty =
                SpecialtyCreate.creatSpecialtyValid();

        BDDMockito.when(
                        specialtyRepository.findById(id)
                )
                .thenReturn(Optional.of(specialty));

        // Act
        specialtyService.delete(id);

        // Assert
        BDDMockito.then(specialtyRepository)
                .should()
                .delete(specialty);
    }
}