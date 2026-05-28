package com.consultorio.util;

import com.consultorio.model.Patient;

import java.time.LocalDate;

public final class PatientCreate {

    public static Patient creatPatientValid(){
        return  Patient.builder()
                .id(1L)
                .phone("(24)99836259")
                .name("Gustavo")
                .birthDate(LocalDate.of(2001, 6, 21))
                .build();
    }

}
