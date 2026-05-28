package com.consultorio.controller;

import com.consultorio.dto.schedule.ScheduleCreateDto;
import com.consultorio.dto.schedule.ScheduleUpdateDto;
import com.consultorio.model.Schedule;
import com.consultorio.service.ScheduleService;
import com.consultorio.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.DayOfWeek;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScheduleController.class)
class ScheduleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ScheduleService scheduleService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldCreateScheduleSuccessfully() throws Exception {

        Schedule schedule = ScheduleCreate.creatScheduleValid();
        ScheduleCreateDto dto = ScheduleDtoCreate.scheduleCreatDtoValid();

        BDDMockito.when(scheduleService.save(dto))
                .thenReturn(schedule);

        mockMvc.perform(post("/schedules")
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(schedule.getId()));

        BDDMockito.then(scheduleService)
                .should()
                .save(dto);
    }

    @Test
    void shouldReturnAllSchedules() throws Exception {

        Schedule schedule = ScheduleCreate.creatScheduleValid();
        List<Schedule> schedules = List.of(schedule);

        BDDMockito.when(scheduleService.findAll())
                .thenReturn(schedules);

        mockMvc.perform(get("/schedules"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(schedule.getId()));

        BDDMockito.then(scheduleService)
                .should()
                .findAll();
    }

    @Test
    void shouldReturnSchedule_whenFindById() throws Exception {

        Long id = 1L;

        Schedule schedule = ScheduleCreate.creatScheduleValid();
        schedule.setId(id);

        BDDMockito.when(scheduleService.findByIdOrThrowRequestException(id))
                .thenReturn(schedule);

        mockMvc.perform(get("/schedules/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));

        BDDMockito.then(scheduleService)
                .should()
                .findByIdOrThrowRequestException(id);
    }

    @Test
    void shouldReturnSchedules_whenFindByDoctor() throws Exception {

        Long id = 1L;

        Schedule schedule = ScheduleCreate.creatScheduleValid();
        schedule.setId(id);

        List<Schedule> schedules = List.of(schedule);

        BDDMockito.when(scheduleService.findByDoctor(id))
                .thenReturn(schedules);

        mockMvc.perform(get("/schedules/by-doctor")
                        .param("doctorId", id.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id));

        BDDMockito.then(scheduleService)
                .should()
                .findByDoctor(id);
    }

    @Test
    void shouldReturnSchedules_whenFindByDayOfWeek() throws Exception {

        String day = "MONDAY";

        Schedule schedule = ScheduleCreate.creatScheduleValid();
        List<Schedule> schedules = List.of(schedule);

        BDDMockito.when(scheduleService.findByDayOfWeek(DayOfWeek.valueOf(day)))
                .thenReturn(schedules);

        mockMvc.perform(get("/schedules/by-day")
                        .param("dayOfWeek", day))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(schedule.getId()));

        BDDMockito.then(scheduleService)
                .should()
                .findByDayOfWeek(DayOfWeek.valueOf(day));
    }

    @Test
    void shouldUpdateScheduleSuccessfully() throws Exception {

        Long id = 1L;

        Schedule schedule = ScheduleCreate.creatScheduleValid();
        schedule.setId(id);

        ScheduleUpdateDto dto = ScheduleDtoCreate.scheduleUpdateDtoValid();

        BDDMockito.when(scheduleService.update(id, dto))
                .thenReturn(schedule);

        mockMvc.perform(put("/schedules/{id}", id)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));

        BDDMockito.then(scheduleService)
                .should()
                .update(id, dto);
    }

    @Test
    void shouldDeleteScheduleSuccessfully() throws Exception {

        Long id = 1L;

        BDDMockito.doNothing()
                .when(scheduleService)
                .delete(id);

        mockMvc.perform(delete("/schedules/{id}", id))
                .andExpect(status().isNoContent());

        BDDMockito.then(scheduleService)
                .should()
                .delete(id);
    }
}