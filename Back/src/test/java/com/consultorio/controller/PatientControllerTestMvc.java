package com.consultorio.controller;

import com.consultorio.dto.patient.PatientCreateDto;
import com.consultorio.dto.patient.PatientUpdateDto;
import com.consultorio.model.Patient;
import com.consultorio.service.PatientService;
import com.consultorio.util.PatientCreate;
import com.consultorio.util.PatientDtoCreate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PatientController.class)
class PatientControllerTestMvc {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PatientService patientService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldCreatePatient() throws Exception {

        Patient patient = PatientCreate.creatPatientValid();
        PatientCreateDto patientDto = PatientDtoCreate.patientCreatDtoValid();

        BDDMockito.when(patientService.save(patientDto))
                .thenReturn(patient);

        mockMvc.perform(post("/patients")
                        .content(objectMapper.writeValueAsString(patientDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(patient.getName()));

        BDDMockito.then(patientService)
                .should()
                .save(patientDto);
    }

    @Test
    void shouldReturnPageOfPatients() throws Exception {

        Patient patient = PatientCreate.creatPatientValid();
        Page<Patient> patients = new PageImpl<>(List.of(patient));

        BDDMockito.when(patientService.findAll(ArgumentMatchers.any(Pageable.class)))
                .thenReturn(patients);

        mockMvc.perform(get("/patients")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value(patient.getName()))
                .andExpect(jsonPath("$.totalElements").value(1));

        BDDMockito.then(patientService)
                .should()
                .findAll(ArgumentMatchers.any(Pageable.class));
    }

    @Test
    void shouldReturnPatientById() throws Exception {

        Patient patient = PatientCreate.creatPatientValid();
        Long id = patient.getId();

        BDDMockito.when(patientService.findByIdOrThrowRequestException(id))
                .thenReturn(patient);

        mockMvc.perform(get("/patients/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(patient.getName()));

        BDDMockito.then(patientService)
                .should()
                .findByIdOrThrowRequestException(id);
    }

    @Test
    void shouldReturnPatientsByName() throws Exception {

        Patient patient = PatientCreate.creatPatientValid();
        String name = patient.getName();

        List<Patient> patients = List.of(patient);

        BDDMockito.when(patientService.findByName(name))
                .thenReturn(patients);

        mockMvc.perform(get("/patients/search")
                        .param("name", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(name));

        BDDMockito.then(patientService)
                .should()
                .findByName(name);
    }

    @Test
    void shouldUpdatePatient() throws Exception {

        Patient patient = PatientCreate.creatPatientValid();
        Long id = patient.getId();

        PatientUpdateDto patientDto = PatientDtoCreate.patientUpdateDtoValid();

        BDDMockito.when(patientService.update(id, patientDto))
                .thenReturn(patient);

        mockMvc.perform(put("/patients/{id}", id)
                        .content(objectMapper.writeValueAsString(patientDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(patient.getName()));

        BDDMockito.then(patientService)
                .should()
                .update(id, patientDto);
    }

    @Test
    void shouldDeletePatient() throws Exception {

        Long id = 1L;

        BDDMockito.doNothing()
                .when(patientService)
                .delete(id);

        mockMvc.perform(delete("/patients/{id}", id))
                .andExpect(status().isNoContent());

        BDDMockito.then(patientService)
                .should()
                .delete(id);
    }
}
