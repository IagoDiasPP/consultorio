package com.consultorio.util;

import com.consultorio.model.Schedule;

public final class ScheduleCreate {

    public static Schedule creatScheduleValid(){
       return Schedule.builder()
                .id(1L)
                .build();
    }
}
