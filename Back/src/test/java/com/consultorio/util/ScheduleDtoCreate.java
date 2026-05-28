package com.consultorio.util;

import com.consultorio.dto.schedule.ScheduleCreateDto;
import com.consultorio.dto.schedule.ScheduleUpdateDto;

import java.time.DayOfWeek;
import java.time.LocalTime;

public final class ScheduleDtoCreate {
    public static ScheduleCreateDto scheduleCreatDtoValid(){
        return ScheduleCreateDto.builder()
                .doctorId(1L)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(17, 0))
                .breakStart(LocalTime.of(12, 0))
                .breakEnd(LocalTime.of(13, 0))
                .build();
    }

    public static ScheduleUpdateDto scheduleUpdateDtoValid() {
        return ScheduleUpdateDto.builder()
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(18, 0))
                .build();
    }
}
