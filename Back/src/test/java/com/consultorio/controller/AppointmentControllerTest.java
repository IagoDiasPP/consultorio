package com.consultorio.controller;

import com.consultorio.dto.appointment.AppointmentCreateDto;
import com.consultorio.dto.appointment.AppointmentUpdateDto;
import com.consultorio.model.Appointment;
import com.consultorio.service.AppointmentService;
import com.consultorio.util.AppointmentCreate;
import com.consultorio.util.AppointmentDtoCreate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppointmentController.class)
class AppointmentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AppointmentService appointmentService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldCreateAppointment() throws Exception {

        Appointment appointment = AppointmentCreate.creatAppointmentValid();
        AppointmentCreateDto dto = AppointmentDtoCreate.creatAppointmentDtoValid();

        BDDMockito.when(appointmentService.save(dto))
                .thenReturn(appointment);

        mockMvc.perform(post("/appointments")
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(appointment.getId()));

        BDDMockito.then(appointmentService)
                .should()
                .save(dto);
    }

    @Test
    void shouldReturnAppointmentById() throws Exception {

        Long id = 1L;

        Appointment appointment = AppointmentCreate.creatAppointmentValid();
        appointment.setId(id);

        BDDMockito.when(appointmentService.findByIdOrThrowRequestException(id))
                .thenReturn(appointment);

        mockMvc.perform(get("/appointments/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));

        BDDMockito.then(appointmentService)
                .should()
                .findByIdOrThrowRequestException(id);
    }

    @Test
    void shouldReturnWaitingAppointments() throws Exception {

        Appointment appointment = AppointmentCreate.creatAppointmentValid();
        List<Appointment> list = List.of(appointment);

        BDDMockito.when(appointmentService.findWaiting())
                .thenReturn(list);

        mockMvc.perform(get("/appointments/waiting"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(appointment.getId()));

        BDDMockito.then(appointmentService)
                .should()
                .findWaiting();
    }

    @Test
    void shouldProcessQueue() throws Exception {

        mockMvc.perform(post("/appointments/process-queue")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        BDDMockito.then(appointmentService)
                .should()
                .tentarAgendarFila(ArgumentMatchers.any(Pageable.class));
    }

    @Test
    void shouldUpdateAppointment() throws Exception {

        Long id = 1L;

        Appointment appointment = AppointmentCreate.creatAppointmentValid();
        appointment.setId(id);

        AppointmentUpdateDto dto = AppointmentDtoCreate.updateValid();

        BDDMockito.when(appointmentService.update(id, dto))
                .thenReturn(appointment);

        mockMvc.perform(put("/appointments/{id}", id)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));

        BDDMockito.then(appointmentService)
                .should()
                .update(id, dto);
    }

    @Test
    void shouldDeleteAppointment() throws Exception {

        Long id = 1L;

        BDDMockito.doNothing()
                .when(appointmentService)
                .delete(id);

        mockMvc.perform(delete("/appointments/{id}", id))
                .andExpect(status().isNoContent());

        BDDMockito.then(appointmentService)
                .should()
                .delete(id);
    }
}