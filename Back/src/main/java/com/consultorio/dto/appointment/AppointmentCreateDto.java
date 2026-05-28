package com.consultorio.dto.appointment;

import com.consultorio.enums.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@Data
public class AppointmentCreateDto {

    @NotNull
    private Long patientId;

    @NotNull
    private Long specialtyId;


    private Long doctorId;


    private LocalDate date;


    private LocalTime startTime;


}