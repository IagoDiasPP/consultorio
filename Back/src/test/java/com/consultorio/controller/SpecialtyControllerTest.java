package com.consultorio.controller;

import com.consultorio.dto.specialty.SpecialtyCreateDto;
import com.consultorio.dto.specialty.SpecialtyUpdateDto;
import com.consultorio.model.Specialty;
import com.consultorio.service.SpecialtyService;
import com.consultorio.util.SpecialtyCreate;
import com.consultorio.util.SpecialtyDtoCreate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SpecialtyController.class)
class SpecialtyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SpecialtyService specialtyService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldCreateSpecialty() throws Exception {

        Specialty specialty = SpecialtyCreate.specialtyCreat();
        SpecialtyCreateDto dto = SpecialtyDtoCreate.creatSpecialtyDtoValid();

        BDDMockito.when(specialtyService.save(dto))
                .thenReturn(specialty);

        mockMvc.perform(post("/specialties")
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(specialty.getName()));

        BDDMockito.then(specialtyService)
                .should()
                .save(dto);
    }

    @Test
    void shouldReturnAllSpecialties() throws Exception {

        Specialty specialty = SpecialtyCreate.specialtyCreat();
        List<Specialty> specialties = List.of(specialty);

        BDDMockito.when(specialtyService.listAll())
                .thenReturn(specialties);

        mockMvc.perform(get("/specialties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(specialty.getName()));

        BDDMockito.then(specialtyService)
                .should()
                .listAll();
    }

    @Test
    void shouldReturnSpecialtyById() throws Exception {

        Specialty specialty = SpecialtyCreate.specialtyCreat();
        Long id = specialty.getId();

        BDDMockito.when(specialtyService.findByIdOrThrowRequestException(id))
                .thenReturn(specialty);

        mockMvc.perform(get("/specialties/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(specialty.getName()));

        BDDMockito.then(specialtyService)
                .should()
                .findByIdOrThrowRequestException(id);
    }

    @Test
    void shouldUpdateSpecialty() throws Exception {

        Specialty specialty = SpecialtyCreate.specialtyCreat();
        Long id = specialty.getId();

        SpecialtyUpdateDto dto = SpecialtyDtoCreate.creatSpecialtyUpdateDtoValid();

        BDDMockito.when(specialtyService.update(id, dto))
                .thenReturn(specialty);

        mockMvc.perform(put("/specialties/{id}", id)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(specialty.getName()));

        BDDMockito.then(specialtyService)
                .should()
                .update(id, dto);
    }

    @Test
    void shouldDeleteSpecialty() throws Exception {

        Long id = 1L;

        BDDMockito.doNothing()
                .when(specialtyService)
                .delete(id);

        mockMvc.perform(delete("/specialties/{id}", id))
                .andExpect(status().isNoContent());

        BDDMockito.then(specialtyService)
                .should()
                .delete(id);
    }
}