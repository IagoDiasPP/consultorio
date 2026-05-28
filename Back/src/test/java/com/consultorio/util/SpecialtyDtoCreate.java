package com.consultorio.util;

import com.consultorio.dto.specialty.SpecialtyCreateDto;
import com.consultorio.dto.specialty.SpecialtyUpdateDto;

public final class SpecialtyDtoCreate {
    public static SpecialtyCreateDto creatSpecialtyDtoValid(){
        return SpecialtyCreateDto.builder()
                .name("Endo")
                .active(true)
                .build();
    }

    public static SpecialtyUpdateDto creatSpecialtyUpdateDtoValid() {
        return SpecialtyUpdateDto.builder()
                .name("Endo")
                .active(true)
                .build();
    }
}
