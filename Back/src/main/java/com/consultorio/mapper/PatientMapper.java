package com.consultorio.mapper;

import com.consultorio.dto.patient.PatientCreateDto;
import com.consultorio.dto.patient.PatientUpdateDto;
import com.consultorio.model.Patient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    public abstract Patient toPatient(PatientCreateDto patientDto);

    public abstract Patient toPatient(PatientUpdateDto patientDto);
}
