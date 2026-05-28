package com.consultorio.mapper;


import com.consultorio.dto.appointment.AppointmentCreateDto;
import com.consultorio.dto.appointment.AppointmentUpdateDto;
import com.consultorio.model.Appointment;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ConsultaMapper {
     Appointment toAppointment(AppointmentCreateDto appointmentDto);

     Appointment toAppointment(AppointmentUpdateDto appointmentDto);

}
