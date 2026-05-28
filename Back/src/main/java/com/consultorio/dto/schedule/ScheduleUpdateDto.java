package com.consultorio.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ScheduleUpdateDto {

    private DayOfWeek dayOfWeek;

    private LocalTime startTime;

    private LocalTime endTime;

    private LocalTime breakStart;

    private LocalTime breakEnd;
}
