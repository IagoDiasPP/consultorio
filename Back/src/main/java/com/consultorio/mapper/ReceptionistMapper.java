package com.consultorio.mapper;

import com.consultorio.dto.receptionist.ReceptionistCreateDto;
import com.consultorio.dto.receptionist.ReceptionistUpdateDto;
import com.consultorio.model.Receptionist;
import org.mapstruct.Mapper;

@Mapper (componentModel =  "spring")
public interface ReceptionistMapper {

    Receptionist toReceptionist(ReceptionistCreateDto receptionistDto);

    Receptionist toReceptionist(ReceptionistUpdateDto receptionistDto);

}

