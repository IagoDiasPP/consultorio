package com.consultorio.dto.patient;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class PatientUpdateDto {

    private String name;

    private String phone;

    private LocalDate birthDate;
}
