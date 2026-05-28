package com.consultorio.dto.schedule;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class ScheduleCreateDto {

    @NotNull
    private Long doctorId;

    @NotNull
    private DayOfWeek dayOfWeek;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @NotNull
    private LocalTime breakStart;

    @NotNull
    private LocalTime breakEnd;
}
