package com.consultorio.dto.patient;


import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    private LocalDate birthDate;
}
