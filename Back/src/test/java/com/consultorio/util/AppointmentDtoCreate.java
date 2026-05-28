package com.consultorio.util;

import com.consultorio.dto.appointment.AppointmentCreateDto;
import com.consultorio.dto.appointment.AppointmentUpdateDto;

import java.time.LocalDate;
import java.time.LocalTime;

public final class AppointmentDtoCreate {

    private AppointmentDtoCreate() {}

    public static AppointmentCreateDto creatAppointmentDtoValid() {
        AppointmentCreateDto dto = new AppointmentCreateDto();

        dto.setPatientId(1L);
        dto.setSpecialtyId(1L);

        // esses podem ser null dependendo da sua regra
        dto.setDoctorId(null);
        dto.setDate(null);
        dto.setStartTime(null);

        return dto;
    }

    public static AppointmentUpdateDto updateValid() {
        AppointmentUpdateDto dto = new AppointmentUpdateDto();

        dto.setDate(LocalDate.now().plusDays(1));
        dto.setStartTime(LocalTime.of(10, 0));

        return dto;
    }
}
