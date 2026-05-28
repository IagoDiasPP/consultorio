package com.consultorio.util;

import com.consultorio.dto.doctor.DoctorCreateDto;
import com.consultorio.dto.doctor.DoctorUpdateDto;

public final class DoctorDtoCreate {

    public static DoctorCreateDto  creatDoctorDtoValid(){

          return  DoctorCreateDto.builder()
                .name("Gabriel")
                  .email("gabriel@gmail.com")
                .phone("(24)999658479")
                .password("gabrial")
                .specialtyId(1L)
                .build();

    }

    public static DoctorUpdateDto creatDoctorUpdateDtoValid() {
        return  DoctorUpdateDto.builder()
                .name("Gabriel")
                .email("gabriel@gmail.com")
                .phone("(24)999658479")
                .specialtyId(1L)
                .build();

    }
}
