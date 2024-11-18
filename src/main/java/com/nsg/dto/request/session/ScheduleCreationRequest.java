package com.nsg.dto.request.session;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduleCreationRequest {
    String timeSlotId;

    String roomNumber;
}
