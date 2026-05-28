package com.consultorio.dto.doctor;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class DoctorUpdateDto {

    private String name;

    private Long specialtyId;

    private String phone;

    @Email
    private String email;
}
