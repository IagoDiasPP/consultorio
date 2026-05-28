package com.consultorio.service;

import com.consultorio.dto.receptionist.ReceptionistCreateDto;
import com.consultorio.dto.receptionist.ReceptionistUpdateDto;
import com.consultorio.exeption.BadRequestException;
import com.consultorio.mapper.ReceptionistMapper;
import com.consultorio.model.Receptionist;
import com.consultorio.repository.ReceptionistRepository;
import com.consultorio.util.ReceptionistCreate;
import com.consultorio.util.ReceptionistDtoCreate;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

@ExtendWith(SpringExtension.class)
class ReceptionistServiceTest {

    @InjectMocks
    ReceptionistService receptionistService;

    @Mock
    ReceptionistRepository receptionistRepository;

    @Mock
    ReceptionistMapper receptionistMapper;

    @Test
    void save_ReturnsReceptionist_WhenSuccessful() {

        // Arrange
        ReceptionistCreateDto dto =
                ReceptionistDtoCreate.creatReceptionistDtoValid();

        Receptionist receptionist =
                ReceptionistCreate.creatReceptionistValid();

        BDDMockito.when(
                        receptionistMapper.toReceptionist(dto)
                )
                .thenReturn(receptionist);

        BDDMockito.when(
                        receptionistRepository.save(any(Receptionist.class))
                )
                .thenAnswer(invocation -> {
                    Receptionist receptionistSaved =
                            invocation.getArgument(0);

                    receptionistSaved.setId(1L);

                    return receptionistSaved;
                });

        // Act
        Receptionist receptionistSaved =
                receptionistService.save(dto);

        // Assert
        Assertions.assertThat(receptionistSaved)
                .isNotNull();

        Assertions.assertThat(receptionistSaved.getId())
                .isEqualTo(1L);

        Assertions.assertThat(receptionistSaved)
                .usingRecursiveComparison()
                .isEqualTo(receptionist);

        BDDMockito.then(receptionistMapper)
                .should()
                .toReceptionist(dto);

        BDDMockito.then(receptionistRepository)
                .should()
                .save(receptionist);
    }

    @Test
    void findAll_ReturnsPageOfReceptionists_WhenSuccessful() {

        // Arrange
        Receptionist receptionist =
                ReceptionistCreate.creatReceptionistValid();

        Pageable pageable =
                PageRequest.of(0, 10);

        Page<Receptionist> receptionists =
                new PageImpl<>(List.of(receptionist));

        BDDMockito.when(
                        receptionistRepository.findAll(pageable)
                )
                .thenReturn(receptionists);

        // Act
        Page<Receptionist> result =
                receptionistService.findAll(pageable);

        // Assert
        Assertions.assertThat(result)
                .isNotNull();

        Assertions.assertThat(result.getContent())
                .hasSize(1);

        Assertions.assertThat(result.getContent().get(0))
                .usingRecursiveComparison()
                .isEqualTo(receptionist);

        BDDMockito.then(receptionistRepository)
                .should()
                .findAll(pageable);
    }

    @Test
    void findByIdOrThrowRequestException_ReturnsReceptionist_WhenSuccessful() {

        // Arrange
        Long id = 1L;

        Receptionist receptionist =
                ReceptionistCreate.creatReceptionistValid();

        BDDMockito.when(
                        receptionistRepository.findById(id)
                )
                .thenReturn(Optional.of(receptionist));

        // Act
        Receptionist receptionistFound =
                receptionistService.findByIdOrThrowRequestException(id);

        // Assert
        Assertions.assertThat(receptionistFound)
                .usingRecursiveComparison()
                .isEqualTo(receptionist);

        BDDMockito.then(receptionistRepository)
                .should()
                .findById(id);
    }

    @Test
    void findByIdOrThrowRequestException_ThrowsException_WhenReceptionistNotFound() {

        // Arrange
        Long id = 1L;

        BDDMockito.when(
                        receptionistRepository.findById(id)
                )
                .thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThatThrownBy(() ->
                        receptionistService
                                .findByIdOrThrowRequestException(id))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Não encontrado");

        BDDMockito.then(receptionistRepository)
                .should()
                .findById(id);
    }

    @Test
    void findByName_ReturnsReceptionists_WhenSuccessful() {

        // Arrange
        String name = "Maria";

        Receptionist receptionist =
                ReceptionistCreate.creatReceptionistValid();

        List<Receptionist> receptionists =
                List.of(receptionist);

        BDDMockito.when(
                        receptionistRepository.findByName(name)
                )
                .thenReturn(receptionists);

        // Act
        List<Receptionist> result =
                receptionistService.findByName(name);

        // Assert
        Assertions.assertThat(result)
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(result.get(0))
                .usingRecursiveComparison()
                .isEqualTo(receptionist);

        BDDMockito.then(receptionistRepository)
                .should()
                .findByName(name);
    }

    @Test
    void update_UpdatesReceptionist_WhenSuccessful() {

        // Arrange
        Long id = 1L;

        Receptionist receptionist =
                ReceptionistCreate.creatReceptionistValid();

        ReceptionistUpdateDto dto =
                ReceptionistDtoCreate.creatReceptionistUpdateDtoValid();

        BDDMockito.when(
                        receptionistRepository.findById(id)
                )
                .thenReturn(Optional.of(receptionist));

        BDDMockito.when(
                        receptionistRepository.existsByEmail(any())
                )
                .thenReturn(false);

        BDDMockito.when(
                        receptionistRepository.save(any(Receptionist.class))
                )
                .thenReturn(receptionist);

        // Act
        Receptionist receptionistUpdated =
                receptionistService.update(id, dto);

        // Assert
        Assertions.assertThat(receptionistUpdated)
                .isNotNull();

        Assertions.assertThat(receptionistUpdated.getName())
                .isEqualTo(dto.getName());

        Assertions.assertThat(receptionistUpdated.getEmail())
                .isEqualTo(dto.getEmail());

        BDDMockito.then(receptionistRepository)
                .should()
                .save(receptionist);
    }

    @Test
    void update_ThrowsException_WhenEmailAlreadyExists() {

        // Arrange
        Long id = 1L;

        Receptionist receptionist =
                ReceptionistCreate.creatReceptionistValid();

        ReceptionistUpdateDto dto =
                ReceptionistDtoCreate.creatReceptionistUpdateDtoValid();

        receptionist.setEmail("old@gmail.com");

        dto.setEmail("new@gmail.com");

        BDDMockito.when(
                        receptionistRepository.findById(id)
                )
                .thenReturn(Optional.of(receptionist));

        BDDMockito.when(
                        receptionistRepository.existsByEmail(any())
                )
                .thenReturn(true);

        // Act & Assert
        Assertions.assertThatThrownBy(() ->
                        receptionistService.update(id, dto))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Email já cadastrado");

        BDDMockito.then(receptionistRepository)
                .should(never())
                .save(any());
    }

    @Test
    void delete_RemovesReceptionist_WhenSuccessful() {

        // Arrange
        Long id = 1L;

        Receptionist receptionist =
                ReceptionistCreate.creatReceptionistValid();

        BDDMockito.when(
                        receptionistRepository.findById(id)
                )
                .thenReturn(Optional.of(receptionist));

        // Act
        receptionistService.delete(id);

        // Assert
        BDDMockito.then(receptionistRepository)
                .should()
                .delete(receptionist);
    }
}