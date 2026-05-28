package com.consultorio.mapper;

import com.consultorio.dto.callList.CallListUpdateDto;
import com.consultorio.model.CallList;
import org.mapstruct.Mapper;

@Mapper(componentModel =  "spring")
public interface CallListMapper {

    CallList toCalllist (CallListUpdateDto callListDto);
}
