package com.nsg.dto.response.timeSlot;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TimeSlotResponse {
    String timeSLotId;
    String name;
    LocalTime startTime;
    LocalTime endTime;
}
