package com.consultorio.util;

import com.consultorio.dto.receptionist.ReceptionistCreateDto;
import com.consultorio.dto.receptionist.ReceptionistUpdateDto;

public final class ReceptionistDtoCreate {

    public static ReceptionistCreateDto creatReceptionistDtoValid(){
        return ReceptionistCreateDto.builder()
                .name("Rose")
                .email("Rose@gmail.com")
                .password("Rose")
                .build();
    }

    public static ReceptionistUpdateDto creatReceptionistUpdateDtoValid() {
        return ReceptionistUpdateDto.builder()
                .name("Rose")
                .email("Rose@gmail.com")
                .build();
    }
}
