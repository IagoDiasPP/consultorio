package com.consultorio.dto.appointment;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class DoctorSlotResponseDto {

    private String time;

    private boolean available;

    private Long appointmentId;

    private String patientName;

    private String status;

    private String patientPhone;

    private String specialtyName;

    private String doctorName;

    private LocalDate date;
}

