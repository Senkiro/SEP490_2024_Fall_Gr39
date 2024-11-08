package com.nsg.dto.request.timeSlot;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TimeSlotCreationRequest {

    @NotNull(message = "TIME_SLOT_NAME_IS_NULL")
    String name;

    @NotNull(message = "START_TIME_IS_NULL")
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "INVALID_TIME_FORMAT")
    @JsonFormat(pattern = "HH:mm")
    String startTime;

    @NotNull(message = "END_TIME_IS_NULL")
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "INVALID_TIME_FORMAT")
    @JsonFormat(pattern = "HH:mm")
    String endTime;

}
