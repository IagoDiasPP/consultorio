package com.consultorio.dto.callList;

import com.consultorio.enums.CallStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CallListUpdateDto {

    @NotNull
    private CallStatus status;
}
