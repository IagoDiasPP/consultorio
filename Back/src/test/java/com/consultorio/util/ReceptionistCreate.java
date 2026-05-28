package com.consultorio.util;

import com.consultorio.model.Receptionist;

public final class ReceptionistCreate {

    public static Receptionist creatReceptionistValid(){
        return Receptionist.builder()
                .name("Rose")
                .id(1L)
                .email("Rose@gmail.com")
                .password("rose")
                .build();
    }
}
