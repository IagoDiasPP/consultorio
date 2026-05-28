package com.consultorio.dto.appointment;

import com.consultorio.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class AppointmentUpdateDto {

    private LocalDate date;

    private LocalTime startTime;

    private AppointmentStatus status;
}
