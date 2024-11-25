package com.nsg.dto.request.session;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduleCreationRequest {
    @NotBlank
    String timeSlotId;

    @NotBlank
    String roomNumber;

    @NotBlank
    int curriculumnListId;
}
