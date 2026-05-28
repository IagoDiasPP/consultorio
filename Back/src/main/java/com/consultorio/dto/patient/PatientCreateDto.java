package com.consultorio.dto.patient;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientCreateDto {

    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    @NotNull
    private LocalDate birthDate;
}
