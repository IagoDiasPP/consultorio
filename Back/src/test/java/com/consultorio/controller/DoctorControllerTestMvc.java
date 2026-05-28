package com.consultorio.controller;

import com.consultorio.dto.doctor.DoctorCreateDto;
import com.consultorio.dto.doctor.DoctorUpdateDto;
import com.consultorio.model.Doctor;
import com.consultorio.service.DoctorService;
import com.consultorio.util.DoctorCreate;
import com.consultorio.util.DoctorDtoCreate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.data.domain.PageRequest;


@WebMvcTest(DoctorController.class)
class DoctorControllerTestMvc {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DoctorService doctorService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldCreateDoctor() throws Exception {

        Doctor doctor = DoctorCreate.creatDoctorValid();
        DoctorCreateDto doctorDto = DoctorDtoCreate.creatDoctorDtoValid();

        BDDMockito.when(doctorService.save(doctorDto))
                .thenReturn(doctor);

        mockMvc.perform(post("/doctors")
                        .content(objectMapper.writeValueAsString(doctorDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(doctor.getName()));

        BDDMockito.then(doctorService)
                .should()
                .save(doctorDto);
    }

    @Test
    void shouldReturnPageOfDoctors() throws Exception {

        Doctor doctor = DoctorCreate.creatDoctorValid();
        Page<Doctor> doctors = new PageImpl<>(List.of(doctor));

        BDDMockito.when(doctorService.findAll(PageRequest.of(0, 10)))
                .thenReturn(doctors);

        mockMvc.perform(get("/doctors")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value(doctor.getName()))
                .andExpect(jsonPath("$.totalElements").value(1));

        BDDMockito.then(doctorService)
                .should()
                .findAll(PageRequest.of(0, 10));
    }

    @Test
    void shouldReturnDoctorsBySpecialty() throws Exception {

        Doctor doctor = DoctorCreate.creatDoctorValid();
        Long specialtyId = doctor.getSpecialty().getId();

        Page<Doctor> doctors = new PageImpl<>(List.of(doctor));

        BDDMockito.when(doctorService.findBySpecialty(specialtyId, PageRequest.of(0, 10)))
                .thenReturn(doctors);

        mockMvc.perform(get("/doctors/by-specialty")
                        .param("specialtyId", specialtyId.toString())
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value(doctor.getName()))
                .andExpect(jsonPath("$.totalElements").value(1));

        BDDMockito.then(doctorService)
                .should()
                .findBySpecialty(specialtyId, PageRequest.of(0, 10));
    }

    @Test
    void shouldReturnDoctorById() throws Exception {

        Doctor doctor = DoctorCreate.creatDoctorValid();
        Long id = doctor.getId();

        BDDMockito.when(doctorService.findByIdOrThrowRequestException(id))
                .thenReturn(doctor);

        mockMvc.perform(get("/doctors/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(doctor.getName()));

        BDDMockito.then(doctorService)
                .should()
                .findByIdOrThrowRequestException(id);
    }

    @Test
    void shouldReturnDoctorsByName() throws Exception {

        Doctor doctor = DoctorCreate.creatDoctorValid();
        String name = doctor.getName();

        List<Doctor> doctors = List.of(doctor);

        BDDMockito.when(doctorService.findByName(name))
                .thenReturn(doctors);

        mockMvc.perform(get("/doctors/search")
                        .param("name", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(name));

        BDDMockito.then(doctorService)
                .should()
                .findByName(name);
    }

    @Test
    void shouldUpdateDoctor() throws Exception {

        Doctor doctor = DoctorCreate.creatDoctorValid();
        Long id = doctor.getId();

        DoctorUpdateDto doctorDto = DoctorDtoCreate.creatDoctorUpdateDtoValid();

        BDDMockito.when(doctorService.update(id, doctorDto))
                .thenReturn(doctor);

        mockMvc.perform(put("/doctors/{id}", id)
                        .content(objectMapper.writeValueAsString(doctorDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(doctor.getName()));

        BDDMockito.then(doctorService)
                .should()
                .update(id, doctorDto);
    }

    @Test
    void shouldDeleteDoctor() throws Exception {

        Long id = 1L;

        BDDMockito.doNothing()
                .when(doctorService)
                .delete(id);

        mockMvc.perform(delete("/doctors/{id}", id))
                .andExpect(status().isNoContent());

        BDDMockito.then(doctorService)
                .should()
                .delete(id);
    }
}