package com.consultorio.util;

import com.consultorio.model.Doctor;
import com.consultorio.model.Specialty;


public final class DoctorCreate {

    public static Doctor creatDoctorValid() {

        Specialty specialty = new Specialty();
        specialty.setId(1L);

        return Doctor.builder()
                .id(1L)
                .name("Gabriel")
                .email("gabriel@gmail.com")
                .phone("(24)999658479")
                .password("gabrial")
                .specialty(specialty)
                .build();
    }


}
