package com.consultorio.mapper;

import com.consultorio.dto.specialty.SpecialtyCreateDto;
import com.consultorio.dto.specialty.SpecialtyUpdateDto;
import com.consultorio.model.Specialty;
import org.mapstruct.Mapper;

@Mapper(componentModel =  "spring")
public interface SpecialtyMapper {

    Specialty toSpecialty (SpecialtyCreateDto spacialtyDto);

    Specialty toSpecialty (SpecialtyUpdateDto spacialtyDto);
}
