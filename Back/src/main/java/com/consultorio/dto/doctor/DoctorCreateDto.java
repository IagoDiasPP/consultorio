package com.consultorio.dto.doctor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorCreateDto {

    @NotBlank
    private String name;

    @NotNull
    private Long specialtyId;

    @NotBlank
    private String phone;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
