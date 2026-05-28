package com.consultorio.dto.schedule;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScheduleResponseDto {

    private Long id;

    private Long doctorId;

    private String doctorName;

    private String dayOfWeek;

    private String startTime;

    private String endTime;

    private String breakStart;

    private String breakEnd;

}
