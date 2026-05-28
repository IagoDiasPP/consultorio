package com.consultorio.mapper;

import com.consultorio.dto.doctor.DoctorCreateDto;
import com.consultorio.dto.doctor.DoctorUpdateDto;
import com.consultorio.model.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    Doctor toDoctor(DoctorCreateDto doctorDto);

    void updateFromDto(DoctorUpdateDto dto, @MappingTarget Doctor entity);
}
