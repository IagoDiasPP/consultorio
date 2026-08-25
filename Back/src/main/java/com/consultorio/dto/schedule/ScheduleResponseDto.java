package com.consultorio.dto.schedule;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
