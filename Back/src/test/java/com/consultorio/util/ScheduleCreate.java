package com.consultorio.util;

import com.consultorio.model.Doctor;
import com.consultorio.model.Schedule;

import java.time.DayOfWeek;
import java.time.LocalTime;

public final class ScheduleCreate {

    public static Schedule creatScheduleValid() {

        Doctor doctor = Doctor.builder()
                .id(1L)
                .build();

        return Schedule.builder()
                .id(1L)
                .doctor(doctor)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(18, 0))
                .breakStart(LocalTime.of(12, 0))
                .breakEnd(LocalTime.of(13, 0))
                .build();
    }
}
