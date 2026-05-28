package com.consultorio.mapper;

import com.consultorio.dto.appointment.AppointmentCreateDto;
import com.consultorio.dto.appointment.AppointmentUpdateDto;
import com.consultorio.model.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy =
                NullValuePropertyMappingStrategy.IGNORE
)
public interface AppointmentMapper {

    Appointment toAppointment(AppointmentCreateDto dto);

    void updateFromDto(
            AppointmentUpdateDto dto,
            @MappingTarget Appointment appointment
    );
}
