package com.consultorio.util;

import com.consultorio.model.Specialty;

public final class SpecialtyCreate {

    public static Specialty creatSpecialtyValid(){

        return Specialty.builder()
                .name("Endo")
                .id(1L)
                .active(true)
                .build();
    }
}
