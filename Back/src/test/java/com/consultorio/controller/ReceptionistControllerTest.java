package com.consultorio.controller;

import com.consultorio.dto.receptionist.ReceptionistCreateDto;
import com.consultorio.dto.receptionist.ReceptionistUpdateDto;
import com.consultorio.model.Receptionist;
import com.consultorio.service.ReceptionistService;
import com.consultorio.util.ReceptionistCreate;
import com.consultorio.util.ReceptionistDtoCreate;
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

@WebMvcTest(ReceptionistController.class)
class ReceptionistControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ReceptionistService receptionistService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldCreateReceptionist() throws Exception {

        Receptionist receptionist = ReceptionistCreate.creatReceptionistValid();
        ReceptionistCreateDto dto = ReceptionistDtoCreate.creatReceptionistDtoValid();

        BDDMockito.when(receptionistService.save(dto))
                .thenReturn(receptionist);

        mockMvc.perform(post("/receptionists")
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(receptionist.getName()));

        BDDMockito.then(receptionistService)
                .should()
                .save(dto);
    }

    @Test
    void shouldReturnPageOfReceptionists() throws Exception {

        Receptionist receptionist = ReceptionistCreate.creatReceptionistValid();
        Page<Receptionist> receptionists = new PageImpl<>(List.of(receptionist));

        BDDMockito.when(receptionistService.findAll(ArgumentMatchers.any(Pageable.class)))
                .thenReturn(receptionists);

        mockMvc.perform(get("/receptionists")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value(receptionist.getName()))
                .andExpect(jsonPath("$.totalElements").value(1));

        BDDMockito.then(receptionistService)
                .should()
                .findAll(ArgumentMatchers.any(Pageable.class));
    }

    @Test
    void shouldReturnReceptionistById() throws Exception {

        Receptionist receptionist = ReceptionistCreate.creatReceptionistValid();
        Long id = receptionist.getId();

        BDDMockito.when(receptionistService.findByIdOrThrowRequestException(id))
                .thenReturn(receptionist);

        mockMvc.perform(get("/receptionists/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(receptionist.getName()));

        BDDMockito.then(receptionistService)
                .should()
                .findByIdOrThrowRequestException(id);
    }

    @Test
    void shouldReturnReceptionistsByName() throws Exception {

        Receptionist receptionist = ReceptionistCreate.creatReceptionistValid();
        String name = receptionist.getName();

        List<Receptionist> receptionists = List.of(receptionist);

        BDDMockito.when(receptionistService.findByName(name))
                .thenReturn(receptionists);

        mockMvc.perform(get("/receptionists/search")
                        .param("name", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(name));

        BDDMockito.then(receptionistService)
                .should()
                .findByName(name);
    }

    @Test
    void shouldUpdateReceptionist() throws Exception {

        Receptionist receptionist = ReceptionistCreate.creatReceptionistValid();
        Long id = receptionist.getId();

        ReceptionistUpdateDto dto = ReceptionistDtoCreate.creatReceptionistUpdateDtoValid();

        BDDMockito.when(receptionistService.update(id, dto))
                .thenReturn(receptionist);

        mockMvc.perform(put("/receptionists/{id}", id)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(receptionist.getName()));

        BDDMockito.then(receptionistService)
                .should()
                .update(id, dto);
    }

    @Test
    void shouldDeleteReceptionist() throws Exception {

        Long id = 1L;

        BDDMockito.doNothing()
                .when(receptionistService)
                .delete(id);

        mockMvc.perform(delete("/receptionists/{id}", id))
                .andExpect(status().isNoContent());

        BDDMockito.then(receptionistService)
                .should()
                .delete(id);
    }
}