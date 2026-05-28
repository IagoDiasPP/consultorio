package com.consultorio.mapper;

import com.consultorio.dto.schedule.ScheduleCreateDto;
import com.consultorio.dto.schedule.ScheduleUpdateDto;
import com.consultorio.model.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    Schedule toSchedule(ScheduleCreateDto scheduleDto);

    void updateScheduleFromDto(
            ScheduleUpdateDto dto,
            @MappingTarget Schedule schedule
    );
}