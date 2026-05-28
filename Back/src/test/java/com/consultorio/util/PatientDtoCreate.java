package com.consultorio.util;

import com.consultorio.dto.patient.PatientCreateDto;
import com.consultorio.dto.patient.PatientUpdateDto;

import java.time.LocalDate;

public final class PatientDtoCreate {

    public static PatientCreateDto patientCreatDtoValid(){
        return PatientCreateDto.builder()
                .name("Marcos")
                .phone("(24)998547814")
                .birthDate(LocalDate.of(2001, 6, 21))
                .build();
    }

    public static PatientUpdateDto patientUpdateDtoValid() {
        return PatientUpdateDto.builder()
                .name("Marcos")
                .phone("(24)998547814")
                .birthDate(LocalDate.of(2001, 6, 21))
                .build();
    }
}
